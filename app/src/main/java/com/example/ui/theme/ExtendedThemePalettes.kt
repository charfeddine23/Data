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
        primary = Color(0xFF268BD2), secondary = Color(0xFF2AA198),
        textPrimary = Color(0xFFFDF6E3), textSecondary = Color(0xFF93A1A1), textTertiary = Color(0xFF657B83),
        isDark = true
    )
    AppThemeMode.SOLARIZED_LIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFFFDF6E3), bgBottom = Color(0xFFEEE8D5),
        surface = Color(0xFFFFFCF2), cardSubtle = Color(0xFFF3ECD8),
        primary = Color(0xFF268BD2), secondary = Color(0xFF2AA198),
        textPrimary = Color(0xFF073642), textSecondary = Color(0xFF586E75), textTertiary = Color(0xFF839496),
        isDark = false
    )
    AppThemeMode.ONE_DARK_PRO -> ThemePaletteSpec(
        bgTop = Color(0xFF282C34), bgBottom = Color(0xFF21252B),
        surface = Color(0xFF313640), cardSubtle = Color(0xFF23272F),
        primary = Color(0xFF61AFEF), secondary = Color(0xFF98C379),
        textPrimary = Color(0xFFABB2BF), textSecondary = Color(0xFF8E96A4), textTertiary = Color(0xFF5C6370),
        isDark = true
    )
    AppThemeMode.ONE_LIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFFFAFAFA), bgBottom = Color(0xFFEDEDEE),
        surface = Color(0xFFFFFFFF), cardSubtle = Color(0xFFF1F2F4),
        primary = Color(0xFF4078F2), secondary = Color(0xFF50A14F),
        textPrimary = Color(0xFF383A42), textSecondary = Color(0xFF696C77), textTertiary = Color(0xFFA0A1A7),
        isDark = false
    )
    AppThemeMode.DRACULA -> ThemePaletteSpec(
        bgTop = Color(0xFF282A36), bgBottom = Color(0xFF1E1F29),
        surface = Color(0xFF343746), cardSubtle = Color(0xFF242632),
        primary = Color(0xFFBD93F9), secondary = Color(0xFFFF79C6),
        textPrimary = Color(0xFFF8F8F2), textSecondary = Color(0xFFBCC2CD), textTertiary = Color(0xFF6272A4),
        isDark = true
    )
    AppThemeMode.TOMORROW_NIGHT_EIGHTIES -> ThemePaletteSpec(
        bgTop = Color(0xFF2D2D2D), bgBottom = Color(0xFF232323),
        surface = Color(0xFF393939), cardSubtle = Color(0xFF282828),
        primary = Color(0xFF6699CC), secondary = Color(0xFFF2777A),
        textPrimary = Color(0xFFCCCCCC), textSecondary = Color(0xFF999999), textTertiary = Color(0xFF777777),
        isDark = true
    )
    AppThemeMode.NORD -> ThemePaletteSpec(
        bgTop = Color(0xFF2E3440), bgBottom = Color(0xFF242933),
        surface = Color(0xFF3B4252), cardSubtle = Color(0xFF292E39),
        primary = Color(0xFF88C0D0), secondary = Color(0xFF81A1C1),
        textPrimary = Color(0xFFECEFF4), textSecondary = Color(0xFFD8DEE9), textTertiary = Color(0xFF8F9AA9),
        isDark = true
    )
    AppThemeMode.NIGHT_OWL -> ThemePaletteSpec(
        bgTop = Color(0xFF011627), bgBottom = Color(0xFF01101D),
        surface = Color(0xFF0B253A), cardSubtle = Color(0xFF061B2C),
        primary = Color(0xFF82AAFF), secondary = Color(0xFF7FDBCA),
        textPrimary = Color(0xFFD6DEEB), textSecondary = Color(0xFF9EB3C8), textTertiary = Color(0xFF5F7E97),
        isDark = true
    )
    AppThemeMode.BLULOCO_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF282C34), bgBottom = Color(0xFF1E2128),
        surface = Color(0xFF323741), cardSubtle = Color(0xFF22262E),
        primary = Color(0xFF10B1FE), secondary = Color(0xFF3FC56B),
        textPrimary = Color(0xFFB9C0CB), textSecondary = Color(0xFF8A92A0), textTertiary = Color(0xFF636D83),
        isDark = true
    )
    AppThemeMode.AYU_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF0B0E14), bgBottom = Color(0xFF07090E),
        surface = Color(0xFF131721), cardSubtle = Color(0xFF0E1119),
        primary = Color(0xFFE6B450), secondary = Color(0xFF39BAE6),
        textPrimary = Color(0xFFBFBDB6), textSecondary = Color(0xFF8C8A84), textTertiary = Color(0xFF565B66),
        isDark = true
    )
    AppThemeMode.AYU_MIRAGE -> ThemePaletteSpec(
        bgTop = Color(0xFF1F2430), bgBottom = Color(0xFF171B24),
        surface = Color(0xFF272D3C), cardSubtle = Color(0xFF1B202B),
        primary = Color(0xFFFFCC66), secondary = Color(0xFF5CCFE6),
        textPrimary = Color(0xFFCCCAC2), textSecondary = Color(0xFF98968E), textTertiary = Color(0xFF707A8C),
        isDark = true
    )
    AppThemeMode.AYU_LIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFFFAFAFA), bgBottom = Color(0xFFF2F3F4),
        surface = Color(0xFFFFFFFF), cardSubtle = Color(0xFFF3F4F5),
        primary = Color(0xFFFF9940), secondary = Color(0xFF399EE6),
        textPrimary = Color(0xFF575F66), textSecondary = Color(0xFF787B80), textTertiary = Color(0xFF9DA2A6),
        isDark = false
    )
    AppThemeMode.WINTER_IS_COMING -> ThemePaletteSpec(
        bgTop = Color(0xFF011627), bgBottom = Color(0xFF010E19),
        surface = Color(0xFF0C2438), cardSubtle = Color(0xFF071A2B),
        primary = Color(0xFF00BFF9), secondary = Color(0xFF7FDBCA),
        textPrimary = Color(0xFFD6DEEB), textSecondary = Color(0xFF97ACC1), textTertiary = Color(0xFF637777),
        isDark = true
    )
    AppThemeMode.ZENBURN -> ThemePaletteSpec(
        bgTop = Color(0xFF3F3F3F), bgBottom = Color(0xFF333333),
        surface = Color(0xFF4B4B4B), cardSubtle = Color(0xFF383838),
        primary = Color(0xFFF0DFAF), secondary = Color(0xFF7F9F7F),
        textPrimary = Color(0xFFDCDCCC), textSecondary = Color(0xFFB5B5A5), textTertiary = Color(0xFF8C8C7E),
        isDark = true
    )
    AppThemeMode.SYNTHWAVE_84 -> ThemePaletteSpec(
        bgTop = Color(0xFF262335), bgBottom = Color(0xFF1B1726),
        surface = Color(0xFF332E47), cardSubtle = Color(0xFF211E2F),
        primary = Color(0xFFFF7EDB), secondary = Color(0xFF36F9F6),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFC9C4DF), textTertiary = Color(0xFF848BBD),
        isDark = true
    )
    AppThemeMode.SHADES_OF_PURPLE -> ThemePaletteSpec(
        bgTop = Color(0xFF2D2B55), bgBottom = Color(0xFF1E1D3B),
        surface = Color(0xFF3A376E), cardSubtle = Color(0xFF252348),
        primary = Color(0xFFFAD000), secondary = Color(0xFFA599E9),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFC7C2E8), textTertiary = Color(0xFF8B84B8),
        isDark = true
    )
    AppThemeMode.LASER_WAVE -> ThemePaletteSpec(
        bgTop = Color(0xFF27212E), bgBottom = Color(0xFF1D1823),
        surface = Color(0xFF342C3D), cardSubtle = Color(0xFF221D29),
        primary = Color(0xFFEB64B9), secondary = Color(0xFF40B4C4),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFD4CCE0), textTertiary = Color(0xFF91889B),
        isDark = true
    )
    AppThemeMode.ABYSS -> ThemePaletteSpec(
        bgTop = Color(0xFF000C18), bgBottom = Color(0xFF00070F),
        surface = Color(0xFF071B2F), cardSubtle = Color(0xFF031120),
        primary = Color(0xFF55B4D4), secondary = Color(0xFF77B4CC),
        textPrimary = Color(0xFFE6EDF5), textSecondary = Color(0xFF88A3BF), textTertiary = Color(0xFF4B647D),
        isDark = true
    )
    AppThemeMode.COBALT2 -> ThemePaletteSpec(
        bgTop = Color(0xFF193549), bgBottom = Color(0xFF122738),
        surface = Color(0xFF214660), cardSubtle = Color(0xFF152E42),
        primary = Color(0xFFFFC600), secondary = Color(0xFF0088FF),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFB3CCE0), textTertiary = Color(0xFF6A8FA8),
        isDark = true
    )
    AppThemeMode.RED -> ThemePaletteSpec(
        bgTop = Color(0xFF390000), bgBottom = Color(0xFF260000),
        surface = Color(0xFF4F0909), cardSubtle = Color(0xFF310404),
        primary = Color(0xFFFF5A5A), secondary = Color(0xFFFFB86C),
        textPrimary = Color(0xFFF8F8F8), textSecondary = Color(0xFFE0B4B4), textTertiary = Color(0xFFAC7676),
        isDark = true
    )
    AppThemeMode.POIMANDRES -> ThemePaletteSpec(
        bgTop = Color(0xFF1B1E28), bgBottom = Color(0xFF141720),
        surface = Color(0xFF252936), cardSubtle = Color(0xFF171A24),
        primary = Color(0xFF5DE4C7), secondary = Color(0xFFADD7FF),
        textPrimary = Color(0xFFE4F0FB), textSecondary = Color(0xFFA6ACCD), textTertiary = Color(0xFF767C9D),
        isDark = true
    )
    AppThemeMode.HOPSCOTCH -> ThemePaletteSpec(
        bgTop = Color(0xFF322931), bgBottom = Color(0xFF251E24),
        surface = Color(0xFF413540), cardSubtle = Color(0xFF2C232B),
        primary = Color(0xFF1290BF), secondary = Color(0xFF8FC13E),
        textPrimary = Color(0xFFD5D3D5), textSecondary = Color(0xFFABA6AA), textTertiary = Color(0xFF797379),
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
        primary = Color(0xFFD79921), secondary = Color(0xFF689D6A),
        textPrimary = Color(0xFF3C3836), textSecondary = Color(0xFF504945), textTertiary = Color(0xFF7C6F64),
        isDark = false
    )
    AppThemeMode.OCEANIC_NEXT -> ThemePaletteSpec(
        bgTop = Color(0xFF1B2B34), bgBottom = Color(0xFF142129),
        surface = Color(0xFF253A46), cardSubtle = Color(0xFF182730),
        primary = Color(0xFF6699CC), secondary = Color(0xFF5FB3B3),
        textPrimary = Color(0xFFD8DEE9), textSecondary = Color(0xFFA7B5C2), textTertiary = Color(0xFF65737E),
        isDark = true
    )
    AppThemeMode.BASE16_EIGHTIES -> ThemePaletteSpec(
        bgTop = Color(0xFF2D2D2D), bgBottom = Color(0xFF222222),
        surface = Color(0xFF393939), cardSubtle = Color(0xFF282828),
        primary = Color(0xFF6699CC), secondary = Color(0xFF66CCCC),
        textPrimary = Color(0xFFD3D0C8), textSecondary = Color(0xFFA09F93), textTertiary = Color(0xFF747369),
        isDark = true
    )
    AppThemeMode.TOMORROW_NIGHT_BLUE -> ThemePaletteSpec(
        bgTop = Color(0xFF002451), bgBottom = Color(0xFF001A3D),
        surface = Color(0xFF00346E), cardSubtle = Color(0xFF00204A),
        primary = Color(0xFFBBDAFF), secondary = Color(0xFF99FFFF),
        textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFFBFD4EE), textTertiary = Color(0xFF7285B7),
        isDark = true
    )
    AppThemeMode.MATERIAL_OCEANIC -> ThemePaletteSpec(
        bgTop = Color(0xFF263238), bgBottom = Color(0xFF1D272C),
        surface = Color(0xFF314048), cardSubtle = Color(0xFF222D33),
        primary = Color(0xFF80CBC4), secondary = Color(0xFF82AAFF),
        textPrimary = Color(0xFFEEFFFF), textSecondary = Color(0xFFB0BEC5), textTertiary = Color(0xFF607D8B),
        isDark = true
    )
    AppThemeMode.MATERIAL_PALENIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFF292D3E), bgBottom = Color(0xFF202331),
        surface = Color(0xFF343A50), cardSubtle = Color(0xFF242838),
        primary = Color(0xFFC792EA), secondary = Color(0xFF82AAFF),
        textPrimary = Color(0xFFA6ACCD), textSecondary = Color(0xFF8B92B8), textTertiary = Color(0xFF676E95),
        isDark = true
    )
    AppThemeMode.MATERIAL_DARKER -> ThemePaletteSpec(
        bgTop = Color(0xFF212121), bgBottom = Color(0xFF171717),
        surface = Color(0xFF2D2D2D), cardSubtle = Color(0xFF1C1C1C),
        primary = Color(0xFFFF9800), secondary = Color(0xFF82AAFF),
        textPrimary = Color(0xFFEEFFFF), textSecondary = Color(0xFFB2B8C2), textTertiary = Color(0xFF6E6E6E),
        isDark = true
    )
    AppThemeMode.CATPPUCCIN_MOCHA -> ThemePaletteSpec(
        bgTop = Color(0xFF1E1E2E), bgBottom = Color(0xFF181825),
        surface = Color(0xFF29293E), cardSubtle = Color(0xFF1B1B2B),
        primary = Color(0xFFCBA6F7), secondary = Color(0xFF89B4FA),
        textPrimary = Color(0xFFCDD6F4), textSecondary = Color(0xFFA6ADC8), textTertiary = Color(0xFF7F849C),
        isDark = true
    )
    AppThemeMode.KANAGAWA -> ThemePaletteSpec(
        bgTop = Color(0xFF1F1F28), bgBottom = Color(0xFF16161D),
        surface = Color(0xFF2A2A37), cardSubtle = Color(0xFF1B1B24),
        primary = Color(0xFF7E9CD8), secondary = Color(0xFF98BB6C),
        textPrimary = Color(0xFFDCD7BA), textSecondary = Color(0xFFC8C093), textTertiary = Color(0xFF727169),
        isDark = true
    )
    AppThemeMode.ROSE_PINE -> ThemePaletteSpec(
        bgTop = Color(0xFF191724), bgBottom = Color(0xFF12101B),
        surface = Color(0xFF232136), cardSubtle = Color(0xFF161420),
        primary = Color(0xFFEBBCBA), secondary = Color(0xFF9CCFD8),
        textPrimary = Color(0xFFE0DEF4), textSecondary = Color(0xFF908CAA), textTertiary = Color(0xFF6E6A86),
        isDark = true
    )
    AppThemeMode.GITHUB_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF0D1117), bgBottom = Color(0xFF080B10),
        surface = Color(0xFF161B22), cardSubtle = Color(0xFF10151C),
        primary = Color(0xFF58A6FF), secondary = Color(0xFF3FB950),
        textPrimary = Color(0xFFF0F6FC), textSecondary = Color(0xFF8B949E), textTertiary = Color(0xFF6E7681),
        isDark = true
    )
    AppThemeMode.GITHUB_LIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFFFFFFFF), bgBottom = Color(0xFFF6F8FA),
        surface = Color(0xFFFFFFFF), cardSubtle = Color(0xFFF3F4F6),
        primary = Color(0xFF0969DA), secondary = Color(0xFF1A7F37),
        textPrimary = Color(0xFF1F2328), textSecondary = Color(0xFF656D76), textTertiary = Color(0xFF8C959F),
        isDark = false
    )
    AppThemeMode.PANDA_THEME -> ThemePaletteSpec(
        bgTop = Color(0xFF292A2B), bgBottom = Color(0xFF1E1F20),
        surface = Color(0xFF343638), cardSubtle = Color(0xFF242527),
        primary = Color(0xFFFF75B5), secondary = Color(0xFF19F9D8),
        textPrimary = Color(0xFFE6E6E6), textSecondary = Color(0xFFB0B0B0), textTertiary = Color(0xFF757575),
        isDark = true
    )
    AppThemeMode.ATOM_ONE_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF282C34), bgBottom = Color(0xFF20232A),
        surface = Color(0xFF323741), cardSubtle = Color(0xFF242830),
        primary = Color(0xFF528BFF), secondary = Color(0xFF56B6C2),
        textPrimary = Color(0xFFD7DAE0), textSecondary = Color(0xFF9DA5B4), textTertiary = Color(0xFF5C6370),
        isDark = true
    )
    AppThemeMode.TOKYO_NIGHT -> ThemePaletteSpec(
        bgTop = Color(0xFF1A1B26), bgBottom = Color(0xFF13141D),
        surface = Color(0xFF24283B), cardSubtle = Color(0xFF171925),
        primary = Color(0xFF7AA2F7), secondary = Color(0xFFBB9AF7),
        textPrimary = Color(0xFFC0CAF5), textSecondary = Color(0xFF9AA5CE), textTertiary = Color(0xFF565F89),
        isDark = true
    )

    // --- 20 Luxury Liquid Glass Themes ---
    AppThemeMode.OBSIDIAN_LIQUID_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF080B11), bgBottom = Color(0xFF04060A),
        surface = Color(0xFF101723), cardSubtle = Color(0xFF0B1019),
        primary = Color(0xFF00E5FF), secondary = Color(0xFF7C4DFF),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFF94A3B8), textTertiary = Color(0xFF64748B),
        isDark = true, blurDp = 22
    )
    AppThemeMode.FROSTED_ARCTIC_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFFEBF5FD), bgBottom = Color(0xFFDCEBF9),
        surface = Color(0xFFF7FBFF), cardSubtle = Color(0xFFE3EFFA),
        primary = Color(0xFF0284C7), secondary = Color(0xFF0EA5E9),
        textPrimary = Color(0xFF0F172A), textSecondary = Color(0xFF475569), textTertiary = Color(0xFF64748B),
        isDark = false, blurDp = 24
    )
    AppThemeMode.CYBER_NEON_LIQUID -> ThemePaletteSpec(
        bgTop = Color(0xFF090814), bgBottom = Color(0xFF05040B),
        surface = Color(0xFF141229), cardSubtle = Color(0xFF0E0C1D),
        primary = Color(0xFF00FFAA), secondary = Color(0xFFFF007F),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFA5B4FC), textTertiary = Color(0xFF6366F1),
        isDark = true, blurDp = 20
    )
    AppThemeMode.SMOKED_QUARTZ_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF141216), bgBottom = Color(0xFF0D0B0E),
        surface = Color(0xFF211E25), cardSubtle = Color(0xFF18151B),
        primary = Color(0xFFD4AF37), secondary = Color(0xFFA8A29E),
        textPrimary = Color(0xFFFAF8F5), textSecondary = Color(0xFFD6D3D1), textTertiary = Color(0xFF78716C),
        isDark = true, blurDp = 18
    )
    AppThemeMode.MIDNIGHT_VELVET_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF110B1E), bgBottom = Color(0xFF080511),
        surface = Color(0xFF1C1332), cardSubtle = Color(0xFF140D25),
        primary = Color(0xFFC084FC), secondary = Color(0xFFF472B6),
        textPrimary = Color(0xFFFAF5FF), textSecondary = Color(0xFFD8B4FE), textTertiary = Color(0xFF9333EA),
        isDark = true, blurDp = 20
    )
    AppThemeMode.AURORA_BOREALIS_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF06161B), bgBottom = Color(0xFF030D11),
        surface = Color(0xFF0E262E), cardSubtle = Color(0xFF091B21),
        primary = Color(0xFF34D399), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFECFDF5), textSecondary = Color(0xFFA7F3D0), textTertiary = Color(0xFF6EE7B7),
        isDark = true, blurDp = 22
    )
    AppThemeMode.CRYSTAL_PRISM_LIQUID -> ThemePaletteSpec(
        bgTop = Color(0xFFF6F7FF), bgBottom = Color(0xFFEBEEFF),
        surface = Color(0xFFFFFFFF), cardSubtle = Color(0xFFEEF0FF),
        primary = Color(0xFF6366F1), secondary = Color(0xFFEC4899),
        textPrimary = Color(0xFF1E1B4B), textSecondary = Color(0xFF4338CA), textTertiary = Color(0xFF6366F1),
        isDark = false, blurDp = 24
    )
    AppThemeMode.DEEP_OCEAN_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF041526), bgBottom = Color(0xFF020C17),
        surface = Color(0xFF0A243E), cardSubtle = Color(0xFF061A2E),
        primary = Color(0xFF00D2FF), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFF0F9FF), textSecondary = Color(0xFFBAE6FD), textTertiary = Color(0xFF0284C7),
        isDark = true, blurDp = 20
    )
    AppThemeMode.AMBER_MERCURY_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF17110A), bgBottom = Color(0xFF0E0A05),
        surface = Color(0xFF261C10), cardSubtle = Color(0xFF1B140B),
        primary = Color(0xFFF59E0B), secondary = Color(0xFFFBBF24),
        textPrimary = Color(0xFFFFFBEB), textSecondary = Color(0xFFFDE68A), textTertiary = Color(0xFFB45309),
        isDark = true, blurDp = 18
    )
    AppThemeMode.TITANIUM_MIST_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFFEEF2F6), bgBottom = Color(0xFFE2E8F0),
        surface = Color(0xFFF8FAFC), cardSubtle = Color(0xFFE5EBF2),
        primary = Color(0xFF334155), secondary = Color(0xFF0284C7),
        textPrimary = Color(0xFF0F172A), textSecondary = Color(0xFF475569), textTertiary = Color(0xFF64748B),
        isDark = false, blurDp = 22
    )
    AppThemeMode.HOLOGRAPHIC_LIQUID_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF0D1021), bgBottom = Color(0xFF070915),
        surface = Color(0xFF181C38), cardSubtle = Color(0xFF101329),
        primary = Color(0xFF22D3EE), secondary = Color(0xFFE879F9),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFC4B5FD), textTertiary = Color(0xFF818CF8),
        isDark = true, blurDp = 22
    )
    AppThemeMode.CARBON_BLUR_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF111315), bgBottom = Color(0xFF0A0B0D),
        surface = Color(0xFF1C2024), cardSubtle = Color(0xFF14171A),
        primary = Color(0xFF38BDF8), secondary = Color(0xFF94A3B8),
        textPrimary = Color(0xFFF1F5F9), textSecondary = Color(0xFF94A3B8), textTertiary = Color(0xFF64748B),
        isDark = true, blurDp = 20
    )
    AppThemeMode.EMERALD_TRANSLUCENT_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF061812), bgBottom = Color(0xFF030F0B),
        surface = Color(0xFF0C291F), cardSubtle = Color(0xFF081F17),
        primary = Color(0xFF10B981), secondary = Color(0xFF34D399),
        textPrimary = Color(0xFFECFDF5), textSecondary = Color(0xFFA7F3D0), textTertiary = Color(0xFF059669),
        isDark = true, blurDp = 20
    )
    AppThemeMode.ULTRAVIOLET_FROST_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF120A24), bgBottom = Color(0xFF0A0516),
        surface = Color(0xFF1F123B), cardSubtle = Color(0xFF150C2A),
        primary = Color(0xFFA855F7), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFFAF5FF), textSecondary = Color(0xFFD8B4FE), textTertiary = Color(0xFF7E22CE),
        isDark = true, blurDp = 22
    )
    AppThemeMode.SAPPHIRE_LIQUID_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF061128), bgBottom = Color(0xFF030A1A),
        surface = Color(0xFF0E1F45), cardSubtle = Color(0xFF091633),
        primary = Color(0xFF3B82F6), secondary = Color(0xFF06B6D4),
        textPrimary = Color(0xFFEFF6FF), textSecondary = Color(0xFFBFDBFE), textTertiary = Color(0xFF60A5FA),
        isDark = true, blurDp = 20
    )
    AppThemeMode.PEARLESCENT_DARK_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF15171E), bgBottom = Color(0xFF0D0F14),
        surface = Color(0xFF20242F), cardSubtle = Color(0xFF181B23),
        primary = Color(0xFFE2E8F0), secondary = Color(0xFF7DD3FC),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFCBD5E1), textTertiary = Color(0xFF64748B),
        isDark = true, blurDp = 20
    )
    AppThemeMode.RUBY_REFRACTIVE_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF1D080E), bgBottom = Color(0xFF120408),
        surface = Color(0xFF2E0D17), cardSubtle = Color(0xFF210910),
        primary = Color(0xFFF43F5E), secondary = Color(0xFFFB7185),
        textPrimary = Color(0xFFFFF1F2), textSecondary = Color(0xFFFECDD3), textTertiary = Color(0xFFBE123C),
        isDark = true, blurDp = 20
    )
    AppThemeMode.CHROMATIC_ABERRATION_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF0C0B14), bgBottom = Color(0xFF06050A),
        surface = Color(0xFF181626), cardSubtle = Color(0xFF100F1B),
        primary = Color(0xFFFF2A85), secondary = Color(0xFF00E5FF),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFFC4B5FD), textTertiary = Color(0xFF818CF8),
        isDark = true, blurDp = 22
    )
    AppThemeMode.LIQUID_MERCURY_DARK -> ThemePaletteSpec(
        bgTop = Color(0xFF16191D), bgBottom = Color(0xFF0E1013),
        surface = Color(0xFF22272E), cardSubtle = Color(0xFF191C21),
        primary = Color(0xFFCBD5E1), secondary = Color(0xFF38BDF8),
        textPrimary = Color(0xFFF8FAFC), textSecondary = Color(0xFF94A3B8), textTertiary = Color(0xFF64748B),
        isDark = true, blurDp = 18
    )
    AppThemeMode.BIOLUMINESCENT_GLASS -> ThemePaletteSpec(
        bgTop = Color(0xFF031618), bgBottom = Color(0xFF010C0E),
        surface = Color(0xFF08262A), cardSubtle = Color(0xFF051A1E),
        primary = Color(0xFF00F5D4), secondary = Color(0xFF00BBF9),
        textPrimary = Color(0xFFECFEFF), textSecondary = Color(0xFFA5F3FC), textTertiary = Color(0xFF0891B2),
        isDark = true, blurDp = 22
    )

    // Fallback for core entries if queried via helper
    else -> ThemePaletteSpec(
        bgTop = ObsidianVoidTop, bgBottom = ObsidianVoidBottom,
        surface = ObsidianGlassSurface, cardSubtle = Color(0xFF0D131D),
        primary = ElectricCyan, secondary = EmeraldMint,
        textPrimary = TextPrimary, textSecondary = TextSecondary, textTertiary = TextTertiary,
        isDark = true
    )
}

