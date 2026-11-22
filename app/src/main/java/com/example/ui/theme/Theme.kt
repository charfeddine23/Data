package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.model.AppLanguage
import com.example.model.AppThemeMode
import com.example.model.ThemeMode

/**
 * Computes a high-contrast foreground color (deep ink vs pure crisp white)
 * for any filled button, badge, or pill background color.
 */
fun contrastOnColor(background: Color): Color {
    return if (background.luminance() > 0.42f) Color(0xFF0A1018) else Color(0xFFFFFFFF)
}

@Immutable
data class LiquidGlassStyle(
    val surfaceColor: Color,
    val surfaceTranslucent: Color,
    val borderColor: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val accentPrimary: Color,
    val accentSecondary: Color,
    val cardBackground: Color,
    val cardSubtleBg: Color,
    val rimLightBrush: Brush,
    val backdropBrush: Brush,
    val blurRadius: Dp = 16.dp,
    val isDark: Boolean = true,
    val isNeumorphic: Boolean = false,
    val neumorphicBase: Color = Color(0xFF0F1423),
    val neumorphicSurface: Color = Color(0xFF141C2E),
    val neumorphicHighlight: Color = Color(0x384E699A),
    val neumorphicShadow: Color = Color(0xE603050B),
    val neumorphicBorderLight: Color = Color(0x2BFFFFFF),
    val neumorphicBorderDark: Color = Color(0x66000000),
    val iosLiquidGlassEnabled: Boolean = true,
    val onAccentPrimary: Color = contrastOnColor(accentPrimary),
    val onAccentSecondary: Color = contrastOnColor(accentSecondary),
    val statusSafe: Color = if (isDark) Color(0xFF10E88A) else Color(0xFF059669),
    val statusWarning: Color = if (isDark) Color(0xFFFFB74D) else Color(0xFFD97706),
    val statusDanger: Color = if (isDark) Color(0xFFFF5252) else Color(0xFFDC2626),
    val progressTrackBg: Color = if (isDark) Color(0x3394A3B8) else Color(0x2264748B)
)

val LocalLiquidGlass = staticCompositionLocalOf {
    createLiquidGlassStyle(AppThemeMode.LIQUID_GLASS_DEFAULT)
}

/**
 * Creates Material 3 colorScheme corresponding to each AppThemeMode,
 * seamlessly resolving SYSTEM based on the device's isSystemInDarkTheme() setting.
 */
