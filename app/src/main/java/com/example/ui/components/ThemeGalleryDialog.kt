package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.AppThemeMode
import com.example.model.ThemeCategory
import com.example.ui.theme.LocalLiquidGlass
import com.example.ui.theme.createLiquidGlassStyle

/**
 * Compact 3-dot palette swatch card used in the Multi-Theme horizontal strip and full Gallery.
 */
@Composable
fun ThemeSwatchCard(
    themeMode: AppThemeMode,
    isSelected: Boolean,
    appLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current
    val previewStyle = remember(themeMode) { createLiquidGlassStyle(themeMode) }
    val shape = RoundedCornerShape(14.dp)

    val animatedBg by animateColorAsState(
        targetValue = when {
            isSelected && glassStyle.isNeumorphic -> glassStyle.cardSubtleBg
            isSelected -> previewStyle.accentPrimary.copy(alpha = if (glassStyle.isDark) 0.22f else 0.14f)
            glassStyle.isNeumorphic -> glassStyle.neumorphicSurface
            else -> glassStyle.cardSubtleBg
        },
        animationSpec = tween(durationMillis = 180),
        label = "swatch_bg"
    )

    val borderColor = when {
        isSelected -> previewStyle.accentPrimary
        glassStyle.isNeumorphic -> glassStyle.neumorphicBorderLight
        else -> glassStyle.borderColor
    }

    Box(
        modifier = modifier
            .tactileSpringPress(pressedScale = 0.96f, onClick = onClick)
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
            .border(width = if (isSelected) 1.4.dp else 0.8.dp, color = borderColor, shape = shape)
            .padding(horizontal = 10.dp, vertical = 9.dp)
            .testTag("theme_swatch_${themeMode.name.lowercase()}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3 live color swatch dots: surface, primary accent, secondary accent
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(11.dp)
                            .clip(CircleShape)
                            .background(previewStyle.surfaceColor)
                            .border(0.7.dp, previewStyle.textTertiary.copy(alpha = 0.45f), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(11.dp)
                            .clip(CircleShape)
                            .background(previewStyle.accentPrimary)
                    )
                    Box(
                        modifier = Modifier
                            .size(11.dp)
                            .clip(CircleShape)
                            .background(previewStyle.accentSecondary)
                    )
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = previewStyle.accentPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = themeMode.titleEn,
                color = glassStyle.textPrimary,
                fontSize = 11.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (appLanguage == AppLanguage.ARABIC) themeMode.titleAr else themeMode.descEn,
                color = glassStyle.textTertiary,
                fontSize = 9.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Searchable & filterable Multi-Theme Engine gallery allowing instant selection across all 66 themes.
 */
@Composable
fun ThemeGalleryDialog(
    currentThemeMode: AppThemeMode,
    appLanguage: AppLanguage,
    onSelectTheme: (AppThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val glassStyle = LocalLiquidGlass.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ThemeCategory?>(null) }

    val selectableThemes = remember {
        AppThemeMode.values().filter { it != AppThemeMode.SYSTEM }
    }

    val filteredThemes = remember(searchQuery, selectedCategory) {
        val q = searchQuery.trim()
        selectableThemes.filter { mode ->
            val matchesCat = selectedCategory == null || mode.category == selectedCategory
            val matchesQuery = q.isEmpty() ||
                mode.titleEn.contains(q, ignoreCase = true) ||
                mode.titleAr.contains(q, ignoreCase = true) ||
                mode.descEn.contains(q, ignoreCase = true)
            matchesCat && matchesQuery
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .widthIn(max = 580.dp)
                .fillMaxHeight(0.82f)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = if (glassStyle.isNeumorphic) {
                        Brush.verticalGradient(listOf(glassStyle.surfaceColor, glassStyle.neumorphicBase))
                    } else {
                        glassStyle.backdropBrush
                    }
                )
                .border(1.1.dp, glassStyle.borderColor, RoundedCornerShape(24.dp))
                .padding(16.dp)
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
                            icon = Icons.Default.Palette,
                            contentDescription = null,
                            size = 36.dp,
                            iconSize = 18.dp,
                            tintColor = glassStyle.accentPrimary,
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Multi-Theme Engine",
                                color = glassStyle.textPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) {
                                    "${selectableThemes.size} ثيم متاح • اختر التصميم"
                                } else {
                                    "${selectableThemes.size} Curated Themes Available"
                                },
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
                        tintColor = glassStyle.textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Live Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) {
                                "ابحث عن ثيم (مثل Dracula, Monokai, Glass...)"
                            } else {
                                "Search themes (Dracula, Monokai, Glass...)"
                            },
                            fontSize = 12.sp,
                            color = glassStyle.textTertiary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = glassStyle.accentPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = glassStyle.textTertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = glassStyle.textPrimary,
                        fontSize = 12.5.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = glassStyle.accentPrimary,
                        unfocusedBorderColor = glassStyle.borderColor,
                        focusedContainerColor = glassStyle.cardSubtleBg,
                        unfocusedContainerColor = glassStyle.cardSubtleBg
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GalleryFilterChip(
                        label = if (appLanguage == AppLanguage.ARABIC) "الكل (${selectableThemes.size})" else "All (${selectableThemes.size})",
                        isSelected = selectedCategory == null,
                        onClick = { selectedCategory = null }
                    )
                    ThemeCategory.values().forEach { cat ->
                        val count = selectableThemes.count { it.category == cat }
                        GalleryFilterChip(
                            label = if (appLanguage == AppLanguage.ARABIC) "${cat.titleAr} ($count)" else "${cat.titleEn} ($count)",
                            isSelected = selectedCategory == cat,
                            onClick = { selectedCategory = cat }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2-Column Theme Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredThemes, key = { it.name }) { mode ->
                        ThemeSwatchCard(
                            themeMode = mode,
                            isSelected = currentThemeMode == mode,
                            appLanguage = appLanguage,
                            onClick = { onSelectTheme(mode) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GalleryFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val glassStyle = LocalLiquidGlass.current
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .iosLiquidGlassSurface(
                shape = shape,
                tintColor = if (isSelected) glassStyle.accentPrimary else glassStyle.textSecondary,
                isFilledAccent = isSelected,
                elevation = if (isSelected && glassStyle.iosLiquidGlassEnabled) 3.dp else 0.dp
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) glassStyle.onAccentPrimary else glassStyle.textSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
        )
    }
}
