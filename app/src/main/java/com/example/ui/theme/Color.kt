package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// =========================================================================
// 0. LUXURIOUS PEARL & PLATINUM LIGHT PALETTE (Flagship Luxury Aesthetic)
// Background: Soft, warm pearl/platinum off-white (#F4F6F9 to #E9ECEF)
// Surface: Crystalline pure white (#FFFFFF)
// SurfaceTranslucent: Frosted white glass (#F8FFFFFF with ultra-fine blur)
// Primary: High-end Royal Electric Azure (#0066FF)
// Accent: Refined Royal Violet (#7C3AED)
// Border: Ultra-fine frosted glass rim (#14111827 ~ 8% opacity)
// BorderSubtle: Soft ambient border (#0A111827 ~ 4% opacity)
// CardBg: Pure White (#FFFFFF)
// CardSubtleBg: Soft Pearl / Platinum (#F0F3F8)
//
// Typography: Razor-sharp high contrast without eye strain
// Primary: Deep obsidian/charcoal (#111827)
// Secondary: Refined slate (#4B5563)
// Tertiary: Muted neutral slate (#6B7280)
//
// Accents & Pulse Glows (Refined for light surfaces):
// Ocean Cyan: #0284C7 (Crisp and vivid on light backgrounds)
// Amber Alert: #D97706 (Amber 600, warm and saturated)
// Orange Accent: #EA580C (Orange 600)
// Coral Red: #DC2626 (Red 600)
// Crimson Limit: #991B1B (Deep rich ruby red)
// =========================================================================
val PearlPlatinumLightBase = Color(0xFFF4F6F9)
val PearlPlatinumLightEnd = Color(0xFFE9ECEF)
val PearlPlatinumSurface = Color(0xFFFFFFFF)
val PearlPlatinumSurfaceTranslucent = Color(0xF8FFFFFF)
val PearlPlatinumPrimary = Color(0xFF0066FF)
val PearlPlatinumAccent = Color(0xFF7C3AED)
val PearlPlatinumBorder = Color(0x14111827)        // Ultra-fine frosted border (~8%)
val PearlPlatinumBorderSubtle = Color(0x0A111827)  // Soft ambient border (~4%)
val PearlPlatinumCardBg = Color(0xFFFFFFFF)
val PearlPlatinumCardSubtleBg = Color(0xFFF0F3F8)

val PearlPlatinumTextPrimary = Color(0xFF111827)   // Deep obsidian/charcoal #111827
val PearlPlatinumTextSecondary = Color(0xFF4B5563) // Refined slate #4B5563
val PearlPlatinumTextTertiary = Color(0xFF6B7280)  // Muted neutral slate #6B7280

// Light Mode Pulse Accents (Refined contrast for light surfaces)
val LightPulseOceanCyan = Color(0xFF0284C7)
val LightPulseTeal = Color(0xFF0D9488)
val LightPulseAmber = Color(0xFFD97706)
val LightPulseOrange = Color(0xFFEA580C)
val LightPulseCoralRed = Color(0xFFDC2626)
val LightPulseCrimson = Color(0xFF991B1B)
val LightPulseIceSlate = Color(0xFF64748B)

// Aliases mapped to the new Pearl & Platinum tokens
val LightModeBackground = PearlPlatinumLightBase
val LightModeSurface = PearlPlatinumSurface
val LightModeSurfaceTranslucent = PearlPlatinumSurfaceTranslucent
val LightModePrimary = PearlPlatinumPrimary
val LightModeAccent = PearlPlatinumAccent
val LightModeBorder = PearlPlatinumBorder
val LightModeCardBg = PearlPlatinumCardBg
val LightModeCardSubtleBg = PearlPlatinumCardSubtleBg
val LightModeTextPrimary = PearlPlatinumTextPrimary
val LightModeTextSecondary = PearlPlatinumTextSecondary
val LightModeTextTertiary = PearlPlatinumTextTertiary

// =========================================================================
// 1. APPLE SPATIAL LIQUID GLASS PALETTE (Deep Midnight Sapphire -> Ice Azure)
// Deep Midnight Indigo-Obsidian Background: #050B16 to #0A1528
// Frosted Translucent Glass Surface: #0E192E
// Primary: Apple Spatial Sky Azure (#38BDF8)
// Secondary: Iridescent Frosted Teal-Mint (#2DD4BF)
// =========================================================================
val ObsidianVoidTop = Color(0xFF050B16)
val ObsidianVoidBottom = Color(0xFF0A1528)
val ObsidianGlassSurface = Color(0xFF0E192E)
val ObsidianGlassElevated = Color(0xFF152440)
val ElectricCyan = Color(0xFF38BDF8)
val EmeraldMint = Color(0xFF2DD4BF)

val NeumorphicSlateBase = ObsidianVoidTop
val NeumorphicSlateSurface = ObsidianGlassSurface
val NeumorphicSlateSurfaceSubtle = Color(0xFF091223)
val NeumorphicSlateHighlight = Color(0x3038BDF8) // Subtle Sky Azure top-left rim glow
val NeumorphicSlateShadow = Color(0xEC02050D)    // Deep bottom-right ambient shadow
val NeumorphicBorderHighlight = Color(0x2EFFFFFF)
val NeumorphicBorderShadow = Color(0x73000000)

