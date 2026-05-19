package com.example.wtcchallenge.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.simpleVerticalScrollbar(
    state: LazyListState,
    width: Dp = 6.dp,
    color: Color = Color.White
): Modifier = composed {
    val targetAlpha = if (state.isScrollInProgress) 0.7f else 0.3f
    val duration = if (state.isScrollInProgress) 150 else 800
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration),
        label = "scrollbar-alpha"
    )

    drawWithContent {
        drawContent()
        val totalItemsCount = state.layoutInfo.totalItemsCount
        val visibleItemsInfo = state.layoutInfo.visibleItemsInfo

        if (totalItemsCount > 0 && visibleItemsInfo.isNotEmpty()) {
            val firstVisibleItemIndex = state.firstVisibleItemIndex
            val visibleItemsCount = visibleItemsInfo.size

            if (visibleItemsCount < totalItemsCount) {
                val scrollbarHeight = size.height * (visibleItemsCount.toFloat() / totalItemsCount)
                val scrollbarOffsetY = size.height * (firstVisibleItemIndex.toFloat() / totalItemsCount)
                val widthPx = width.toPx()

                drawRoundRect(
                    color = color.copy(alpha = alpha),
                    topLeft = Offset(size.width - widthPx - 4f, scrollbarOffsetY),
                    size = Size(widthPx, scrollbarHeight),
                    cornerRadius = CornerRadius(widthPx / 2, widthPx / 2)
                )
            }
        }
    }
}
