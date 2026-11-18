package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalLiquidGlass
import com.example.ui.theme.contrastOnColor

/**
 * Apple iOS 26-inspired Liquid Glass optical surface modifier.
 *
 * When enabled (via [LocalLiquidGlass.current.iosLiquidGlassEnabled] and [enabled]), applies:
 * 1. Translucent tinted refractive body layer
 * 2. Curved top specular meniscus dome catchlight across the upper ~46% of the capsule/squircle
 * 3. Subtle bottom internal bounce refraction glow
 * 4. Diagonal crystalline glass rim border (pure white catchlight top-left -> soft tinted edge bottom-right)
 *
 * When disabled, gracefully falls back to a clean matte theme-aware container with subtle hairline border.
 */
fun Modifier.iosLiquidGlassSurface(
    shape: Shape = RoundedCornerShape(16.dp),
    tintColor: Color = Color.Unspecified,
    isFilledAccent: Boolean = false,
    isPressed: Boolean = false,
    cornerRadius: Dp = 16.dp,
    enabled: Boolean = true,
    elevation: Dp = 4.dp
): Modifier = composed {
    val glassStyle = LocalLiquidGlass.current
    val active = enabled && glassStyle.iosLiquidGlassEnabled

    val resolvedTint = when {
        tintColor != Color.Unspecified -> tintColor
        else -> glassStyle.accentPrimary
    }

    val pressBoost = if (isPressed) 0.14f else 0f

    if (!active) {
        val matteBg = if (isFilledAccent) {
            resolvedTint
        } else {
            glassStyle.cardSubtleBg.copy(alpha = 0.92f)
        }
        val matteBorder = if (isFilledAccent) {
            Color.Transparent
        } else {
            glassStyle.borderColor.copy(alpha = 0.5f)
        }
        this
            .clip(shape)
            .background(matteBg, shape)
            .drawBehind {
                if (matteBorder != Color.Transparent) {
                    val strokePx = 1.dp.toPx()
                    val outline = shape.createOutline(size, layoutDirection, this)
                    drawOutline(outline = outline, color = matteBorder, style = Stroke(width = strokePx))
                }
            }
    } else {
        val lensBase = if (isFilledAccent) {
            resolvedTint.copy(alpha = if (glassStyle.isDark) 0.88f else 0.94f)
        } else {
            resolvedTint
                .copy(alpha = if (glassStyle.isDark) 0.15f else 0.12f)
                .compositeOver(glassStyle.surfaceTranslucent.copy(alpha = if (glassStyle.isDark) 0.72f else 0.86f))
        }

        val lensBottom = if (isFilledAccent) {
            resolvedTint
                .copy(alpha = 0.78f)
                .compositeOver(if (glassStyle.isDark) Color(0xFF040812) else Color(0xFF0E172A))
        } else {
            glassStyle.cardSubtleBg.copy(alpha = if (glassStyle.isDark) 0.80f else 0.92f)
        }

        val specularTopAlpha = ((if (isFilledAccent) 0.44f else if (glassStyle.isDark) 0.28f else 0.78f) + pressBoost)
            .coerceAtMost(0.92f)
        val specularMidAlpha = if (isFilledAccent) 0.08f else if (glassStyle.isDark) 0.05f else 0.16f
        val bottomBounceAlpha = (if (isFilledAccent) 0.26f else 0.16f) + pressBoost * 0.5f

        val rimStrokeBrush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = if (glassStyle.isDark) 0.78f else 0.95f),
                Color.White.copy(alpha = if (glassStyle.isDark) 0.20f else 0.48f),
                resolvedTint.copy(alpha = if (isFilledAccent) 0.70f else 0.48f),
                Color.White.copy(alpha = if (glassStyle.isDark) 0.46f else 0.72f)
            ),
            start = Offset.Zero,
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )

        val shadowModifier = if (elevation > 0.dp && !glassStyle.isNeumorphic) {
            Modifier.shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = resolvedTint.copy(alpha = if (glassStyle.isDark) 0.35f else 0.18f),
                spotColor = resolvedTint.copy(alpha = if (glassStyle.isDark) 0.40f else 0.22f)
            )
        } else {
            Modifier
        }

        this
            .then(shadowModifier)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(listOf(lensBase, lensBottom)),
                shape = shape
            )
            .drawWithContent {
                drawContent()

                val outline = shape.createOutline(size, layoutDirection, this)
                val clipShapePath = androidx.compose.ui.graphics.Path().apply {
                    when (outline) {
                        is androidx.compose.ui.graphics.Outline.Rectangle -> addRect(outline.rect)
                        is androidx.compose.ui.graphics.Outline.Rounded -> addRoundRect(outline.roundRect)
                        is androidx.compose.ui.graphics.Outline.Generic -> addPath(outline.path)
                    }
                }

                clipPath(clipShapePath) {
                    // Upper Apple Liquid Glass specular meniscus highlight (top ~48% dome)
                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.00f to Color.White.copy(alpha = specularTopAlpha),
                                0.26f to Color.White.copy(alpha = specularMidAlpha),
                                0.52f to Color.Transparent
                            ),
                            startY = 0f,
                            endY = size.height
                        )
                    )

                    // Lower optical internal refraction bounce light
                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.68f to Color.Transparent,
                                1.00f to (if (isFilledAccent) Color.White else resolvedTint).copy(alpha = bottomBounceAlpha)
                            ),
                            startY = 0f,
                            endY = size.height
                        )
                    )
                }

                // Prismatic crystalline outer glass rim
                val strokePx = 1.15.dp.toPx()
                drawOutline(
                    outline = outline,
                    brush = rimStrokeBrush,
                    style = Stroke(width = strokePx)
                )
            }
    }
}

