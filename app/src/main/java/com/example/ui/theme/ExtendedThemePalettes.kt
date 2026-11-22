package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.model.AppThemeMode

@Immutable
data class ThemePaletteSpec(
    val bgTop: Color,
    val bgBottom: Color,
    val surface: Color,
    val cardSubtle: Color,
    val primary: Color,
    val secondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val isDark: Boolean,
    val blurDp: Int = 16
)

/**
 * Returns curated palette specification for each of the 60 extended themes
 * (40 Classic Editor/Developer themes + 20 Luxury Liquid Glass themes).
 */
fun getExtendedPaletteSpec(mode: AppThemeMode): ThemePaletteSpec = when (mode) {
    // --- 40 Classic Developer & Editor Themes ---
    AppThemeMode.MONOKAI -> ThemePaletteSpec(
        bgTop = Color(0xFF272822), bgBottom = Color(0xFF1E1F1C),
        surface = Color(0xFF31322B), cardSubtle = Color(0xFF23241E),
        primary = Color(0xFFA6E22E), secondary = Color(0xFFF92672),
        textPrimary = Color(0xFFF8F8F2), textSecondary = Color(0xFFCFCFC2), textTertiary = Color(0xFF90908A),
        isDark = true
    )
    AppThemeMode.MONOKAI_PRO -> ThemePaletteSpec(
        bgTop = Color(0xFF2D2A2E), bgBottom = Color(0xFF221F22),
        surface = Color(0xFF38353A), cardSubtle = Color(0xFF272428),
        primary = Color(0xFFFFD866), secondary = Color(0xFFFF6188),
        textPrimary = Color(0xFFFCFCFA), textSecondary = Color(0xFFC1C0C0), textTertiary = Color(0xFF939293),
        isDark = true
    )
    AppThemeMode.SOLARIZED_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF002B36), bgBottom = Color(0xFF00212B),
        surface = Color(0xFF073642), cardSubtle = Color(0xFF042832),
        primary = Color(0xFF2AA198), secondary = Color(0xFF268BD2),
        textPrimary = Color(0xFFFDF6E3), textSecondary = Color(0xFFB3C2C2), textTertiary = Color(0xFF839496),
        isDark = true
    )
    AppThemeMode.SOLARIZED_LIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFFFDF6E3), bgBottom = Color(0xFFEEE8D5),
        surface = Color(0xFFFFFCF2), cardSubtle = Color(0xFFF3ECD8),
        primary = Color(0xFF268BD2), secondary = Color(0xFF2AA198),
        textPrimary = Color(0xFF073642), textSecondary = Color(0xFF475C63), textTertiary = Color(0xFF6B7F82),
        isDark = false
    )
    AppThemeMode.ONE_DARK_PRO -> ThemePaletteSpec(
        bgTop = Color(0xFF282C34), bgBottom = Color(0xFF21252B),
        surface = Color(0xFF313640), cardSubtle = Color(0xFF23272F),
        primary = Color(0xFF61AFEF), secondary = Color(0xFF98C379),
        textPrimary = Color(0xFFD7DAE0), textSecondary = Color(0xFF9DA5B4), textTertiary = Color(0xFF7B8496),
        isDark = true
    )
    AppThemeMode.ONE_LIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFFFAFAFA), bgBottom = Color(0xFFEDEDEE),
        surface = Color(0xFFFFFFFF), cardSubtle = Color(0xFFF1F2F4),
        primary = Color(0xFF4078F2), secondary = Color(0xFF50A14F),
        textPrimary = Color(0xFF23252C), textSecondary = Color(0xFF545763), textTertiary = Color(0xFF777B86),
        isDark = false
    )
    AppThemeMode.DRACULA -> ThemePaletteSpec(
        bgTop = Color(0xFF282A36), bgBottom = Color(0xFF1E1F29),
        surface = Color(0xFF343746), cardSubtle = Color(0xFF242632),
        primary = Color(0xFFBD93F9), secondary = Color(0xFFFF79C6),
        textPrimary = Color(0xFFF8F8F2), textSecondary = Color(0xFFC5CAD6), textTertiary = Color(0xFF8C97BD),
        isDark = true
    )
    AppThemeMode.TOMORROW_NIGHT_EIGHTIES -> ThemePaletteSpec(
        bgTop = Color(0xFF2D2D2D), bgBottom = Color(0xFF232323),
        surface = Color(0xFF393939), cardSubtle = Color(0xFF282828),
        primary = Color(0xFF6699CC), secondary = Color(0xFFF2777A),
        textPrimary = Color(0xFFE2E2E2), textSecondary = Color(0xFFB3B3B3), textTertiary = Color(0xFF8D8D8D),
        isDark = true
    )
    AppThemeMode.NORD -> ThemePaletteSpec(
        bgTop = Color(0xFF2E3440), bgBottom = Color(0xFF242933),
        surface = Color(0xFF3B4252), cardSubtle = Color(0xFF292E39),
        primary = Color(0xFF88C0D0), secondary = Color(0xFF81A1C1),
        textPrimary = Color(0xFFECEFF4), textSecondary = Color(0xFFD8DEE9), textTertiary = Color(0xFF9AA7B8),
        isDark = true
    )
    AppThemeMode.NIGHT_OWL -> ThemePaletteSpec(
        bgTop = Color(0xFF011627), bgBottom = Color(0xFF01101D),
        surface = Color(0xFF0B253A), cardSubtle = Color(0xFF061B2C),
        primary = Color(0xFF82AAFF), secondary = Color(0xFF7FDBCA),
        textPrimary = Color(0xFFD6DEEB), textSecondary = Color(0xFFA6BDD4), textTertiary = Color(0xFF7A97B0),
        isDark = true
    )
    AppThemeMode.BLULOCO_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF282C34), bgBottom = Color(0xFF1E2128),
        surface = Color(0xFF323741), cardSubtle = Color(0xFF22262E),
        primary = Color(0xFF10B1FE), secondary = Color(0xFF3FC56B),
        textPrimary = Color(0xFFD3D8E0), textSecondary = Color(0xFF9BA3B2), textTertiary = Color(0xFF788298),
        isDark = true
    )
    AppThemeMode.AYU_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF0B0E14), bgBottom = Color(0xFF07090E),
        surface = Color(0xFF131721), cardSubtle = Color(0xFF0E1119),
        primary = Color(0xFFE6B450), secondary = Color(0xFF39BAE6),
        textPrimary = Color(0xFFE0DDD5), textSecondary = Color(0xFFA5A29B), textTertiary = Color(0xFF7B808C),
        isDark = true
    )
    AppThemeMode.AYU_MIRAGE -> ThemePaletteSpec(
        bgTop = Color(0xFF1F2430), bgBottom = Color(0xFF171B24),
        surface = Color(0xFF272D3C), cardSubtle = Color(0xFF1B202B),
        primary = Color(0xFFFFCC66), secondary = Color(0xFF5CCFE6),
        textPrimary = Color(0xFFE2E0D8), textSecondary = Color(0xFFAAB0BD), textTertiary = Color(0xFF828B9E),
        isDark = true
    )
    AppThemeMode.AYU_LIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFFFAFAFA), bgBottom = Color(0xFFF0F2F4),
        surface = Color(0xFFFFFFFF), cardSubtle = Color(0xFFF3F4F6),
        primary = Color(0xFFE67E22), secondary = Color(0xFF2D8CD5),
        textPrimary = Color(0xFF363D44), textSecondary = Color(0xFF5F656B), textTertiary = Color(0xFF7B8187),
        isDark = false
    )
    AppThemeMode.WINTER_IS_COMING -> ThemePaletteSpec(
        bgTop = Color(0xFF011627), bgBottom = Color(0xFF010E19),
        surface = Color(0xFF0C2438), cardSubtle = Color(0xFF071A2B),
        primary = Color(0xFF00BFF9), secondary = Color(0xFF7FDBCA),
        textPrimary = Color(0xFFD6DEEB), textSecondary = Color(0xFFA3B8CD), textTertiary = Color(0xFF7B939D),
        isDark = true
    )
    AppThemeMode.ZENBURN -> ThemePaletteSpec(
        bgTop = Color(0xFF3F3F3F), bgBottom = Color(0xFF333333),
        surface = Color(0xFF4B4B4B), cardSubtle = Color(0xFF383838),
        primary = Color(0xFFF0DFAF), secondary = Color(0xFF8FB28F),
        textPrimary = Color(0xFFE4E4D4), textSecondary = Color(0xFFC2C2B2), textTertiary = Color(0xFF9C9C8D),
        isDark = true
    )
    AppThemeMode.SYNTHWAVE_84 -> ThemePaletteSpec(
        bgTop = Color(0xFF262335), bgBottom = Color(0xFF1B1726),
        surface = Color(0xFF332E47), cardSubtle = Color(0xFF211E2F),
        primary = Color(0xFFFF7EDB), secondary = Color(0xFF36F9F6),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFD2CEE6), textTertiary = Color(0xFF9B9FCA),
        isDark = true
    )
    AppThemeMode.SHADES_OF_PURPLE -> ThemePaletteSpec(
        bgTop = Color(0xFF2D2B55), bgBottom = Color(0xFF1E1D3B),
        surface = Color(0xFF3A376E), cardSubtle = Color(0xFF252348),
        primary = Color(0xFFFAD000), secondary = Color(0xFFA599E9),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFD1CCEF), textTertiary = Color(0xFFA099C9),
        isDark = true
    )
    AppThemeMode.LASER_WAVE -> ThemePaletteSpec(
        bgTop = Color(0xFF27212E), bgBottom = Color(0xFF1D1823),
        surface = Color(0xFF342C3D), cardSubtle = Color(0xFF221D29),
        primary = Color(0xFFEB64B9), secondary = Color(0xFF40B4C4),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFD8D0E3), textTertiary = Color(0xFFA49BAE),
        isDark = true
    )
    AppThemeMode.ABYSS -> ThemePaletteSpec(
        bgTop = Color(0xFF000C18), bgBottom = Color(0xFF00070F),
        surface = Color(0xFF071B2F), cardSubtle = Color(0xFF031120),
        primary = Color(0xFF55B4D4), secondary = Color(0xFF77B4CC),
        textPrimary = Color(0xFFE6EDF5), textSecondary = Color(0xFF9CB4CE), textTertiary = Color(0xFF6F88A3),
        isDark = true
    )
    AppThemeMode.COBALT2 -> ThemePaletteSpec(
        bgTop = Color(0xFF193549), bgBottom = Color(0xFF122738),
        surface = Color(0xFF214660), cardSubtle = Color(0xFF152E42),
        primary = Color(0xFFFFC600), secondary = Color(0xFF38A6FF),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFBFD6E8), textTertiary = Color(0xFF8AA8BF),
        isDark = true
    )
    AppThemeMode.RED -> ThemePaletteSpec(
        bgTop = Color(0xFF390000), bgBottom = Color(0xFF260000),
        surface = Color(0xFF4F0909), cardSubtle = Color(0xFF310404),
        primary = Color(0xFFFF6B6B), secondary = Color(0xFFFFB86C),
        textPrimary = Color(0xFFFAF5F5), textSecondary = Color(0xFFE4C0C0), textTertiary = Color(0xFFBE8D8D),
        isDark = true
    )
    AppThemeMode.POIMANDRES -> ThemePaletteSpec(
        bgTop = Color(0xFF1B1E28), bgBottom = Color(0xFF141720),
        surface = Color(0xFF252936), cardSubtle = Color(0xFF171A24),
        primary = Color(0xFF5DE4C7), secondary = Color(0xFFADD7FF),
        textPrimary = Color(0xFFE4F0FB), textSecondary = Color(0xFFB4BAD8), textTertiary = Color(0xFF898FAD),
        isDark = true
    )
    AppThemeMode.HOPSCOTCH -> ThemePaletteSpec(
        bgTop = Color(0xFF322931), bgBottom = Color(0xFF251E24),
        surface = Color(0xFF413540), cardSubtle = Color(0xFF2C232B),
        primary = Color(0xFF38B6E6), secondary = Color(0xFF98CA4B),
        textPrimary = Color(0xFFE4E2E4), textSecondary = Color(0xFFB8B3B7), textTertiary = Color(0xFF928B91),
        isDark = true
    )
    AppThemeMode.GRUVBOX_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF282828), bgBottom = Color(0xFF1D2021),
        surface = Color(0xFF3C3836), cardSubtle = Color(0xFF2D2A28),
        primary = Color(0xFFFABD2F), secondary = Color(0xFFB8BB26),
        textPrimary = Color(0xFFEBDBB2), textSecondary = Color(0xFFD5C4A1), textTertiary = Color(0xFFA89984),
        isDark = true
    )
    AppThemeMode.GRUVBOX_LIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFFFBF1C7), bgBottom = Color(0xFFF2E5BC),
        surface = Color(0xFFFFF8DF), cardSubtle = Color(0xFFF3E6C3),
        primary = Color(0xFFB57614), secondary = Color(0xFF427B58),
        textPrimary = Color(0xFF3C3836), textSecondary = Color(0xFF504945), textTertiary = Color(0xFF73665B),
        isDark = false
    )
    AppThemeMode.OCEANIC_NEXT -> ThemePaletteSpec(
        bgTop = Color(0xFF1B2B34), bgBottom = Color(0xFF142129),
        surface = Color(0xFF253A46), cardSubtle = Color(0xFF182730),
        primary = Color(0xFF6699CC), secondary = Color(0xFF5FB3B3),
        textPrimary = Color(0xFFE3E8F0), textSecondary = Color(0xFFB2C0CD), textTertiary = Color(0xFF83939E),
        isDark = true
    )
    AppThemeMode.BASE16_EIGHTIES -> ThemePaletteSpec(
        bgTop = Color(0xFF2D2D2D), bgBottom = Color(0xFF222222),
        surface = Color(0xFF393939), cardSubtle = Color(0xFF282828),
        primary = Color(0xFF6699CC), secondary = Color(0xFF66CCCC),
        textPrimary = Color(0xFFDDDAD3), textSecondary = Color(0xFFB0AFA4), textTertiary = Color(0xFF8A887E),
        isDark = true
    )
    AppThemeMode.TOMORROW_NIGHT_BLUE -> ThemePaletteSpec(
        bgTop = Color(0xFF002451), bgBottom = Color(0xFF001A3D),
        surface = Color(0xFF00346E), cardSubtle = Color(0xFF00204A),
        primary = Color(0xFF99CCFF), secondary = Color(0xFF66E6E6),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFC7DAF2), textTertiary = Color(0xFF8FA2CE),
        isDark = true
    )
    AppThemeMode.MATERIAL_OCEANIC -> ThemePaletteSpec(
        bgTop = Color(0xFF263238), bgBottom = Color(0xFF1D272C),
        surface = Color(0xFF314048), cardSubtle = Color(0xFF222D33),
        primary = Color(0xFF80CBC4), secondary = Color(0xFF82AAFF),
        textPrimary = Color(0xFFEEFFFF), textSecondary = Color(0xFFB8C6CD), textTertiary = Color(0xFF849AA5),
        isDark = true
    )
    AppThemeMode.MATERIAL_PALENIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFF292D3E), bgBottom = Color(0xFF202331),
        surface = Color(0xFF343A50), cardSubtle = Color(0xFF242838),
        primary = Color(0xFFC792EA), secondary = Color(0xFF82AAFF),
        textPrimary = Color(0xFFD5DAEE), textSecondary = Color(0xFFA0A7C8), textTertiary = Color(0xFF7D84AA),
        isDark = true
    )
    AppThemeMode.MATERIAL_DARKER -> ThemePaletteSpec(
        bgTop = Color(0xFF212121), bgBottom = Color(0xFF171717),
        surface = Color(0xFF2D2D2D), cardSubtle = Color(0xFF1C1C1C),
        primary = Color(0xFFFF9800), secondary = Color(0xFF82AAFF),
        textPrimary = Color(0xFFF3F6F9), textSecondary = Color(0xFFB8BEC7), textTertiary = Color(0xFF898F96),
        isDark = true
    )
    AppThemeMode.CATPPUCCIN_MOCHA -> ThemePaletteSpec(
        bgTop = Color(0xFF1E1E2E), bgBottom = Color(0xFF181825),
        surface = Color(0xFF29293E), cardSubtle = Color(0xFF1B1B2B),
        primary = Color(0xFFCBA6F7), secondary = Color(0xFF89B4FA),
        textPrimary = Color(0xFFCDD6F4), textSecondary = Color(0xFFA6ADC8), textTertiary = Color(0xFF888EA6),
        isDark = true
    )
    AppThemeMode.KANAGAWA -> ThemePaletteSpec(
        bgTop = Color(0xFF1F1F28), bgBottom = Color(0xFF16161D),
        surface = Color(0xFF2A2A37), cardSubtle = Color(0xFF1B1B24),
        primary = Color(0xFF7E9CD8), secondary = Color(0xFF98BB6C),
        textPrimary = Color(0xFFE5DFC5), textSecondary = Color(0xFFC8C093), textTertiary = Color(0xFF8F8D84),
        isDark = true
    )
    AppThemeMode.ROSE_PINE -> ThemePaletteSpec(
        bgTop = Color(0xFF191724), bgBottom = Color(0xFF12101B),
        surface = Color(0xFF232136), cardSubtle = Color(0xFF161420),
        primary = Color(0xFFEBBCBA), secondary = Color(0xFF9CCFD8),
        textPrimary = Color(0xFFE0DEF4), textSecondary = Color(0xFFA9A5C2), textTertiary = Color(0xFF86829E),
        isDark = true
    )
    AppThemeMode.GITHUB_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF0D1117), bgBottom = Color(0xFF080B10),
        surface = Color(0xFF161B22), cardSubtle = Color(0xFF10151C),
        primary = Color(0xFF58A6FF), secondary = Color(0xFF3FB950),
        textPrimary = Color(0xFFF0F6FC), textSecondary = Color(0xFF9EA7B3), textTertiary = Color(0xFF7B8490),
        isDark = true
    )
    AppThemeMode.GITHUB_LIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFFF6F8FA), bgBottom = Color(0xFFECEFF4),
        surface = Color(0xFFFFFFFF), cardSubtle = Color(0xFFF0F3F6),
        primary = Color(0xFF0969DA), secondary = Color(0xFF1A7F37),
        textPrimary = Color(0xFF1F2328), textSecondary = Color(0xFF57606A), textTertiary = Color(0xFF6E7781),
        isDark = false
    )
    AppThemeMode.PANDA_THEME -> ThemePaletteSpec(
        bgTop = Color(0xFF292A2B), bgBottom = Color(0xFF1E1F20),
        surface = Color(0xFF343638), cardSubtle = Color(0xFF242527),
        primary = Color(0xFFFF75B5), secondary = Color(0xFF19F9D8),
        textPrimary = Color(0xFFEAEAEA), textSecondary = Color(0xFFB8B8B8), textTertiary = Color(0xFF8C8C8C),
        isDark = true
    )
    AppThemeMode.ATOM_ONE_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF282C34), bgBottom = Color(0xFF20232A),
        surface = Color(0xFF323741), cardSubtle = Color(0xFF242830),
        primary = Color(0xFF528BFF), secondary = Color(0xFF56B6C2),
        textPrimary = Color(0xFFD7DAE0), textSecondary = Color(0xFF9DA5B4), textTertiary = Color(0xFF7B8496),
        isDark = true
    )
    AppThemeMode.TOKYO_NIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFF1A1B26), bgBottom = Color(0xFF13141D),
        surface = Color(0xFF24283B), cardSubtle = Color(0xFF171925),
        primary = Color(0xFF7AA2F7), secondary = Color(0xFFBB9AF7),
        textPrimary = Color(0xFFC0CAF5), textSecondary = Color(0xFF9AA5CE), textTertiary = Color(0xFF7982AB),
        isDark = true
    )

    // --- 20 Luxury Liquid Glass Themes (Cohesive Spatial Jewel & Frosted Palettes) ---
    AppThemeMode.OBSIDIAN_LIQUID_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF060A13), bgBottom = Color(0xFF0A1222),
        surface = Color(0xFF101B30), cardSubtle = Color(0xFF0B1324),
        primary = Color(0xFF38BDF8), secondary = Color(0xFF818CF8),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFB3C2D6), textTertiary = Color(0xFF7D8FA8),
        isDark = true, blurDp = 24
    )
    AppThemeMode.FROSTED_ARCTIC_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFFF0F7FF), bgBottom = Color(0xFFE0EEFC),
        surface = Color(0xFFFAFDFF), cardSubtle = Color(0xFFE8F2FC),
        primary = Color(0xFF0284C7), secondary = Color(0xFF0D9488),
        textPrimary = Color(0xFF0F172A), textSecondary = Color(0xFF475569), textTertiary = Color(0xFF64748B),
        isDark = false, blurDp = 24
    )
    AppThemeMode.CYBER_NEON_LIQUID -> ThemePaletteSpec(
        bgTop = Color(0xFF080716), bgBottom = Color(0xFF110D28),
        surface = Color(0xFF171333), cardSubtle = Color(0xFF100C24),
        primary = Color(0xFF2DD4BF), secondary = Color(0xFFF472B6),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFC4CEF5), textTertiary = Color(0xFF8F9BC9),
        isDark = true, blurDp = 22
    )
    AppThemeMode.SMOKED_QUARTZ_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF121015), bgBottom = Color(0xFF1A161E),
        surface = Color(0xFF231F29), cardSubtle = Color(0xFF19151D),
        primary = Color(0xFFEAB308), secondary = Color(0xFFD6D3D1),
        textPrimary = Color(0xFFFAF8F5), textSecondary = Color(0xFFD6D3D1), textTertiary = Color(0xFF9F9893),
        isDark = true, blurDp = 20
    )
    AppThemeMode.MIDNIGHT_VELVET_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF0F091D), bgBottom = Color(0xFF180F2D),
        surface = Color(0xFF20153B), cardSubtle = Color(0xFF150E27),
        primary = Color(0xFFC084FC), secondary = Color(0xFFF472B6),
        textPrimary = Color(0xFFFAF5FF), textSecondary = Color(0xFFDCC9FC), textTertiary = Color(0xFFA58DCE),
        isDark = true, blurDp = 22
    )
    AppThemeMode.AURORA_BOREALIS_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF051319), bgBottom = Color(0xFF0A2029),
        surface = Color(0xFF0E2934), cardSubtle = Color(0xFF081B23),
        primary = Color(0xFF34D399), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFECFDF5), textSecondary = Color(0xFFB0EAD7), textTertiary = Color(0xFF76B8A7),
        isDark = true, blurDp = 24
    )
    AppThemeMode.CRYSTAL_PRISM_LIQUID -> ThemePaletteSpec(
        bgTop = Color(0xFFF6F7FF), bgBottom = Color(0xFFEAEFFF),
        surface = Color(0xFFFFFFFF), cardSubtle = Color(0xFFEEF1FF),
        primary = Color(0xFF4F46E5), secondary = Color(0xFF0EA5E9),
        textPrimary = Color(0xFF1E1B4B), textSecondary = Color(0xFF474B7A), textTertiary = Color(0xFF686D99),
        isDark = false, blurDp = 24
    )
    AppThemeMode.DEEP_OCEAN_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF041222), bgBottom = Color(0xFF081E38),
        surface = Color(0xFF0B2746), cardSubtle = Color(0xFF061A31),
        primary = Color(0xFF22D3EE), secondary = Color(0xFF60A5FA),
        textPrimary = Color(0xFFF0F9FF), textSecondary = Color(0xFFBAE6FD), textTertiary = Color(0xFF78ACC9),
        isDark = true, blurDp = 22
    )
    AppThemeMode.AMBER_MERCURY_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF140F09), bgBottom = Color(0xFF1F160C),
        surface = Color(0xFF281D11), cardSubtle = Color(0xFF1C140B),
        primary = Color(0xFFF59E0B), secondary = Color(0xFFFCD34D),
        textPrimary = Color(0xFFFFFBEB), textSecondary = Color(0xFFF3DE9B), textTertiary = Color(0xFFC2A06E),
        isDark = true, blurDp = 20
    )
    AppThemeMode.TITANIUM_MIST_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFFF2F5F9), bgBottom = Color(0xFFE6ECF3),
        surface = Color(0xFFFBFCFE), cardSubtle = Color(0xFFE9EFF6),
        primary = Color(0xFF0284C7), secondary = Color(0xFF475569),
        textPrimary = Color(0xFF0F172A), textSecondary = Color(0xFF475569), textTertiary = Color(0xFF64748B),
        isDark = false, blurDp = 24
    )
    AppThemeMode.HOLOGRAPHIC_LIQUID_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF0B0E1E), bgBottom = Color(0xFF131733),
        surface = Color(0xFF1A1F3F), cardSubtle = Color(0xFF11152C),
        primary = Color(0xFF38BDF8), secondary = Color(0xFFE879F9),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFC8C2F8), textTertiary = Color(0xFF939AE6),
        isDark = true, blurDp = 24
    )
    AppThemeMode.CARBON_BLUR_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF0F1115), bgBottom = Color(0xFF16191F),
        surface = Color(0xFF1E222A), cardSubtle = Color(0xFF15181E),
        primary = Color(0xFF60A5FA), secondary = Color(0xFF94A3B8),
        textPrimary = Color(0xFFF1F5F9), textSecondary = Color(0xFFACB8C8), textTertiary = Color(0xFF7D8A9B),
        isDark = true, blurDp = 22
    )
    AppThemeMode.EMERALD_TRANSLUCENT_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF051510), bgBottom = Color(0xFF09221A),
        surface = Color(0xFF0D2D23), cardSubtle = Color(0xFF082018),
        primary = Color(0xFF10B981), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFECFDF5), textSecondary = Color(0xFFAFE8D4), textTertiary = Color(0xFF74B39D),
        isDark = true, blurDp = 22
    )
    AppThemeMode.ULTRAVIOLET_FROST_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF100921), bgBottom = Color(0xFF190E34),
        surface = Color(0xFF221444), cardSubtle = Color(0xFF170D2F),
        primary = Color(0xFFA855F7), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFFAF5FF), textSecondary = Color(0xFFD9C0FC), textTertiary = Color(0xFFA28BC4),
        isDark = true, blurDp = 24
    )
    AppThemeMode.SAPPHIRE_LIQUID_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF061026), bgBottom = Color(0xFF0B1C3F),
        surface = Color(0xFF10244D), cardSubtle = Color(0xFF091736),
        primary = Color(0xFF60A5FA), secondary = Color(0xFF22D3EE),
        textPrimary = Color(0xFFEFF6FF), textSecondary = Color(0xFFC2DCFC), textTertiary = Color(0xFF8AA9D8),
        isDark = true, blurDp = 22
    )
    AppThemeMode.PEARLESCENT_DARK_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF13161E), bgBottom = Color(0xFF1B1F2A),
        surface = Color(0xFF232836), cardSubtle = Color(0xFF181C26),
        primary = Color(0xFFE2E8F0), secondary = Color(0xFF7DD3FC),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFCBD5E1), textTertiary = Color(0xFF8C99AC),
        isDark = true, blurDp = 22
    )
    AppThemeMode.RUBY_REFRACTIVE_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF1A080E), bgBottom = Color(0xFF280C15),
        surface = Color(0xFF32101B), cardSubtle = Color(0xFF220A12),
        primary = Color(0xFFFB7185), secondary = Color(0xFFFDA4AF),
        textPrimary = Color(0xFFFFF1F2), textSecondary = Color(0xFFFECDD3), textTertiary = Color(0xFFC58A99),
        isDark = true, blurDp = 22
    )
    AppThemeMode.CHROMATIC_ABERRATION_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF0A0A14), bgBottom = Color(0xFF121124),
        surface = Color(0xFF191830), cardSubtle = Color(0xFF111021),
        primary = Color(0xFFF43F5E), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFC9C4F8), textTertiary = Color(0xFF939AE6),
        isDark = true, blurDp = 24
    )
    AppThemeMode.LIQUID_MERCURY_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF13161B), bgBottom = Color(0xFF1B1F26),
        surface = Color(0xFF242933), cardSubtle = Color(0xFF181C23),
        primary = Color(0xFFCBD5E1), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFB0BCCB), textTertiary = Color(0xFF808D9F),
        isDark = true, blurDp = 20
    )
    AppThemeMode.BIOLUMINESCENT_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF031418), bgBottom = Color(0xFF062128),
        surface = Color(0xFF0A2C35), cardSubtle = Color(0xFF051C22),
        primary = Color(0xFF2DD4BF), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFECFEFF), textSecondary = Color(0xFFB2EEF7), textTertiary = Color(0xFF74B3BE),
        isDark = true, blurDp = 24
    )

    // Fallback for core entries if queried via helper
    else -> ThemePaletteSpec(
        bgTop = ObsidianVoidTop, bgBottom = ObsidianVoidBottom,
        surface = ObsidianGlassSurface, cardSubtle = Color(0xFF091223),
        primary = ElectricCyan, secondary = EmeraldMint,
        textPrimary = TextPrimary, textSecondary = TextSecondary, textTertiary = TextTertiary,
        isDark = true
    )
}

