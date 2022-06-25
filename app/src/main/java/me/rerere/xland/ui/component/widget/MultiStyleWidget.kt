package me.rerere.xland.ui.component.widget

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class CardStyle {
    Elevated, Filled, Outlined
}

@Composable
fun MultiStyleCard(
    modifier: Modifier = Modifier,
    style: CardStyle = CardStyle.Filled,
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    when(style) {
        CardStyle.Elevated -> {
            ElevatedCard(
                modifier = modifier,
                onClick = onClick,
                content = content
            )
        }
        CardStyle.Filled -> {
            Card(
                modifier = modifier,
                onClick = onClick,
                content = content
            )
        }
        CardStyle.Outlined -> {
            OutlinedCard(
                modifier = modifier,
                onClick = onClick,
                content = content
            )
        }
    }
}