fun createAppColorScheme(mode: AppThemeMode, isSystemDark: Boolean = false): ColorScheme = when (mode) {
    AppThemeMode.SYSTEM -> {
        if (isSystemDark) createAppColorScheme(AppThemeMode.LIQUID_GLASS_DEFAULT, true)
        else createAppColorScheme(AppThemeMode.LIGHT_MODE, false)
    }

    AppThemeMode.LIGHT_MODE -> lightColorScheme(
        primary = PearlPlatinumPrimary,
        onPrimary = contrastOnColor(PearlPlatinumPrimary),
        primaryContainer = Color(0xFFDBEAFE),
        onPrimaryContainer = Color(0xFF1E3A8A),
        secondary = PearlPlatinumAccent,
        onSecondary = contrastOnColor(PearlPlatinumAccent),
        secondaryContainer = Color(0xFFEDE9FE),
        onSecondaryContainer = Color(0xFF4C1D95),
        tertiary = LightPulseOceanCyan,
        onTertiary = contrastOnColor(LightPulseOceanCyan),
        background = PearlPlatinumLightBase,
        onBackground = PearlPlatinumTextPrimary,
        surface = PearlPlatinumSurface,
        onSurface = PearlPlatinumTextPrimary,
        surfaceVariant = PearlPlatinumCardSubtleBg,
        onSurfaceVariant = PearlPlatinumTextSecondary,
        outline = PearlPlatinumBorder,
        outlineVariant = PearlPlatinumBorderSubtle,
        error = LightPulseCoralRed,
        onError = Color.White
    )

    AppThemeMode.NEUMORPHISM -> lightColorScheme(
        primary = NeumorphicColors.AccentBlue,
        onPrimary = contrastOnColor(NeumorphicColors.AccentBlue),
        primaryContainer = Color(0xFFDBEAFE),
        onPrimaryContainer = Color(0xFF1E3A8A),
        secondary = NeumorphicColors.AccentMint,
        onSecondary = contrastOnColor(NeumorphicColors.AccentMint),
        secondaryContainer = Color(0xFFD1FAE5),
        onSecondaryContainer = Color(0xFF065F46),
        tertiary = NeumorphicColors.AccentBlue,
        onTertiary = contrastOnColor(NeumorphicColors.AccentBlue),
        background = NeumorphicColors.Background,
        onBackground = NeumorphicColors.TextPrimary,
        surface = NeumorphicColors.Surface,
        onSurface = NeumorphicColors.TextPrimary,
        surfaceVariant = NeumorphicColors.SurfaceSubtle,
        onSurfaceVariant = NeumorphicColors.TextSecondary,
        outline = NeumorphicColors.DarkShadow,
        outlineVariant = NeumorphicColors.BorderDark,
        error = NeumorphicColors.AccentRed,
        onError = Color.White
    )

    AppThemeMode.NEUMORPHISM_DARK -> darkColorScheme(
        primary = NeumorphicDarkColors.AccentBlue,
        onPrimary = contrastOnColor(NeumorphicDarkColors.AccentBlue),
        primaryContainer = Color(0xFF1E3A8A),
        onPrimaryContainer = Color(0xFFDBEAFE),
        secondary = NeumorphicDarkColors.AccentMint,
        onSecondary = contrastOnColor(NeumorphicDarkColors.AccentMint),
        secondaryContainer = Color(0xFF064E3B),
        onSecondaryContainer = Color(0xFFD1FAE5),
        tertiary = NeumorphicDarkColors.AccentBlue,
        onTertiary = contrastOnColor(NeumorphicDarkColors.AccentBlue),
        background = NeumorphicDarkColors.Background,
        onBackground = NeumorphicDarkColors.TextPrimary,
        surface = NeumorphicDarkColors.Surface,
        onSurface = NeumorphicDarkColors.TextPrimary,
        surfaceVariant = NeumorphicDarkColors.SurfaceSubtle,
        onSurfaceVariant = NeumorphicDarkColors.TextSecondary,
        outline = NeumorphicDarkColors.Highlight,
        outlineVariant = NeumorphicDarkColors.BorderDark,
        error = NeumorphicDarkColors.AccentRed,
        onError = Color.White
    )

    AppThemeMode.LIQUID_GLASS_DEFAULT -> darkColorScheme(
        primary = LiquidGlassDefaultPrimary,
        onPrimary = contrastOnColor(LiquidGlassDefaultPrimary),
        primaryContainer = Color(0xFF003B4A),
        onPrimaryContainer = Color(0xFFB3F7FF),
        secondary = LiquidGlassDefaultAccent,
        onSecondary = contrastOnColor(LiquidGlassDefaultAccent),
        secondaryContainer = Color(0xFF004528),
        onSecondaryContainer = Color(0xFFB9FCD8),
        tertiary = ElectricBlue,
        onTertiary = contrastOnColor(ElectricBlue),
        background = LiquidGlassDefaultBackground,
        onBackground = TextPrimary,
        surface = LiquidGlassDefaultSurface,
        onSurface = TextPrimary,
        surfaceVariant = Color(0xFF131D31),
        onSurfaceVariant = TextSecondary,
        outline = Color(0x4094A3B8),
        error = RedAlert,
        onError = Color.White
    )

    AppThemeMode.LIQUID_NEON_CYAN -> darkColorScheme(
        primary = LiquidNeonCyanPrimary,
        onPrimary = contrastOnColor(LiquidNeonCyanPrimary),
        primaryContainer = Color(0xFF004F59),
        onPrimaryContainer = LiquidNeonCyanPrimary,
        secondary = LiquidNeonCyanAccent,
        onSecondary = contrastOnColor(LiquidNeonCyanAccent),
        secondaryContainer = Color(0xFF005330),
        onSecondaryContainer = LiquidNeonCyanAccent,
        tertiary = CyanPulse,
        onTertiary = contrastOnColor(CyanPulse),
        background = LiquidNeonCyanBackground,
        onBackground = TextPrimary,
        surface = LiquidNeonCyanSurface,
        onSurface = TextPrimary,
        surfaceVariant = Color(0xFF0E1A2F),
        onSurfaceVariant = TextSecondary,
        outline = Color(0x4000E5FF),
        error = RedAlert,
        onError = Color.White
    )

    AppThemeMode.AMOLED_PITCH_BLACK -> darkColorScheme(
        primary = AmoledPitchBlackPrimary,
        onPrimary = contrastOnColor(AmoledPitchBlackPrimary),
        primaryContainer = Color(0xFF1E3A8A),
        onPrimaryContainer = Color(0xFFDBEAFE),
        secondary = AmoledPitchBlackAccent,
        onSecondary = contrastOnColor(AmoledPitchBlackAccent),
        secondaryContainer = Color(0xFF155E75),
        onSecondaryContainer = Color(0xFFCFFAFE),
        tertiary = ElectricBlue,
        onTertiary = contrastOnColor(ElectricBlue),
        background = AmoledPitchBlackBackground,
        onBackground = TextPrimary,
        surface = AmoledPitchBlackSurface,
        onSurface = TextPrimary,
        surfaceVariant = Color(0xFF1A1A1A),
        onSurfaceVariant = TextSecondary,
        outline = Color(0x33FFFFFF),
        error = RedAlert,
        onError = Color.White
    )

    else -> createExtendedColorScheme(getExtendedPaletteSpec(mode))
}

