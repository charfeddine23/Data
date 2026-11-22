package com.example.ui.components

import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.FormatUtils
import com.example.model.LiveSpeedSample
import com.example.model.NetworkType
import com.example.model.QuotaStatus
import com.example.ui.theme.HyperMonoFontFamily
import com.example.ui.theme.LocalAppDimensions
import com.example.ui.theme.LocalLiquidGlass
import com.example.ui.theme.contrastOnColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Apple iOS 26 Liquid Glass & Neumorphic Depth Container with multi-layered refractive translucency,
 * curved specular top meniscus glint, luminous chromatic rims, optional 60/120fps breathing glow,
 * and optional GPU-accelerated tactile spring press physics.
 */
@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    enableBlur: Boolean = true,
    enableBreathingGlow: Boolean = false,
    glowColor: Color? = null,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {
    val glassStyle = LocalLiquidGlass.current
    val effectiveBg = backgroundColor ?: glassStyle.surfaceColor
    val effectiveBorder = borderColor ?: glassStyle.borderColor
    val effectiveGlow = glowColor ?: effectiveBorder
    val useIosOpticalGlass = glassStyle.iosLiquidGlassEnabled && !glassStyle.isNeumorphic

    val breathingAlphaState = if (enableBreathingGlow) {
        val infiniteTransition = rememberInfiniteTransition(label = "glass_breathing_glow")
        infiniteTransition.animateFloat(
            initialValue = 0.35f,
            targetValue = 0.92f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glass_glow_alpha"
        )
    } else {
        null
    }

    val interactiveModifier = if (onClick != null) {
        modifier.tactileSpringPress(pressedScale = 0.975f, onClick = onClick)
    } else {
        modifier
    }

    Box(
        modifier = interactiveModifier
            .then(
                when {
                    glassStyle.isNeumorphic -> Modifier.neumorphicDepth(
                        shape = shape,
                        lightColor = glassStyle.neumorphicHighlight,
                        darkColor = glassStyle.neumorphicShadow,
                        offsetDp = 6.dp,
                        surfaceColor = effectiveBg,
                        blurDp = 12.dp
                    )
                    glassStyle.isDark -> Modifier.shadow(
                        elevation = if (useIosOpticalGlass) 10.dp else 4.dp,
                        shape = shape,
                        spotColor = effectiveGlow.copy(alpha = 0.24f),
                        ambientColor = glassStyle.neumorphicShadow.copy(alpha = 0.45f)
                    )
                    else -> Modifier.shadow(
                        elevation = if (useIosOpticalGlass) 10.dp else 4.dp,
                        shape = shape,
                        spotColor = Color(0x160F172A),
                        ambientColor = Color(0x0A0F172A)
                    )
                }
            )
            .clip(shape)
    ) {
        // Frosted Blur Layer (Android 12+)
        if (!glassStyle.isNeumorphic && enableBlur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(glassStyle.blurRadius)
                    .background(if (glassStyle.isDark) Color(0x52090C12) else Color(0x28FFFFFF))
            )
        }

        // Optical Translucent Body + Specular Rim + iOS 26 Curved Catchlight
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = when {
                        glassStyle.isNeumorphic -> Brush.verticalGradient(
                            colors = listOf(effectiveBg, glassStyle.neumorphicBase)
                        )
                        glassStyle.isDark -> Brush.verticalGradient(
                            colors = listOf(
                                effectiveBg.copy(alpha = if (useIosOpticalGlass) 0.78f else 0.94f),
                                glassStyle.cardSubtleBg.copy(alpha = if (useIosOpticalGlass) 0.68f else 0.90f)
                            )
                        )
                        else -> Brush.verticalGradient(
                            colors = listOf(
                                effectiveBg.copy(alpha = if (useIosOpticalGlass) 0.84f else 0.96f),
                                glassStyle.cardSubtleBg.copy(alpha = if (useIosOpticalGlass) 0.72f else 0.92f)
                            )
                        )
                    }
                )
                .then(
                    if (useIosOpticalGlass) {
                        Modifier.drawBehind {
                            // Crisp top edge optical shimmer strictly along the 1dp rim
                            drawLine(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = if (glassStyle.isDark) 0.34f else 0.72f),
                                        Color.Transparent
                                    )
                                ),
                                start = Offset(size.width * 0.10f, 1.dp.toPx()),
                                end = Offset(size.width * 0.90f, 1.dp.toPx()),
                                strokeWidth = 0.9.dp.toPx()
                            )
                        }
                    } else Modifier
                )
                .border(
                    width = if (useIosOpticalGlass) 1.05.dp else 1.dp,
                    brush = when {
                        glassStyle.isNeumorphic -> Brush.linearGradient(
                            colors = listOf(
                                glassStyle.neumorphicBorderLight,
                                if (borderColor != null) effectiveBorder.copy(alpha = 0.55f) else glassStyle.neumorphicBorderDark
                            )
                        )
                        glassStyle.isDark -> Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = if (useIosOpticalGlass) 0.34f else 0.16f),
                                effectiveBorder.copy(alpha = 0.52f),
                                glassStyle.accentSecondary.copy(alpha = if (useIosOpticalGlass) 0.24f else 0.12f),
                                Color.White.copy(alpha = if (useIosOpticalGlass) 0.18f else 0.08f)
                            )
                        )
                        else -> Brush.linearGradient(
                            colors = listOf(
                                Color(0xD9FFFFFF),
                                effectiveBorder.copy(alpha = 0.55f),
                                Color(0x73FFFFFF)
                            )
                        )
                    },
                    shape = shape
                )
                .then(
                    if (breathingAlphaState != null) {
                        Modifier.drawBehindCompat(
                            shape = shape,
                            glowColor = effectiveGlow,
                            alphaProvider = { breathingAlphaState.value }
                        )
                    } else {
                        Modifier
                    }
                )
        )

        // Card Content
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

