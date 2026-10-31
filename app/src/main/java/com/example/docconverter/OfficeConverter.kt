package com.example.docconverter

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

private val SERVER_URL: String = BuildConfig.SERVER_URL
private val SERVER_USER: String = BuildConfig.SERVER_USER
private val SERVER_PASS: String = BuildConfig.SERVER_PASS

private val client = OkHttpClient.Builder()
    .connectTimeout(150, TimeUnit.SECONDS)   // free server may be asleep
    .readTimeout(240, TimeUnit.SECONDS)
    .writeTimeout(240, TimeUnit.SECONDS)
    .build()

fun displayName(context: Context, uri: Uri): String {
    context.contentResolver.query(uri, null, null, null, null)?.use { c ->
        val i = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (i >= 0 && c.moveToFirst()) return c.getString(i)
    }
    return "file.docx"
}

// Returns null on success, or an error message
fun officeToPdf(context: Context, input: Uri, output: Uri): String? {
    return try {
        val name = displayName(context, input)
        val bytes = context.contentResolver.openInputStream(input)!!.use { it.readBytes() }

        if (bytes.size > 20 * 1024 * 1024) return "File too big (max 20 MB)"

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "files", name,
                bytes.toRequestBody("application/octet-stream".toMediaType())
            )
            .build()

        val request = Request.Builder()
            .url("$SERVER_URL/forms/libreoffice/convert")
            .header("Authorization", Credentials.basic(SERVER_USER, SERVER_PASS))
            .post(body)
            .build()

        client.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) return "Server error ${resp.code}"
            context.contentResolver.openOutputStream(output)!!.use { out ->
                resp.body!!.byteStream().copyTo(out)
            }
        }
        null
    } catch (e: Exception) {
        android.util.Log.e("Converter", "office convert failed", e)
        e.message ?: "Unknown error"
    }
}