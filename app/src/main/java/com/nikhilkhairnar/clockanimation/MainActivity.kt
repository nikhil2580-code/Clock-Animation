package com.nikhilkhairnar.clockanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ClockScreen()
            }
        }
    }
}

@Composable
fun ClockScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        ClockAnimation()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = SimpleDateFormat("EEEE d", Locale.getDefault()).format(Date()),
                color = Color.White,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "That Cool Song",
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = "It's Artist",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ClockAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val colorPosition1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.size(200.dp)) {
        val canvasSize = size.minDimension
        val radius = canvasSize / 2
        val strokeWidth = 4.dp.toPx()
        val outerCircleRadius = radius - strokeWidth

        // Draw the flaming effect background
        drawFlamingEffect(radius = outerCircleRadius, colorPosition1)

        drawArc(
            color = Color.Yellow,
            startAngle = angle,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        val hour = Calendar.getInstance().get(Calendar.HOUR)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)
        val hourAngle = (hour * 30 + minute * 0.5).toFloat()
        val minuteAngle = (minute * 6).toFloat()

        // Hour hand
        drawLine(
            color = Color.White,
            start = center,
            end = center + Offset(
                x = (cos(Math.toRadians((hourAngle - 90).toDouble())) * radius * 0.5).toFloat(),
                y = (sin(Math.toRadians((hourAngle - 90).toDouble())) * radius * 0.5).toFloat()
            ),
            strokeWidth = strokeWidth
        )

        // Minute hand
        drawLine(
            color = Color.White,
            start = center,
            end = center + Offset(
                x = (cos(Math.toRadians((minuteAngle - 90).toDouble())) * radius * 0.75).toFloat(),
                y = (sin(Math.toRadians((minuteAngle - 90).toDouble())) * radius * 0.75).toFloat()
            ),
            strokeWidth = strokeWidth
        )
    }
}

fun DrawScope.drawFlamingEffect(radius: Float, colorPosition: Float) {
    val colors = listOf(Color.Red, Color.Yellow, Color.Blue)

    drawCircle(
        brush = Brush.radialGradient(
            colors = colors,
            center = center,
            radius = radius,
            tileMode = TileMode.Mirror
        ),
        radius = radius,
        style = Stroke(width = 8.dp.toPx())
    )

    // Adding additional flame effect with arcs
    drawArc(
        brush = Brush.radialGradient(
            colors = listOf(Color.Yellow.copy(alpha = colorPosition), Color.Red.copy(alpha = colorPosition)),
            center = center,
            radius = radius,
            tileMode = TileMode.Mirror
        ),
        startAngle = 0f,
        sweepAngle = 120f,
        useCenter = false,
        style = Stroke(width = 8.dp.toPx())
    )

    drawArc(
        brush = Brush.radialGradient(
            colors = listOf(Color.Red.copy(alpha = colorPosition), Color.Blue.copy(alpha = colorPosition)),
            center = center,
            radius = radius,
            tileMode = TileMode.Mirror
        ),
        startAngle = 120f,
        sweepAngle = 120f,
        useCenter = false,
        style = Stroke(width = 8.dp.toPx())
    )

    drawArc(
        brush = Brush.radialGradient(
            colors = listOf(Color.Blue.copy(alpha = colorPosition), Color.Yellow.copy(alpha = colorPosition)),
            center = center,
            radius = radius,
            tileMode = TileMode.Mirror
        ),
        startAngle = 240f,
        sweepAngle = 120f,
        useCenter = false,
        style = Stroke(width = 8.dp.toPx())
    )
}
