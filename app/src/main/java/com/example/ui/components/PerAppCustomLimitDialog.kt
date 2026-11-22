package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.AppUsageItem
import com.example.ui.screens.AppIconImage
import com.example.ui.theme.HyperMonoFontFamily
import com.example.ui.theme.LocalLiquidGlass

/**
 * Harmonious Apple iOS 26 Liquid Glass (with automatic Neumorphism parity) Dialog
 * for customizing per-app daily limit and alert settings on long-press.
 */
@Composable
fun PerAppCustomLimitDialog(
    appItem: AppUsageItem,
    initialDailyLimitGB: Float,
    initialAlertEnabled: Boolean,
    appLanguage: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (dailyLimitGB: Float, alertEnabled: Boolean) -> Unit
) {
    val glassStyle = LocalLiquidGlass.current
    val haptics = rememberAppHaptics()

    var dailyLimit by remember(initialDailyLimitGB) { mutableStateOf(initialDailyLimitGB) }
    var alertEnabled by remember(initialAlertEnabled) { mutableStateOf(initialAlertEnabled) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        GlassContainer(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .widthIn(max = 420.dp),
            shape = RoundedCornerShape(30.dp),
            borderColor = glassStyle.accentPrimary.copy(alpha = 0.50f),
            contentPadding = PaddingValues(20.dp)
        ) {
            Column {
                // Header with App Icon and Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppIconImage(drawable = appItem.icon, appName = appItem.appName)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = appItem.appName,
                                color = glassStyle.textPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = appItem.packageName,
                                color = glassStyle.textTertiary,
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
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

                Spacer(modifier = Modifier.height(16.dp))

                // Custom Daily Limit Frosted Well
                val sectionShape = RoundedCornerShape(18.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(sectionShape)
                        .background(glassStyle.cardSubtleBg)
                        .border(0.9.dp, glassStyle.borderColor.copy(alpha = 0.55f), sectionShape)
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "الحد اليومي المخصص" else "Custom Daily Limit",
                                color = glassStyle.textSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IosLiquidGlassIconButton(
                                    onClick = {
                                        haptics.performSegmentSwitch()
                                        dailyLimit = (dailyLimit - 0.5f).coerceAtLeast(0.5f)
                                    },
                                    icon = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    visualSize = 34.dp,
                                    iconSize = 17.dp,
                                    tintColor = glassStyle.accentPrimary
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                val formattedLimit = if (dailyLimit % 1f == 0f) "${dailyLimit.toInt()} GB" else "%.1f GB".format(dailyLimit)
                                Text(
                                    text = formattedLimit,
                                    fontFamily = HyperMonoFontFamily,
                                    color = glassStyle.accentPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                IosLiquidGlassIconButton(
                                    onClick = {
                                        haptics.performSegmentSwitch()
                                        dailyLimit = (dailyLimit + 0.5f).coerceAtMost(50f)
                                    },
                                    icon = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    visualSize = 34.dp,
                                    iconSize = 17.dp,
                                    tintColor = glassStyle.accentPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Preset Capsules
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            listOf(1.0f, 2.0f, 3.0f, 5.0f).forEach { preset ->
                                val isSelected = (kotlin.math.abs(dailyLimit - preset) < 0.05f)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .defaultMinSize(minHeight = 38.dp)
                                        .iosLiquidGlassSurface(
                                            shape = RoundedCornerShape(12.dp),
                                            tintColor = if (isSelected) glassStyle.accentPrimary else glassStyle.textSecondary,
                                            isFilledAccent = isSelected,
                                            elevation = if (isSelected && glassStyle.iosLiquidGlassEnabled) 3.dp else 0.dp
                                        )
                                        .clickable {
                                            haptics.performSegmentSwitch()
                                            dailyLimit = preset
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${preset.toInt()} GB",
                                        fontFamily = HyperMonoFontFamily,
                                        color = if (isSelected) glassStyle.onAccentPrimary else glassStyle.textSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Toggle Notification Card with IosLiquidGlassSwitch
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(sectionShape)
                        .background(glassStyle.cardSubtleBg)
                        .border(0.9.dp, glassStyle.borderColor.copy(alpha = 0.55f), sectionShape)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "تفعيل إشعار التجاوز للتطبيق" else "Enable Alerts for this App",
                                color = glassStyle.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC)
                                    "إرسال تنبيه فوري عند بلوغ هذا التطبيق حده اليومي"
                                else
                                    "Send instant alert when this app consumes custom quota",
                                color = glassStyle.textTertiary,
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IosLiquidGlassSwitch(
                            checked = alertEnabled,
                            onCheckedChange = {
                                haptics.performSegmentSwitch()
                                alertEnabled = it
                            },
                            accentColor = glassStyle.accentPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons (Cancel / Save)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IosLiquidGlassButton(
                        onClick = onDismiss,
                        containerColor = glassStyle.cardSubtleBg,
                        contentColor = glassStyle.textSecondary,
                        isFilledAccent = false,
                        shape = RoundedCornerShape(16.dp),
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
                            haptics.performConfirm()
                            onSave(dailyLimit, alertEnabled)
                            onDismiss()
                        },
                        containerColor = glassStyle.accentPrimary,
                        contentColor = glassStyle.onAccentPrimary,
                        isFilledAccent = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = glassStyle.onAccentPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (appLanguage == AppLanguage.ARABIC) "حفظ التخصيص" else "Save Limit",
                            color = glassStyle.onAccentPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
