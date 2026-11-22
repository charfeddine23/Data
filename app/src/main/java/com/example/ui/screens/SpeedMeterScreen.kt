package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.FormatUtils
import com.example.model.LiveSpeedSample
import com.example.model.NetworkType
import com.example.ui.MainViewModel
import com.example.ui.components.ContinuousThroughputGraph
import com.example.ui.components.GlassContainer
import com.example.ui.components.IosLiquidGlassIconBadge
import com.example.ui.components.IosLiquidGlassInsetDivider
import com.example.ui.components.IosLiquidGlassSectionHeader
import com.example.ui.components.iosLiquidGlassSurface
import com.example.ui.components.rememberAppHaptics
import com.example.ui.components.tactileSpringPress
import com.example.ui.navigation.LocalFloatingChromePadding
import com.example.ui.theme.HyperMonoFontFamily
import com.example.ui.theme.LocalAppDimensions
import com.example.ui.theme.LocalLiquidGlass
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedMeterScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val speedSample by viewModel.speedSample.collectAsStateWithLifecycle()
    val speedHistory by viewModel.speedHistory.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current
    val chromePadding = LocalFloatingChromePadding.current
    val context = LocalContext.current
    val haptics = rememberAppHaptics()

    val openSpeedTest: () -> Unit = {
        haptics.performConfirm()
        val speedTestUrl = "https://fast.com"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(speedTestUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=speedtest")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
            } catch (_: Exception) {}
        }
    }

    val isOffline = (speedSample.connectionType == NetworkType.OFFLINE)
    val peakPrefix = if (appLanguage == AppLanguage.ARABIC) "الذروة: " else "Peak: "

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = dimensions.screenPaddingHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(chromePadding.calculateTopPadding() + 6.dp))

        // Editorial Screen Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IosLiquidGlassIconBadge(
                icon = Icons.Default.Speed,
                contentDescription = null,
                size = dimensions.headerIconBoxSize,
                iconSize = dimensions.headerIconSize * 0.56f,
                tintColor = glassStyle.accentPrimary,
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = AppStrings.speedMeterTitle(appLanguage),
                    color = glassStyle.textPrimary,
                    fontSize = dimensions.textTitleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = AppStrings.speedMeterSubtitle(appLanguage),
                    color = glassStyle.textSecondary,
                    fontSize = dimensions.textCaption,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (dimensions.isTablet) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensions.gridSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    SpeedometerDial(
                        currentSpeedBps = speedSample.downloadBps,
                        formattedSpeed = speedSample.downloadFormatted,
                        appLanguage = appLanguage,
                        onOpenSpeedTest = openSpeedTest,
                        modifier = Modifier.size(dimensions.speedometerDialSize)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensions.gridSpacing)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensions.gridSpacing)
                    ) {
                        SpeedTelemetryCard(
                            title = AppStrings.currentDownload(appLanguage),
                            speed = speedSample.downloadFormatted,
                            peak = "$peakPrefix${FormatUtils.formatSpeed(speedSample.peakDownloadBps)}",
                            icon = Icons.Default.ArrowDownward,
                            accentColor = glassStyle.accentPrimary,
                            modifier = Modifier.weight(1f)
                        )

                        SpeedTelemetryCard(
                            title = AppStrings.currentUpload(appLanguage),
                            speed = speedSample.uploadFormatted,
                            peak = "$peakPrefix${FormatUtils.formatSpeed(speedSample.peakUploadBps)}",
                            icon = Icons.Default.ArrowUpward,
                            accentColor = glassStyle.accentSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    NetworkDiagnosticsCard(speedSample = speedSample, appLanguage = appLanguage)
                }
            }
        } else {
            // Optical Glass Hero Stage wrapping Speedometer Dial
            GlassContainer(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                borderColor = glassStyle.accentPrimary.copy(alpha = 0.30f),
                contentPadding = PaddingValues(vertical = 18.dp, horizontal = 14.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SpeedometerDial(
                        currentSpeedBps = speedSample.downloadBps,
                        formattedSpeed = speedSample.downloadFormatted,
                        appLanguage = appLanguage,
                        onOpenSpeedTest = openSpeedTest,
                        modifier = Modifier.size(dimensions.speedometerDialSize)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Real-time Download & Upload Bento Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SpeedTelemetryCard(
                    title = AppStrings.currentDownload(appLanguage),
                    speed = speedSample.downloadFormatted,
                    peak = "$peakPrefix${FormatUtils.formatSpeed(speedSample.peakDownloadBps)}",
                    icon = Icons.Default.ArrowDownward,
                    accentColor = glassStyle.accentPrimary,
                    modifier = Modifier.weight(1f)
                )

                SpeedTelemetryCard(
                    title = AppStrings.currentUpload(appLanguage),
                    speed = speedSample.uploadFormatted,
                    peak = "$peakPrefix${FormatUtils.formatSpeed(speedSample.peakUploadBps)}",
                    icon = Icons.Default.ArrowUpward,
                    accentColor = glassStyle.accentSecondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Continuous Throughput Wave Graph
        ContinuousThroughputGraph(
            dataPoints = speedHistory,
            currentSpeedBps = speedSample.downloadBps,
            isOffline = isOffline,
            appLanguage = appLanguage,
            modifier = Modifier.fillMaxWidth()
        )

        if (!dimensions.isTablet) {
            Spacer(modifier = Modifier.height(16.dp))
            IosLiquidGlassSectionHeader(
                title = AppStrings.networkDiagnostics(appLanguage)
            )
            Spacer(modifier = Modifier.height(6.dp))
            NetworkDiagnosticsCard(speedSample = speedSample, appLanguage = appLanguage)
        }

        Spacer(modifier = Modifier.height(chromePadding.calculateBottomPadding()))
    }
}

@Composable
fun NetworkDiagnosticsCard(
    speedSample: LiveSpeedSample,
    appLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    val connectionNameTranslated = if (appLanguage == AppLanguage.ARABIC) {
        when (speedSample.connectionType) {
            NetworkType.WIFI -> "واي فاي"
            NetworkType.CELLULAR_5G -> "شبكة الجيل الخامس 5G"
            NetworkType.CELLULAR_4G -> "شبكة الجيل الرابع 4G"
            NetworkType.CELLULAR_3G -> "شبكة الجيل الثالث 3G"
            NetworkType.CELLULAR_GENERIC -> "بيانات الجوال"
            NetworkType.ETHERNET -> "إيثرنت"
            NetworkType.OFFLINE -> "غير متصل"
        }
    } else {
        when (speedSample.connectionType) {
            NetworkType.WIFI -> "Wi-Fi"
            NetworkType.CELLULAR_5G -> "5G Cellular"
            NetworkType.CELLULAR_4G -> "4G LTE"
            NetworkType.CELLULAR_3G -> "3G Mobile"
            NetworkType.CELLULAR_GENERIC -> "Cellular Data"
            NetworkType.ETHERNET -> "Ethernet"
            NetworkType.OFFLINE -> "Disconnected"
        }
    }

    val disconnectedLabel = if (appLanguage == AppLanguage.ARABIC) "غير متصل" else "Disconnected"
    val unknownLabel = if (appLanguage == AppLanguage.ARABIC) "غير متوفر" else "Unavailable"
    val latencyUnit = if (appLanguage == AppLanguage.ARABIC) "مللي ثانية" else "ms"

    // Apple Settings-style grouped inset panel
    GlassContainer(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DiagnosticRow(
                icon = Icons.Default.Router,
                tint = glassStyle.accentPrimary,
                label = AppStrings.activeInterface(appLanguage),
                value = if (speedSample.connectionName == "Disconnected") disconnectedLabel else speedSample.connectionName,
                useMono = false
            )
            IosLiquidGlassInsetDivider()
            DiagnosticRow(
                icon = Icons.Default.Public,
                tint = glassStyle.accentSecondary,
                label = AppStrings.localIp(appLanguage),
                value = if (speedSample.ipAddress == "Unknown") unknownLabel else speedSample.ipAddress,
                useMono = true
            )
            IosLiquidGlassInsetDivider()
            DiagnosticRow(
                icon = Icons.Default.NetworkCheck,
                tint = glassStyle.statusWarning,
                label = AppStrings.estimatedLatency(appLanguage),
                value = "${speedSample.pingMs} $latencyUnit",
                useMono = true
            )
            IosLiquidGlassInsetDivider()
            DiagnosticRow(
                icon = Icons.Default.SignalCellularAlt,
                tint = glassStyle.statusSafe,
                label = AppStrings.connectionState(appLanguage),
                value = connectionNameTranslated,
                useMono = false
            )
        }
    }
}

@Composable
fun SpeedometerDial(
    currentSpeedBps: Double,
    formattedSpeed: String,
    appLanguage: AppLanguage,
    modifier: Modifier = Modifier,
    onOpenSpeedTest: () -> Unit = {}
) {
    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current

    val currentMbps = (currentSpeedBps / 1_000_000.0).toFloat().coerceIn(0f, 100f)
    val sweepFraction = (currentMbps / 100f).coerceIn(0.02f, 1f)

    val animatedSweep by animateFloatAsState(
        targetValue = sweepFraction,
        animationSpec = tween(durationMillis = 550),
        label = "speedometer_sweep"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = dimensions.speedometerDialStroke.toPx()
            val tickInset = 10.dp.toPx()
            val arcMargin = strokeWidth + tickInset
            val arcSize = size.width - arcMargin * 2f
            val topLeft = Offset(arcMargin, arcMargin)
            val center = Offset(size.width / 2f, size.height / 2f)

            // Calibrated outer tick ring (37 ticks over 240 deg)
            val tickOuterR = size.width / 2f - 2.dp.toPx()
            val totalTicks = 36
            for (i in 0..totalTicks) {
                val frac = i / totalTicks.toFloat()
                val deg = 150f + frac * 240f
                val rad = Math.toRadians(deg.toDouble())
                val isMajor = i % 6 == 0
                val innerR = tickOuterR - (if (isMajor) 7.dp.toPx() else 4.dp.toPx())
                val isLit = frac <= animatedSweep
                drawLine(
                    color = if (isLit) glassStyle.accentPrimary.copy(alpha = 0.85f) else glassStyle.textTertiary.copy(alpha = 0.25f),
                    start = Offset(
                        center.x + innerR * cos(rad).toFloat(),
                        center.y + innerR * sin(rad).toFloat()
                    ),
                    end = Offset(
                        center.x + tickOuterR * cos(rad).toFloat(),
                        center.y + tickOuterR * sin(rad).toFloat()
                    ),
                    strokeWidth = if (isMajor) 2.dp.toPx() else 1.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Track arc (240 deg)
            drawArc(
                color = glassStyle.progressTrackBg,
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active luminous refractive arc
            val activeSweep = 240f * animatedSweep
            drawArc(
                brush = Brush.sweepGradient(
                    0.4f to glassStyle.accentPrimary,
                    0.8f to glassStyle.accentSecondary,
                    1.0f to glassStyle.accentPrimary
                ),
                startAngle = 150f,
                sweepAngle = activeSweep,
                useCenter = false,
                topLeft = topLeft,
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Specular tip pearl dot
            val angleRad = Math.toRadians((150f + activeSweep).toDouble())
            val radius = arcSize / 2f
            val cx = center.x + radius * cos(angleRad).toFloat()
            val cy = center.y + radius * sin(angleRad).toFloat()

            drawCircle(
                color = glassStyle.accentPrimary.copy(alpha = 0.35f),
                radius = 9.dp.toPx(),
                center = Offset(cx, cy)
            )
            drawCircle(
                color = Color.White,
                radius = 5.5.dp.toPx(),
                center = Offset(cx, cy)
            )
        }

        val speedTestLabel = if (appLanguage == AppLanguage.ARABIC) "قياس سرعة الإنترنت" else "Test Internet Speed"
        Box(
            modifier = Modifier
                .size(dimensions.speedometerInnerWell)
                .tactileSpringPress(pressedScale = 0.94f, onClick = onOpenSpeedTest)
                .iosLiquidGlassSurface(
                    shape = CircleShape,
                    tintColor = glassStyle.accentPrimary,
                    isFilledAccent = false,
                    elevation = if (glassStyle.iosLiquidGlassEnabled) 8.dp else 2.dp
                )
                .clickable(
                    role = Role.Button,
                    onClickLabel = speedTestLabel,
                    onClick = onOpenSpeedTest
                )
                .testTag("speedometer_open_speed_test"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = speedTestLabel,
                    tint = glassStyle.accentPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = formattedSpeed,
                    fontFamily = HyperMonoFontFamily,
                    color = glassStyle.textPrimary,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = AppStrings.liveBandwidth(appLanguage),
                        color = glassStyle.textTertiary,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = speedTestLabel,
                        tint = glassStyle.accentPrimary,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SpeedTelemetryCard(
    title: String,
    speed: String,
    peak: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    GlassContainer(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        borderColor = accentColor.copy(alpha = 0.35f),
        contentPadding = PaddingValues(15.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IosLiquidGlassIconBadge(
                    icon = icon,
                    contentDescription = title,
                    size = 30.dp,
                    iconSize = 15.dp,
                    tintColor = accentColor,
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = glassStyle.textSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = speed,
                fontFamily = HyperMonoFontFamily,
                color = glassStyle.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = peak,
                fontFamily = HyperMonoFontFamily,
                color = glassStyle.textTertiary,
                fontSize = 10.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DiagnosticRow(
    icon: ImageVector,
    tint: Color,
    label: String,
    value: String,
    useMono: Boolean = false
) {
    val glassStyle = LocalLiquidGlass.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            IosLiquidGlassIconBadge(
                icon = icon,
                contentDescription = null,
                size = 28.dp,
                iconSize = 15.dp,
                tintColor = tint,
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                color = glassStyle.textSecondary,
                fontSize = 12.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontFamily = if (useMono) HyperMonoFontFamily else com.example.ui.theme.HyperDisplayFontFamily,
            color = glassStyle.textPrimary,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
