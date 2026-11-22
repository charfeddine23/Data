package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.ForecastStatus
import com.example.model.FormatUtils
import com.example.model.SmartDataForecast
import com.example.ui.theme.HyperMonoFontFamily
import com.example.ui.theme.LocalLiquidGlass

/**
 * Harmonized Apple Health / iOS 26 Liquid Glass "Smart Data Forecasting" card.
 * Uses optical glass containers, crisp monospace telemetry pills, and spring-animated controls.
 */
@Composable
fun SmartDataForecastingCard(
    forecast: SmartDataForecast,
    appLanguage: AppLanguage,
    onConfigurePlanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    val statusColor = when (forecast.status) {
        ForecastStatus.CRITICAL_DEPLETION, ForecastStatus.EXHAUSTED -> glassStyle.statusDanger
        ForecastStatus.EARLY_DEPLETION -> glassStyle.statusWarning
        ForecastStatus.SUSTAINABLE -> glassStyle.accentPrimary
    }

    val infiniteTransition = rememberInfiniteTransition(label = "forecast_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = if (forecast.status == ForecastStatus.CRITICAL_DEPLETION) 0.85f else 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = statusColor.copy(alpha = glowAlpha),
        label = "border_color"
    )

    GlassContainer(
        modifier = modifier
            .fillMaxWidth()
            .testTag("smart_data_forecasting_card"),
        shape = RoundedCornerShape(26.dp),
        borderColor = animatedBorderColor,
        enableBreathingGlow = (forecast.status != ForecastStatus.SUSTAINABLE),
        glowColor = statusColor,
        contentPadding = PaddingValues(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. Header: Optical Icon Badge + Title + Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IosLiquidGlassIconBadge(
                        icon = Icons.Default.AutoAwesome,
                        contentDescription = AppStrings.smartForecastTitle(appLanguage),
                        size = 42.dp,
                        iconSize = 20.dp,
                        tintColor = statusColor,
                        shape = CircleShape
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = AppStrings.smartForecastTitle(appLanguage),
                            color = glassStyle.textPrimary,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = AppStrings.smartForecastSubtitle(appLanguage),
                            color = glassStyle.textTertiary,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                val statusLabel = when (forecast.status) {
                    ForecastStatus.CRITICAL_DEPLETION -> AppStrings.statusCritical(appLanguage)
                    ForecastStatus.EARLY_DEPLETION -> AppStrings.statusEarlyWarning(appLanguage)
                    ForecastStatus.SUSTAINABLE -> AppStrings.statusSustainable(appLanguage)
                    ForecastStatus.EXHAUSTED -> AppStrings.statusExhausted(appLanguage)
                }

                Box(
                    modifier = Modifier
                        .iosLiquidGlassSurface(
                            shape = CircleShape,
                            tintColor = statusColor,
                            isFilledAccent = false,
                            elevation = 0.dp
                        )
                        .padding(horizontal = 11.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = statusLabel,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Frosted Warning / Status Callout Well
            val bannerBg = statusColor.copy(alpha = if (glassStyle.isDark) 0.13f else 0.10f)
            val bannerBorder = statusColor.copy(alpha = 0.42f)

            val bannerText = when (forecast.status) {
                ForecastStatus.EXHAUSTED -> AppStrings.exhaustedBanner(appLanguage)
                ForecastStatus.CRITICAL_DEPLETION -> AppStrings.warningCriticalBanner(
                    days = forecast.predictedDaysRemaining,
                    planCapGB = forecast.planLimitGB,
                    lang = appLanguage
                )
                ForecastStatus.EARLY_DEPLETION -> AppStrings.warningEarlyBanner(
                    days = forecast.predictedDaysRemaining,
                    cycleDays = forecast.daysRemainingInCycle,
                    lang = appLanguage
                )
                ForecastStatus.SUSTAINABLE -> {
                    val surplusFormatted = FormatUtils.formatBytes(forecast.projectedSurplusDeficitBytes.coerceAtLeast(0L))
                    AppStrings.safeBanner(
                        surplusFormatted = surplusFormatted,
                        daysLeft = forecast.daysRemainingInCycle,
                        lang = appLanguage
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(bannerBg)
                    .border(0.9.dp, bannerBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 11.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (forecast.status == ForecastStatus.SUSTAINABLE) Icons.Default.AutoAwesome else Icons.Default.WarningAmber,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = bannerText,
                        color = glassStyle.textPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Three Symmetrical Bento Metric Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ForecastMetricChip(
                    title = AppStrings.forecastBurnRate(appLanguage),
                    value = FormatUtils.formatBytes(forecast.dailyBurnRateBytes),
                    unit = if (appLanguage == AppLanguage.ARABIC) "/يوم" else "/day",
                    icon = Icons.Default.LocalFireDepartment,
                    accentColor = glassStyle.statusWarning,
                    modifier = Modifier.weight(1f)
                )

                val depletionValue = if (forecast.status == ForecastStatus.EXHAUSTED) {
                    if (appLanguage == AppLanguage.ARABIC) "منفدة" else "0 Days"
                } else {
                    AppStrings.forecastDays(forecast.predictedDaysRemaining, appLanguage)
                }
                ForecastMetricChip(
                    title = AppStrings.forecastDaysToDeplete(appLanguage),
                    value = depletionValue,
                    unit = "",
                    icon = Icons.Default.DateRange,
                    accentColor = statusColor,
                    modifier = Modifier.weight(1f)
                )

                val endProjectionFormatted = FormatUtils.formatBytes(forecast.projectedEndOfCycleUsageBytes)
                ForecastMetricChip(
                    title = AppStrings.forecastEndOfCycle(appLanguage),
                    value = endProjectionFormatted,
                    unit = "",
                    icon = if (forecast.isSurplus) Icons.Default.TrendingDown else Icons.Default.TrendingUp,
                    accentColor = if (forecast.isSurplus) glassStyle.statusSafe else glassStyle.statusDanger,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Layered Projected Consumption Progress Track
            val limit = forecast.planLimitBytes.toFloat().coerceAtLeast(1f)
            val currentUsedFraction = (forecast.usedBytes.toFloat() / limit).coerceIn(0f, 1f)
            val projectedFraction = (forecast.projectedEndOfCycleUsageBytes.toFloat() / limit).coerceIn(0f, 1.25f)

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${AppStrings.used(appLanguage)}: ${FormatUtils.formatBytes(forecast.usedBytes)} / ${forecast.planLimitGB.toInt()} GB",
                        fontFamily = HyperMonoFontFamily,
                        color = glassStyle.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    val deltaText = if (forecast.isSurplus) {
                        "+${FormatUtils.formatBytes(forecast.projectedSurplusDeficitBytes)} ${AppStrings.forecastSurplus(appLanguage)}"
                    } else {
                        "-${FormatUtils.formatBytes(-forecast.projectedSurplusDeficitBytes)} ${AppStrings.forecastDeficit(appLanguage)}"
                    }
                    Text(
                        text = deltaText,
                        fontFamily = HyperMonoFontFamily,
                        color = if (forecast.isSurplus) glassStyle.statusSafe else glassStyle.statusDanger,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp)
                        .clip(CircleShape)
                        .background(glassStyle.progressTrackBg)
                ) {
                    if (projectedFraction > currentUsedFraction) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(projectedFraction.coerceAtMost(1f))
                                .height(9.dp)
                                .clip(CircleShape)
                                .background(
                                    statusColor.copy(alpha = if (glassStyle.isDark) 0.34f else 0.24f)
                                )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(currentUsedFraction)
                            .height(9.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        glassStyle.accentSecondary,
                                        statusColor
                                    )
                                )
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer CTA Capsule to adjust monthly plan cap
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IosLiquidGlassButton(
                    onClick = onConfigurePlanClick,
                    containerColor = glassStyle.accentPrimary,
                    contentColor = glassStyle.accentPrimary,
                    isFilledAccent = false,
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${AppStrings.editCap(appLanguage)} (${forecast.planLimitGB.toInt()} GB) →",
                        color = glassStyle.accentPrimary,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Harmonized optical metric chip with tabular monospace numerals & soft frosted bezel.
 */
@Composable
private fun ForecastMetricChip(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    Box(
        modifier = modifier
            .heightIn(min = 78.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(glassStyle.cardSubtleBg)
            .border(0.9.dp, glassStyle.borderColor.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    color = glassStyle.textTertiary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontFamily = HyperMonoFontFamily,
                    color = glassStyle.textPrimary,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = unit,
                        color = glassStyle.textSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
