package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.AppThemeMode
import com.example.model.PrivateDnsPreset
import com.example.model.ThemeCategory
import com.example.model.ThemeMode
import com.example.ui.MainViewModel
import com.example.ui.components.GlassContainer
import com.example.ui.components.IosLiquidGlassButton
import com.example.ui.components.IosLiquidGlassIconBadge
import com.example.ui.components.IosLiquidGlassIconButton
import com.example.ui.components.IosLiquidGlassInsetDivider
import com.example.ui.components.IosLiquidGlassSectionHeader
import com.example.ui.components.IosLiquidGlassSegmentedControl
import com.example.ui.components.IosLiquidGlassSwitch
import com.example.ui.components.RestrictedSettingsGuideDialog
import com.example.ui.components.ThemeGalleryDialog
import com.example.ui.components.ThemeSwatchCard
import com.example.ui.components.iosLiquidGlassSurface
import com.example.ui.components.neumorphicDepth
import com.example.ui.components.rememberAppHaptics
import com.example.ui.components.tactileSpringPress
import com.example.ui.navigation.LocalFloatingChromePadding
import com.example.ui.theme.AmoledPitchBlackPrimary
import com.example.ui.theme.HyperMonoFontFamily
import com.example.ui.theme.LightModePrimary
import com.example.ui.theme.LiquidGlassDefaultPrimary
import com.example.ui.theme.LiquidNeonCyanPrimary
import com.example.ui.theme.LocalAppDimensions
import com.example.ui.theme.LocalLiquidGlass
import com.example.ui.theme.NeumorphicColors
import com.example.ui.theme.NeumorphicDarkColors
import com.example.ui.theme.contrastOnColor
import com.example.update.AppUpdateUiState

/**
 * Safely launches the system's Private DNS or Wireless settings activity across all Android versions.
 */
