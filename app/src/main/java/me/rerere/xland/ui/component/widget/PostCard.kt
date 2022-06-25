package me.rerere.xland.ui.component.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import me.rerere.xland.data.model.Post

@Composable
fun PostCard(
    post: Post,
    showReply: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = post.user_hash
                )

                Text(
                    text = post.fName
                )

                Text(
                    text = post.now
                )
            }

            // content
            ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                HtmlText(
                    text = post.content
                )
            }

            // 回复预览
            if (showReply) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val textSpinner by produceState(initialValue = "...") {
                       while (true) {
                            post.Replies.forEach {
                                value = it.content
                                delay(1500)
                            }
                        }
                    }
                    AnimatedContent(
                        targetState = textSpinner,
                        transitionSpec = {
                            slideInVertically(
                                initialOffsetY = { -it },
                                animationSpec = tween()
                            ) with slideOutVertically(
                                animationSpec = tween(),
                                targetOffsetY = { it }
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                            HtmlText(
                                text = it,
                                maxLines = 1
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Outlined.Comment,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                        Text(post.ReplyCount.toString())
                    }
                }
            }
        }
    }
}