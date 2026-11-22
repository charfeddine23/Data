package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.FormatUtils
import com.example.ui.theme.HyperMonoFontFamily
import com.example.ui.theme.LocalLiquidGlass
import kotlin.math.sin
import kotlin.random.Random

/**
 * Snowflake particle model for interactive offline state
 */
private data class Snowflake(
    val initialXRatio: Float,
    val speed: Float,
    val radiusDp: Float,
    val alpha: Float,
    val swayAmplitude: Float,
    val swayPhase: Float
)

/**
 * Ultra-Smooth Apple iOS 26 Liquid Glass Continuous Throughput Graph with:
 * 1. Fluid continuous wave interpolation (harmonic phase pulse & calibrated horizon guides).
 * 2. Dynamic speed-based chromatic shifting (High speed -> Red/Orange, Mid -> Amber, Normal -> Safe/Accent).
 * 3. Interactive offline frosted ice state ("Ice & Snow" effect with drifting crystals).
 * 4. Harmonized iOS 26 optical glass stage + Neumorphic support.
 */
@Composable
fun ContinuousThroughputGraph(
    dataPoints: List<Float>,
    currentSpeedBps: Double,
    isOffline: Boolean,
    appLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    val speedMbps = (currentSpeedBps / 1_000_000.0).toFloat().coerceAtLeast(0f)
    val speedRatio = (speedMbps / 25.0f).coerceIn(0f, 1f)

    val targetPrimaryColor = when {
        isOffline -> if (glassStyle.isDark) Color(0xFFE0F7FA) else Color(0xFF0284C7)
        speedRatio > 0.5f -> glassStyle.statusDanger
        speedRatio > 0.2f -> glassStyle.statusWarning
        else -> glassStyle.statusSafe
    }

    val targetSecondaryColor = when {
        isOffline -> if (glassStyle.isDark) Color(0xFF80DEEA) else Color(0xFF0284C7)
        speedRatio > 0.5f -> glassStyle.statusWarning
        speedRatio > 0.2f -> glassStyle.accentPrimary
        else -> glassStyle.accentPrimary
    }

    val animatedPrimaryColor by animateColorAsState(
        targetValue = targetPrimaryColor,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "graph_primary_color"
    )

    val animatedSecondaryColor by animateColorAsState(
        targetValue = targetSecondaryColor,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "graph_secondary_color"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "wave_motion")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI.toFloat()),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    val snowTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "snow_fall"
    )

    val snowflakes = remember {
        val rand = Random(42)
        List(32) {
            Snowflake(
                initialXRatio = rand.nextFloat(),
                speed = 0.6f + rand.nextFloat() * 0.8f,
                radiusDp = 1.5f + rand.nextFloat() * 2.5f,
                alpha = 0.35f + rand.nextFloat() * 0.55f,
                swayAmplitude = 8f + rand.nextFloat() * 16f,
                swayPhase = rand.nextFloat() * 6.28f
            )
        }
    }

    val latestSpeedNormalized = if (dataPoints.isNotEmpty()) {
        val maxVal = (dataPoints.maxOrNull() ?: 1f).coerceAtLeast(1f)
        (dataPoints.last() / maxVal).coerceIn(0.05f, 1f)
    } else 0.05f

    val smoothedHeightAnim = remember { Animatable(latestSpeedNormalized) }
    LaunchedEffect(latestSpeedNormalized) {
        smoothedHeightAnim.animateTo(
            targetValue = latestSpeedNormalized,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        )
    }

    val containerShape = RoundedCornerShape(24.dp)
    val offlineFrostBg = if (glassStyle.isDark) Color(0xFF0C1A24) else Color(0xFFECF8FF)

    GlassContainer(
        modifier = modifier.fillMaxWidth(),
        shape = containerShape,
        backgroundColor = if (isOffline && !glassStyle.isNeumorphic) offlineFrostBg else null,
        borderColor = if (isOffline) {
            animatedPrimaryColor.copy(alpha = 0.55f)
        } else {
            animatedPrimaryColor.copy(alpha = if (glassStyle.isDark) 0.42f else 0.30f)
        },
        contentPadding = PaddingValues(16.dp)
    ) {
        Column {
            // Header Row: Optical Icon Badge + Title + Monospace Speed Capsule
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IosLiquidGlassIconBadge(
                        icon = if (isOffline) Icons.Default.AcUnit else Icons.Default.TrendingUp,
                        contentDescription = null,
                        size = 34.dp,
                        iconSize = 17.dp,
                        tintColor = animatedPrimaryColor,
                        shape = RoundedCornerShape(11.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) "مخطط تدفق البيانات المستمر" else "Continuous Throughput",
                            color = glassStyle.textPrimary,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.2.sp
                        )
                        Text(
                            text = if (isOffline) {
                                if (appLanguage == AppLanguage.ARABIC) "وضع التجمد - غير متصل بالشبكة" else "Frozen State - Network Offline"
                            } else {
                                if (speedRatio > 0.5f) {
                                    if (appLanguage == AppLanguage.ARABIC) "تدفق فائق (عالي السرعة)" else "High Speed Burst"
                                } else {
                                    if (appLanguage == AppLanguage.ARABIC) "تدفق مستقر (عادي)" else "Stable Low Latency"
                                }
                            },
                            color = animatedPrimaryColor,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Real-time speed capsule badge
                Box(
                    modifier = Modifier
                        .iosLiquidGlassSurface(
                            shape = CircleShape,
                            tintColor = animatedPrimaryColor,
                            isFilledAccent = false,
                            elevation = if (glassStyle.iosLiquidGlassEnabled) 2.dp else 0.dp
                        )
                        .padding(horizontal = 11.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isOffline) {
                            if (appLanguage == AppLanguage.ARABIC) "مجمد" else "Frozen"
                        } else {
                            FormatUtils.formatSpeed(currentSpeedBps)
                        },
                        fontFamily = HyperMonoFontFamily,
                        color = animatedPrimaryColor,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Canvas inside sleek recessed optical graph stage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(118.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isOffline) {
                            animatedPrimaryColor.copy(alpha = if (glassStyle.isDark) 0.06f else 0.12f)
                        } else {
                            glassStyle.cardSubtleBg.copy(alpha = if (glassStyle.isDark) 0.55f else 0.65f)
                        }
                    )
                    .border(
                        width = 0.8.dp,
                        color = glassStyle.borderColor.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Subtle horizontal calibration reference guides
                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f), 0f)
                    listOf(0.32f, 0.64f).forEach { fraction ->
                        val guideY = canvasHeight * fraction
                        drawLine(
                            color = glassStyle.textTertiary.copy(alpha = 0.16f),
                            start = Offset(0f, guideY),
                            end = Offset(canvasWidth, guideY),
                            strokeWidth = 0.8.dp.toPx(),
                            pathEffect = dashEffect
                        )
                    }

                    if (isOffline) {
                        val flatY = canvasHeight * 0.74f
                        val icePath = Path().apply {
                            moveTo(0f, flatY)
                            val steps = 8
                            for (i in 1..steps) {
                                val x = (canvasWidth / steps) * i
                                val jitter = if (i % 2 == 0) -2.dp.toPx() else 2.dp.toPx()
                                lineTo(x, flatY + jitter)
                            }
                            lineTo(canvasWidth, canvasHeight)
                            lineTo(0f, canvasHeight)
                            close()
                        }

                        drawPath(
                            path = icePath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF80DEEA).copy(alpha = 0.25f),
                                    Color(0xFF006064).copy(alpha = 0.04f)
                                )
                            )
                        )

                        drawLine(
                            color = if (glassStyle.isDark) Color(0xFFE0F7FA) else Color(0xFF0284C7),
                            start = Offset(0f, flatY),
                            end = Offset(canvasWidth, flatY),
                            strokeWidth = 2.5.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        snowflakes.forEach { flake ->
                            val currentYProgress = (snowTime * flake.speed + flake.initialXRatio) % 1f
                            val flakeY = currentYProgress * canvasHeight
                            val swayOffset = sin((snowTime * 6.28f + flake.swayPhase).toDouble()).toFloat() * flake.swayAmplitude.dp.toPx()
                            val flakeX = (flake.initialXRatio * canvasWidth + swayOffset).coerceIn(0f, canvasWidth)

                            drawCircle(
                                color = if (glassStyle.isDark) Color.White.copy(alpha = flake.alpha) else Color(0xFF0284C7).copy(alpha = flake.alpha * 0.35f),
                                radius = flake.radiusDp.dp.toPx(),
                                center = Offset(flakeX, flakeY)
                            )
                        }
                    } else if (dataPoints.isNotEmpty()) {
                        val maxVal = (dataPoints.maxOrNull() ?: 1f).coerceAtLeast(1f)
                        val wavePath = Path()
                        val fillPath = Path()

                        val pointCount = dataPoints.size
                        val stepX = canvasWidth / (pointCount - 1).coerceAtLeast(1)

                        val points = dataPoints.mapIndexed { i, value ->
                            val x = i * stepX
                            val norm = (value / maxVal).coerceIn(0f, 1f)
                            val harmonic = sin((wavePhase + (i * 0.45f)).toDouble()).toFloat() * 3.dp.toPx()
                            val y = (canvasHeight - (norm * (canvasHeight * 0.72f)) - (canvasHeight * 0.12f) + harmonic)
                                .coerceIn(6.dp.toPx(), canvasHeight - 6.dp.toPx())
                            Offset(x, y)
                        }

                        wavePath.moveTo(points.first().x, points.first().y)
                        fillPath.moveTo(points.first().x, canvasHeight)
                        fillPath.lineTo(points.first().x, points.first().y)

                        for (i in 0 until points.size - 1) {
                            val p0 = points[i]
                            val p1 = points[i + 1]
                            val cx = (p0.x + p1.x) / 2f
                            wavePath.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                            fillPath.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                        }

                        fillPath.lineTo(canvasWidth, canvasHeight)
                        fillPath.close()

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    animatedPrimaryColor.copy(alpha = 0.34f),
                                    animatedSecondaryColor.copy(alpha = 0.08f),
                                    Color.Transparent
                                )
                            )
                        )

                        drawPath(
                            path = wavePath,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    animatedSecondaryColor,
                                    animatedPrimaryColor
                                )
                            ),
                            style = Stroke(
                                width = 3.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )

                        val lastPoint = points.last()
                        drawCircle(
                            color = animatedPrimaryColor.copy(alpha = 0.32f),
                            radius = 7.5.dp.toPx(),
                            center = lastPoint
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.6.dp.toPx(),
                            center = lastPoint
                        )
                    }
                }
            }
        }
    }
}
