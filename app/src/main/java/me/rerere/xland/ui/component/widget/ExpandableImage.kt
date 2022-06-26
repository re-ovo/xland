package me.rerere.xland.ui.component.widget

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ExpandableImage(
    path: String,
    ext: String
) {
    var expand by remember {
        mutableStateOf(false)
    }
    AsyncImage(
        model = "https://image.nmb.fastmirror.org/${if(expand) "image" else "thumb"}/$path$ext",
        contentDescription = null,
        modifier = Modifier
            .animateContentSize()
            .clickable {
                expand = !expand
            }
            .then(
                if(expand) Modifier.fillMaxWidth() else Modifier.heightIn(max = 100.dp)
            ),
        contentScale = if(expand) ContentScale.FillWidth else ContentScale.Fit
    )
}