fun openPrivateDnsSettings(context: Context) {
    val intents = listOf(
        Intent("android.settings.NETWORK_PROVIDER_SETTINGS"),
        Intent(Settings.ACTION_WIRELESS_SETTINGS),
        Intent("android.settings.PRIVATE_DNS_SETTINGS"),
        Intent(Settings.ACTION_SETTINGS)
    )
    for (intent in intents) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            return
        } catch (_: Exception) {
            // Fallback to next candidate
        }
    }
}

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onRequestUsagePermission: () -> Unit,
    onOpenAppInfo: () -> Unit,
    onNavigateBack: (() -> Unit)? = null,
    onNavigateToDns: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (onNavigateBack != null) {
        BackHandler { onNavigateBack() }
    }

    val quota by viewModel.dataPlanQuota.collectAsStateWithLifecycle()
    val hasUsagePerm by viewModel.hasUsagePermission.collectAsStateWithLifecycle()
    val appThemeMode by viewModel.appThemeMode.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val powerSavingMode by viewModel.powerSavingMode.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val privateDnsPreset by viewModel.privateDnsPreset.collectAsStateWithLifecycle()
    val privateDnsCustomHostname by viewModel.privateDnsCustomHostname.collectAsStateWithLifecycle()
    val dnsVpnActive by viewModel.dnsVpnActive.collectAsStateWithLifecycle()
    val dnsVpnHost by viewModel.dnsVpnHost.collectAsStateWithLifecycle()
    val dnsQueriesForwarded by viewModel.dnsQueriesForwarded.collectAsStateWithLifecycle()
    val appUpdateState by viewModel.appUpdateState.collectAsStateWithLifecycle()
    val iosLiquidGlassEnabled by viewModel.iosLiquidGlassEnabled.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current
    val chromePadding = LocalFloatingChromePadding.current
    val appHaptics = rememberAppHaptics()
    var showThemeGallery by remember { mutableStateOf(false) }

    // 1. Language & Light/Dark Appearance Card (Apple Grouped Panel with Fluid Segmented Pickers)
    val languageCard: @Composable () -> Unit = {
        Column(modifier = Modifier.fillMaxWidth()) {
            IosLiquidGlassSectionHeader(
                title = AppStrings.appLanguageSection(appLanguage)
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassContainer(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IosLiquidGlassIconBadge(
                            icon = Icons.Default.Language,
                            contentDescription = "Language",
                            size = 30.dp,
                            iconSize = 16.dp,
                            tintColor = glassStyle.accentPrimary,
                            shape = RoundedCornerShape(9.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) "لغة الواجهة" else "Interface Language",
                            color = glassStyle.textPrimary,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val englishLabel = if (appLanguage == AppLanguage.ARABIC) "الإنغليزية" else "English"
                    IosLiquidGlassSegmentedControl(
                        options = listOf("العربية", englishLabel),
                        selectedIndex = if (appLanguage == AppLanguage.ARABIC) 0 else 1,
                        onSelect = { idx ->
                            appHaptics.performSegmentSwitch()
                            viewModel.setAppLanguage(if (idx == 0) AppLanguage.ARABIC else AppLanguage.ENGLISH)
                        },
                        optionModifier = { idx ->
                            Modifier.testTag(if (idx == 0) "language_option_arabic" else "language_option_english")
                        }
                    )
                }
            }
        }
    }

    // 2. Separated Light & Dark Appearance Mode Card
    val appearanceModeCard: @Composable () -> Unit = {
        GlassContainer(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IosLiquidGlassIconBadge(
                            icon = if (themeMode == ThemeMode.DARK || (themeMode == ThemeMode.SYSTEM && glassStyle.isDark)) {
                                Icons.Default.DarkMode
                            } else {
                                Icons.Default.LightMode
                            },
                            contentDescription = "Appearance Mode",
                            size = 30.dp,
                            iconSize = 16.dp,
                            tintColor = glassStyle.accentPrimary,
                            shape = RoundedCornerShape(9.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) "الوضع الليلي والنهاري" else "Light & Dark Mode",
                            color = glassStyle.textPrimary,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .iosLiquidGlassSurface(
                                shape = CircleShape,
                                tintColor = glassStyle.accentPrimary,
                                isFilledAccent = false,
                                elevation = 0.dp
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = when (themeMode) {
                                ThemeMode.SYSTEM -> if (appLanguage == AppLanguage.ARABIC) "تلقائي" else "System"
                                ThemeMode.LIGHT -> if (appLanguage == AppLanguage.ARABIC) "نهاري" else "Light"
                                ThemeMode.DARK -> if (appLanguage == AppLanguage.ARABIC) "ليلي" else "Dark"
                            },
                            color = glassStyle.accentPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                val modes = listOf(ThemeMode.SYSTEM, ThemeMode.LIGHT, ThemeMode.DARK)
                val labels = if (appLanguage == AppLanguage.ARABIC) {
                    listOf("اتبع الهاتف", "نهاري", "ليلي")
                } else {
                    listOf("System", "Light", "Dark")
                }
                val tags = listOf("appearance_mode_system", "appearance_mode_light", "appearance_mode_dark")

                IosLiquidGlassSegmentedControl(
                    options = labels,
                    selectedIndex = modes.indexOf(themeMode).coerceAtLeast(0),
                    onSelect = { idx ->
                        appHaptics.performSegmentSwitch()
                        viewModel.setThemeMode(modes[idx])
                    },
                    optionModifier = { idx -> Modifier.testTag(tags[idx]) }
                )
            }
        }
    }

    // 3. Dedicated Multi-Theme Engine Studio
    val extendedThemes = remember {
        AppThemeMode.values().filter { it.category != ThemeCategory.CORE }
    }

    val multiThemeEngineCard: @Composable () -> Unit = {
        Column(modifier = Modifier.fillMaxWidth()) {
            IosLiquidGlassSectionHeader(
                title = "Multi-Theme Engine",
                trailing = {
                    Text(
                        text = appThemeMode.getDisplayName(appLanguage),
                        color = glassStyle.accentPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassContainer(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            IosLiquidGlassIconBadge(
                                icon = Icons.Default.Palette,
                                contentDescription = "Theme",
                                size = 30.dp,
                                iconSize = 16.dp,
                                tintColor = glassStyle.accentPrimary,
                                shape = RoundedCornerShape(9.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "محرك الثيمات الشامل" else "Theme Studio",
                                color = glassStyle.textPrimary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IosLiquidGlassButton(
                            onClick = {
                                appHaptics.performConfirm()
                                showThemeGallery = true
                            },
                            containerColor = glassStyle.accentPrimary,
                            contentColor = glassStyle.accentPrimary,
                            isFilledAccent = false,
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("open_theme_gallery_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = null,
                                tint = glassStyle.accentPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "الكل (66)" else "All (66)",
                                color = glassStyle.accentPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        SmallThemeButton(
                            themeMode = AppThemeMode.LIQUID_GLASS_DEFAULT,
                            icon = Icons.Default.DarkMode,
                            accentColor = LiquidGlassDefaultPrimary,
                            isSelected = appThemeMode == AppThemeMode.LIQUID_GLASS_DEFAULT,
                            appLanguage = appLanguage,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setAppThemeMode(AppThemeMode.LIQUID_GLASS_DEFAULT)
                            },
                            modifier = Modifier.weight(1f)
                        )

                        SmallThemeButton(
                            themeMode = AppThemeMode.LIQUID_NEON_CYAN,
                            icon = Icons.Default.Palette,
                            accentColor = LiquidNeonCyanPrimary,
                            isSelected = appThemeMode == AppThemeMode.LIQUID_NEON_CYAN,
                            appLanguage = appLanguage,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setAppThemeMode(AppThemeMode.LIQUID_NEON_CYAN)
                            },
                            modifier = Modifier.weight(1f)
                        )

                        SmallThemeButton(
                            themeMode = AppThemeMode.AMOLED_PITCH_BLACK,
                            icon = Icons.Default.Eco,
                            accentColor = AmoledPitchBlackPrimary,
                            isSelected = appThemeMode == AppThemeMode.AMOLED_PITCH_BLACK,
                            appLanguage = appLanguage,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setAppThemeMode(AppThemeMode.AMOLED_PITCH_BLACK)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(7.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        SmallThemeButton(
                            themeMode = AppThemeMode.LIGHT_MODE,
                            icon = Icons.Default.LightMode,
                            accentColor = LightModePrimary,
                            isSelected = appThemeMode == AppThemeMode.LIGHT_MODE,
                            appLanguage = appLanguage,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setAppThemeMode(AppThemeMode.LIGHT_MODE)
                            },
                            modifier = Modifier.weight(1f)
                        )

                        SmallThemeButton(
                            themeMode = AppThemeMode.NEUMORPHISM,
                            icon = Icons.Default.Layers,
                            accentColor = NeumorphicColors.AccentBlue,
                            isSelected = appThemeMode == AppThemeMode.NEUMORPHISM,
                            appLanguage = appLanguage,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setAppThemeMode(AppThemeMode.NEUMORPHISM)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_option_neumorphism")
                        )

                        SmallThemeButton(
                            themeMode = AppThemeMode.NEUMORPHISM_DARK,
                            icon = Icons.Default.Layers,
                            accentColor = NeumorphicDarkColors.AccentBlue,
                            isSelected = appThemeMode == AppThemeMode.NEUMORPHISM_DARK,
                            appLanguage = appLanguage,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setAppThemeMode(AppThemeMode.NEUMORPHISM_DARK)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_option_neumorphism_dark")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (appLanguage == AppLanguage.ARABIC) {
                            "ثيمات مختارة (+${extendedThemes.size} ثيم)"
                        } else {
                            "Curated Studio (${extendedThemes.size} Themes)"
                        },
                        color = glassStyle.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(extendedThemes, key = { it.name }) { mode ->
                            ThemeSwatchCard(
                                themeMode = mode,
                                isSelected = appThemeMode == mode,
                                appLanguage = appLanguage,
                                onClick = {
                                    appHaptics.performSegmentSwitch()
                                    viewModel.setAppThemeMode(mode)
                                },
                                modifier = Modifier.width(146.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // 3B. Apple iOS 26 Liquid Glass Optical Effect Toggle Card
    val iosLiquidGlassCard: @Composable () -> Unit = {
        GlassContainer(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            borderColor = if (iosLiquidGlassEnabled) glassStyle.accentPrimary.copy(alpha = 0.45f) else glassStyle.borderColor,
            contentPadding = PaddingValues(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IosLiquidGlassIconBadge(
                        icon = Icons.Default.AutoAwesome,
                        contentDescription = if (appLanguage == AppLanguage.ARABIC) "الزجاج السائل iOS" else "iOS Liquid Glass",
                        size = 36.dp,
                        iconSize = 19.dp,
                        tintColor = glassStyle.accentPrimary,
                        shape = RoundedCornerShape(11.dp),
                        isFilled = iosLiquidGlassEnabled
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "الزجاج السائل (Apple iOS 26)" else "Apple iOS 26 Liquid Glass",
                                color = glassStyle.textPrimary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        (if (iosLiquidGlassEnabled) glassStyle.statusSafe else glassStyle.textTertiary)
                                            .copy(alpha = 0.16f)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 1.5.dp)
                            ) {
                                Text(
                                    text = if (iosLiquidGlassEnabled) {
                                        if (appLanguage == AppLanguage.ARABIC) "مفعّل" else "ON"
                                    } else {
                                        if (appLanguage == AppLanguage.ARABIC) "معطّل" else "OFF"
                                    },
                                    color = if (iosLiquidGlassEnabled) glassStyle.statusSafe else glassStyle.textTertiary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) {
                                "انكسار ضوئي ولمعان الزجاج السائل في جميع الأزرار والأيقونات"
                            } else {
                                "Optical specular glass highlights across all buttons & icons"
                            },
                            color = glassStyle.textSecondary,
                            fontSize = 10.5.sp,
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IosLiquidGlassSwitch(
                    checked = iosLiquidGlassEnabled,
                    onCheckedChange = { enabled ->
                        appHaptics.performSegmentSwitch()
                        viewModel.toggleIosLiquidGlass(enabled)
                    },
                    accentColor = glassStyle.accentPrimary,
                    modifier = Modifier.testTag("settings_ios_liquid_glass_switch")
                )
            }
        }
    }

    // 4. Smart DNS Shield Shortcut Card
    val dnsShortcutCard: @Composable () -> Unit = {
        val emeraldAdGuard = glassStyle.statusSafe
        val isShieldHighlighted = dnsVpnActive || privateDnsPreset == PrivateDnsPreset.ADGUARD
        val resolvedHost = when {
            dnsVpnActive -> dnsVpnHost
            privateDnsPreset == PrivateDnsPreset.CUSTOM && privateDnsCustomHostname.isNotBlank() -> privateDnsCustomHostname
            privateDnsPreset.hostname != null -> privateDnsPreset.hostname!!
            else -> AppStrings.dnsIspMode(appLanguage)
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            IosLiquidGlassSectionHeader(
                title = AppStrings.dnsScreenTitle(appLanguage)
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassContainer(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                borderColor = if (isShieldHighlighted) emeraldAdGuard.copy(alpha = 0.50f) else glassStyle.borderColor,
                enableBreathingGlow = dnsVpnActive,
                glowColor = emeraldAdGuard,
                contentPadding = PaddingValues(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IosLiquidGlassIconBadge(
                            icon = if (isShieldHighlighted) Icons.Default.Shield else Icons.Default.Dns,
                            contentDescription = AppStrings.dnsScreenTitle(appLanguage),
                            size = 38.dp,
                            iconSize = 19.dp,
                            tintColor = if (isShieldHighlighted) emeraldAdGuard else glassStyle.accentPrimary,
                            shape = RoundedCornerShape(11.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.dnsScreenTitle(appLanguage),
                                color = glassStyle.textPrimary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (dnsVpnActive) {
                                    "${AppStrings.dnsVpnActiveLabel(resolvedHost, appLanguage)} • $dnsQueriesForwarded"
                                } else {
                                    resolvedHost
                                },
                                fontFamily = HyperMonoFontFamily,
                                color = if (dnsVpnActive) emeraldAdGuard else glassStyle.textSecondary,
                                fontSize = 10.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    val btnContainer = if (isShieldHighlighted) emeraldAdGuard else glassStyle.accentPrimary
                    val btnFg = contrastOnColor(btnContainer)
                    IosLiquidGlassButton(
                        onClick = {
                            appHaptics.performConfirm()
                            onNavigateToDns()
                        },
                        containerColor = btnContainer,
                        contentColor = btnFg,
                        isFilledAccent = true,
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 13.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("settings_open_dns_button")
                    ) {
                        Text(
                            text = AppStrings.dnsOpenDedicatedPage(appLanguage),
                            color = btnFg,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = btnFg,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }

    // 5. Billing Cycle Reset Day Card
    val billingCycleCard: @Composable () -> Unit = {
        GlassContainer(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(14.dp)
        ) {
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
                        icon = Icons.Default.CalendarToday,
                        contentDescription = null,
                        size = 34.dp,
                        iconSize = 17.dp,
                        tintColor = glassStyle.accentPrimary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = AppStrings.billingCycleSection(appLanguage),
                            color = glassStyle.textPrimary,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = AppStrings.billingCycleDay(quota.billingCycleStartDay, appLanguage),
                            color = glassStyle.textSecondary,
                            fontSize = 11.5.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IosLiquidGlassIconButton(
                        onClick = {
                            appHaptics.performSegmentSwitch()
                            viewModel.updateBillingCycleDay(quota.billingCycleStartDay - 1)
                        },
                        icon = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        visualSize = 36.dp,
                        iconSize = 18.dp,
                        tintColor = glassStyle.accentPrimary,
                        shape = CircleShape
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "${quota.billingCycleStartDay}",
                        fontFamily = HyperMonoFontFamily,
                        color = glassStyle.accentPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    IosLiquidGlassIconButton(
                        onClick = {
                            appHaptics.performSegmentSwitch()
                            viewModel.updateBillingCycleDay(quota.billingCycleStartDay + 1)
                        },
                        icon = Icons.Default.Add,
                        contentDescription = "Increase",
                        visualSize = 36.dp,
                        iconSize = 18.dp,
                        tintColor = glassStyle.accentPrimary,
                        shape = CircleShape
                    )
                }
            }
        }
    }

    // 6. Smart Alerts & Battery Grouped Card
    val smartAlertsCard: @Composable () -> Unit = {
        Column(modifier = Modifier.fillMaxWidth()) {
            IosLiquidGlassSectionHeader(
                title = AppStrings.smartAlertsSection(appLanguage)
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassContainer(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(14.dp)
            ) {
                Column {
                    // 80% Alert Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.alert80Title(appLanguage),
                                color = glassStyle.textPrimary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = AppStrings.alert80Desc(appLanguage),
                                color = glassStyle.textTertiary,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IosLiquidGlassSwitch(
                            checked = quota.alertThreshold80,
                            onCheckedChange = {
                                appHaptics.performSegmentSwitch()
                                viewModel.toggleAlert80(it)
                            },
                            accentColor = glassStyle.statusWarning
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    IosLiquidGlassInsetDivider(startIndent = 0.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // 95% Alert Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.alert95Title(appLanguage),
                                color = glassStyle.textPrimary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = AppStrings.alert95Desc(appLanguage),
                                color = glassStyle.textTertiary,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IosLiquidGlassSwitch(
                            checked = quota.alertThreshold95,
                            onCheckedChange = {
                                appHaptics.performSegmentSwitch()
                                viewModel.toggleAlert95(it)
                            },
                            accentColor = glassStyle.statusDanger
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    IosLiquidGlassButton(
                        onClick = {
                            appHaptics.performConfirm()
                            viewModel.sendTestNotification()
                        },
                        containerColor = glassStyle.accentPrimary,
                        contentColor = glassStyle.textPrimary,
                        isFilledAccent = false,
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 11.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "اختبار الإشعار العائم (Heads-Up)" else "Test Heads-Up Alert Banner",
                                color = glassStyle.textPrimary,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "إرسال إشعار فوري للتأكد من ظهور البانر العائم في أعلى الشاشة" else "Simulate a live floating push banner with interactive buttons",
                                color = glassStyle.textTertiary,
                                fontSize = 10.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IosLiquidGlassIconBadge(
                            icon = Icons.Default.NotificationsActive,
                            contentDescription = "Test Notification",
                            size = 32.dp,
                            iconSize = 16.dp,
                            tintColor = glassStyle.accentPrimary,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }
    }

    // 7. Power Saving Card
    val powerSavingCard: @Composable () -> Unit = {
        GlassContainer(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(14.dp)
        ) {
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
                        icon = Icons.Default.Eco,
                        contentDescription = "Power Saving",
                        size = 34.dp,
                        iconSize = 18.dp,
                        tintColor = glassStyle.accentPrimary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.powerSavingTitle(appLanguage),
                            color = glassStyle.textPrimary,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (powerSavingMode)
                                AppStrings.powerSavingActive(appLanguage)
                            else
                                AppStrings.powerSavingInactive(appLanguage),
                            color = if (powerSavingMode) glassStyle.accentPrimary else glassStyle.textTertiary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IosLiquidGlassSwitch(
                    checked = powerSavingMode,
                    onCheckedChange = {
                        appHaptics.performSegmentSwitch()
                        viewModel.togglePowerSavingMode(it)
                    },
                    accentColor = glassStyle.accentPrimary,
                    modifier = Modifier.testTag("power_saving_switch")
                )
            }
        }
    }

    // 8. Permissions Status Card
    val permissionsCard: @Composable () -> Unit = {
        GlassContainer(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(14.dp)
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
                            icon = Icons.Default.Security,
                            contentDescription = "Permissions",
                            size = 34.dp,
                            iconSize = 18.dp,
                            tintColor = glassStyle.accentPrimary,
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.usageAccess(appLanguage),
                                color = glassStyle.textPrimary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (hasUsagePerm) AppStrings.permGranted(appLanguage) else AppStrings.permNotGranted(appLanguage),
                                color = if (hasUsagePerm) glassStyle.statusSafe else glassStyle.statusWarning,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (!hasUsagePerm) {
                        val grantFg = contrastOnColor(glassStyle.statusWarning)
                        IosLiquidGlassButton(
                            onClick = onRequestUsagePermission,
                            containerColor = glassStyle.statusWarning,
                            contentColor = grantFg,
                            isFilledAccent = true,
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("settings_grant_permission_button")
                        ) {
                            Text(
                                text = AppStrings.grantPerm(appLanguage),
                                color = grantFg,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    } else {
                        IosLiquidGlassIconBadge(
                            icon = Icons.Default.CheckCircle,
                            contentDescription = "Granted",
                            size = 34.dp,
                            iconSize = 18.dp,
                            tintColor = glassStyle.statusSafe,
                            shape = CircleShape
                        )
                    }
                }

                if (!hasUsagePerm) {
                    Spacer(modifier = Modifier.height(10.dp))
                    var showGuide by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .iosLiquidGlassSurface(
                                shape = RoundedCornerShape(12.dp),
                                tintColor = glassStyle.accentPrimary,
                                isFilledAccent = false,
                                elevation = 0.dp
                            )
                            .clickable { showGuide = true }
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.restrictedGuideQ(appLanguage),
                            color = glassStyle.accentPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = AppStrings.viewGuide(appLanguage),
                            color = glassStyle.accentPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (showGuide) {
                        RestrictedSettingsGuideDialog(
                            onDismiss = { showGuide = false },
                            onOpenAppInfo = onOpenAppInfo,
                            onOpenUsageSettings = onRequestUsagePermission,
                            appLanguage = appLanguage
                        )
                    }
                }
            }
        }
    }

    // 9. Developer Branding & App Update Card
    val developerCard: @Composable () -> Unit = {
        GlassContainer(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            borderColor = glassStyle.accentSecondary.copy(alpha = 0.42f),
            contentPadding = PaddingValues(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(dimensions.headerIconBoxSize + 6.dp)
                            .iosLiquidGlassSurface(
                                shape = CircleShape,
                                tintColor = glassStyle.accentPrimary,
                                isFilledAccent = false,
                                elevation = 2.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_brand_logo),
                            contentDescription = "HyperNet",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(1.5.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "HyperNet",
                            color = glassStyle.textPrimary,
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = AppStrings.developedBy(appLanguage),
                            color = glassStyle.accentPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = AppStrings.appVersionInfo(appLanguage),
                    fontFamily = HyperMonoFontFamily,
                    color = glassStyle.textSecondary,
                    fontSize = 11.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = AppStrings.appDescription(appLanguage),
                    color = glassStyle.textTertiary,
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                val isCheckingUpdate = (appUpdateState is AppUpdateUiState.Checking)
                val isUpToDate = (appUpdateState is AppUpdateUiState.UpToDate)
                IosLiquidGlassButton(
                    onClick = {
                        appHaptics.performConfirm()
                        viewModel.checkForAppUpdate(manual = true)
                    },
                    enabled = !isCheckingUpdate,
                    containerColor = glassStyle.accentPrimary,
                    contentColor = glassStyle.accentPrimary,
                    isFilledAccent = false,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_check_update_button")
                ) {
                    Icon(
                        imageVector = if (isUpToDate) Icons.Default.CheckCircle else Icons.Default.SystemUpdate,
                        contentDescription = null,
                        tint = glassStyle.accentPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            isCheckingUpdate -> if (appLanguage == AppLanguage.ARABIC) "جاري التحقق من التحديثات..." else "Checking for updates..."
                            isUpToDate -> if (appLanguage == AppLanguage.ARABIC) "أنت تستخدم أحدث إصدار ✓" else "You're on the latest version ✓"
                            else -> if (appLanguage == AppLanguage.ARABIC) "التحقق من التحديثات التلقائية" else "Check for In-App Updates"
                        },
                        color = glassStyle.accentPrimary,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = dimensions.screenPaddingHorizontal)
    ) {
        Spacer(modifier = Modifier.height(chromePadding.calculateTopPadding() + 6.dp))

        if (dimensions.isTablet) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensions.sectionSpacing)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensions.sectionSpacing)
                ) {
                    languageCard()
                    appearanceModeCard()
                    multiThemeEngineCard()
                    iosLiquidGlassCard()
                    dnsShortcutCard()
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensions.sectionSpacing)
                ) {
                    billingCycleCard()
                    smartAlertsCard()
                    powerSavingCard()
                    permissionsCard()
                    developerCard()
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.sectionSpacing)
            ) {
                languageCard()
                appearanceModeCard()
                multiThemeEngineCard()
                iosLiquidGlassCard()
                dnsShortcutCard()
                billingCycleCard()
                smartAlertsCard()
                powerSavingCard()
                permissionsCard()
                developerCard()
            }
        }

        Spacer(modifier = Modifier.height(chromePadding.calculateBottomPadding()))
    }

    if (showThemeGallery) {
        ThemeGalleryDialog(
            currentThemeMode = appThemeMode,
            appLanguage = appLanguage,
            onSelectTheme = { mode ->
                appHaptics.performSegmentSwitch()
                viewModel.setAppThemeMode(mode)
            },
            onDismiss = { showThemeGallery = false }
        )
    }
}

@Composable
fun LanguagePillOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val shape = RoundedCornerShape(11.dp)

    val animatedBgTop by animateColorAsState(
        targetValue = when {
            isSelected && glassStyle.isNeumorphic -> glassStyle.surfaceColor
            isSelected && glassStyle.isDark -> glassStyle.accentPrimary.copy(alpha = 0.24f)
            isSelected -> Color.White
            else -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 220),
        label = "lang_bg_top"
    )
    val animatedBgBottom by animateColorAsState(
        targetValue = when {
            isSelected && glassStyle.isNeumorphic -> glassStyle.neumorphicBase
            isSelected && glassStyle.isDark -> glassStyle.accentSecondary.copy(alpha = 0.14f)
            isSelected -> glassStyle.accentPrimary.copy(alpha = 0.08f)
            else -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 220),
        label = "lang_bg_bottom"
    )
    val animatedBorderStart by animateColorAsState(
        targetValue = if (isSelected) glassStyle.accentPrimary else Color.Transparent,
        animationSpec = tween(durationMillis = 220),
        label = "lang_border_start"
    )
    val animatedBorderEnd by animateColorAsState(
        targetValue = if (isSelected) glassStyle.accentSecondary.copy(alpha = 0.65f) else Color.Transparent,
        animationSpec = tween(durationMillis = 220),
        label = "lang_border_end"
    )
    val animatedTextColor by animateColorAsState(
        targetValue = if (isSelected) glassStyle.textPrimary else glassStyle.textSecondary,
        animationSpec = tween(durationMillis = 200),
        label = "lang_text"
    )

    Box(
        modifier = modifier
            .height(48.dp)
            .tactileSpringPress(pressedScale = 0.95f, onClick = onClick)
            .then(
                when {
                    isSelected && glassStyle.isNeumorphic -> Modifier.neumorphicDepth(
                        shape = shape,
                        lightColor = glassStyle.neumorphicHighlight,
                        darkColor = glassStyle.neumorphicShadow,
                        offsetDp = 3.dp,
                        surfaceColor = glassStyle.surfaceColor,
                        blurDp = 6.dp
                    )
                    else -> Modifier
                }
            )
            .then(
                if (isSelected && glassStyle.iosLiquidGlassEnabled) {
                    Modifier.iosLiquidGlassSurface(
                        shape = shape,
                        tintColor = glassStyle.accentPrimary,
                        isFilledAccent = false,
                        elevation = if (glassStyle.isNeumorphic) 0.dp else 4.dp
                    )
                } else {
                    Modifier
                        .clip(shape)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(animatedBgTop, animatedBgBottom)
                            )
                        )
                        .border(
                            width = if (isSelected) 1.2.dp else 0.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(animatedBorderStart, animatedBorderEnd)
                            ),
                            shape = shape
                        )
                }
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = label,
            transitionSpec = {
                fadeIn(tween(180)) togetherWith fadeOut(tween(140))
            },
            label = "language_pill_text"
        ) { targetText ->
            Text(
                text = targetText,
                color = animatedTextColor,
                fontSize = 13.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                letterSpacing = 0.25.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SmallThemeButton(
    themeMode: AppThemeMode,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    appLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val shape = RoundedCornerShape(14.dp)

    val animatedBg by animateColorAsState(
        targetValue = when {
            isSelected && glassStyle.isNeumorphic -> glassStyle.cardSubtleBg
            isSelected -> accentColor.copy(alpha = if (glassStyle.isDark) 0.22f else 0.14f)
            glassStyle.isNeumorphic -> glassStyle.neumorphicSurface
            else -> glassStyle.cardSubtleBg
        },
        animationSpec = tween(durationMillis = 200),
        label = "small_theme_bg"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = when {
            isSelected -> accentColor
            glassStyle.isNeumorphic -> glassStyle.neumorphicBorderLight
            else -> glassStyle.borderColor
        },
        animationSpec = tween(durationMillis = 200),
        label = "small_theme_border"
    )

    val animatedContentColor by animateColorAsState(
        targetValue = if (isSelected) glassStyle.textPrimary else glassStyle.textSecondary,
        animationSpec = tween(durationMillis = 200),
        label = "small_theme_text"
    )

    Box(
        modifier = modifier
            .height(48.dp)
            .tactileSpringPress(pressedScale = 0.95f, onClick = onClick)
            .then(
                if (glassStyle.isNeumorphic) {
                    Modifier.neumorphicDepth(
                        shape = shape,
                        lightColor = glassStyle.neumorphicHighlight,
                        darkColor = glassStyle.neumorphicShadow,
                        offsetDp = 3.dp,
                        isPressed = isSelected,
                        surfaceColor = if (isSelected) Color.Unspecified else glassStyle.neumorphicSurface,
                        blurDp = 6.dp
                    )
                } else Modifier
            )
            .then(
                if (glassStyle.iosLiquidGlassEnabled) {
                    Modifier.iosLiquidGlassSurface(
                        shape = shape,
                        tintColor = if (isSelected) accentColor else glassStyle.textTertiary,
                        isFilledAccent = false,
                        isPressed = isSelected,
                        elevation = if (glassStyle.isNeumorphic || !isSelected) 0.dp else 4.dp
                    )
                } else {
                    Modifier
                        .clip(shape)
                        .background(animatedBg)
                        .border(
                            width = if (isSelected) 1.2.dp else 0.8.dp,
                            color = animatedBorderColor,
                            shape = shape
                        )
                }
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = themeMode.getDisplayName(appLanguage),
                tint = if (isSelected) accentColor else glassStyle.textTertiary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = themeMode.getDisplayName(appLanguage),
                color = animatedContentColor,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
