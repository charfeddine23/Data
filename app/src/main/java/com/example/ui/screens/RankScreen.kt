package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.AppUsageItem
import com.example.model.FormatUtils
import com.example.ui.MainViewModel
import com.example.ui.components.ElasticFlamePullToRefreshBox
import com.example.ui.components.GlassContainer
import com.example.ui.components.IosLiquidGlassIconBadge
import com.example.ui.components.IosLiquidGlassIconButton
import com.example.ui.components.UsagePermissionCard
import com.example.ui.components.iosLiquidGlassSurface
import com.example.ui.navigation.LocalFloatingChromePadding
import com.example.ui.theme.HyperMonoFontFamily
import com.example.ui.theme.LocalAppDimensions
import com.example.ui.theme.LocalLiquidGlass

private val GoldDark = Color(0xFFFFD700)
private val GoldLight = Color(0xFFD97706)
private val GoldAccent = Color(0xFFFFA000)
private val SilverDark = Color(0xFFE2E8F0)
private val SilverLight = Color(0xFF475569)
private val SilverAccent = Color(0xFF94A3B8)
private val BronzeDark = Color(0xFFF59E0B)
private val BronzeLight = Color(0xFFB45309)
private val BronzeAccent = Color(0xFF92400E)

@Composable
fun RankScreen(
    viewModel: MainViewModel,
    onRequestUsagePermission: () -> Unit,
    onOpenAppInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rankedApps by viewModel.allTimeRankedApps.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRankRefreshing.collectAsStateWithLifecycle()
    val hasUsagePerm by viewModel.hasUsagePermission.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    val glassStyle = LocalLiquidGlass.current
    val dimensions = LocalAppDimensions.current
    val chromePadding = LocalFloatingChromePadding.current

    ElasticFlamePullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshRankings() },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensions.screenPaddingHorizontal),
            contentPadding = PaddingValues(
                top = chromePadding.calculateTopPadding() + 6.dp,
                bottom = chromePadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                RankHeader(
                    appLanguage = appLanguage,
                    onRefresh = { viewModel.refreshRankings() }
                )
            }

            if (!hasUsagePerm) {
                item {
                    UsagePermissionCard(
                        onRequestPermission = onRequestUsagePermission,
                        onOpenAppInfo = onOpenAppInfo,
                        appLanguage = appLanguage
                    )
                }
            } else if (rankedApps.isEmpty()) {
                item {
                    GlassContainer(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = glassStyle.accentPrimary.copy(alpha = 0.45f),
                        enableBreathingGlow = true,
                        glowColor = glassStyle.accentPrimary,
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 32.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            IosLiquidGlassIconBadge(
                                icon = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                size = 58.dp,
                                iconSize = 30.dp,
                                tintColor = if (glassStyle.isDark) GoldDark else GoldLight,
                                shape = RoundedCornerShape(18.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "جاري احتساب استهلاك التطبيقات على مر التاريخ..." else "Calculating all-time application consumption...",
                                color = glassStyle.textPrimary,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = if (appLanguage == AppLanguage.ARABIC) "اسحب للأسفل للتحديث واحتساب النتائج فوراً" else "Pull down to refresh and compute rankings",
                                color = glassStyle.textTertiary,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // 1. Optical iOS 26 3-Tier Champions Podium
                item {
                    TopChampionsPodium(
                        apps = rankedApps.take(3),
                        appLanguage = appLanguage
                    )
                }

                // 2. Remaining Applications Leaderboard (#4+)
                if (rankedApps.size > 3) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IosLiquidGlassIconBadge(
                                    icon = Icons.Default.MilitaryTech,
                                    contentDescription = null,
                                    size = 28.dp,
                                    iconSize = 15.dp,
                                    tintColor = glassStyle.accentPrimary,
                                    shape = RoundedCornerShape(9.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = AppStrings.rankRemaining(appLanguage),
                                    color = glassStyle.textPrimary,
                                    fontSize = 15.sp,
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
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${rankedApps.size - 3}",
                                    fontFamily = HyperMonoFontFamily,
                                    color = glassStyle.accentPrimary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    val leaderBytes = rankedApps.first().totalBytes.coerceAtLeast(1L)
                    itemsIndexed(rankedApps.drop(3), key = { _, item -> item.packageName }) { index, item ->
                        val rankPosition = index + 4
                        RankedAppRow(
                            rank = rankPosition,
                            item = item,
                            leaderBytes = leaderBytes,
                            appLanguage = appLanguage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RankHeader(
    appLanguage: AppLanguage,
    onRefresh: () -> Unit
) {
    val glassStyle = LocalLiquidGlass.current
    val goldColor = if (glassStyle.isDark) GoldDark else GoldLight

    GlassContainer(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        borderColor = goldColor.copy(alpha = 0.40f),
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
                        icon = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        size = 42.dp,
                        iconSize = 22.dp,
                        tintColor = goldColor,
                        shape = RoundedCornerShape(13.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.rankTitle(appLanguage),
                                color = glassStyle.textPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(goldColor.copy(alpha = 0.16f))
                                    .border(0.8.dp, goldColor.copy(alpha = 0.45f), CircleShape)
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = AppStrings.rankAllTimeBadge(appLanguage),
                                    color = goldColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = AppStrings.rankSubtitle(appLanguage),
                            color = glassStyle.textSecondary,
                            fontSize = 11.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IosLiquidGlassIconButton(
                    onClick = onRefresh,
                    icon = Icons.Default.Refresh,
                    contentDescription = AppStrings.refresh(appLanguage),
                    visualSize = 38.dp,
                    iconSize = 19.dp,
                    tintColor = goldColor,
                    shape = CircleShape,
                    modifier = Modifier.testTag("refresh_rank_button")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(glassStyle.cardSubtleBg)
                    .border(0.8.dp, glassStyle.borderColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = AppStrings.rankTotalCombined(appLanguage),
                        color = glassStyle.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SignalCellularAlt, null, tint = glassStyle.accentPrimary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("+", color = glassStyle.textTertiary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(Icons.Default.Wifi, null, tint = glassStyle.accentSecondary, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

/**
 * Top 3 Champions Optical Glass Podium Showcase:
 * - Center: Rank 1 (Elevated Gold)
 * - Left: Rank 2 (Silver)
 * - Right: Rank 3 (Bronze)
 */
@Composable
fun TopChampionsPodium(
    apps: List<AppUsageItem>,
    appLanguage: AppLanguage
) {
    if (apps.isEmpty()) return
    val glassStyle = LocalLiquidGlass.current
    val goldColor = if (glassStyle.isDark) GoldDark else GoldLight
    val silverColor = if (glassStyle.isDark) SilverDark else SilverLight
    val bronzeColor = if (glassStyle.isDark) BronzeDark else BronzeLight

    GlassContainer(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        borderColor = goldColor.copy(alpha = 0.35f),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = goldColor,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = AppStrings.rankPodiumTitle(appLanguage),
                    color = goldColor,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                if (apps.size >= 2) {
                    PodiumPillar(
                        rank = 2,
                        app = apps[1],
                        pillarHeight = 132.dp,
                        accentColor = silverColor,
                        gradientColor = SilverAccent,
                        rankLabel = AppStrings.rankSecond(appLanguage),
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                if (apps.isNotEmpty()) {
                    PodiumPillar(
                        rank = 1,
                        app = apps[0],
                        pillarHeight = 162.dp,
                        accentColor = goldColor,
                        gradientColor = GoldAccent,
                        rankLabel = AppStrings.rankFirst(appLanguage),
                        modifier = Modifier.weight(1.16f)
                    )
                }

                if (apps.size >= 3) {
                    PodiumPillar(
                        rank = 3,
                        app = apps[2],
                        pillarHeight = 118.dp,
                        accentColor = bronzeColor,
                        gradientColor = BronzeAccent,
                        rankLabel = AppStrings.rankThird(appLanguage),
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun PodiumPillar(
    rank: Int,
    app: AppUsageItem,
    pillarHeight: Dp,
    accentColor: Color,
    gradientColor: Color,
    rankLabel: String,
    modifier: Modifier = Modifier
) {
    val glassStyle = LocalLiquidGlass.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Icon with Optical Champion Halo
        Box(
            modifier = Modifier
                .size(if (rank == 1) 52.dp else 44.dp)
                .iosLiquidGlassSurface(
                    shape = CircleShape,
                    tintColor = accentColor,
                    isFilledAccent = false,
                    elevation = if (glassStyle.iosLiquidGlassEnabled) 6.dp else 0.dp
                )
                .padding(4.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AppIconBitmap(drawable = app.icon, appName = app.appName)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = app.appName,
            color = glassStyle.textPrimary,
            fontSize = if (rank == 1) 12.5.sp else 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Text(
            text = FormatUtils.formatBytes(app.totalBytes),
            fontFamily = HyperMonoFontFamily,
            color = accentColor,
            fontSize = if (rank == 1) 12.sp else 10.5.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(7.dp))

        // Optical Glass Pedestal Pillar with top glossy edge highlight
        val pillarShape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 8.dp, bottomEnd = 8.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pillarHeight)
                .clip(pillarShape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            accentColor.copy(alpha = if (rank == 1) 0.28f else 0.18f),
                            gradientColor.copy(alpha = 0.06f)
                        )
                    )
                )
                .drawBehind {
                    if (glassStyle.iosLiquidGlassEnabled) {
                        drawLine(
                            brush = Brush.horizontalGradient(
                                listOf(Color.Transparent, Color.White.copy(alpha = 0.8f), Color.Transparent)
                            ),
                            start = Offset(size.width * 0.12f, 1.5.dp.toPx()),
                            end = Offset(size.width * 0.88f, 1.5.dp.toPx()),
                            strokeWidth = 1.4.dp.toPx()
                        )
                    }
                }
                .border(
                    1.dp,
                    accentColor.copy(alpha = if (rank == 1) 0.65f else 0.40f),
                    pillarShape
                )
                .padding(vertical = 10.dp, horizontal = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "#$rank",
                    fontFamily = HyperMonoFontFamily,
                    color = accentColor,
                    fontSize = if (rank == 1) 17.sp else 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = rankLabel,
                    color = glassStyle.textSecondary,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = FormatUtils.formatBytes(app.mobileBytes),
                        fontFamily = HyperMonoFontFamily,
                        color = glassStyle.accentPrimary,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Text(
                        text = FormatUtils.formatBytes(app.wifiBytes),
                        fontFamily = HyperMonoFontFamily,
                        color = glassStyle.textTertiary,
                        fontSize = 8.5.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun RankedAppRow(
    rank: Int,
    item: AppUsageItem,
    leaderBytes: Long,
    appLanguage: AppLanguage
) {
    val glassStyle = LocalLiquidGlass.current

    GlassContainer(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        contentPadding = PaddingValues(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .iosLiquidGlassSurface(
                            shape = RoundedCornerShape(11.dp),
                            tintColor = glassStyle.accentPrimary,
                            isFilledAccent = false,
                            elevation = 0.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#$rank",
                        fontFamily = HyperMonoFontFamily,
                        color = glassStyle.textSecondary,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(11.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AppIconBitmap(drawable = item.icon, appName = item.appName)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.appName,
                        color = glassStyle.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.packageName,
                        color = glassStyle.textTertiary,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = FormatUtils.formatBytes(item.totalBytes),
                        fontFamily = HyperMonoFontFamily,
                        color = glassStyle.accentPrimary,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "%.1f%%".format(item.percentageOfTotal * 100f),
                        fontFamily = HyperMonoFontFamily,
                        color = glassStyle.textTertiary,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CellTower,
                        contentDescription = null,
                        tint = glassStyle.accentPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${AppStrings.filterCellular(appLanguage)}: ${FormatUtils.formatBytes(item.mobileBytes)}",
                        fontFamily = HyperMonoFontFamily,
                        color = glassStyle.textSecondary,
                        fontSize = 10.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = null,
                        tint = glassStyle.accentSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${AppStrings.filterWifi(appLanguage)}: ${FormatUtils.formatBytes(item.wifiBytes)}",
                        fontFamily = HyperMonoFontFamily,
                        color = glassStyle.textSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(7.dp))

            val relativeFraction = (item.totalBytes.toFloat() / leaderBytes.toFloat()).coerceIn(0.01f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(glassStyle.progressTrackBg)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(relativeFraction)
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(glassStyle.accentSecondary, glassStyle.accentPrimary)
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun AppIconBitmap(drawable: Drawable?, appName: String) {
    val glassStyle = LocalLiquidGlass.current
    val bitmap = remember(drawable) {
        drawable?.let { d ->
            try {
                if (d is BitmapDrawable && d.bitmap != null) {
                    d.bitmap
                } else {
                    val w = d.intrinsicWidth.coerceAtLeast(1)
                    val h = d.intrinsicHeight.coerceAtLeast(1)
                    val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bmp)
                    d.setBounds(0, 0, canvas.width, canvas.height)
                    d.draw(canvas)
                    bmp
                }
            } catch (_: Exception) {
                null
            }
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = appName,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(glassStyle.cardSubtleBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = appName.take(1).uppercase(),
                color = glassStyle.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