private fun Modifier.drawBehindCompat(
    shape: RoundedCornerShape,
    glowColor: Color,
    alphaProvider: () -> Float
): Modifier = this.drawBehind {
    val a = alphaProvider()
    val cornerPx = shape.topStart.toPx(size, this)
    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(
                glowColor.copy(alpha = 0.52f * a),
                Color.White.copy(alpha = 0.18f * a),
                glowColor.copy(alpha = 0.42f * a)
            )
        ),
        cornerRadius = CornerRadius(cornerPx, cornerPx),
        style = Stroke(width = 1.4.dp.toPx())
    )
}

/**
 * Floating Translucent Apple iOS 26 Top App Bar anchored over scrolling content.
 */
@Composable
fun PersistentTopAppBar(
    speedSample: LiveSpeedSample,
    appLanguage: AppLanguage,
    isSettingsActive: Boolean,
    onOpenSettings: () -> Unit,
    isForecastActive: Boolean = false,
    onOpenForecast: () -> Unit = {},
    title: String = "HyperNet",
    onUsageTipClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalAppDimensions.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            DashboardHeader(
                speedSample = speedSample,
                appLanguage = appLanguage,
                onOpenSettings = onOpenSettings,
                isSettingsActive = isSettingsActive,
                isForecastActive = isForecastActive,
                onOpenForecast = onOpenForecast,
                title = title,
                onUsageTipClick = onUsageTipClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = dimensions.contentMaxWidth)
                    .padding(horizontal = dimensions.screenPaddingHorizontal, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun DashboardHeader(
    speedSample: LiveSpeedSample,
    appLanguage: AppLanguage,
    onOpenSettings: () -> Unit = {},
    isSettingsActive: Boolean = false,
    isForecastActive: Boolean = false,
    onOpenForecast: () -> Unit = {},
    title: String = "HyperNet",
    onUsageTipClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current
    val haptics = rememberAppHaptics()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.headerIconBoxSize.coerceAtLeast(48.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            // Optical Liquid Glass Medallion around the HyperNet Brand Mark
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .iosLiquidGlassSurface(
                        shape = CircleShape,
                        tintColor = glassStyle.accentPrimary,
                        isFilledAccent = false,
                        elevation = if (glassStyle.iosLiquidGlassEnabled) 4.dp else 0.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_brand_logo),
                    contentDescription = "HyperNet",
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Crossfade(targetState = title, label = "top_bar_title_crossfade") { displayTitle ->
                Text(
                    text = displayTitle,
                    color = glassStyle.textPrimary,
                    fontSize = dimensions.textTitleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.2).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right Dynamic Island-Inspired Controls Slot
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AnimatedContent(
                targetState = onUsageTipClick != null,
                transitionSpec = {
                    (fadeIn(tween(200)) + scaleIn(initialScale = 0.85f, animationSpec = tween(200))) togetherWith
                        (fadeOut(tween(150)) + scaleOut(targetScale = 0.85f, animationSpec = tween(150)))
                },
                contentAlignment = Alignment.CenterEnd,
                label = "top_bar_status_slot_transition"
            ) { showLightbulb ->
                if (showLightbulb && onUsageTipClick != null) {
                    DynamicGlowingLightbulbButton(
                        onClick = {
                            haptics.performConfirm()
                            onUsageTipClick()
                        }
                    )
                } else {
                    ConnectionStatusPill(speedSample = speedSample, appLanguage = appLanguage)
                }
            }

            TopBarSmartForecastButton(
                isSelected = isForecastActive,
                appLanguage = appLanguage,
                onClick = {
                    haptics.performSegmentSwitch()
                    onOpenForecast()
                }
            )

            LiquidGlassHamburgerMenuButton(
                onClick = {
                    haptics.performSegmentSwitch()
                    onOpenSettings()
                },
                isSelected = isSettingsActive,
                contentDescription = AppStrings.openSettingsMenu(appLanguage)
            )
        }
    }
}

@Composable
fun DynamicGlowingLightbulbButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val warmGold = glassStyle.statusWarning
    val warmAmber = if (glassStyle.isDark) Color(0xFFFF9100) else glassStyle.statusWarning

    val infiniteTransition = rememberInfiniteTransition(label = "lightbulb_dynamics")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bulb_glow_alpha"
    )
    val bulbScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bulb_scale"
    )
    val rayRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bulb_ray_rotation"
    )

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .testTag("analytics_tip_lightbulb"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val auraRadius = size.minDimension * 0.48f

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        warmGold.copy(alpha = 0.34f * glowAlpha),
                        warmAmber.copy(alpha = 0.14f * glowAlpha),
                        Color.Transparent
                    ),
                    center = center,
                    radius = auraRadius
                ),
                radius = auraRadius,
                center = center
            )

            val rayCount = 6
            val innerR = size.minDimension * 0.36f
            val outerR = size.minDimension * 0.45f
            for (i in 0 until rayCount) {
                val angleDeg = rayRotation + i * (360f / rayCount)
                val angleRad = (angleDeg * PI / 180.0).toFloat()
                val start = Offset(center.x + cos(angleRad) * innerR, center.y + sin(angleRad) * innerR)
                val end = Offset(center.x + cos(angleRad) * outerR, center.y + sin(angleRad) * outerR)
                drawLine(
                    color = warmGold.copy(alpha = 0.55f * glowAlpha),
                    start = start,
                    end = end,
                    strokeWidth = 1.6.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Box(
            modifier = Modifier
                .size(38.dp)
                .iosLiquidGlassSurface(
                    shape = CircleShape,
                    tintColor = warmGold,
                    isFilledAccent = false,
                    elevation = 4.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = "Usage Tip",
                tint = warmGold,
                modifier = Modifier
                    .size(20.dp)
                    .graphicsLayer {
                        scaleX = bulbScale
                        scaleY = bulbScale
                    }
            )
        }
    }
}

