package me.rerere.xland.ui.component.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import coil.compose.AsyncImage
import me.rerere.xland.data.model.Post

@Composable
fun PostCard(
    post: Post,
    showReply: Boolean = false,
    onClick: () -> Unit
) {
    MultiStyleCard(
        style = CardStyle.Filled,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 标题
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = post.nick,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = "No.${post.tid}",
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            // 内容
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodySmall
            )

            post.image?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }

            // 信息
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                    Text(
                        text = post.date,
                        maxLines = 1
                    )
                    Text(
                        text = "UID:${post.uid}",
                        maxLines = 1
                    )
                }
            }

            if(showReply) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 12.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("回复:")
                        post.reply.fastForEach {
                            Text(
                                text = "${it.uid}: ${it.content}",
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}