package com.example.educonnect.auth

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark
import com.example.educonnect.ui.theme.White

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    var isAnimationFinished by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isAnimationFinished) 1f else 0.5f,
        animationSpec = tween(durationMillis = 1000),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        isAnimationFinished = true
        Handler(Looper.getMainLooper()).postDelayed({
            onSplashComplete()
        }, 2000)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.scale(scale),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.School,
                contentDescription = null,
                tint = PurpleMain,
                modifier = Modifier.size(80.dp)
            )
            Text(
                "EduConnect",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = TextDark,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}