@Composable
fun AnalyticsTipPopupDialog(
    appLanguage: AppLanguage,
    onDismiss: () -> Unit
) {
    val glassStyle = LocalLiquidGlass.current
    val warmGold = glassStyle.statusWarning
    val scaleAnim = remember { Animatable(0.86f) }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .widthIn(max = 380.dp)
                .graphicsLayer {
                    scaleX = scaleAnim.value
                    scaleY = scaleAnim.value
                }
        ) {
            GlassContainer(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                borderColor = warmGold.copy(alpha = 0.6f),
                enableBreathingGlow = true,
                glowColor = warmGold
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IosLiquidGlassIconBadge(
                        icon = Icons.Default.Lightbulb,
                        contentDescription = null,
                        size = 60.dp,
                        iconSize = 30.dp,
                        tintColor = warmGold,
                        shape = CircleShape
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .iosLiquidGlassSurface(
                                shape = RoundedCornerShape(12.dp),
                                tintColor = warmGold,
                                isFilledAccent = false,
                                elevation = 0.dp
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) "تخصيص ذكي للتطبيقات" else "SMART APP CONTROLS",
                            color = warmGold,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (appLanguage == AppLanguage.ARABIC) {
                            "اضغط مطولاً على أي تطبيق لتخصيص الحد اليومي وإشعاراته"
                        } else {
                            "Long-press any app to customize its daily data limit & alerts"
                        },
                        color = glassStyle.textPrimary,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 23.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    IosLiquidGlassButton(
                        onClick = onDismiss,
                        containerColor = glassStyle.accentPrimary,
                        shape = RoundedCornerShape(22.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("analytics_tip_dismiss_button")
                    ) {
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) "حسناً، فهمت" else "Got it",
                            color = glassStyle.onAccentPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiquidGlassHamburgerMenuButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    contentDescription: String = "Open Settings"
) {
    val glassStyle = LocalLiquidGlass.current
    val shape = CircleShape

    Box(
        modifier = modifier
            .size(40.dp)
            .tactileSpringPress(pressedScale = 0.89f, onClick = onClick)
            .iosLiquidGlassSurface(
                shape = shape,
                tintColor = if (isSelected) glassStyle.accentPrimary else glassStyle.accentSecondary,
                isFilledAccent = isSelected,
                elevation = if (glassStyle.iosLiquidGlassEnabled) 4.dp else 0.dp
            )
            .testTag("top_bar_settings_menu_button"),
        contentAlignment = Alignment.Center
    ) {
        val lineColorTop = if (isSelected) glassStyle.onAccentPrimary else glassStyle.accentPrimary
        val lineColorMid = if (isSelected) glassStyle.onAccentPrimary else glassStyle.textPrimary
        val lineColorBot = if (isSelected) glassStyle.onAccentPrimary else glassStyle.accentSecondary

        Column(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(3.5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(2.dp)
                    .clip(CircleShape)
                    .background(lineColorTop)
            )
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(2.dp)
                    .clip(CircleShape)
                    .background(lineColorMid)
            )
            Box(
                modifier = Modifier
                    .width(11.dp)
                    .height(2.dp)
                    .clip(CircleShape)
                    .background(lineColorBot)
            )
        }
    }
}

@Composable
fun TopBarSmartForecastButton(
    isSelected: Boolean,
    appLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current
    val infiniteTransition = rememberInfiniteTransition(label = "top_forecast_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "top_forecast_alpha"
    )

    val label = AppStrings.navForecast(appLanguage)
    val forecastShape = CircleShape

    Box(
        modifier = modifier
            .height(40.dp)
            .tactileSpringPress(pressedScale = 0.89f, onClick = onClick)
            .iosLiquidGlassSurface(
                shape = forecastShape,
                tintColor = glassStyle.accentPrimary,
                isFilledAccent = isSelected,
                elevation = if (glassStyle.iosLiquidGlassEnabled) 4.dp else 0.dp
            )
            .then(
                if (!isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                glassStyle.accentPrimary.copy(alpha = 0.55f * glowAlpha),
                                glassStyle.accentSecondary.copy(alpha = 0.35f * glowAlpha)
                            )
                        ),
                        shape = forecastShape
                    )
                } else Modifier
            )
            .padding(horizontal = if (dimensions.isTablet) 12.dp else 10.dp)
            .testTag("top_bar_smart_forecast_button"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = label,
                tint = if (isSelected) glassStyle.onAccentPrimary else glassStyle.accentPrimary,
                modifier = Modifier.size(18.dp)
            )
            if (dimensions.isTablet) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    color = if (isSelected) glassStyle.onAccentPrimary else glassStyle.textSecondary,
                    fontSize = 11.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ConnectionStatusPill(
    speedSample: LiveSpeedSample,
    appLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val (icon, tint, label) = when (speedSample.connectionType) {
        NetworkType.WIFI -> Triple(Icons.Default.Wifi, glassStyle.accentPrimary, AppStrings.wifiActive(appLanguage))
        NetworkType.CELLULAR_5G -> Triple(Icons.Default.SignalCellularAlt, glassStyle.accentSecondary, AppStrings.cellular5g(appLanguage))
        NetworkType.CELLULAR_4G, NetworkType.CELLULAR_3G, NetworkType.CELLULAR_GENERIC ->
            Triple(Icons.Default.SignalCellularAlt, glassStyle.accentSecondary, AppStrings.cellularGeneric(appLanguage))
        NetworkType.ETHERNET -> Triple(Icons.Default.Wifi, glassStyle.statusSafe, AppStrings.ethernet(appLanguage))
        NetworkType.OFFLINE -> Triple(Icons.Default.WifiOff, glassStyle.statusDanger, AppStrings.disconnected(appLanguage))
    }

    val isOnline = speedSample.connectionType != NetworkType.OFFLINE
    val pulseAlpha by rememberInfiniteTransition(label = "conn_dot_pulse").animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(1100), RepeatMode.Reverse),
        label = "conn_dot_alpha"
    )

    Box(
        modifier = modifier
            .height(36.dp)
            .iosLiquidGlassSurface(
                shape = CircleShape,
                tintColor = tint,
                isFilledAccent = false,
                elevation = if (glassStyle.iosLiquidGlassEnabled) 3.dp else 0.dp
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = if (isOnline) pulseAlpha else 0.75f))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                color = glassStyle.textPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LiveSpeedCard(
    speedSample: LiveSpeedSample,
    speedHistory: List<Float>,
    appLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    GlassContainer(
        modifier = modifier.fillMaxWidth(),
        borderColor = glassStyle.accentPrimary.copy(alpha = 0.45f),
        enableBreathingGlow = true,
        glowColor = glassStyle.accentPrimary
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.liveSpeedTitle(appLanguage),
                    color = glassStyle.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Box(
                    modifier = Modifier
                        .iosLiquidGlassSurface(
                            shape = CircleShape,
                            tintColor = glassStyle.accentPrimary,
                            isFilledAccent = false,
                            elevation = 0.dp
                        )
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${speedSample.pingMs} ${AppStrings.ms(appLanguage)}",
                        color = glassStyle.accentPrimary,
                        fontSize = 11.sp,
                        fontFamily = HyperMonoFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SpeedMetricItem(
                    title = AppStrings.download(appLanguage),
                    speedFormatted = speedSample.downloadFormatted,
                    peakSpeed = "${AppStrings.peak(appLanguage)}: ${FormatUtils.formatSpeed(speedSample.peakDownloadBps)}",
                    icon = Icons.Default.ArrowDownward,
                    color = glassStyle.accentPrimary,
                    modifier = Modifier.weight(1f)
                )

                SpeedMetricItem(
                    title = AppStrings.upload(appLanguage),
                    speedFormatted = speedSample.uploadFormatted,
                    peakSpeed = "${AppStrings.peak(appLanguage)}: ${FormatUtils.formatSpeed(speedSample.peakUploadBps)}",
                    icon = Icons.Default.ArrowUpward,
                    color = glassStyle.accentSecondary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            SpeedSparkline(
                dataPoints = speedHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
        }
    }
}

@Composable
fun SpeedMetricItem(
    title: String,
    speedFormatted: String,
    peakSpeed: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    Box(
        modifier = modifier
            .iosLiquidGlassSurface(
                shape = RoundedCornerShape(18.dp),
                tintColor = color,
                isFilledAccent = false,
                elevation = 2.dp
            )
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IosLiquidGlassIconBadge(
                    icon = icon,
                    contentDescription = title,
                    size = 28.dp,
                    iconSize = 15.dp,
                    tintColor = color,
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = glassStyle.textSecondary,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = speedFormatted,
                color = glassStyle.textPrimary,
                fontSize = 19.sp,
                fontFamily = HyperMonoFontFamily,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = peakSpeed,
                color = glassStyle.textTertiary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun SpeedSparkline(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    Canvas(modifier = modifier) {
        if (dataPoints.isEmpty()) return@Canvas

        val maxVal = (dataPoints.maxOrNull() ?: 1f).coerceAtLeast(1f)
        val stepX = size.width / (dataPoints.size - 1).coerceAtLeast(1)

        val path = Path()
        val fillPath = Path()

        dataPoints.forEachIndexed { index, value ->
            val x = index * stepX
            val normalized = (value / maxVal).coerceIn(0f, 1f)
            val y = size.height - (normalized * (size.height * 0.8f)) - (size.height * 0.1f)

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, size.height)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }

        fillPath.lineTo(size.width, size.height)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    glassStyle.accentPrimary.copy(alpha = if (glassStyle.isDark) 0.25f else 0.15f),
                    glassStyle.accentPrimary.copy(alpha = 0.0f)
                )
            )
        )

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(glassStyle.accentSecondary, glassStyle.accentPrimary)
            ),
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )

        val lastIdx = dataPoints.size - 1
        val lastX = lastIdx * stepX
        val lastNorm = (dataPoints[lastIdx] / maxVal).coerceIn(0f, 1f)
        val lastY = size.height - (lastNorm * (size.height * 0.8f)) - (size.height * 0.1f)
        drawCircle(
            color = glassStyle.accentPrimary,
            radius = 4.dp.toPx(),
            center = Offset(lastX, lastY)
        )
    }
}

