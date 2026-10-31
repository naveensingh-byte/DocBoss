package com.example.docconverter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen(onFinished = {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            })
        }
    }
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var started by remember { mutableStateOf(false) }

    val logoAlpha by animateFloatAsState(if (started) 1f else 0f, tween(600), label = "a")
    val logoScale by animateFloatAsState(if (started) 1f else 0.7f, tween(600), label = "s")
    val textAlpha by animateFloatAsState(if (started) 1f else 0f, tween(500, delayMillis = 300), label = "t")

    LaunchedEffect(Unit) {
        started = true
        delay(2200)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(colorResource(R.color.docboss_primary)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.ic_docboss_logo),
                contentDescription = "DocBoss logo",
                modifier = Modifier.size(120.dp).graphicsLayer {
                    alpha = logoAlpha
                    scaleX = logoScale
                    scaleY = logoScale
                }
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "DocBoss",
                color = colorResource(R.color.docboss_white),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(textAlpha)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Convert any document, instantly",
                color = colorResource(R.color.docboss_accent),
                fontSize = 15.sp,
                modifier = Modifier.alpha(textAlpha)
            )
        }
    }
}