fun createExtendedColorScheme(spec: ThemePaletteSpec): ColorScheme =
    if (spec.isDark) {
        darkColorScheme(
            primary = spec.primary,
            onPrimary = Color(0xFF081018),
            primaryContainer = spec.primary.copy(alpha = 0.25f),
            onPrimaryContainer = spec.textPrimary,
            secondary = spec.secondary,
            onSecondary = Color(0xFF081018),
            secondaryContainer = spec.secondary.copy(alpha = 0.22f),
            onSecondaryContainer = spec.textPrimary,
            tertiary = spec.secondary,
            onTertiary = spec.textPrimary,
            background = spec.bgTop,
            onBackground = spec.textPrimary,
            surface = spec.surface,
            onSurface = spec.textPrimary,
            surfaceVariant = spec.cardSubtle,
            onSurfaceVariant = spec.textSecondary,
            outline = spec.primary.copy(alpha = 0.35f),
            error = RedAlert,
            onError = Color.White
        )
    } else {
        lightColorScheme(
            primary = spec.primary,
            onPrimary = Color.White,
            primaryContainer = spec.primary.copy(alpha = 0.14f),
            onPrimaryContainer = spec.textPrimary,
            secondary = spec.secondary,
            onSecondary = Color.White,
            secondaryContainer = spec.secondary.copy(alpha = 0.14f),
            onSecondaryContainer = spec.textPrimary,
            tertiary = spec.secondary,
            onTertiary = Color.White,
            background = spec.bgTop,
            onBackground = spec.textPrimary,
            surface = spec.surface,
            onSurface = spec.textPrimary,
            surfaceVariant = spec.cardSubtle,
            onSurfaceVariant = spec.textSecondary,
            outline = spec.textTertiary.copy(alpha = 0.35f),
            error = RedAlert,
            onError = Color.White
        )
    }

fun createExtendedLiquidGlassStyle(spec: ThemePaletteSpec): LiquidGlassStyle {
    val borderColor = if (spec.isDark) {
        spec.primary.copy(alpha = 0.28f)
    } else {
        spec.textPrimary.copy(alpha = 0.10f)
    }

    return LiquidGlassStyle(
        surfaceColor = spec.surface,
        surfaceTranslucent = spec.surface.copy(alpha = if (spec.isDark) 0.86f else 0.96f),
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
                spec.primary.copy(alpha = if (spec.isDark) 0.42f else 0.25f),
                Color.White.copy(alpha = if (spec.isDark) 0.16f else 0.65f),
                spec.secondary.copy(alpha = if (spec.isDark) 0.36f else 0.20f)
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
        neumorphicHighlight = if (spec.isDark) spec.primary.copy(alpha = 0.22f) else Color(0xCCFFFFFF),
        neumorphicShadow = if (spec.isDark) Color(0xE6020408) else Color(0x140F172A),
        neumorphicBorderLight = if (spec.isDark) Color(0x2BFFFFFF) else Color(0x80FFFFFF),
        neumorphicBorderDark = if (spec.isDark) Color(0x66000000) else Color(0x0D0F172A)
    )
}
