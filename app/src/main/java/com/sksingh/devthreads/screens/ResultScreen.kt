package com.sksingh.devthreads.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sksingh.devthreads.R
import java.text.DecimalFormat
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, petName: String, score: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Check Result") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val resources = LocalContext.current.resources
                val scoreInt = score.toFloatOrNull()?.roundToInt() ?: 0
                val imageName = remember(scoreInt) {
                    when (scoreInt) {
                        in 90..100 -> "excellent"
                        in 70..89 -> "good"
                        in 0..69 -> "sad"
                        else -> "excellent" // Default to excellent
                    }
                }

                val resourceId = remember(petName, imageName) {
                    val petFolder = petName.lowercase()
                    resources.getIdentifier(
                        "healthResult_${petFolder}_${imageName}",
                        "drawable",
                        "com.example.petland"
                    )
                }

                if (resourceId != 0) {
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = "Result Image for $petName",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                } else {
                    Text("Không tìm thấy ảnh") // Hiển thị nếu không tìm thấy ảnh
                }

                AnimatedCircularProgressIndicator(percentage = score.toFloatOrNull()?.div(100f) ?: 0f, number = score)
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = when (scoreInt) {
                        in 90..100 -> "Excellent health!"
                        in 70..89 -> "Good, but needs attention."
                        in 0..69 -> "Needs medical attention soon!"
                        else -> "Unknown result"
                    },
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Composable
fun AnimatedCircularProgressIndicator(
    percentage: Float,
    number: String,
    radius: Dp = 80.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 12.dp,
    animDuration: Int = 1500,
    fontSize: TextUnit = 36.sp
) {
    val currentPercentage = remember { Animatable(0f) }
    val currentNumber = remember { Animatable(0f) } // Hoạt hình cho số float
    val scale = remember { Animatable(1f) }
    val decimalFormat = remember { DecimalFormat("0.0") }

    LaunchedEffect(percentage, number) {
        val targetPercentage = percentage
        val targetNumber = number.toFloatOrNull() ?: 0f

        // Hoạt hình cho phần trăm
        currentPercentage.animateTo(
            targetValue = targetPercentage,
            animationSpec = tween(durationMillis = animDuration, delayMillis = 0)
        )
        // Hoạt hình cho số
        currentNumber.animateTo(
            targetValue = targetNumber,
            animationSpec = tween(durationMillis = animDuration, delayMillis = 0)
        )
        // Hiệu ứng "nổ" nhẹ khi đạt đến số cuối cùng
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(durationMillis = 300, easing = EaseOutBounce)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 200)
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2)
    ) {
        Canvas(modifier = Modifier.size(radius * 2)) {
            val sweepAngle = currentPercentage.value * 360
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = decimalFormat.format(currentNumber.value), // Hiển thị số với 1 chữ số thập phân
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.scale(scale.value)
        )
    }
}