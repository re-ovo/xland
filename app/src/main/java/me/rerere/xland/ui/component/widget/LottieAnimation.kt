package me.rerere.xland.ui.component.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import me.rerere.xland.R

@Composable
fun ErrorAnimation(
    modifier: Modifier,
    throwable: Throwable,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val res by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(R.raw.circle_chart)
            )
            LottieAnimation(
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .aspectRatio(1f),
                composition = res,
                iterations = LottieConstants.IterateForever
            )
            Text("加载出错啦 (|||ﾟдﾟ)")
            Text(throwable.message ?: throwable.javaClass.simpleName)
        }
    }
}