package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HyperDisplayFontFamily
import com.example.ui.theme.LocalLiquidGlass
import com.example.ui.theme.contrastOnColor
import kotlin.math.roundToInt

/**
 * Apple iOS 26 Liquid Glass optical lens surface modifier.
 *
 * When enabled (via [LocalLiquidGlass.current.iosLiquidGlassEnabled] and [enabled]), applies:
 * 1. Multi-stop translucent refractive body tint over frosted lens glass
 * 2. Curved top specular meniscus dome highlight across upper ~46% + bright pinpoint top edge catchlight
 * 3. Diagonal internal edge light bounce along the bottom rim
 * 4. Chromatic dispersion crystalline bevel rim (crystalline pure white -> prismatic cyan/accent tint -> bounce)
 *
 * When disabled, gracefully renders a crisp flat/frosted matte container synchronized to active theme tokens.
 */
fun Modifier.iosLiquidGlassSurface(
    shape: Shape = RoundedCornerShape(18.dp),
    tintColor: Color = Color.Unspecified,
    isFilledAccent: Boolean = false,
    isPressed: Boolean = false,
    cornerRadius: Dp = 18.dp,
    enabled: Boolean = true,
    elevation: Dp = 6.dp,
    bodyAlphaOverride: Float? = null,
    useInteriorShadow: Boolean = true
): Modifier = composed {
    val glassStyle = LocalLiquidGlass.current
    val active = enabled && glassStyle.iosLiquidGlassEnabled

    val resolvedTint = when {
        tintColor != Color.Unspecified -> tintColor
        else -> glassStyle.accentPrimary
    }

    val pressBoost = if (isPressed) 0.16f else 0f

    if (!active) {
        val matteBg = if (isFilledAccent) {
            resolvedTint
        } else {
            glassStyle.cardSubtleBg.copy(alpha = 0.94f)
        }
        val matteBorder = if (isFilledAccent) {
            Color.Transparent
        } else {
            glassStyle.borderColor.copy(alpha = 0.55f)
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
        val isUltraSheer = bodyAlphaOverride != null && bodyAlphaOverride <= 0.12f
        val lensBase = when {
            isFilledAccent -> resolvedTint.copy(alpha = if (glassStyle.isDark) 0.90f else 0.96f)
            bodyAlphaOverride != null -> resolvedTint
                .copy(alpha = 0.28f)
                .compositeOver(glassStyle.surfaceTranslucent)
                .copy(alpha = bodyAlphaOverride)
            else -> resolvedTint
                .copy(alpha = if (glassStyle.isDark) 0.16f else 0.11f)
                .compositeOver(glassStyle.surfaceTranslucent.copy(alpha = if (glassStyle.isDark) 0.68f else 0.84f))
        }

        val lensMid = when {
            isFilledAccent -> resolvedTint.copy(alpha = if (glassStyle.isDark) 0.84f else 0.92f)
            bodyAlphaOverride != null -> glassStyle.surfaceColor.copy(alpha = bodyAlphaOverride)
            else -> glassStyle.surfaceColor.copy(alpha = if (glassStyle.isDark) 0.62f else 0.78f)
        }

        val lensBottom = when {
            isFilledAccent -> resolvedTint
                .copy(alpha = 0.76f)
                .compositeOver(if (glassStyle.isDark) Color(0xFF040814) else Color(0xFF0F172A))
            bodyAlphaOverride != null -> glassStyle.cardSubtleBg.copy(alpha = bodyAlphaOverride)
            else -> glassStyle.cardSubtleBg.copy(alpha = if (glassStyle.isDark) 0.76f else 0.90f)
        }

        val specularTopAlpha = when {
            isUltraSheer -> ((if (glassStyle.isDark) 0.16f else 0.36f) + pressBoost).coerceAtMost(0.60f)
            else -> ((if (isFilledAccent) 0.48f else if (glassStyle.isDark) 0.30f else 0.82f) + pressBoost).coerceAtMost(0.95f)
        }
        val specularMidAlpha = when {
            isUltraSheer -> if (glassStyle.isDark) 0.02f else 0.05f
            isFilledAccent -> 0.10f
            glassStyle.isDark -> 0.06f
            else -> 0.18f
        }
        val bottomBounceAlpha = (if (isUltraSheer) 0.10f else if (isFilledAccent) 0.28f else 0.18f) + pressBoost * 0.5f

        val prismaticSecondary = glassStyle.accentSecondary.copy(alpha = if (isFilledAccent) 0.55f else 0.38f)
        val rimStrokeBrush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = if (glassStyle.isDark) 0.82f else 0.96f),
                Color.White.copy(alpha = if (glassStyle.isDark) 0.20f else 0.48f),
                resolvedTint.copy(alpha = if (isFilledAccent) 0.78f else 0.48f),
                prismaticSecondary,
                Color.White.copy(alpha = if (glassStyle.isDark) 0.48f else 0.74f)
            ),
            start = Offset.Zero,
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )

        val shadowModifier = if (useInteriorShadow && elevation > 0.dp && !glassStyle.isNeumorphic) {
            Modifier.shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = resolvedTint.copy(alpha = if (glassStyle.isDark) 0.38f else 0.16f),
                spotColor = resolvedTint.copy(alpha = if (glassStyle.isDark) 0.45f else 0.22f)
            )
        } else {
            Modifier
        }

        this
            .then(shadowModifier)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(listOf(lensBase, lensMid, lensBottom)),
                shape = shape
            )
            .drawBehind {
                val outline = shape.createOutline(size, layoutDirection, this)
                val clipShapePath = Path().apply {
                    when (outline) {
                        is Outline.Rectangle -> addRect(outline.rect)
                        is Outline.Rounded -> addRoundRect(outline.roundRect)
                        is Outline.Generic -> addPath(outline.path)
                    }
                }

                // Crisp top rim pinpoint catchlight strictly on the upper border edge (never behind words)
                clipPath(clipShapePath) {
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = (specularTopAlpha * 0.85f).coerceAtMost(0.80f)),
                                Color.Transparent
                            )
                        ),
                        start = Offset(size.width * 0.12f, 0.9.dp.toPx()),
                        end = Offset(size.width * 0.88f, 0.9.dp.toPx()),
                        strokeWidth = 0.9.dp.toPx()
                    )
                }

                // Prismatic chromatic glass bevel border
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
 * Apple iOS 26 Liquid Glass Squircle / Circular Icon Badge.
 */
