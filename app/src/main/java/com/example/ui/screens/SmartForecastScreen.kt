package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.ForecastStatus
import com.example.model.SmartDataForecast
import com.example.ui.MainViewModel
import com.example.ui.components.GlassContainer
import com.example.ui.components.IosLiquidGlassIconBadge
import com.example.ui.components.SmartDataForecastingCard
import com.example.ui.components.iosLiquidGlassSurface
import com.example.ui.navigation.LocalFloatingChromePadding
import com.example.ui.theme.HyperMonoFontFamily
import com.example.ui.theme.LocalAppDimensions
import com.example.ui.theme.LocalLiquidGlass

/**
 * Dedicated "Smart Analytics & Forecast" screen for HyperNet in Apple iOS 26 Liquid Glass styling.
 */
@Composable
fun SmartForecastScreen(
    viewModel: MainViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (onNavigateBack != null) {
        BackHandler { onNavigateBack() }
    }
    val dataForecast by viewModel.dataForecast.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current
    val chromePadding = LocalFloatingChromePadding.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.screenPaddingHorizontal),
        contentPadding = PaddingValues(
            top = chromePadding.calculateTopPadding() + 6.dp,
            bottom = chromePadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(dimensions.sectionSpacing)
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IosLiquidGlassIconBadge(
                        icon = Icons.Default.Insights,
                        contentDescription = null,
                        size = dimensions.headerIconBoxSize,
                        iconSize = dimensions.headerIconSize * 0.55f,
                        tintColor = glassStyle.accentPrimary,
                        shape = RoundedCornerShape(14.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.smartAnalyticsTitle(appLanguage),
                            color = glassStyle.textPrimary,
                            fontSize = dimensions.textTitleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = AppStrings.smartAnalyticsSubtitle(appLanguage),
                            color = glassStyle.textSecondary,
                            fontSize = dimensions.textCaption,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        if (dimensions.isTablet) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.gridSpacing)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(dimensions.gridSpacing)
                    ) {
                        SmartDataForecastingCard(
                            forecast = dataForecast,
                            appLanguage = appLanguage,
                            onConfigurePlanClick = onNavigateToSettings
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(dimensions.gridSpacing)
                    ) {
                        CycleRunwayCard(
                            forecast = dataForecast,
                            appLanguage = appLanguage
                        )

                        SmartRunwayTipsCard(
                            forecast = dataForecast,
                            appLanguage = appLanguage
                        )
                    }
                }
            }
        } else {
            item {
                SmartDataForecastingCard(
                    forecast = dataForecast,
                    appLanguage = appLanguage,
                    onConfigurePlanClick = onNavigateToSettings
                )
            }

            item {
                CycleRunwayCard(
                    forecast = dataForecast,
                    appLanguage = appLanguage
                )
            }

            item {
                SmartRunwayTipsCard(
                    forecast = dataForecast,
                    appLanguage = appLanguage
                )
            }
        }
    }
}