/**
 * Creates custom LiquidGlassStyle tokens corresponding to each AppThemeMode
 */
fun createLiquidGlassStyle(
    mode: AppThemeMode,
    isSystemDark: Boolean = false,
    iosLiquidGlassEnabled: Boolean = true
): LiquidGlassStyle = when (mode) {
    AppThemeMode.SYSTEM -> {
        if (isSystemDark) createLiquidGlassStyle(AppThemeMode.LIQUID_GLASS_DEFAULT, true, iosLiquidGlassEnabled)
        else createLiquidGlassStyle(AppThemeMode.LIGHT_MODE, false, iosLiquidGlassEnabled)
    }

    AppThemeMode.LIGHT_MODE -> LiquidGlassStyle(
        surfaceColor = PearlPlatinumSurface,
        surfaceTranslucent = PearlPlatinumSurfaceTranslucent,
        borderColor = PearlPlatinumBorder,
        textPrimary = PearlPlatinumTextPrimary,
        textSecondary = PearlPlatinumTextSecondary,
        textTertiary = PearlPlatinumTextTertiary,
        accentPrimary = PearlPlatinumPrimary,
        accentSecondary = PearlPlatinumAccent,
        cardBackground = PearlPlatinumCardBg,
        cardSubtleBg = PearlPlatinumCardSubtleBg,
        rimLightBrush = Brush.linearGradient(
            listOf(
                Color(0x99FFFFFF),
                Color(0x240066FF),
                Color(0x55FFFFFF)
            )
        ),
        backdropBrush = Brush.verticalGradient(
            listOf(
                Color(0xFFFBFBFD),
                Color(0xFFF4F6F9),
                Color(0xFFE9ECEF)
            )
        ),
        blurRadius = 20.dp,
        isDark = false,
        neumorphicBase = PearlPlatinumLightBase,
        neumorphicSurface = PearlPlatinumSurface,
        neumorphicHighlight = Color(0xCCFFFFFF),
        neumorphicShadow = Color(0x0F0F172A),
        neumorphicBorderLight = Color(0x80FFFFFF),
        neumorphicBorderDark = Color(0x0D0F172A),
        iosLiquidGlassEnabled = iosLiquidGlassEnabled,
        onAccentPrimary = contrastOnColor(PearlPlatinumPrimary),
        onAccentSecondary = contrastOnColor(PearlPlatinumAccent),
        statusSafe = Color(0xFF059669),
        statusWarning = Color(0xFFD97706),
        statusDanger = Color(0xFFDC2626),
        progressTrackBg = Color(0x1F0F172A)
    )

    AppThemeMode.NEUMORPHISM -> LiquidGlassStyle(
        surfaceColor = NeumorphicColors.Surface,
        surfaceTranslucent = NeumorphicColors.Surface,
        borderColor = NeumorphicColors.DarkShadow,
        textPrimary = NeumorphicColors.TextPrimary,
        textSecondary = NeumorphicColors.TextSecondary,
        textTertiary = NeumorphicColors.TextTertiary,
        accentPrimary = NeumorphicColors.AccentBlue,
        accentSecondary = NeumorphicColors.AccentMint,
        cardBackground = NeumorphicColors.Surface,
        cardSubtleBg = NeumorphicColors.SurfaceSubtle,
        rimLightBrush = Brush.linearGradient(
            listOf(
                NeumorphicColors.Highlight,
                NeumorphicColors.DarkShadow.copy(alpha = 0.75f)
            )
        ),
        backdropBrush = Brush.verticalGradient(
            listOf(
                NeumorphicColors.Background,
                NeumorphicColors.Background
            )
        ),
        blurRadius = 0.dp,
        isDark = false,
        isNeumorphic = true,
        neumorphicBase = NeumorphicColors.Background,
        neumorphicSurface = NeumorphicColors.Surface,
        neumorphicHighlight = NeumorphicColors.Highlight,
        neumorphicShadow = NeumorphicColors.DarkShadow,
        neumorphicBorderLight = NeumorphicColors.BorderLight,
        neumorphicBorderDark = NeumorphicColors.BorderDark,
        iosLiquidGlassEnabled = iosLiquidGlassEnabled,
        onAccentPrimary = contrastOnColor(NeumorphicColors.AccentBlue),
        onAccentSecondary = contrastOnColor(NeumorphicColors.AccentMint),
        statusSafe = Color(0xFF059669),
        statusWarning = Color(0xFFD97706),
        statusDanger = NeumorphicColors.AccentRed,
        progressTrackBg = Color(0x45B8C4D4)
    )

    AppThemeMode.NEUMORPHISM_DARK -> LiquidGlassStyle(
        surfaceColor = NeumorphicDarkColors.Surface,
        surfaceTranslucent = NeumorphicDarkColors.Surface,
        borderColor = NeumorphicDarkColors.DarkShadow,
        textPrimary = NeumorphicDarkColors.TextPrimary,
        textSecondary = NeumorphicDarkColors.TextSecondary,
        textTertiary = NeumorphicDarkColors.TextTertiary,
        accentPrimary = NeumorphicDarkColors.AccentBlue,
        accentSecondary = NeumorphicDarkColors.AccentMint,
        cardBackground = NeumorphicDarkColors.Surface,
        cardSubtleBg = NeumorphicDarkColors.SurfaceSubtle,
        rimLightBrush = Brush.linearGradient(
            listOf(
                NeumorphicDarkColors.Highlight,
                NeumorphicDarkColors.DarkShadow.copy(alpha = 0.85f)
            )
        ),
        backdropBrush = Brush.verticalGradient(
            listOf(
                NeumorphicDarkColors.Background,
                NeumorphicDarkColors.Background
            )
        ),
        blurRadius = 0.dp,
        isDark = true,
        isNeumorphic = true,
        neumorphicBase = NeumorphicDarkColors.Background,
        neumorphicSurface = NeumorphicDarkColors.Surface,
        neumorphicHighlight = NeumorphicDarkColors.Highlight,
        neumorphicShadow = NeumorphicDarkColors.DarkShadow,
        neumorphicBorderLight = NeumorphicDarkColors.BorderLight,
        neumorphicBorderDark = NeumorphicDarkColors.BorderDark,
        iosLiquidGlassEnabled = iosLiquidGlassEnabled,
        onAccentPrimary = contrastOnColor(NeumorphicDarkColors.AccentBlue),
        onAccentSecondary = contrastOnColor(NeumorphicDarkColors.AccentMint),
        statusSafe = Color(0xFF10E88A),
        statusWarning = Color(0xFFFBBF24),
        statusDanger = NeumorphicDarkColors.AccentRed,
        progressTrackBg = Color(0x450E1116)
    )

    AppThemeMode.LIQUID_GLASS_DEFAULT -> LiquidGlassStyle(
        surfaceColor = LiquidGlassDefaultSurface,
        surfaceTranslucent = LiquidGlassDefaultSurfaceTranslucent,
        borderColor = LiquidGlassDefaultBorder,
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textTertiary = TextTertiary,
        accentPrimary = LiquidGlassDefaultPrimary,
        accentSecondary = LiquidGlassDefaultAccent,
        cardBackground = LiquidGlassDefaultCardBg,
        cardSubtleBg = LiquidGlassDefaultCardSubtleBg,
        rimLightBrush = Brush.linearGradient(
            listOf(Color(0x5238BDF8), Color(0x33FFFFFF), Color(0x422DD4BF))
        ),
        backdropBrush = Brush.verticalGradient(
            listOf(ObsidianVoidTop, ObsidianVoidBottom, Color(0xFF060E1E))
        ),
        blurRadius = 22.dp,
        isDark = true,
        neumorphicBase = NeumorphicSlateBase,
        neumorphicSurface = NeumorphicSlateSurface,
        neumorphicHighlight = NeumorphicSlateHighlight,
        neumorphicShadow = NeumorphicSlateShadow,
        neumorphicBorderLight = NeumorphicBorderHighlight,
        neumorphicBorderDark = NeumorphicBorderShadow,
        iosLiquidGlassEnabled = iosLiquidGlassEnabled,
        onAccentPrimary = contrastOnColor(LiquidGlassDefaultPrimary),
        onAccentSecondary = contrastOnColor(LiquidGlassDefaultAccent),
        statusSafe = Color(0xFF10B981),
        statusWarning = Color(0xFFFBBF24),
        statusDanger = Color(0xFFFB7185),
        progressTrackBg = Color(0x381E293B)
    )

    AppThemeMode.LIQUID_NEON_CYAN -> LiquidGlassStyle(
        surfaceColor = LiquidNeonCyanSurface,
        surfaceTranslucent = LiquidNeonCyanSurfaceTranslucent,
        borderColor = LiquidNeonCyanBorder,
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textTertiary = TextTertiary,
        accentPrimary = LiquidNeonCyanPrimary,
        accentSecondary = LiquidNeonCyanAccent,
        cardBackground = LiquidNeonCyanCardBg,
        cardSubtleBg = LiquidNeonCyanCardSubtleBg,
        rimLightBrush = Brush.linearGradient(
            listOf(Color(0x4D22D3EE), Color(0x2EFFFFFF), Color(0x4D60A5FA))
        ),
        backdropBrush = Brush.verticalGradient(
            listOf(LiquidNeonCyanBackground, Color(0xFF0A192F), Color(0xFF061020))
        ),
        blurRadius = 20.dp,
        isDark = true,
        neumorphicBase = LiquidNeonCyanBackground,
        neumorphicSurface = LiquidNeonCyanSurface,
        neumorphicHighlight = Color(0x2E22D3EE),
        neumorphicShadow = Color(0xCC03060C),
        neumorphicBorderLight = Color(0x2E22D3EE),
        neumorphicBorderDark = Color(0x66000000),
        iosLiquidGlassEnabled = iosLiquidGlassEnabled,
        onAccentPrimary = contrastOnColor(LiquidNeonCyanPrimary),
        onAccentSecondary = contrastOnColor(LiquidNeonCyanAccent),
        statusSafe = Color(0xFF10B981),
        statusWarning = Color(0xFFFBBF24),
        statusDanger = Color(0xFFFB7185),
        progressTrackBg = Color(0x3D162440)
    )

    AppThemeMode.AMOLED_PITCH_BLACK -> LiquidGlassStyle(
        surfaceColor = AmoledPitchBlackSurface,
        surfaceTranslucent = AmoledPitchBlackSurfaceTranslucent,
        borderColor = AmoledPitchBlackBorder,
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textTertiary = TextTertiary,
        accentPrimary = AmoledPitchBlackPrimary,
        accentSecondary = AmoledPitchBlackAccent,
        cardBackground = AmoledPitchBlackCardBg,
        cardSubtleBg = AmoledPitchBlackCardSubtleBg,
        rimLightBrush = Brush.linearGradient(
            listOf(Color(0x523B82F6), Color(0x22FFFFFF), Color(0x5200E5FF))
        ),
        backdropBrush = Brush.verticalGradient(
            listOf(Color(0xFF0B0B0E), Color(0xFF000000))
        ),
        blurRadius = 8.dp,
        isDark = true,
        neumorphicBase = AmoledPitchBlackBackground,
        neumorphicSurface = AmoledPitchBlackSurface,
        neumorphicHighlight = Color(0x263B82F6),
        neumorphicShadow = Color(0xE6000000),
        neumorphicBorderLight = Color(0x1FFFFFFF),
        neumorphicBorderDark = Color(0x80000000),
        iosLiquidGlassEnabled = iosLiquidGlassEnabled,
        onAccentPrimary = contrastOnColor(AmoledPitchBlackPrimary),
        onAccentSecondary = contrastOnColor(AmoledPitchBlackAccent),
        statusSafe = Color(0xFF00E676),
        statusWarning = Color(0xFFFBBF24),
        statusDanger = Color(0xFFFF5252),
        progressTrackBg = Color(0x521F2430)
    )

    else -> createExtendedLiquidGlassStyle(getExtendedPaletteSpec(mode), iosLiquidGlassEnabled)
}