val LiquidGlassDefaultBackground = ObsidianVoidTop
val LiquidGlassDefaultSurface = ObsidianGlassSurface
val LiquidGlassDefaultSurfaceTranslucent = Color(0xCC0E192E)
val LiquidGlassDefaultPrimary = ElectricCyan
val LiquidGlassDefaultAccent = EmeraldMint
val LiquidGlassDefaultBorder = Color(0x36FFFFFF)
val LiquidGlassDefaultCardBg = ObsidianGlassSurface
val LiquidGlassDefaultCardSubtleBg = Color(0xFF091223)

// =========================================================================
// 2. LIQUID_NEON_CYAN Palette (Deep Lagoon Glass & Aqua Crystal)
// Background: Deep ocean slate background (#050D1A)
// Surface: Frosted lagoon surface (#0C192E)
// Primary: Luminous Aqua Cyan (#22D3EE)
// Accent: Glacial Sky Blue (#60A5FA)
// =========================================================================
val LiquidNeonCyanBackground = Color(0xFF050D1A)
val LiquidNeonCyanSurface = Color(0xFF0C192E)
val LiquidNeonCyanSurfaceTranslucent = Color(0xCC0C192E)
val LiquidNeonCyanPrimary = Color(0xFF22D3EE)
val LiquidNeonCyanAccent = Color(0xFF60A5FA)
val LiquidNeonCyanBorder = Color(0x3622D3EE)
val LiquidNeonCyanCardBg = Color(0xFF0C192E)
val LiquidNeonCyanCardSubtleBg = Color(0xFF081222)

// =========================================================================
// 3. AMOLED_PITCH_BLACK Palette (Neumorphic OLED Soft Dark)
// Background: Pure pitch-black (#000000) for OLED battery saving
// Surface: Soft tactile deep dark surface (#111116)
// Primary: Electric Azure (#3B82F6)
// Accent: Ice Cyan (#38BDF8)
// =========================================================================
val AmoledPitchBlackBackground = Color(0xFF000000)
val AmoledPitchBlackSurface = Color(0xFF111116)
val AmoledPitchBlackSurfaceTranslucent = Color(0xD9111116)
val AmoledPitchBlackPrimary = Color(0xFF3B82F6)
val AmoledPitchBlackAccent = Color(0xFF38BDF8)
val AmoledPitchBlackBorder = Color(0x2E3B82F6)
val AmoledPitchBlackCardBg = Color(0xFF111116)
val AmoledPitchBlackCardSubtleBg = Color(0xFF0A0A0E)

// Shared Common Colors & Status Alerts
val CyanPulse = ElectricCyan
val ElectricBlue = Color(0xFF60A5FA)
val NeonPurple = Color(0xFFA78BFA)
val DeepViolet = Color(0xFF7C3AED)
val AmberAlert = Color(0xFFF59E0B)
val RedAlert = Color(0xFFEF4444)
val EmeraldSafe = Color(0xFF10B981)

val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFFB4C2D6)
val TextTertiary = Color(0xFF7C8EA6)

// Preserved aliases for existing references
val VoidBlack = Color(0xFF040812)
val DeepObsidian = LiquidGlassDefaultBackground
val GlassSurfaceDark = LiquidGlassDefaultSurface
val GlassSurfaceTranslucent = LiquidGlassDefaultSurfaceTranslucent
val GlassSurfaceUltraLight = Color(0x29FFFFFF)
val GlassCardBorder = LiquidGlassDefaultBorder
val GlassBorderSubtle = Color(0x1EFFFFFF)
val GlassRimHighlight = Color(0x47FFFFFF)

val PrimaryDark = LiquidGlassDefaultPrimary
val OnPrimaryDark = Color(0xFFFFFFFF)
val SecondaryDark = LiquidGlassDefaultAccent
val OnSecondaryDark = Color(0xFFFFFFFF)
val TertiaryDark = ElectricBlue
val OnTertiaryDark = Color(0xFF00223A)
val BackgroundDark = LiquidGlassDefaultBackground
val OnBackgroundDark = TextPrimary
val SurfaceDark = LiquidGlassDefaultSurface
val OnSurfaceDark = TextPrimary
val SurfaceVariantDark = Color(0xFF132038)
val OnSurfaceVariantDark = TextSecondary
val OutlineDark = Color(0x4094A3B8)

// Liquid Glass Gradients
object LiquidGradients {
    val glassCardBackdrop = Brush.verticalGradient(
        colors = listOf(
            Color(0xE00E192E),
            Color(0xC208101E)
        )
    )

    val glassRimLight = Brush.linearGradient(
        colors = listOf(
            Color(0x4238BDF8),
            Color(0x26FFFFFF),
            Color(0x3D2DD4BF)
        )
    )

    val subtleBorder = Brush.linearGradient(
        colors = listOf(
            Color(0x33FFFFFF),
            Color(0x0DFFFFFF),
            Color(0x2638BDF8)
        )
    )

    val cyanToViolet = Brush.horizontalGradient(
        colors = listOf(CyanPulse, ElectricBlue, NeonPurple)
    )
}
