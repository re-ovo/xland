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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import me.rerere.slantedtext.SlantedMode
import me.rerere.slantedtext.SlantedText
import me.rerere.xland.data.model.Post
import me.rerere.xland.data.model.Ref
import me.rerere.xland.util.TimeUtil

enum class PostCardType {
    Preview,
    Detail
}

@Composable
fun PostCard(
    post: Post,
    type: PostCardType,
    onRequestRef: suspend (Long) -> Ref? = { null },
    onClick: (() -> Unit)? = null
) {
    SlantedText(
        text = "SAGE",
        visible = post.sage == 1,
        backGroundColor = MaterialTheme.colorScheme.secondary,
        slantedMode = SlantedMode.TOP_RIGHT,
        padding = 16.dp,
        thickness = 10.dp,
        textSize = 15.sp
    ) {
        val content = remember(post) {
            movableContentOf {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // info
                    ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = post.user_hash,
                                color = if (post.admin == 1) Color.Red else Color.Unspecified
                            )

                            Text(
                                text = post.fName ?: "No.${post.id}"
                            )

                            Text(
                                text = TimeUtil.convertTimeToBetterFormat(post.now)
                            )
                        }
                    }

                    // title
                    if(post.title != "?????????") {
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    // content
                    ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                        HtmlText(
                            text = post.content,
                            maxLines = if (type == PostCardType.Preview) 5 else Int.MAX_VALUE,
                            onRequestRef = onRequestRef
                        )
                    }

                    if(post.img.isNotEmpty()) {
                        ExpandableImage(path = post.img, ext = post.ext)
                    }

                    // ????????????
                    if (type == PostCardType.Preview) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val textSpinner by produceState(initialValue = "...") {
                                while (isActive) {
                                    post.Replies.forEach {
                                        value = it.content
                                        delay(1500)
                                    }
                                    delay(1500)
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
        if (onClick != null) {
            Card(
                onClick = onClick
            ) {
                content()
            }
        } else {
            Card {
                content()
            }
        }
    }
}