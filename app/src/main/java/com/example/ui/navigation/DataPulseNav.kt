package com.example.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.ui.MainViewModel
import com.example.ui.components.AnalyticsTipPopupDialog
import com.example.ui.components.AppUpdateDialog
import com.example.ui.components.LocalRootFlameOverlay
import com.example.ui.components.PersistentTopAppBar
import com.example.ui.components.RootFlameOverlayCanvas
import com.example.ui.components.RootFlameOverlayController
import com.example.ui.components.iosLiquidGlassSurface
import com.example.ui.components.neumorphicDepth
import com.example.ui.components.rememberAppHaptics
import com.example.ui.components.tactileSpringPress
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DnsControlScreen
import com.example.ui.screens.RankScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SmartForecastScreen
import com.example.ui.screens.SpeedMeterScreen
import com.example.ui.theme.AdaptiveScreenContainer
import com.example.ui.theme.LiquidGlassStyle
import com.example.ui.theme.LocalAppDimensions
import com.example.ui.theme.LocalLiquidGlass
import com.example.update.AppUpdateUiState
import kotlin.math.roundToInt

/**
 * Provides exact measured clearance padding for scrollable screens so cards glide
 * smoothly *behind* the translucent floating iOS 26 Top Bar and Bottom Capsule Dock
 * without clipping initial or final items.
 */
val LocalFloatingChromePadding = staticCompositionLocalOf {
    PaddingValues(top = 88.dp, bottom = 104.dp)
}

enum class NavDestination(val icon: ImageVector, val tag: String) {
    DASHBOARD(Icons.Default.GraphicEq, "nav_dashboard"),
    USAGE(Icons.Default.Equalizer, "nav_usage"),
    SPEED(Icons.Default.Speed, "nav_speed"),
    DNS(Icons.Default.Shield, "nav_dns"),
    FORECAST(Icons.Default.AutoAwesome, "nav_forecast"),
    SETTINGS(Icons.Default.Settings, "nav_settings"),
    RANK(Icons.Default.EmojiEvents, "nav_rank")
}

fun NavDestination.getLabel(lang: AppLanguage): String = when (this) {
    NavDestination.DASHBOARD -> AppStrings.navPulse(lang)
    NavDestination.USAGE -> AppStrings.navUsage(lang)
    NavDestination.SPEED -> AppStrings.navSpeed(lang)
    NavDestination.DNS -> AppStrings.navDns(lang)
    NavDestination.FORECAST -> AppStrings.navForecast(lang)
    NavDestination.SETTINGS -> AppStrings.navSettings(lang)
    NavDestination.RANK -> AppStrings.navRank(lang)
}