/**
 * Finds the natural Light/Dark counterpart for paired theme families
 * (e.g. Neumorphism Light <-> Dark, Solarized Light <-> Dark, Gruvbox, One, Ayu, GitHub).
 */
fun findPairedThemeMode(mode: AppThemeMode, wantDark: Boolean): AppThemeMode = when (mode) {
    AppThemeMode.NEUMORPHISM, AppThemeMode.NEUMORPHISM_DARK ->
        if (wantDark) AppThemeMode.NEUMORPHISM_DARK else AppThemeMode.NEUMORPHISM
    AppThemeMode.SOLARIZED_LIGHT, AppThemeMode.SOLARIZED_DARK ->
        if (wantDark) AppThemeMode.SOLARIZED_DARK else AppThemeMode.SOLARIZED_LIGHT
    AppThemeMode.ONE_LIGHT, AppThemeMode.ONE_DARK_PRO ->
        if (wantDark) AppThemeMode.ONE_DARK_PRO else AppThemeMode.ONE_LIGHT
    AppThemeMode.AYU_LIGHT, AppThemeMode.AYU_DARK ->
        if (wantDark) AppThemeMode.AYU_DARK else AppThemeMode.AYU_LIGHT
    AppThemeMode.GRUVBOX_LIGHT, AppThemeMode.GRUVBOX_DARK ->
        if (wantDark) AppThemeMode.GRUVBOX_DARK else AppThemeMode.GRUVBOX_LIGHT
    AppThemeMode.GITHUB_LIGHT, AppThemeMode.GITHUB_DARK ->
        if (wantDark) AppThemeMode.GITHUB_DARK else AppThemeMode.GITHUB_LIGHT
    else -> {
        if (mode == AppThemeMode.SYSTEM) {
            if (wantDark) AppThemeMode.LIQUID_GLASS_DEFAULT else AppThemeMode.LIGHT_MODE
        } else if (mode.isDark == wantDark) {
            mode
        } else {
            if (wantDark) AppThemeMode.LIQUID_GLASS_DEFAULT else AppThemeMode.LIGHT_MODE
        }
    }
}

