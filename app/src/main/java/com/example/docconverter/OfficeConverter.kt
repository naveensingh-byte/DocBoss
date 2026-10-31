package com.example.docconverter

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

const val SERVER_URL = "http://172.20.144.1:3000"

private val client = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(180, TimeUnit.SECONDS)
    .writeTimeout(180, TimeUnit.SECONDS)
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

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "files", name,
                bytes.toRequestBody("application/octet-stream".toMediaType())
            )
            .build()

        val request = Request.Builder()
            .url("$SERVER_URL/forms/libreoffice/convert")
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
        Log.e("Converter", "office convert failed", e)
        e.message ?: "Unknown error"
    }
}