@Composable
private fun CycleRunwayCard(
    forecast: SmartDataForecast,
    appLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    val velocityColor = when {
        forecast.status == ForecastStatus.CRITICAL_DEPLETION || forecast.status == ForecastStatus.EXHAUSTED -> glassStyle.statusDanger
        forecast.willDepleteBeforeCycleEnd -> glassStyle.statusWarning
        else -> glassStyle.statusSafe
    }

    val velocityText = when {
        forecast.status == ForecastStatus.EXHAUSTED -> AppStrings.statusExhausted(appLanguage)
        forecast.willDepleteBeforeCycleEnd -> "${AppStrings.burnVelocityFast(appLanguage)} (${String.format("%.1fx", forecast.burnRateVelocityRatio)})"
        else -> AppStrings.burnVelocityOptimal(appLanguage)
    }

    GlassContainer(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        contentPadding = PaddingValues(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IosLiquidGlassIconBadge(
                        icon = Icons.Default.Timeline,
                        contentDescription = null,
                        size = 32.dp,
                        iconSize = 17.dp,
                        tintColor = glassStyle.accentPrimary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppStrings.cycleProgress(appLanguage),
                        color = glassStyle.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .iosLiquidGlassSurface(
                            shape = CircleShape,
                            tintColor = velocityColor,
                            isFilledAccent = false,
                            elevation = 0.dp
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = velocityText,
                        color = velocityColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val progressAnimated by animateFloatAsState(
                targetValue = forecast.cycleProgressPercent,
                animationSpec = tween(600, easing = FastOutSlowInEasing),
                label = "cycle_progress"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.cycleDaysElapsed(
                        elapsed = forecast.daysElapsedInCycle,
                        total = forecast.totalCycleDays,
                        lang = appLanguage
                    ),
                    color = glassStyle.textSecondary,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${forecast.daysRemainingInCycle} ${AppStrings.forecastDays(forecast.daysRemainingInCycle, appLanguage).split(" ").last()}",
                    fontFamily = HyperMonoFontFamily,
                    color = glassStyle.accentPrimary,
                    fontSize = 11.5.sp,
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressAnimated)
                        .height(9.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(glassStyle.accentSecondary, glassStyle.accentPrimary)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RunwaySubMetricBox(
                    label = AppStrings.safeRunwayBudget(appLanguage),
                    value = forecast.recommendedSafeDailyFormatted,
                    subtext = AppStrings.safeRunwayDesc(forecast.daysRemainingInCycle, appLanguage),
                    accentColor = glassStyle.statusSafe,
                    modifier = Modifier.weight(1f)
                )

                val burnSubtext = if (appLanguage == AppLanguage.ARABIC) {
                    if (forecast.willDepleteBeforeCycleEnd) "يتجاوز المعدل الآمن" else "ضمن المعدل الآمن"
                } else {
                    if (forecast.willDepleteBeforeCycleEnd) "Exceeds safe runway" else "Healthy pace"
                }
                RunwaySubMetricBox(
                    label = AppStrings.forecastBurnRate(appLanguage),
                    value = forecast.dailyBurnRateFormatted,
                    subtext = burnSubtext,
                    accentColor = if (forecast.willDepleteBeforeCycleEnd) glassStyle.statusWarning else glassStyle.accentPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RunwaySubMetricBox(
    label: String,
    value: String,
    subtext: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    Box(
        modifier = modifier
            .heightIn(min = 82.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(glassStyle.cardSubtleBg)
            .border(0.9.dp, glassStyle.borderColor.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = glassStyle.textTertiary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = value,
                fontFamily = HyperMonoFontFamily,
                color = accentColor,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtext,
                color = glassStyle.textSecondary,
                fontSize = 9.5.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SmartRunwayTipsCard(
    forecast: SmartDataForecast,
    appLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    val (tipText, tipColor, tipIcon) = when (forecast.status) {
        ForecastStatus.CRITICAL_DEPLETION, ForecastStatus.EXHAUSTED ->
            Triple(AppStrings.recCritical(appLanguage), glassStyle.statusDanger, Icons.Default.WarningAmber)
        ForecastStatus.EARLY_DEPLETION ->
            Triple(AppStrings.recWarning(appLanguage), glassStyle.statusWarning, Icons.Default.Speed)
        ForecastStatus.SUSTAINABLE ->
            Triple(AppStrings.recSafe(appLanguage), glassStyle.statusSafe, Icons.Default.AutoAwesome)
    }

    GlassContainer(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = if (!glassStyle.isNeumorphic) tipColor.copy(alpha = if (glassStyle.isDark) 0.10f else 0.08f) else null,
        borderColor = tipColor.copy(alpha = 0.40f),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IosLiquidGlassIconBadge(
                icon = tipIcon,
                contentDescription = null,
                size = 42.dp,
                iconSize = 20.dp,
                tintColor = tipColor,
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = AppStrings.recommendationsTitle(appLanguage),
                    color = tipColor,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tipText,
                    color = glassStyle.textPrimary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}