fun createExtendedColorScheme(spec: ThemePaletteSpec): ColorScheme =
    if (spec.isDark) {
        darkColorScheme(
            primary = spec.primary,
            onPrimary = contrastOnColor(spec.primary),
            primaryContainer = spec.primary.copy(alpha = 0.24f),
            onPrimaryContainer = spec.textPrimary,
            secondary = spec.secondary,
            onSecondary = contrastOnColor(spec.secondary),
            secondaryContainer = spec.secondary.copy(alpha = 0.22f),
            onSecondaryContainer = spec.textPrimary,
            tertiary = spec.secondary,
            onTertiary = contrastOnColor(spec.secondary),
            background = spec.bgTop,
            onBackground = spec.textPrimary,
            surface = spec.surface,
            onSurface = spec.textPrimary,
            surfaceVariant = spec.cardSubtle,
            onSurfaceVariant = spec.textSecondary,
            outline = spec.primary.copy(alpha = 0.32f),
            error = RedAlert,
            onError = Color.White
        )
    } else {
        lightColorScheme(
            primary = spec.primary,
            onPrimary = contrastOnColor(spec.primary),
            primaryContainer = spec.primary.copy(alpha = 0.14f),
            onPrimaryContainer = spec.textPrimary,
            secondary = spec.secondary,
            onSecondary = contrastOnColor(spec.secondary),
            secondaryContainer = spec.secondary.copy(alpha = 0.14f),
            onSecondaryContainer = spec.textPrimary,
            tertiary = spec.secondary,
            onTertiary = contrastOnColor(spec.secondary),
            background = spec.bgTop,
            onBackground = spec.textPrimary,
            surface = spec.surface,
            onSurface = spec.textPrimary,
            surfaceVariant = spec.cardSubtle,
            onSurfaceVariant = spec.textSecondary,
            outline = spec.textTertiary.copy(alpha = 0.35f),
            error = LightPulseCoralRed,
            onError = Color.White
        )
    }