@Composable
fun IosLiquidGlassIconBadge(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    iconSize: Dp = 20.dp,
    tintColor: Color = LocalLiquidGlass.current.accentPrimary,
    shape: Shape = RoundedCornerShape(13.dp),
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
                elevation = if (glassStyle.iosLiquidGlassEnabled) 4.dp else 0.dp
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
 * Interactive Apple iOS 26 Liquid Glass Icon Button with spring tactile bounce & 48dp accessible hit area.
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
        targetValue = if (isPressed && enabled) 0.89f else 1f,
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
                    elevation = if (glassStyle.iosLiquidGlassEnabled && enabled) 5.dp else 0.dp
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
 * Interactive Apple iOS 26 Liquid Glass Capsule / Pill Button.
 */
@Composable
fun IosLiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = LocalLiquidGlass.current.accentPrimary,
    contentColor: Color = Color.Unspecified,
    isFilledAccent: Boolean = true,
    shape: Shape = RoundedCornerShape(18.dp),
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
                elevation = if (glassStyle.iosLiquidGlassEnabled && enabled) 7.dp else 0.dp
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

/**
 * Apple iOS 26 Spotlight-style Capsule Frosted Search Field.
 */
@Composable
fun IosLiquidGlassSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    onSearchDone: (() -> Unit)? = null
) {
    val glassStyle = LocalLiquidGlass.current
    val pillShape = RoundedCornerShape(24.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .iosLiquidGlassSurface(
                shape = pillShape,
                tintColor = glassStyle.accentPrimary,
                isFilledAccent = false,
                elevation = if (glassStyle.iosLiquidGlassEnabled) 4.dp else 0.dp
            )
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = if (query.isNotEmpty()) glassStyle.accentPrimary else glassStyle.textTertiary,
            modifier = Modifier.size(19.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    color = glassStyle.textTertiary,
                    fontSize = 13.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = HyperDisplayFontFamily,
                    color = glassStyle.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(glassStyle.accentPrimary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearchDone?.invoke() }),
                modifier = textFieldModifier.fillMaxWidth()
            )
        }

        if (query.isNotEmpty()) {
            Spacer(modifier = Modifier.width(6.dp))
            IosLiquidGlassIconButton(
                onClick = { onQueryChange("") },
                icon = Icons.Default.Clear,
                contentDescription = "Clear",
                visualSize = 30.dp,
                iconSize = 15.dp,
                tintColor = glassStyle.textTertiary,
                shape = CircleShape
            )
        }
    }
}

/**
 * Fluid iOS 26 Segmented Picker with a spring-animated Liquid Glass droplet indicator
 * that smoothly glides behind the active selection.
 */
