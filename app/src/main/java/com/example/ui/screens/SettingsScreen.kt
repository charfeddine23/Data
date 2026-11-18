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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.BrightnessAuto
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.ui.components.ThemeGalleryDialog
import com.example.ui.components.ThemeSwatchCard
import com.example.ui.components.neumorphicDepth
import com.example.ui.components.rememberAppHaptics
import com.example.ui.components.tactileSpringPress
import com.example.update.AppUpdateUiState
import com.example.ui.theme.AmberAlert
import com.example.ui.theme.LocalAppDimensions
import com.example.ui.theme.NeumorphicColors
import com.example.ui.theme.NeumorphicDarkColors
import com.example.ui.theme.RedAlert

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
    val scrollState = rememberScrollState()
    val glassStyle = com.example.ui.theme.LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current
    val appHaptics = rememberAppHaptics()
    var showThemeGallery by remember { mutableStateOf(false) }

    // 1. Language Selection Card
    val languageCard: @Composable () -> Unit = {
        GlassContainer(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = glassStyle.accentPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.appLanguageSection(appLanguage),
                        color = glassStyle.textPrimary,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Sleek, luxurious frosted segmented pill selector
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(glassStyle.cardSubtleBg)
                        .border(0.8.dp, glassStyle.borderColor, RoundedCornerShape(14.dp))
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val arabicLabel = "العربية"
                        val englishLabel = if (appLanguage == AppLanguage.ARABIC) "الإنغليزية" else "English"

                        LanguagePillOption(
                            label = arabicLabel,
                            isSelected = appLanguage == AppLanguage.ARABIC,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setAppLanguage(AppLanguage.ARABIC)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("language_option_arabic")
                        )

                        LanguagePillOption(
                            label = englishLabel,
                            isSelected = appLanguage == AppLanguage.ENGLISH,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setAppLanguage(AppLanguage.ENGLISH)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("language_option_english")
                        )
                    }
                }
            }
        }
    }

    // 2. Separated Light & Dark Mode Card (Appearance Mode)
    val appearanceModeCard: @Composable () -> Unit = {
        GlassContainer(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(glassStyle.accentPrimary.copy(alpha = if (glassStyle.isDark) 0.18f else 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (themeMode == ThemeMode.DARK || (themeMode == ThemeMode.SYSTEM && glassStyle.isDark)) {
                                    Icons.Default.DarkMode
                                } else {
                                    Icons.Default.LightMode
                                },
                                contentDescription = "Appearance Mode",
                                tint = glassStyle.accentPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) "الوضع الليلي والنهاري" else "Light & Dark Mode",
                            color = glassStyle.textPrimary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(glassStyle.cardSubtleBg)
                            .border(0.8.dp, glassStyle.borderColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = when (themeMode) {
                                ThemeMode.SYSTEM -> if (appLanguage == AppLanguage.ARABIC) "تلقائي" else "System"
                                ThemeMode.LIGHT -> if (appLanguage == AppLanguage.ARABIC) "نهاري" else "Light"
                                ThemeMode.DARK -> if (appLanguage == AppLanguage.ARABIC) "ليلي" else "Dark"
                            },
                            color = glassStyle.accentPrimary,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(glassStyle.cardSubtleBg)
                        .border(0.8.dp, glassStyle.borderColor, RoundedCornerShape(14.dp))
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LanguagePillOption(
                            label = if (appLanguage == AppLanguage.ARABIC) "اتبع الهاتف" else "System",
                            isSelected = themeMode == ThemeMode.SYSTEM,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setThemeMode(ThemeMode.SYSTEM)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("appearance_mode_system")
                        )

                        LanguagePillOption(
                            label = if (appLanguage == AppLanguage.ARABIC) "نهاري" else "Light",
                            isSelected = themeMode == ThemeMode.LIGHT,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setThemeMode(ThemeMode.LIGHT)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("appearance_mode_light")
                        )

                        LanguagePillOption(
                            label = if (appLanguage == AppLanguage.ARABIC) "ليلي" else "Dark",
                            isSelected = themeMode == ThemeMode.DARK,
                            onClick = {
                                appHaptics.performSegmentSwitch()
                                viewModel.setThemeMode(ThemeMode.DARK)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("appearance_mode_dark")
                        )
                    }
                }
            }
        }
    }

    // 3. Dedicated Multi-Theme Engine Section (Core Engines + Dark/Light Neumorphism + 60 Themes)
    val extendedThemes = remember {
        AppThemeMode.values().filter { it.category != ThemeCategory.CORE }
    }

    val multiThemeEngineCard: @Composable () -> Unit = {
        GlassContainer(modifier = Modifier.fillMaxWidth()) {
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
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(glassStyle.accentPrimary.copy(alpha = if (glassStyle.isDark) 0.18f else 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Theme",
                                tint = glassStyle.accentPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Multi-Theme Engine",
                            color = glassStyle.textPrimary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Active theme badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(glassStyle.cardSubtleBg)
                            .border(0.8.dp, glassStyle.borderColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = appThemeMode.getDisplayName(appLanguage),
                            color = glassStyle.accentPrimary,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Row 1: Glass & Cyber Engines
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SmallThemeButton(
                        themeMode = AppThemeMode.LIQUID_GLASS_DEFAULT,
                        icon = Icons.Default.DarkMode,
                        accentColor = com.example.ui.theme.LiquidGlassDefaultPrimary,
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
                        accentColor = com.example.ui.theme.LiquidNeonCyanPrimary,
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
                        accentColor = com.example.ui.theme.AmoledPitchBlackPrimary,
                        isSelected = appThemeMode == AppThemeMode.AMOLED_PITCH_BLACK,
                        appLanguage = appLanguage,
                        onClick = {
                            appHaptics.performSegmentSwitch()
                            viewModel.setAppThemeMode(AppThemeMode.AMOLED_PITCH_BLACK)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Row 2: Pearl Glass, Neumorphism Light & Neumorphism Dark
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SmallThemeButton(
                        themeMode = AppThemeMode.LIGHT_MODE,
                        icon = Icons.Default.LightMode,
                        accentColor = com.example.ui.theme.LightModePrimary,
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

                // Subheader + "Explore All 66 Themes" Trigger
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (appLanguage == AppLanguage.ARABIC) {
                            "مكتبة الثيمات (+${extendedThemes.size} ثيم)"
                        } else {
                            "Curated Studio (${extendedThemes.size} Themes)"
                        },
                        color = glassStyle.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(
                        modifier = Modifier
                            .tactileSpringPress(pressedScale = 0.95f, onClick = { showThemeGallery = true })
                            .clip(RoundedCornerShape(9.dp))
                            .background(glassStyle.accentPrimary.copy(alpha = if (glassStyle.isDark) 0.18f else 0.12f))
                            .border(0.8.dp, glassStyle.accentPrimary.copy(alpha = 0.45f), RoundedCornerShape(9.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("open_theme_gallery_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = null,
                                tint = glassStyle.accentPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "استعراض الكل (66)" else "Browse All (66)",
                                color = glassStyle.accentPrimary,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal scrollable 60-theme color swatch strip
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
                            modifier = Modifier.width(142.dp)
                        )
                    }
                }
            }
        }
    }

    // 3. Dedicated DNS Shield Shortcut Card (Full engine lives in its own bottom-bar screen)
    val dnsShortcutCard: @Composable () -> Unit = {
        val emeraldAdGuard = Color(0xFF00E676)
        val isShieldHighlighted = dnsVpnActive || privateDnsPreset == PrivateDnsPreset.ADGUARD
        val resolvedHost = when {
            dnsVpnActive -> dnsVpnHost
            privateDnsPreset == PrivateDnsPreset.CUSTOM && privateDnsCustomHostname.isNotBlank() -> privateDnsCustomHostname
            privateDnsPreset.hostname != null -> privateDnsPreset.hostname!!
            else -> AppStrings.dnsIspMode(appLanguage)
        }

        GlassContainer(
            modifier = Modifier.fillMaxWidth(),
            borderColor = if (isShieldHighlighted) {
                emeraldAdGuard.copy(alpha = if (glassStyle.isDark) 0.55f else 0.40f)
            } else {
                glassStyle.borderColor
            },
            enableBreathingGlow = dnsVpnActive,
            glowColor = emeraldAdGuard
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
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .background(
                                    if (isShieldHighlighted) {
                                        emeraldAdGuard.copy(alpha = if (glassStyle.isDark) 0.20f else 0.14f)
                                    } else {
                                        glassStyle.accentPrimary.copy(alpha = if (glassStyle.isDark) 0.18f else 0.12f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isShieldHighlighted) Icons.Default.Shield else Icons.Default.Dns,
                                contentDescription = AppStrings.dnsScreenTitle(appLanguage),
                                tint = if (isShieldHighlighted) emeraldAdGuard else glassStyle.accentPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = AppStrings.dnsScreenTitle(appLanguage),
                                color = glassStyle.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (dnsVpnActive) {
                                    "${AppStrings.dnsVpnActiveLabel(resolvedHost, appLanguage)} • $dnsQueriesForwarded"
                                } else {
                                    resolvedHost
                                },
                                color = if (dnsVpnActive) emeraldAdGuard else glassStyle.textSecondary,
                                fontSize = 10.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .tactileSpringPress(
                                pressedScale = 0.95f,
                                onClick = {
                                    appHaptics.performConfirm()
                                    onNavigateToDns()
                                }
                            )
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isShieldHighlighted) emeraldAdGuard else glassStyle.accentPrimary
                            )
                            .padding(horizontal = 10.dp, vertical = 7.dp)
                            .testTag("settings_open_dns_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        val btnFg = if (glassStyle.isNeumorphic) Color.White else Color.Black
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.dnsOpenDedicatedPage(appLanguage),
                                color = btnFg,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = btnFg,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // 4. Billing Cycle Reset Day
    val billingCycleCard: @Composable () -> Unit = {
        GlassContainer(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = AppStrings.billingCycleSection(appLanguage),
                            color = glassStyle.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = AppStrings.billingCycleDay(quota.billingCycleStartDay, appLanguage),
                            color = glassStyle.textSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.updateBillingCycleDay(quota.billingCycleStartDay - 1) },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(glassStyle.cardSubtleBg)
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", tint = glassStyle.accentPrimary)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${quota.billingCycleStartDay}",
                            color = glassStyle.accentPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(
                            onClick = { viewModel.updateBillingCycleDay(quota.billingCycleStartDay + 1) },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(glassStyle.cardSubtleBg)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = glassStyle.accentPrimary)
                        }
                    }
                }
            }
        }
    }

    // 5. Smart Alert Thresholds Card
    val smartAlertsCard: @Composable () -> Unit = {
        GlassContainer(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Alerts",
                        tint = AmberAlert,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.smartAlertsSection(appLanguage),
                        color = glassStyle.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

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
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = AppStrings.alert80Desc(appLanguage),
                            color = glassStyle.textTertiary,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = quota.alertThreshold80,
                        onCheckedChange = { viewModel.toggleAlert80(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AmberAlert,
                            checkedTrackColor = AmberAlert.copy(alpha = 0.4f),
                            uncheckedThumbColor = glassStyle.textTertiary,
                            uncheckedTrackColor = if (glassStyle.isDark) Color(0xFF162238) else Color(0xFFCBD5E1)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

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
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = AppStrings.alert95Desc(appLanguage),
                            color = glassStyle.textTertiary,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = quota.alertThreshold95,
                        onCheckedChange = { viewModel.toggleAlert95(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = RedAlert,
                            checkedTrackColor = RedAlert.copy(alpha = 0.4f),
                            uncheckedThumbColor = glassStyle.textTertiary,
                            uncheckedTrackColor = if (glassStyle.isDark) Color(0xFF162238) else Color(0xFFCBD5E1)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Test Heads-Up Notification Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(glassStyle.accentPrimary.copy(alpha = 0.12f))
                        .border(1.dp, glassStyle.accentPrimary.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                        .clickable { viewModel.sendTestNotification() }
                        .padding(horizontal = 14.dp, vertical = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (appLanguage == com.example.model.AppLanguage.ARABIC) "اختبار الإشعار العائم (Heads-Up)" else "Test Heads-Up Alert Banner",
                            color = glassStyle.textPrimary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (appLanguage == com.example.model.AppLanguage.ARABIC) "إرسال إشعار فوري للتأكد من ظهور البانر العائم في أعلى الشاشة" else "Simulate a live floating push banner with interactive buttons",
                            color = glassStyle.textTertiary,
                            fontSize = 10.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Test Notification",
                        tint = glassStyle.accentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    // 6. Power Saving & Battery Card
    val powerSavingCard: @Composable () -> Unit = {
        GlassContainer(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = "Power Saving",
                        tint = glassStyle.accentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.powerSavingSection(appLanguage),
                        color = glassStyle.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.powerSavingTitle(appLanguage),
                            color = glassStyle.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
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

                    Switch(
                        checked = powerSavingMode,
                        onCheckedChange = { viewModel.togglePowerSavingMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = glassStyle.accentPrimary,
                            checkedTrackColor = glassStyle.accentPrimary.copy(alpha = 0.4f),
                            uncheckedThumbColor = glassStyle.textTertiary,
                            uncheckedTrackColor = if (glassStyle.isDark) Color(0xFF162238) else Color(0xFFCBD5E1)
                        ),
                        modifier = Modifier.testTag("power_saving_switch")
                    )
                }
            }
        }
    }

    // 7. Permissions Status Card (Cleaned - without PACKAGE_USAGE_STATS)
    val permissionsCard: @Composable () -> Unit = {
        GlassContainer(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Permissions",
                        tint = glassStyle.accentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.permissionsSection(appLanguage),
                        color = glassStyle.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.usageAccess(appLanguage),
                            color = glassStyle.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (hasUsagePerm) AppStrings.permGranted(appLanguage) else AppStrings.permNotGranted(appLanguage),
                            color = if (hasUsagePerm) glassStyle.accentPrimary else AmberAlert,
                            fontSize = 11.sp
                        )
                    }

                    if (!hasUsagePerm) {
                        Button(
                            onClick = onRequestUsagePermission,
                            colors = ButtonDefaults.buttonColors(containerColor = AmberAlert),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("settings_grant_permission_button")
                        ) {
                            Text(AppStrings.grantPerm(appLanguage), color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Granted",
                            tint = glassStyle.accentPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                if (!hasUsagePerm) {
                    Spacer(modifier = Modifier.height(10.dp))
                    var showGuide by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(glassStyle.cardSubtleBg)
                            .clickable { showGuide = true }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
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
                        com.example.ui.components.RestrictedSettingsGuideDialog(
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

    // 8. Developer Branding & App Version Card
    val developerCard: @Composable () -> Unit = {
        GlassContainer(
            modifier = Modifier.fillMaxWidth(),
            borderColor = glassStyle.accentSecondary.copy(alpha = 0.4f)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(dimensions.headerIconBoxSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_brand_logo),
                            contentDescription = "HyperNet",
                            modifier = Modifier.size(dimensions.headerIconSize)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "HyperNet",
                            color = glassStyle.textPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
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
                    color = glassStyle.textSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = AppStrings.appDescription(appLanguage),
                    color = glassStyle.textTertiary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // In-Device Automatic Update Check Trigger
                val isCheckingUpdate = (appUpdateState is AppUpdateUiState.Checking)
                val isUpToDate = (appUpdateState is AppUpdateUiState.UpToDate)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .tactileSpringPress(
                            pressedScale = 0.965f,
                            enabled = !isCheckingUpdate,
                            onClick = {
                                appHaptics.performConfirm()
                                viewModel.checkForAppUpdate(manual = true)
                            }
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            glassStyle.accentPrimary.copy(alpha = if (glassStyle.isDark) 0.14f else 0.10f)
                        )
                        .border(
                            1.dp,
                            glassStyle.accentPrimary.copy(alpha = 0.45f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 9.dp)
                        .testTag("settings_check_update_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
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
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
        Spacer(modifier = Modifier.height(dimensions.screenPaddingVertical))

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
                dnsShortcutCard()
                billingCycleCard()
                smartAlertsCard()
                powerSavingCard()
                permissionsCard()
                developerCard()
            }
        }

        Spacer(modifier = Modifier.height(dimensions.sectionSpacing * 2))
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
    val glassStyle = com.example.ui.theme.LocalLiquidGlass.current
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
        targetValue = when {
            isSelected && glassStyle.isNeumorphic -> if (glassStyle.isDark) Color.White else glassStyle.accentPrimary
            isSelected && glassStyle.isDark -> Color.White
            isSelected -> glassStyle.textPrimary
            else -> glassStyle.textSecondary
        },
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
                    isSelected && !glassStyle.isDark -> Modifier.shadow(
                        elevation = 3.dp,
                        shape = shape,
                        spotColor = Color(0x140F172A),
                        ambientColor = Color(0x0A0F172A)
                    )
                    else -> Modifier
                }
            )
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
    themeMode: com.example.model.AppThemeMode,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    appLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassStyle = com.example.ui.theme.LocalLiquidGlass.current
    val shape = RoundedCornerShape(12.dp)

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
        targetValue = if (isSelected) {
            if (glassStyle.isDark) Color.White else glassStyle.textPrimary
        } else {
            glassStyle.textSecondary
        },
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
            .clip(shape)
            .background(animatedBg)
            .border(
                width = if (isSelected) 1.2.dp else 0.8.dp,
                color = animatedBorderColor,
                shape = shape
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
                modifier = Modifier.size(13.dp)
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
