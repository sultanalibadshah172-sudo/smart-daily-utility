package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

enum class ToolScreen {
    HOME,
    PERCENTAGE,
    DISCOUNT,
    PROFIT,
    AGE,
    UNIT_CONVERTER
}

private data class ToolCardItem(
    val screen: ToolScreen,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTint: Color,
    val tag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTool: (ToolScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val tools = listOf(
        ToolCardItem(
            screen = ToolScreen.PERCENTAGE,
            title = "Percentage Calculator",
            description = "Percent of values, ratios, and percentage changes",
            icon = Icons.Default.Percent,
            iconBgColor = MaterialTheme.colorScheme.primaryContainer,
            iconTint = MaterialTheme.colorScheme.primary,
            tag = "tool_percentage_card"
        ),
        ToolCardItem(
            screen = ToolScreen.DISCOUNT,
            title = "Discount Calculator",
            description = "Final price, discount savings, and sale amounts",
            icon = Icons.Default.LocalOffer,
            iconBgColor = MaterialTheme.colorScheme.secondaryContainer,
            iconTint = MaterialTheme.colorScheme.secondary,
            tag = "tool_discount_card"
        ),
        ToolCardItem(
            screen = ToolScreen.PROFIT,
            title = "Profit Calculator",
            description = "Cost & selling price margins, markup & target pricing",
            icon = Icons.Default.TrendingUp,
            iconBgColor = Color(0xFFDCFCE7),
            iconTint = Color(0xFF16A34A),
            tag = "tool_profit_card"
        ),
        ToolCardItem(
            screen = ToolScreen.AGE,
            title = "Age Calculator",
            description = "Exact age in years, months, days & upcoming birthdays",
            icon = Icons.Default.CalendarToday,
            iconBgColor = Color(0xFFFEF3C7),
            iconTint = Color(0xFFD97706),
            tag = "tool_age_card"
        ),
        ToolCardItem(
            screen = ToolScreen.UNIT_CONVERTER,
            title = "Unit Converter",
            description = "Length, weight, and temperature unit conversions",
            icon = Icons.Default.SwapHoriz,
            iconBgColor = Color(0xFFEDE9FE),
            iconTint = Color(0xFF7C3AED),
            tag = "tool_converter_card"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.tagline),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ 100% Offline • Lightweight • No Ads or Tracking",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            items(tools.size) { index ->
                val tool = tools[index]
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(tool.tag)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onNavigateToTool(tool.screen) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(tool.iconBgColor)
                        ) {
                            Icon(
                                imageVector = tool.icon,
                                contentDescription = tool.title,
                                tint = tool.iconTint,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tool.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tool.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open ${tool.title}",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