@Composable
fun IosLiquidGlassSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    optionModifier: (Int) -> Modifier = { Modifier }
) {
    if (options.isEmpty()) return
    val glassStyle = LocalLiquidGlass.current
    val density = LocalDensity.current
    val outerShape = RoundedCornerShape(18.dp)
    val thumbShape = RoundedCornerShape(14.dp)

    val clampedSelected = selectedIndex.coerceIn(0, options.lastIndex)
    val thumbIndexAnim = remember { Animatable(clampedSelected.toFloat()) }

    LaunchedEffect(clampedSelected) {
        thumbIndexAnim.animateTo(
            targetValue = clampedSelected.toFloat(),
            animationSpec = spring(
                dampingRatio = 0.72f,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(outerShape)
                .background(
                    if (glassStyle.isDark) glassStyle.cardSubtleBg.copy(alpha = 0.85f)
                    else glassStyle.cardSubtleBg.copy(alpha = 0.90f)
                )
                .drawBehind {
                    drawOutline(
                        outline = outerShape.createOutline(size, layoutDirection, this),
                        color = glassStyle.borderColor.copy(alpha = 0.5f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
                .padding(4.dp)
        ) {
            val totalInnerWidthPx = with(density) { maxWidth.toPx() }
            val slotWidthPx = totalInnerWidthPx / options.size.toFloat()
            val slotWidthDp = with(density) { slotWidthPx.toDp() }
            val currentOffsetPx = thumbIndexAnim.value * slotWidthPx

            // Fluid sliding optical thumb outline (no murky highlight behind words)
            Box(
                modifier = Modifier
                    .offset { IntOffset(currentOffsetPx.roundToInt(), 0) }
                    .width(slotWidthDp)
                    .fillMaxHeight()
                    .iosLiquidGlassSurface(
                        shape = thumbShape,
                        tintColor = glassStyle.accentPrimary,
                        isFilledAccent = false,
                        elevation = 0.dp,
                        bodyAlphaOverride = if (glassStyle.isDark) 0.10f else 0.30f,
                        useInteriorShadow = false
                    )
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEachIndexed { index, label ->
                    val isSelected = (index == clampedSelected)
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) glassStyle.textPrimary else glassStyle.textSecondary,
                        animationSpec = tween(180),
                        label = "seg_text_color"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .defaultMinSize(minHeight = 48.dp)
                            .clip(thumbShape)
                            .clickable { onSelect(index) }
                            .then(optionModifier(index)),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = label,
                            transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(120)) },
                            label = "seg_label"
                        ) { target ->
                            Text(
                                text = target,
                                color = textColor,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Apple iOS 26 Liquid Glass Pill Switch with elongated optical droplet thumb & bouncy spring motion.
 */
@Composable
fun IosLiquidGlassSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = LocalLiquidGlass.current.accentPrimary
) {
    val glassStyle = LocalLiquidGlass.current
    val thumbOffsetDp by animateDpAsState(
        targetValue = if (checked) 24.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = 0.65f,
            stiffness = Spring.StiffnessMedium
        ),
        label = "ios_switch_offset"
    )
    val trackColor by animateColorAsState(
        targetValue = if (checked) accentColor else glassStyle.progressTrackBg,
        animationSpec = tween(220),
        label = "ios_switch_track"
    )

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .defaultMinSize(minWidth = 56.dp, minHeight = 48.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    role = Role.Switch,
                    onClick = { onCheckedChange(!checked) }
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 56.dp, height = 32.dp)
                    .iosLiquidGlassSurface(
                        shape = RoundedCornerShape(16.dp),
                        tintColor = trackColor,
                        isFilledAccent = checked,
                        elevation = if (checked && glassStyle.iosLiquidGlassEnabled) 4.dp else 0.dp
                    )
                    .padding(3.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // Optical frosted droplet knob
                Box(
                    modifier = Modifier
                        .offset(x = thumbOffsetDp)
                        .size(width = 26.dp, height = 26.dp)
                        .shadow(elevation = 3.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.White,
                                    if (glassStyle.isDark) Color(0xFFE2E8F0) else Color(0xFFF8FAFC)
                                )
                            )
                        )
                        .drawBehind {
                            drawCircle(
                                color = Color.White.copy(alpha = 0.9f),
                                style = Stroke(width = 0.8.dp.toPx())
                            )
                        }
                )
            }
        }
    }
}

/**
 * Apple iOS Settings-style uppercase/clean section eyebrow header.
 */
@Composable
fun IosLiquidGlassSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null
) {
    val glassStyle = LocalLiquidGlass.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = glassStyle.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.7.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (trailing != null) {
            trailing()
        }
    }
}

/**
 * Hairline refractive inset divider inside grouped Apple iOS Liquid Glass panels.
 */
@Composable
fun IosLiquidGlassInsetDivider(
    modifier: Modifier = Modifier,
    startIndent: Dp = 52.dp
) {
    val glassStyle = LocalLiquidGlass.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = startIndent)
            .height(0.8.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        glassStyle.borderColor.copy(alpha = if (glassStyle.isDark) 0.35f else 0.45f),
                        glassStyle.borderColor.copy(alpha = 0.08f)
                    )
                )
            )
    )
}