@Composable
fun DataPulseApp(
    viewModel: MainViewModel,
    onRequestUsagePermission: () -> Unit,
    onOpenAppInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentDestination by remember { mutableStateOf(NavDestination.DASHBOARD) }
    var previousDestination by remember { mutableStateOf(NavDestination.DASHBOARD) }
    var showUsageTipPopup by remember { mutableStateOf(false) }
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val speedSample by viewModel.speedSample.collectAsStateWithLifecycle()
    val targetNavDestination by viewModel.targetNavDestination.collectAsStateWithLifecycle()
    val appUpdateState by viewModel.appUpdateState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val density = LocalDensity.current

    LaunchedEffect(targetNavDestination) {
        targetNavDestination?.let { destString ->
            when (destString.uppercase()) {
                "FORECAST" -> if (currentDestination != NavDestination.FORECAST) {
                    previousDestination = currentDestination
                    currentDestination = NavDestination.FORECAST
                }
                "USAGE" -> if (currentDestination != NavDestination.USAGE) {
                    previousDestination = currentDestination
                    currentDestination = NavDestination.USAGE
                }
                "DASHBOARD" -> if (currentDestination != NavDestination.DASHBOARD) {
                    previousDestination = currentDestination
                    currentDestination = NavDestination.DASHBOARD
                }
                "SPEED" -> if (currentDestination != NavDestination.SPEED) {
                    previousDestination = currentDestination
                    currentDestination = NavDestination.SPEED
                }
                "DNS" -> if (currentDestination != NavDestination.DNS) {
                    previousDestination = currentDestination
                    currentDestination = NavDestination.DNS
                }
                "SETTINGS" -> if (currentDestination != NavDestination.SETTINGS) {
                    previousDestination = currentDestination
                    currentDestination = NavDestination.SETTINGS
                }
                "RANK" -> if (currentDestination != NavDestination.RANK) {
                    previousDestination = currentDestination
                    currentDestination = NavDestination.RANK
                }
            }
            viewModel.setTargetNavDestination(null)
        }
    }

    fun navigateTo(dest: NavDestination) {
        if (dest != currentDestination) {
            previousDestination = currentDestination
            currentDestination = dest
        }
    }

    fun navigateBack() {
        currentDestination = previousDestination
    }

    val glassStyle = LocalLiquidGlass.current
    val rootFlameController = remember { RootFlameOverlayController() }
    var topBarHeightPx by remember { mutableFloatStateOf(0f) }
    var bottomBarHeightPx by remember { mutableFloatStateOf(0f) }

    val topPaddingDp = with(density) { topBarHeightPx.toDp() }.coerceAtLeast(80.dp)
    val bottomPaddingDp = with(density) { bottomBarHeightPx.toDp() }.coerceAtLeast(92.dp)
    val floatingChromePadding = remember(topPaddingDp, bottomPaddingDp) {
        PaddingValues(top = topPaddingDp + 4.dp, bottom = bottomPaddingDp + 12.dp)
    }

    CompositionLocalProvider(
        LocalRootFlameOverlay provides rootFlameController,
        LocalFloatingChromePadding provides floatingChromePadding
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(brush = glassStyle.backdropBrush)
        ) {
            // Subtle luminous ambient orbs behind translucent iOS 26 Liquid Glass panels
            AmbientLiquidBackdrop(glassStyle = glassStyle)

            // Main Screen Stage flowing seamlessly edge-to-edge under floating glass chrome
            AdaptiveScreenContainer(modifier = Modifier.fillMaxSize()) { _ ->
                AnimatedContent(
                    targetState = currentDestination,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(210)) togetherWith fadeOut(animationSpec = tween(150))
                    },
                    label = "screen_animated_transition"
                ) { dest ->
                    when (dest) {
                        NavDestination.DASHBOARD -> DashboardScreen(
                            viewModel = viewModel,
                            onRequestUsagePermission = onRequestUsagePermission,
                            onOpenAppInfo = onOpenAppInfo,
                            onNavigateToSettings = { navigateTo(NavDestination.SETTINGS) },
                            onNavigateToAnalytics = { navigateTo(NavDestination.USAGE) }
                        )
                        NavDestination.USAGE -> AnalyticsScreen(
                            viewModel = viewModel,
                            onRequestUsagePermission = onRequestUsagePermission,
                            onOpenAppInfo = onOpenAppInfo
                        )
                        NavDestination.SPEED -> SpeedMeterScreen(
                            viewModel = viewModel
                        )
                        NavDestination.DNS -> DnsControlScreen(
                            viewModel = viewModel
                        )
                        NavDestination.FORECAST -> SmartForecastScreen(
                            viewModel = viewModel,
                            onNavigateToSettings = { navigateTo(NavDestination.SETTINGS) },
                            onNavigateBack = { navigateBack() }
                        )
                        NavDestination.SETTINGS -> SettingsScreen(
                            viewModel = viewModel,
                            onRequestUsagePermission = onRequestUsagePermission,
                            onOpenAppInfo = onOpenAppInfo,
                            onNavigateBack = { navigateBack() },
                            onNavigateToDns = { navigateTo(NavDestination.DNS) }
                        )
                        NavDestination.RANK -> RankScreen(
                            viewModel = viewModel,
                            onRequestUsagePermission = onRequestUsagePermission,
                            onOpenAppInfo = onOpenAppInfo
                        )
                    }
                }
            }

            // Floating Top App Bar
            PersistentTopAppBar(
                speedSample = speedSample,
                appLanguage = appLanguage,
                isSettingsActive = (currentDestination == NavDestination.SETTINGS),
                isForecastActive = (currentDestination == NavDestination.FORECAST),
                title = when (currentDestination) {
                    NavDestination.USAGE -> if (appLanguage == AppLanguage.ARABIC) "الاستهلاك" else "Usage"
                    NavDestination.SETTINGS -> AppStrings.navSettings(appLanguage)
                    NavDestination.FORECAST -> AppStrings.navSmartAnalytics(appLanguage)
                    else -> "HyperNet"
                },
                onUsageTipClick = if (currentDestination == NavDestination.USAGE) {
                    { showUsageTipPopup = true }
                } else {
                    null
                },
                onOpenForecast = {
                    if (currentDestination == NavDestination.FORECAST) {
                        navigateBack()
                    } else {
                        navigateTo(NavDestination.FORECAST)
                    }
                },
                onOpenSettings = {
                    if (currentDestination == NavDestination.SETTINGS) {
                        navigateBack()
                    } else {
                        navigateTo(NavDestination.SETTINGS)
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .onSizeChanged { size ->
                        topBarHeightPx = size.height.toFloat()
                    }
            )

            // Floating Apple iOS 26 Liquid Glass Capsule Dock
            LiquidGlassBottomNav(
                currentDestination = currentDestination,
                appLanguage = appLanguage,
                onSelectDestination = { navigateTo(it) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .onSizeChanged { size ->
                        bottomBarHeightPx = size.height.toFloat()
                    }
            )

            // Screen-top flame overlay for Pulse pull-to-refresh
            if (currentDestination == NavDestination.DASHBOARD) {
                RootFlameOverlayCanvas(
                    controller = rootFlameController,
                    topBarHeightPx = topBarHeightPx,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }

    if (showUsageTipPopup) {
        AnalyticsTipPopupDialog(
            appLanguage = appLanguage,
            onDismiss = { showUsageTipPopup = false }
        )
    }

    if (appUpdateState is AppUpdateUiState.Available ||
        appUpdateState is AppUpdateUiState.Downloading ||
        appUpdateState is AppUpdateUiState.ReadyToInstall ||
        appUpdateState is AppUpdateUiState.NeedsInstallPermission ||
        (appUpdateState is AppUpdateUiState.Error && (appUpdateState as AppUpdateUiState.Error).info != null)
    ) {
        AppUpdateDialog(
            state = appUpdateState,
            appLanguage = appLanguage,
            onDismiss = { viewModel.dismissUpdateDialog() },
            onStartUpdate = { info -> viewModel.startInAppUpdateDownload(context, info) },
            onInstallNow = { ready -> viewModel.installDownloadedUpdate(context, ready) },
            onRequestInstallPermission = { viewModel.requestInstallPermission(context) }
        )
    }
}

/**
 * Ambient luminous refraction orb field drawn efficiently on a single Canvas behind glass panels.
 * Disabled in strict flat-surface Neumorphism mode so `#EEF2F6` / `#1E232B` soft shadows stay pure.
 */
@Composable
private fun AmbientLiquidBackdrop(glassStyle: LiquidGlassStyle) {
    if (glassStyle.isNeumorphic || !glassStyle.iosLiquidGlassEnabled) return

    Canvas(modifier = Modifier.fillMaxSize()) {
        val alphaTop = if (glassStyle.isDark) 0.13f else 0.10f
        val alphaMid = if (glassStyle.isDark) 0.10f else 0.08f
        val alphaBot = if (glassStyle.isDark) 0.11f else 0.07f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(glassStyle.accentPrimary.copy(alpha = alphaTop), Color.Transparent),
                center = Offset(size.width * 0.14f, size.height * 0.10f),
                radius = size.width * 0.65f
            ),
            radius = size.width * 0.65f,
            center = Offset(size.width * 0.14f, size.height * 0.10f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(glassStyle.accentSecondary.copy(alpha = alphaMid), Color.Transparent),
                center = Offset(size.width * 0.88f, size.height * 0.42f),
                radius = size.width * 0.58f
            ),
            radius = size.width * 0.58f,
            center = Offset(size.width * 0.88f, size.height * 0.42f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(glassStyle.accentPrimary.copy(alpha = alphaBot), Color.Transparent),
                center = Offset(size.width * 0.25f, size.height * 0.90f),
                radius = size.width * 0.60f
            ),
            radius = size.width * 0.60f,
            center = Offset(size.width * 0.25f, size.height * 0.90f)
        )
    }
}

/**
 * Iconic Apple iOS 26 Floating Liquid Glass Capsule Dock with fluid spring-gliding
 * active droplet indicator across the 5 navigation destinations.
 */
@Composable
fun LiquidGlassBottomNav(
    currentDestination: NavDestination,
    appLanguage: AppLanguage,
    onSelectDestination: (NavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current
    val haptics = rememberAppHaptics()
    val density = LocalDensity.current

    val navigationItems = remember {
        listOf(
            NavDestination.DASHBOARD,
            NavDestination.USAGE,
            NavDestination.SPEED,
            NavDestination.DNS,
            NavDestination.RANK
        )
    }

    val selectedIndex = navigationItems.indexOf(currentDestination)
    val thumbProgressAnim = remember { Animatable(if (selectedIndex >= 0) selectedIndex.toFloat() else 0f) }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex >= 0) {
            thumbProgressAnim.animateTo(
                targetValue = selectedIndex.toFloat(),
                animationSpec = spring(
                    dampingRatio = 0.68f,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }

    val dockShape = RoundedCornerShape(30.dp)
    val pillShape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                horizontal = dimensions.screenPaddingHorizontal,
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = dimensions.bottomNavMaxWidth)
                .then(
                    when {
                        glassStyle.isNeumorphic -> Modifier
                            .neumorphicDepth(
                                shape = dockShape,
                                lightColor = glassStyle.neumorphicHighlight,
                                darkColor = glassStyle.neumorphicShadow,
                                offsetDp = 6.dp,
                                surfaceColor = glassStyle.surfaceColor,
                                blurDp = 12.dp
                            )
                            .clip(dockShape)
                            .background(Brush.verticalGradient(listOf(glassStyle.surfaceColor, glassStyle.neumorphicBase)))
                            .border(
                                1.dp,
                                Brush.linearGradient(listOf(glassStyle.neumorphicBorderLight, glassStyle.neumorphicBorderDark)),
                                dockShape
                            )
                        glassStyle.iosLiquidGlassEnabled -> Modifier
                            .iosLiquidGlassSurface(
                                shape = dockShape,
                                tintColor = glassStyle.accentPrimary,
                                isFilledAccent = false,
                                elevation = 14.dp
                            )
                        else -> Modifier
                            .shadow(elevation = 8.dp, shape = dockShape)
                            .clip(dockShape)
                            .background(glassStyle.surfaceColor.copy(alpha = 0.96f))
                            .border(1.dp, glassStyle.borderColor.copy(alpha = 0.65f), dockShape)
                    }
                )
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    val slotWidthPx = with(density) { maxWidth.toPx() } / navigationItems.size.toFloat()
                    val slotWidthDp = with(density) { slotWidthPx.toDp() }

                    // Fluid Sliding iOS 26 Optical Droplet Indicator
                    if (selectedIndex >= 0) {
                        val offsetX = (thumbProgressAnim.value * slotWidthPx).roundToInt()
                        Box(
                            modifier = Modifier
                                .offset { IntOffset(offsetX, 0) }
                                .width(slotWidthDp)
                                .fillMaxHeight()
                                .padding(horizontal = 2.dp)
                                .then(
                                    if (glassStyle.isNeumorphic) {
                                        Modifier
                                            .neumorphicDepth(
                                                shape = pillShape,
                                                lightColor = glassStyle.neumorphicHighlight,
                                                darkColor = glassStyle.neumorphicShadow,
                                                offsetDp = 2.dp,
                                                isPressed = true
                                            )
                                            .clip(pillShape)
                                            .background(glassStyle.cardSubtleBg)
                                    } else {
                                        Modifier.iosLiquidGlassSurface(
                                            shape = pillShape,
                                            tintColor = glassStyle.accentPrimary,
                                            isFilledAccent = false,
                                            elevation = if (glassStyle.iosLiquidGlassEnabled) 4.dp else 0.dp
                                        )
                                    }
                                )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        navigationItems.forEach { destination ->
                            val isSelected = (destination == currentDestination)
                            val destLabel = destination.getLabel(appLanguage)

                            val iconScale by animateFloatAsState(
                                targetValue = if (isSelected) 1.12f else 0.95f,
                                animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMedium),
                                label = "nav_icon_scale"
                            )
                            val iconTint by animateColorAsState(
                                targetValue = if (isSelected) glassStyle.accentPrimary else glassStyle.textTertiary,
                                animationSpec = tween(180),
                                label = "nav_icon_tint"
                            )
                            val labelColor by animateColorAsState(
                                targetValue = if (isSelected) glassStyle.textPrimary else glassStyle.textTertiary,
                                animationSpec = tween(180),
                                label = "nav_label_color"
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .clip(pillShape)
                                    .tactileSpringPress(
                                        pressedScale = 0.92f,
                                        onClick = {
                                            haptics.performSegmentSwitch()
                                            onSelectDestination(destination)
                                        }
                                    )
                                    .testTag(destination.tag),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destLabel,
                                    tint = iconTint,
                                    modifier = Modifier
                                        .size(21.dp)
                                        .graphicsLayer {
                                            scaleX = iconScale
                                            scaleY = iconScale
                                        }
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = destLabel,
                                    color = labelColor,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