fun createExtendedLiquidGlassStyle(
    spec: ThemePaletteSpec,
    iosLiquidGlassEnabled: Boolean = true
): LiquidGlassStyle {
    val borderColor = if (spec.isDark) {
        spec.primary.copy(alpha = 0.24f)
    } else {
        spec.primary.copy(alpha = 0.18f)
    }

    val safeStatus = if (spec.isDark) Color(0xFF10B981) else Color(0xFF059669)
    val warnStatus = if (spec.isDark) Color(0xFFFBBF24) else Color(0xFFD97706)
    val dangerStatus = if (spec.isDark) Color(0xFFFB7185) else Color(0xFFDC2626)
    val trackColor = if (spec.isDark) {
        spec.textPrimary.copy(alpha = 0.14f)
    } else {
        spec.textPrimary.copy(alpha = 0.12f)
    }

    return LiquidGlassStyle(
        surfaceColor = spec.surface,
        surfaceTranslucent = spec.surface.copy(alpha = if (spec.isDark) 0.84f else 0.94f),
        borderColor = borderColor,
        textPrimary = spec.textPrimary,
        textSecondary = spec.textSecondary,
        textTertiary = spec.textTertiary,
        accentPrimary = spec.primary,
        accentSecondary = spec.secondary,
        cardBackground = spec.surface,
        cardSubtleBg = spec.cardSubtle,
        rimLightBrush = Brush.linearGradient(
            listOf(
                spec.primary.copy(alpha = if (spec.isDark) 0.38f else 0.24f),
                Color.White.copy(alpha = if (spec.isDark) 0.24f else 0.72f),
                spec.secondary.copy(alpha = if (spec.isDark) 0.34f else 0.20f)
            )
        ),
        backdropBrush = Brush.verticalGradient(
            listOf(spec.bgTop, spec.bgBottom)
        ),
        blurRadius = spec.blurDp.dp,
        isDark = spec.isDark,
        isNeumorphic = false,
        neumorphicBase = spec.bgTop,
        neumorphicSurface = spec.surface,
        neumorphicHighlight = if (spec.isDark) spec.primary.copy(alpha = 0.20f) else Color(0xCCFFFFFF),
        neumorphicShadow = if (spec.isDark) Color(0xE6020408) else Color(0x140F172A),
        neumorphicBorderLight = if (spec.isDark) Color(0x2BFFFFFF) else Color(0x80FFFFFF),
        neumorphicBorderDark = if (spec.isDark) Color(0x66000000) else Color(0x0D0F172A),
        iosLiquidGlassEnabled = iosLiquidGlassEnabled,
        onAccentPrimary = contrastOnColor(spec.primary),
        onAccentSecondary = contrastOnColor(spec.secondary),
        statusSafe = safeStatus,
        statusWarning = warnStatus,
        statusDanger = dangerStatus,
        progressTrackBg = trackColor
    )
}
