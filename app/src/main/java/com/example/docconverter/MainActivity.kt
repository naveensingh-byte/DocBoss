package com.example.docconverter

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { ConverterScreen() } }
    }
}

@Composable
fun ConverterScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("Pick images to turn into a PDF") }
    var picked by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // ---------- Images -> PDF ----------
    val saveLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/pdf")
    ) { outUri ->
        if (outUri != null) scope.launch {
            status = "Converting…"
            val ok = withContext(Dispatchers.IO) {
                runCatching { imagesToPdf(context, picked, outUri) }
                    .onFailure { android.util.Log.e("Converter", "failed", it) }
                    .isSuccess
            }
            status = if (ok) "Done! PDF saved." else "Conversion failed."
        }
    }

    val pickLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            picked = uris
            saveLauncher.launch("converted.pdf")
        }
    }

    // ---------- Word / Excel / PPT -> PDF ----------
    var officeFile by remember { mutableStateOf<Uri?>(null) }

    val officeSaveLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/pdf")
    ) { outUri ->
        val input = officeFile
        if (outUri != null && input != null) scope.launch {
            status = "Converting…"
            val error = withContext(Dispatchers.IO) { officeToPdf(context, input, outUri) }
            status = if (error == null) "Done! PDF saved." else "Failed: $error"
        }
    }

    val officePickLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            officeFile = uri
            officeSaveLauncher.launch("converted.pdf")
        }
    }

    // ---------- Screen ----------
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(status)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { pickLauncher.launch("image/*") }) { Text("Images → PDF") }

        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            officePickLauncher.launch(arrayOf(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/msword",
                "application/vnd.ms-excel",
                "application/vnd.ms-powerpoint"
            ))
        }) { Text("Word/Excel/PPT → PDF") }
    }
}

fun imagesToPdf(context: Context, uris: List<Uri>, out: Uri) {
    val pdf = PdfDocument()
    uris.forEachIndexed { i, uri ->
        val bmp = context.contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it)
        } ?: return@forEachIndexed
        val info = PdfDocument.PageInfo.Builder(bmp.width, bmp.height, i + 1).create()
        val page = pdf.startPage(info)
        page.canvas.drawBitmap(bmp, 0f, 0f, null)
        pdf.finishPage(page)
        bmp.recycle()
    }
    context.contentResolver.openOutputStream(out)?.use { pdf.writeTo(it) }
    pdf.close()
}