/**
 * Hero Spatial iOS 26 Liquid Glass Quota Progress Gauge.
 * Combines calibrated dial tick marks, glowing active sweep arc, the Biologically Living &
 * Freezing Heart engine, inline Used/Remaining/Cycle KPI capsules, and interactive Edit Cap pill.
 */
@Composable
fun CircularQuotaProgressGauge(
    usedBytes: Long,
    limitBytes: Long,
    percentage: Float,
    status: QuotaStatus,
    daysRemaining: Int,
    appLanguage: AppLanguage,
    onConfigureClick: () -> Unit,
    isInternetConnected: Boolean = true,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current
    val statusColor = when (status) {
        QuotaStatus.SAFE -> glassStyle.accentPrimary
        QuotaStatus.WARNING -> glassStyle.statusWarning
        QuotaStatus.CRITICAL, QuotaStatus.EXCEEDED -> glassStyle.statusDanger
    }

    val isArabic = (appLanguage == AppLanguage.ARABIC)
    val heartParams = remember(percentage, isArabic, glassStyle.isDark) {
        BiologicalHeartCalculator.calculate(percentage, isArabic, glassStyle.isDark)
    }

    val animatedEngineColor by animateColorAsState(
        targetValue = heartParams.primaryColor,
        animationSpec = tween(durationMillis = 500),
        label = "gauge_engine_color"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "live_color_shift_transition")
    val colorShiftAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "live_color_shift"
    )

    val animatedSweep by animateFloatAsState(
        targetValue = (percentage / 100f).coerceIn(0.01f, 1f),
        animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessLow),
        label = "circular_quota_sweep"
    )

    val dynamicLiveColors = remember(colorShiftAngle) {
        val rad = Math.toRadians(colorShiftAngle.toDouble())
        val cosVal = ((cos(rad) + 1.0) / 2.0).toFloat()
        val sinVal = ((sin(rad) + 1.0) / 2.0).toFloat()

        val liveColorA = Color(
            red = 0.05f + 0.50f * cosVal,
            green = 0.70f + 0.25f * (1f - cosVal),
            blue = 0.90f + 0.10f * sinVal,
            alpha = 0.65f
        )
        val liveColorB = Color(
            red = 0.65f + 0.30f * sinVal,
            green = 0.15f + 0.20f * cosVal,
            blue = 0.95f,
            alpha = 0.58f
        )
        val liveColorC = Color(
            red = 0.00f,
            green = 0.85f + 0.15f * sinVal,
            blue = 0.75f + 0.25f * cosVal,
            alpha = 0.50f
        )
        listOf(liveColorA, liveColorB, liveColorC)
    }

    val remainingBytes = (limitBytes - usedBytes).coerceAtLeast(0L)

    GlassContainer(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        borderColor = animatedEngineColor.copy(alpha = 0.45f),
        enableBreathingGlow = true,
        glowColor = animatedEngineColor,
        contentPadding = PaddingValues(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row: Monthly Plan Label + Edit Cap Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = AppStrings.monthlyPlanTitle(appLanguage),
                        color = glassStyle.textSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                    if (status != QuotaStatus.SAFE) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = "Warning",
                            tint = animatedEngineColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .tactileSpringPress(pressedScale = 0.93f, onClick = onConfigureClick)
                        .iosLiquidGlassSurface(
                            shape = CircleShape,
                            tintColor = animatedEngineColor,
                            isFilledAccent = false,
                            elevation = 3.dp
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = AppStrings.editCap(appLanguage),
                            tint = animatedEngineColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = AppStrings.editCap(appLanguage),
                            color = animatedEngineColor,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Central Optical Gauge Stage
            Box(
                modifier = Modifier.size(dimensions.circularGaugeSize + 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = dimensions.circularGaugeStroke.toPx()
                    val inset = strokeWidth * 1.4f
                    val arcSize = size.width - inset * 2f
                    val topLeft = Offset(inset, inset)
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val tickOuterR = size.minDimension * 0.495f
                    val tickInnerR = tickOuterR - 4.5.dp.toPx()

                    // Precision outer tick marks (60)
                    for (i in 0 until 60) {
                        val angleRad = (i * 6.0 - 90.0) * PI / 180.0
                        val isMajor = i % 5 == 0
                        val r0 = if (isMajor) tickInnerR - 2.dp.toPx() else tickInnerR
                        drawLine(
                            color = glassStyle.textTertiary.copy(alpha = if (isMajor) 0.35f else 0.16f),
                            start = Offset(cx + cos(angleRad).toFloat() * r0, cy + sin(angleRad).toFloat() * r0),
                            end = Offset(cx + cos(angleRad).toFloat() * tickOuterR, cy + sin(angleRad).toFloat() * tickOuterR),
                            strokeWidth = if (isMajor) 1.4.dp.toPx() else 0.9.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }

                    // Track Background Circle
                    drawArc(
                        color = glassStyle.progressTrackBg,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = Size(arcSize, arcSize),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Active Refractive Sweep Arc
                    val sweepAngle = 360f * animatedSweep
                    drawArc(
                        brush = Brush.sweepGradient(
                            0.0f to dynamicLiveColors[0],
                            0.45f to dynamicLiveColors[1],
                            0.85f to animatedEngineColor,
                            1.0f to animatedEngineColor
                        ),
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = topLeft,
                        size = Size(arcSize, arcSize),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Subtle specular highlight dot at active arc tip
                    val tipRad = (-90.0 + sweepAngle) * PI / 180.0
                    val arcR = arcSize / 2f
                    val tipX = cx + cos(tipRad).toFloat() * arcR
                    val tipY = cy + sin(tipRad).toFloat() * arcR
                    drawCircle(
                        color = Color.White.copy(alpha = 0.88f),
                        radius = strokeWidth * 0.32f,
                        center = Offset(tipX, tipY)
                    )
                }

                // Heartbeat Center Content
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    BiologicallyLivingAndFreezingHeart(
                        percentage = percentage,
                        isInternetConnected = isInternetConnected,
                        appLanguage = appLanguage,
                        heartSize = if (dimensions.isTablet) 34.dp else 28.dp,
                        auraSize = if (dimensions.isTablet) 96.dp else 78.dp,
                        showTitle = true
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "%.1f%%".format(percentage),
                        color = animatedEngineColor,
                        fontSize = (dimensions.textMetricValue.value + 4f).sp,
                        fontFamily = HyperMonoFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "${FormatUtils.formatBytes(usedBytes)} / ${FormatUtils.formatBytes(limitBytes)}",
                        color = glassStyle.textSecondary,
                        fontSize = dimensions.textCaption,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3-Pill Optical Glass Quick KPI Strip: Used | Remaining | Cycle Days
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GaugeKpiMiniPill(
                    label = AppStrings.used(appLanguage),
                    value = FormatUtils.formatBytes(usedBytes),
                    tint = glassStyle.accentSecondary,
                    modifier = Modifier.weight(1f)
                )
                GaugeKpiMiniPill(
                    label = AppStrings.remaining(appLanguage),
                    value = FormatUtils.formatBytes(remainingBytes),
                    tint = animatedEngineColor,
                    modifier = Modifier.weight(1f)
                )
                GaugeKpiMiniPill(
                    label = if (isArabic) "الدورة" else "Cycle",
                    value = if (isArabic) "$daysRemaining يوم" else "${daysRemaining}d left",
                    tint = glassStyle.accentPrimary,
                    modifier = Modifier.weight(0.9f)
                )
            }

            // Warning Banner if >= 80% or >= 95%
            if (status != QuotaStatus.SAFE) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .iosLiquidGlassSurface(
                            shape = RoundedCornerShape(14.dp),
                            tintColor = statusColor,
                            isFilledAccent = false,
                            elevation = 0.dp
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    val alertText = when (status) {
                        QuotaStatus.WARNING -> AppStrings.alert80Warning(appLanguage)
                        QuotaStatus.CRITICAL -> AppStrings.alert95Critical(appLanguage)
                        QuotaStatus.EXCEEDED -> AppStrings.quotaExceeded(appLanguage)
                        else -> ""
                    }
                    Text(
                        text = alertText,
                        color = statusColor,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GaugeKpiMiniPill(
    label: String,
    value: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    Column(
        modifier = modifier
            .iosLiquidGlassSurface(
                shape = RoundedCornerShape(16.dp),
                tintColor = tint,
                isFilledAccent = false,
                elevation = 1.dp
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = glassStyle.textTertiary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = glassStyle.textPrimary,
            fontSize = 12.sp,
            fontFamily = HyperMonoFontFamily,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun QuotaProgressGauge(
    usedBytes: Long,
    limitBytes: Long,
    percentage: Float,
    status: QuotaStatus,
    daysRemaining: Int,
    onConfigureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CircularQuotaProgressGauge(
        usedBytes = usedBytes,
        limitBytes = limitBytes,
        percentage = percentage,
        status = status,
        daysRemaining = daysRemaining,
        appLanguage = AppLanguage.ARABIC,
        onConfigureClick = onConfigureClick,
        modifier = modifier
    )
}

@Composable
fun UsagePermissionCard(
    onRequestPermission: () -> Unit,
    onOpenAppInfo: () -> Unit,
    appLanguage: AppLanguage = AppLanguage.ARABIC,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    var showRestrictedDialog by remember { mutableStateOf(false) }

    GlassContainer(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        borderColor = glassStyle.statusWarning.copy(alpha = 0.6f)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IosLiquidGlassIconBadge(
                    icon = Icons.Default.Security,
                    contentDescription = AppStrings.usageAccess(appLanguage),
                    size = 38.dp,
                    iconSize = 20.dp,
                    tintColor = glassStyle.statusWarning,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = AppStrings.permCardTitle(appLanguage),
                    color = glassStyle.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = AppStrings.privacySafe(appLanguage),
                    tint = glassStyle.statusSafe,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = AppStrings.privacySafe(appLanguage),
                    color = glassStyle.statusSafe,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = AppStrings.permCardDesc(appLanguage),
                color = glassStyle.textSecondary,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IosLiquidGlassButton(
                    onClick = onRequestPermission,
                    containerColor = glassStyle.statusWarning,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("grant_usage_permission_button")
                ) {
                    Text(
                        text = AppStrings.grantPerm(appLanguage),
                        color = contrastOnColor(glassStyle.statusWarning),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                    )
                }

                IosLiquidGlassButton(
                    onClick = { showRestrictedDialog = true },
                    containerColor = glassStyle.accentPrimary,
                    isFilledAccent = false,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("restricted_settings_help_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Help",
                        tint = glassStyle.accentPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = AppStrings.restrictedSettingsHelp(appLanguage),
                        color = glassStyle.accentPrimary,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    if (showRestrictedDialog) {
        RestrictedSettingsGuideDialog(
            onDismiss = { showRestrictedDialog = false },
            onOpenAppInfo = onOpenAppInfo,
            onOpenUsageSettings = onRequestPermission,
            appLanguage = appLanguage
        )
    }
}

@Composable
fun RestrictedSettingsGuideDialog(
    onDismiss: () -> Unit,
    onOpenAppInfo: () -> Unit,
    onOpenUsageSettings: () -> Unit,
    appLanguage: AppLanguage = AppLanguage.ARABIC
) {
    val glassStyle = LocalLiquidGlass.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .widthIn(max = 420.dp)
        ) {
            GlassContainer(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                borderColor = glassStyle.accentPrimary.copy(alpha = 0.55f)
            ) {
                Column {
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
                                icon = Icons.Default.LockOpen,
                                contentDescription = null,
                                size = 36.dp,
                                iconSize = 18.dp,
                                tintColor = glassStyle.statusWarning,
                                shape = CircleShape
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = AppStrings.restrictedDialogTitle(appLanguage),
                                color = glassStyle.textPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IosLiquidGlassIconButton(
                            onClick = onDismiss,
                            icon = Icons.Default.Close,
                            contentDescription = "Close",
                            visualSize = 34.dp,
                            iconSize = 16.dp,
                            tintColor = glassStyle.textSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = AppStrings.restrictedDialogDesc(appLanguage),
                        color = glassStyle.textSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    GuideStepItem(
                        stepNumber = "1",
                        title = AppStrings.step1Title(appLanguage),
                        description = AppStrings.step1Desc(appLanguage)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GuideStepItem(
                        stepNumber = "2",
                        title = AppStrings.step2Title(appLanguage),
                        description = AppStrings.step2Desc(appLanguage)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GuideStepItem(
                        stepNumber = "3",
                        title = AppStrings.step3Title(appLanguage),
                        description = AppStrings.step3Desc(appLanguage)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IosLiquidGlassButton(
                            onClick = {
                                onOpenAppInfo()
                                onDismiss()
                            },
                            containerColor = glassStyle.accentPrimary,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = null,
                                tint = glassStyle.onAccentPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = AppStrings.btnAppInfo(appLanguage),
                                color = glassStyle.onAccentPrimary,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IosLiquidGlassButton(
                            onClick = {
                                onOpenUsageSettings()
                                onDismiss()
                            },
                            containerColor = glassStyle.accentPrimary,
                            isFilledAccent = false,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = AppStrings.btnUsagePerm(appLanguage),
                                color = glassStyle.accentPrimary,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GuideStepItem(
    stepNumber: String,
    title: String,
    description: String
) {
    val glassStyle = LocalLiquidGlass.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .iosLiquidGlassSurface(
                shape = RoundedCornerShape(14.dp),
                tintColor = glassStyle.accentPrimary,
                isFilledAccent = false,
                elevation = 0.dp
            )
            .padding(11.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .iosLiquidGlassSurface(
                    shape = CircleShape,
                    tintColor = glassStyle.accentPrimary,
                    isFilledAccent = true,
                    elevation = 2.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                color = glassStyle.onAccentPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                color = glassStyle.textPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                color = glassStyle.textTertiary,
                fontSize = 11.sp,
                lineHeight = 15.5.sp
            )
        }
    }
}