/**
 * Apple iOS Liquid Glass Squircle / Circular Icon Badge.
 * Wraps status, category, provider, or header icons inside an optical liquid glass lens
 * when iOS Liquid Glass is enabled, or a crisp matte theme badge when disabled.
 */
@Composable
fun IosLiquidGlassIconBadge(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    iconSize: Dp = 20.dp,
    tintColor: Color = LocalLiquidGlass.current.accentPrimary,
    shape: Shape = RoundedCornerShape(12.dp),
    isFilled: Boolean = false
) {
    val glassStyle = LocalLiquidGlass.current
    val resolvedIconColor = if (isFilled) contrastOnColor(tintColor) else tintColor

    Box(
        modifier = modifier
            .size(size)
            .iosLiquidGlassSurface(
                shape = shape,
                tintColor = tintColor,
                isFilledAccent = isFilled,
                elevation = if (glassStyle.iosLiquidGlassEnabled) 3.dp else 0.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = resolvedIconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

/**
 * Interactive Apple iOS Liquid Glass Icon Button with spring tactile bounce & 48dp accessible hit area.
 */
@Composable
fun IosLiquidGlassIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    visualSize: Dp = 42.dp,
    iconSize: Dp = 20.dp,
    tintColor: Color = LocalLiquidGlass.current.accentPrimary,
    shape: Shape = CircleShape,
    isFilled: Boolean = false,
    enabled: Boolean = true
) {
    val glassStyle = LocalLiquidGlass.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.90f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "ios_glass_icon_btn_scale"
    )

    val resolvedIconColor = when {
        !enabled -> glassStyle.textTertiary
        isFilled -> contrastOnColor(tintColor)
        else -> tintColor
    }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = 26.dp, color = tintColor),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(visualSize)
                .iosLiquidGlassSurface(
                    shape = shape,
                    tintColor = tintColor,
                    isFilledAccent = isFilled,
                    isPressed = isPressed,
                    elevation = if (glassStyle.iosLiquidGlassEnabled && enabled) 4.dp else 0.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = resolvedIconColor,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

/**
 * Interactive Apple iOS Liquid Glass Capsule / Pill Button.
 * Automatically renders the glossy liquid glass dome meniscus + crystalline rim when enabled,
 * and ensures WCAG high-contrast foreground text/icons regardless of active theme hue.
 */
@Composable
fun IosLiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = LocalLiquidGlass.current.accentPrimary,
    contentColor: Color = Color.Unspecified,
    isFilledAccent: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val glassStyle = LocalLiquidGlass.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "ios_glass_btn_scale"
    )

    val animatedBg by animateColorAsState(
        targetValue = if (enabled) containerColor else glassStyle.cardSubtleBg,
        label = "ios_glass_btn_bg"
    )

    val resolvedContentColor = when {
        !enabled -> glassStyle.textTertiary
        contentColor != Color.Unspecified -> contentColor
        isFilledAccent -> contrastOnColor(animatedBg)
        else -> glassStyle.textPrimary
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .iosLiquidGlassSurface(
                shape = shape,
                tintColor = animatedBg,
                isFilledAccent = isFilledAccent && enabled,
                isPressed = isPressed,
                elevation = if (glassStyle.iosLiquidGlassEnabled && enabled) 6.dp else 0.dp
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = resolvedContentColor),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .padding(contentPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalContentColor provides resolvedContentColor) {
            content()
        }
    }
}