/**
 * Resolves the effective [AppThemeMode] taking into account whether the user chose
 * Light, Dark, or Follow System appearance alongside their selected Multi-Theme Engine.
 */
fun resolveEffectiveThemeMode(
    appThemeMode: AppThemeMode,
    themeMode: ThemeMode? = null,
    isSystemDark: Boolean = false
): AppThemeMode = when (themeMode) {
    ThemeMode.SYSTEM -> findPairedThemeMode(appThemeMode, wantDark = isSystemDark)
    ThemeMode.LIGHT -> findPairedThemeMode(appThemeMode, wantDark = false)
    ThemeMode.DARK -> findPairedThemeMode(appThemeMode, wantDark = true)
    null -> appThemeMode
}

/**
 * Production-ready Theme Wrapper Composable that dynamically applies the selected AppThemeMode
 * and Apple iOS Liquid Glass button/icon rendering preference.
 */
@Composable
fun DataPulseTheme(
    appThemeMode: AppThemeMode = AppThemeMode.LIQUID_GLASS_DEFAULT,
    themeMode: ThemeMode? = null,
    iosLiquidGlassEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
    val effectiveMode = resolveEffectiveThemeMode(appThemeMode, themeMode, isSystemDark)
    val colorScheme = createAppColorScheme(effectiveMode, isSystemDark)
    val liquidGlassStyle = createLiquidGlassStyle(
        mode = effectiveMode,
        isSystemDark = isSystemDark,
        iosLiquidGlassEnabled = iosLiquidGlassEnabled
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !liquidGlassStyle.isDark
                insetsController.isAppearanceLightNavigationBars = !liquidGlassStyle.isDark
            }
        }
    }

    val dimensions = rememberAppDimensions()

    CompositionLocalProvider(
        LocalLiquidGlass provides liquidGlassStyle,
        LocalAppDimensions provides dimensions,
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

/**
 * Backward compatibility wrapper for existing MyApplicationTheme calls
 */
@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    appThemeMode: AppThemeMode = AppThemeMode.LIQUID_GLASS_DEFAULT,
    appLanguage: AppLanguage = AppLanguage.ARABIC,
    content: @Composable () -> Unit
) {
    DataPulseTheme(
        appThemeMode = appThemeMode,
        content = content
    )
}
