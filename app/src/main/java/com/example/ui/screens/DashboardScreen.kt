package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.FormatUtils
import com.example.model.NetworkType
import com.example.model.NetworkUsageSummary
import com.example.model.SimCardInfo
import com.example.ui.MainViewModel
import com.example.ui.components.CircularQuotaProgressGauge
import com.example.ui.components.ElasticFlamePullToRefreshBox
import com.example.ui.components.GlassContainer
import com.example.ui.components.IosLiquidGlassButton
import com.example.ui.components.IosLiquidGlassIconBadge
import com.example.ui.components.IosLiquidGlassIconButton
import com.example.ui.components.IosLiquidGlassSectionHeader
import com.example.ui.components.UsagePermissionCard
import com.example.ui.components.iosLiquidGlassSurface
import com.example.ui.components.rememberAppHaptics
import com.example.ui.navigation.LocalFloatingChromePadding
import com.example.ui.theme.HyperMonoFontFamily
import com.example.ui.theme.LocalAppDimensions
import com.example.ui.theme.LocalLiquidGlass
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onRequestUsagePermission: () -> Unit,
    onOpenAppInfo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    modifier: Modifier = Modifier
) {
    val speedSample by viewModel.speedSample.collectAsStateWithLifecycle()
    val usageSummary by viewModel.usageSummary.collectAsStateWithLifecycle()
    val quota by viewModel.dataPlanQuota.collectAsStateWithLifecycle()
    val simCards by viewModel.simCards.collectAsStateWithLifecycle()
    val selectedSimIndex by viewModel.selectedSimIndex.collectAsStateWithLifecycle()
    val hasUsagePerm by viewModel.hasUsagePermission.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isDashboardRefreshing.collectAsStateWithLifecycle()
    var showEditCapDialog by remember { mutableStateOf(false) }

    val dimensions = LocalAppDimensions.current
    val glassStyle = LocalLiquidGlass.current
    val chromePadding = LocalFloatingChromePadding.current

    if (showEditCapDialog) {
        EditDataCapDialog(
            currentLimitGB = quota.planLimitGB,
            usedBytes = quota.usedBytes,
            appLanguage = appLanguage,
            onDismiss = { showEditCapDialog = false },
            onSaveLimit = { newLimit ->
                viewModel.updatePlanLimit(newLimit)
                showEditCapDialog = false
            }
        )
    }

    ElasticFlamePullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshAll() },
        useScreenTopOverlay = true,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensions.screenPaddingHorizontal),
            contentPadding = PaddingValues(
                top = chromePadding.calculateTopPadding() + 6.dp,
                bottom = chromePadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(dimensions.sectionSpacing)
        ) {
            if (!hasUsagePerm) {
                item {
                    UsagePermissionCard(
                        onRequestPermission = onRequestUsagePermission,
                        onOpenAppInfo = onOpenAppInfo,
                        appLanguage = appLanguage
                    )
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
                            val limitBytes = (quota.planLimitGB * 1024L * 1024L * 1024L).toLong()
                            val isInternetConnected = (speedSample.connectionType != NetworkType.OFFLINE)
                            CircularQuotaProgressGauge(
                                usedBytes = quota.usedBytes,
                                limitBytes = limitBytes,
                                percentage = quota.usagePercent,
                                status = quota.status,
                                daysRemaining = quota.daysRemaining,
                                appLanguage = appLanguage,
                                isInternetConnected = isInternetConnected,
                                onConfigureClick = { showEditCapDialog = true }
                            )

                            if (simCards.isNotEmpty()) {
                                DualSimSelector(
                                    simCards = simCards,
                                    selectedIndex = selectedSimIndex,
                                    onSelect = { viewModel.setSimIndex(it) }
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(dimensions.gridSpacing)
                        ) {
                            IosLiquidGlassSectionHeader(
                                title = AppStrings.dataBreakdownTitle(appLanguage)
                            )
                            DataBreakdownGrid(
                                usageSummary = usageSummary,
                                appLanguage = appLanguage
                            )
                            QuickDashboardActionsRow(
                                appLanguage = appLanguage,
                                onNavigateToAnalytics = onNavigateToAnalytics,
                                onNavigateToSettings = onNavigateToSettings
                            )
                        }
                    }
                }
            } else {
                if (simCards.isNotEmpty()) {
                    item {
                        DualSimSelector(
                            simCards = simCards,
                            selectedIndex = selectedSimIndex,
                            onSelect = { viewModel.setSimIndex(it) }
                        )
                    }
                }

                item {
                    val limitBytes = (quota.planLimitGB * 1024L * 1024L * 1024L).toLong()
                    val isInternetConnected = (speedSample.connectionType != NetworkType.OFFLINE)
                    CircularQuotaProgressGauge(
                        usedBytes = quota.usedBytes,
                        limitBytes = limitBytes,
                        percentage = quota.usagePercent,
                        status = quota.status,
                        daysRemaining = quota.daysRemaining,
                        appLanguage = appLanguage,
                        isInternetConnected = isInternetConnected,
                        onConfigureClick = { showEditCapDialog = true }
                    )
                }

                item {
                    IosLiquidGlassSectionHeader(
                        title = AppStrings.dataBreakdownTitle(appLanguage)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    DataBreakdownGrid(
                        usageSummary = usageSummary,
                        appLanguage = appLanguage
                    )
                }

                item {
                    QuickDashboardActionsRow(
                        appLanguage = appLanguage,
                        onNavigateToAnalytics = onNavigateToAnalytics,
                        onNavigateToSettings = onNavigateToSettings
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickDashboardActionsRow(
    appLanguage: AppLanguage,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val glassStyle = LocalLiquidGlass.current
    val haptics = rememberAppHaptics()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IosLiquidGlassButton(
            onClick = {
                haptics.performSegmentSwitch()
                onNavigateToAnalytics()
            },
            containerColor = glassStyle.accentPrimary,
            contentColor = glassStyle.textPrimary,
            isFilledAccent = false,
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 11.dp),
            modifier = Modifier.weight(1f)
        ) {
            IosLiquidGlassIconBadge(
                icon = Icons.Default.Apps,
                contentDescription = null,
                size = 28.dp,
                iconSize = 15.dp,
                tintColor = glassStyle.accentPrimary,
                shape = RoundedCornerShape(9.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = AppStrings.navUsage(appLanguage),
                color = glassStyle.textPrimary,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = glassStyle.accentPrimary,
                modifier = Modifier.size(14.dp)
            )
        }

        IosLiquidGlassButton(
            onClick = {
                haptics.performSegmentSwitch()
                onNavigateToSettings()
            },
            containerColor = glassStyle.accentSecondary,
            contentColor = glassStyle.textPrimary,
            isFilledAccent = false,
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 11.dp),
            modifier = Modifier.weight(1f)
        ) {
            IosLiquidGlassIconBadge(
                icon = Icons.Default.Tune,
                contentDescription = null,
                size = 28.dp,
                iconSize = 15.dp,
                tintColor = glassStyle.accentSecondary,
                shape = RoundedCornerShape(9.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = AppStrings.navSettings(appLanguage),
                color = glassStyle.textPrimary,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = glassStyle.accentSecondary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun LiquidGlassDualSimSlider(
    simCards: List<SimCardInfo>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val sim1 = simCards.getOrNull(0) ?: SimCardInfo(
        subId = 1,
        slotIndex = 0,
        carrierName = "SIM 1 (Primary)",
        displayName = "SIM 1",
        isDefaultData = true
    )
    val sim2 = simCards.getOrNull(1) ?: SimCardInfo(
        subId = 2,
        slotIndex = 1,
        carrierName = "SIM 2 (Secondary)",
        displayName = "SIM 2",
        isDefaultData = false
    )

    val progressAnim = remember { Animatable(selectedIndex.toFloat()) }
    var isDragging by remember { mutableStateOf(false) }
    val haptics = rememberAppHaptics()

    LaunchedEffect(selectedIndex) {
        if (!isDragging && abs(progressAnim.value - selectedIndex.toFloat()) > 0.01f) {
            progressAnim.animateTo(
                targetValue = selectedIndex.toFloat(),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    val glowAlpha by animateFloatAsState(
        targetValue = if (isDragging) 0.88f else 0.52f,
        animationSpec = tween(150),
        label = "slider_glow"
    )

    val stretchX by animateFloatAsState(
        targetValue = if (isDragging) 1.04f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "stretch_x"
    )

    val squashY by animateFloatAsState(
        targetValue = if (isDragging) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "squash_y"
    )

    val railShape = RoundedCornerShape(24.dp)
    val thumbShape = RoundedCornerShape(19.dp)

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .iosLiquidGlassSurface(
                    shape = railShape,
                    tintColor = glassStyle.accentPrimary,
                    isFilledAccent = false,
                    elevation = if (glassStyle.iosLiquidGlassEnabled) 4.dp else 0.dp
                )
                .pointerInput(Unit) {
                    val trackW = size.width.toFloat()
                    val pad = with(density) { 4.dp.toPx() }
                    val halfW = (trackW - pad * 2f) / 2f
                    var crossedThreshold = selectedIndex > 0
                    detectHorizontalDragGestures(
                        onDragStart = {
                            isDragging = true
                            crossedThreshold = progressAnim.value > 0.5f
                        },
                        onDragEnd = {
                            isDragging = false
                            val target = if (progressAnim.value > 0.5f) 1 else 0
                            if (target != selectedIndex) {
                                haptics.performSegmentSwitch()
                            }
                            scope.launch {
                                progressAnim.animateTo(
                                    targetValue = target.toFloat(),
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                            onSelect(target)
                        },
                        onDragCancel = {
                            isDragging = false
                            scope.launch {
                                progressAnim.animateTo(
                                    targetValue = selectedIndex.toFloat(),
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                )
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            val deltaProgress = dragAmount / halfW
                            val nextProgress = (progressAnim.value + deltaProgress).coerceIn(-0.06f, 1.06f)
                            val isAbove = nextProgress > 0.5f
                            if (isAbove != crossedThreshold) {
                                crossedThreshold = isAbove
                                haptics.performSegmentSwitch()
                            }
                            scope.launch {
                                progressAnim.snapTo(nextProgress)
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    val trackW = size.width.toFloat()
                    detectTapGestures { offset ->
                        val target = if (offset.x > trackW / 2f) 1 else 0
                        if (target != selectedIndex) {
                            haptics.performSegmentSwitch()
                        }
                        scope.launch {
                            progressAnim.animateTo(
                                targetValue = target.toFloat(),
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                        onSelect(target)
                    }
                }
                .padding(5.dp)
                .testTag("dual_sim_slider"),
            contentAlignment = Alignment.CenterStart
        ) {
            val totalWidthPx = constraints.maxWidth.toFloat()
            val totalHeightPx = constraints.maxHeight.toFloat()
            val paddingPx = with(density) { 5.dp.toPx() }
            val thumbWidthPx = (totalWidthPx - paddingPx * 2f) / 2f
            val thumbWidthDp = with(density) { thumbWidthPx.toDp() }
            val thumbHeightDp = with(density) { (totalHeightPx - paddingPx * 2f).toDp() }

            val currentProgress = progressAnim.value
            val thumbOffsetXPx = (currentProgress * thumbWidthPx)

            // Clean Optical Liquid Glass Thumb Droplet (zero glowing smear behind SIM text)
            Box(
                modifier = Modifier
                    .offset { IntOffset(thumbOffsetXPx.roundToInt(), 0) }
                    .size(width = thumbWidthDp, height = thumbHeightDp)
                    .graphicsLayer {
                        scaleX = stretchX
                        scaleY = squashY
                    }
                    .iosLiquidGlassSurface(
                        shape = thumbShape,
                        tintColor = glassStyle.accentPrimary,
                        isFilledAccent = false,
                        isPressed = isDragging,
                        elevation = 0.dp,
                        bodyAlphaOverride = if (glassStyle.isDark) 0.08f else 0.22f,
                        useInteriorShadow = false
                    )
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SimSlotItem(
                    sim = sim1,
                    proximity = (1f - currentProgress).coerceIn(0f, 1f),
                    modifier = Modifier.weight(1f)
                )
                SimSlotItem(
                    sim = sim2,
                    proximity = currentProgress.coerceIn(0f, 1f),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SimSlotItem(
    sim: SimCardInfo,
    proximity: Float,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val activeColor = glassStyle.accentPrimary
    val isActive = proximity > 0.5f

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IosLiquidGlassIconBadge(
            icon = Icons.Default.SimCard,
            contentDescription = sim.displayName,
            size = 32.dp,
            iconSize = 16.dp,
            tintColor = if (isActive) activeColor else glassStyle.textTertiary,
            shape = CircleShape
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = sim.displayName,
                    color = if (isActive) glassStyle.textPrimary else glassStyle.textSecondary,
                    fontSize = 13.sp,
                    fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium,
                    maxLines = 1
                )
                if (isActive) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(activeColor)
                    )
                }
            }

            Text(
                text = sim.carrierName,
                color = if (isActive) activeColor else glassStyle.textTertiary,
                fontSize = 10.5.sp,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DualSimSelector(
    simCards: List<SimCardInfo>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassDualSimSlider(
        simCards = simCards,
        selectedIndex = selectedIndex,
        onSelect = onSelect,
        modifier = modifier
    )
}

@Composable
fun DataBreakdownGrid(
    usageSummary: NetworkUsageSummary,
    appLanguage: AppLanguage,
    modifier: Modifier = Modifier,
    onViewFullAnalytics: (() -> Unit)? = null
) {
    val glassStyle = LocalLiquidGlass.current

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BreakdownMetricCard(
                title = AppStrings.todayCellular(appLanguage),
                value = FormatUtils.formatBytes(usageSummary.mobileTodayBytes),
                icon = Icons.Default.SignalCellularAlt,
                tint = glassStyle.accentPrimary,
                modifier = Modifier.weight(1f)
            )
            BreakdownMetricCard(
                title = AppStrings.todayWifi(appLanguage),
                value = FormatUtils.formatBytes(usageSummary.wifiTodayBytes),
                icon = Icons.Default.Wifi,
                tint = glassStyle.accentSecondary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BreakdownMetricCard(
                title = AppStrings.monthlyCellular(appLanguage),
                value = FormatUtils.formatBytes(usageSummary.mobileMonthBytes),
                icon = Icons.Default.CellTower,
                tint = glassStyle.statusSafe,
                modifier = Modifier.weight(1f)
            )
            BreakdownMetricCard(
                title = AppStrings.monthlyWifi(appLanguage),
                value = FormatUtils.formatBytes(usageSummary.wifiMonthBytes),
                icon = Icons.Default.TrendingUp,
                tint = glassStyle.accentPrimary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BreakdownMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current

    GlassContainer(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        borderColor = tint.copy(alpha = if (glassStyle.isDark) 0.30f else 0.22f),
        contentPadding = PaddingValues(13.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IosLiquidGlassIconBadge(
                    icon = icon,
                    contentDescription = title,
                    size = 28.dp,
                    iconSize = 15.dp,
                    tintColor = tint,
                    shape = RoundedCornerShape(9.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = glassStyle.textSecondary,
                    fontSize = dimensions.textCaption,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontFamily = HyperMonoFontFamily,
                color = glassStyle.textPrimary,
                fontSize = dimensions.textMetricValue,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EditDataCapDialog(
    currentLimitGB: Float,
    usedBytes: Long,
    appLanguage: AppLanguage,
    onDismiss: () -> Unit,
    onSaveLimit: (Float) -> Unit
) {
    val glassStyle = LocalLiquidGlass.current
    val appHaptics = rememberAppHaptics()
    val focusManager = LocalFocusManager.current

    var limitValue by remember(currentLimitGB) { mutableStateOf(currentLimitGB.coerceIn(1f, 1000f)) }
    var textInput by remember(currentLimitGB) {
        mutableStateOf(if (currentLimitGB % 1f == 0f) currentLimitGB.toInt().toString() else "%.1f".format(currentLimitGB))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        GlassContainer(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .widthIn(max = 440.dp),
            shape = RoundedCornerShape(30.dp),
            borderColor = glassStyle.accentPrimary.copy(alpha = 0.55f),
            contentPadding = PaddingValues(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IosLiquidGlassIconBadge(
                            icon = Icons.Default.PieChart,
                            contentDescription = "Edit Cap",
                            size = 38.dp,
                            iconSize = 19.dp,
                            tintColor = glassStyle.accentPrimary,
                            shape = RoundedCornerShape(11.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.editCap(appLanguage),
                                color = glassStyle.textPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = AppStrings.planLimitSection(appLanguage),
                                color = glassStyle.textSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IosLiquidGlassIconButton(
                        onClick = onDismiss,
                        icon = Icons.Default.Close,
                        contentDescription = "Close",
                        visualSize = 36.dp,
                        iconSize = 18.dp,
                        tintColor = glassStyle.textTertiary,
                        shape = CircleShape
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Current Usage vs Cap Frosted Summary
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(glassStyle.cardSubtleBg)
                        .border(0.9.dp, glassStyle.borderColor.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "المستهلك حالياً" else "Currently Used",
                                color = glassStyle.textSecondary,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = FormatUtils.formatBytes(usedBytes),
                                fontFamily = HyperMonoFontFamily,
                                color = glassStyle.textPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = AppStrings.planCapLabel(appLanguage),
                                color = glassStyle.textSecondary,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "%.1f GB".format(limitValue),
                                fontFamily = HyperMonoFontFamily,
                                color = glassStyle.accentPrimary,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Custom Input Field
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { newVal ->
                        if (newVal.all { it.isDigit() || it == '.' }) {
                            textInput = newVal
                            val parsed = newVal.toFloatOrNull()
                            if (parsed != null && parsed in 0.1f..10000f) {
                                limitValue = parsed.coerceIn(1f, 1000f)
                            }
                        }
                    },
                    label = { Text(AppStrings.customLimitLabel(appLanguage), color = glassStyle.textTertiary, fontSize = 11.sp) },
                    trailingIcon = {
                        Text(
                            text = "GB",
                            fontFamily = HyperMonoFontFamily,
                            color = glassStyle.accentPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = HyperMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = glassStyle.accentPrimary,
                        unfocusedBorderColor = glassStyle.borderColor,
                        focusedContainerColor = glassStyle.cardSubtleBg,
                        unfocusedContainerColor = glassStyle.cardSubtleBg,
                        focusedTextColor = glassStyle.textPrimary,
                        unfocusedTextColor = glassStyle.textPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_quota_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Slider(
                    value = limitValue.coerceIn(1f, 1000f),
                    onValueChange = {
                        limitValue = it
                        textInput = if (it % 1f == 0f) it.toInt().toString() else "%.1f".format(it)
                    },
                    valueRange = 1f..1000f,
                    colors = SliderDefaults.colors(
                        thumbColor = glassStyle.accentPrimary,
                        activeTrackColor = glassStyle.accentPrimary,
                        inactiveTrackColor = glassStyle.progressTrackBg
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quota_slider")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Presets chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(10f, 50f, 100f, 250f, 500f, 1000f).forEach { preset ->
                        val isSelected = (limitValue.toInt() == preset.toInt())
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .iosLiquidGlassSurface(
                                    shape = RoundedCornerShape(10.dp),
                                    tintColor = if (isSelected) glassStyle.accentPrimary else glassStyle.cardSubtleBg,
                                    isFilledAccent = isSelected,
                                    elevation = if (isSelected) 3.dp else 0.dp
                                )
                                .clickable {
                                    appHaptics.performSegmentSwitch()
                                    limitValue = preset
                                    textInput = preset.toInt().toString()
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${preset.toInt()}G",
                                fontFamily = HyperMonoFontFamily,
                                color = if (isSelected) glassStyle.onAccentPrimary else glassStyle.textSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IosLiquidGlassButton(
                        onClick = onDismiss,
                        containerColor = glassStyle.cardSubtleBg,
                        contentColor = glassStyle.textSecondary,
                        isFilledAccent = false,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) "إلغاء" else "Cancel",
                            color = glassStyle.textSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IosLiquidGlassButton(
                        onClick = {
                            appHaptics.performConfirm()
                            val parsed = textInput.toFloatOrNull() ?: limitValue
                            onSaveLimit(parsed.coerceIn(0.1f, 10000f))
                        },
                        containerColor = glassStyle.accentPrimary,
                        contentColor = glassStyle.onAccentPrimary,
                        isFilledAccent = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1.4f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = glassStyle.onAccentPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) "حفظ السقف" else "Save Cap",
                            color = glassStyle.onAccentPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
