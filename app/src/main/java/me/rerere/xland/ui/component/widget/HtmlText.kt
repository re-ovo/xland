package me.rerere.xland.ui.component.widget

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.*
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.core.text.getSpans
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.rerere.xland.data.model.Ref
import me.rerere.xland.util.TimeUtil
import me.rerere.xland.util.openUrl

private val hiddenPattern = Regex("\\[h].+\\[/h]")
private val referencePattern = Regex(">>No.\\d+")

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    text: String,
    urlSpanStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.secondary,
        textDecoration = TextDecoration.Underline),
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    onRequestRef: suspend (Long) -> Ref? = { null }
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // 是否显示隐藏的文字?
    var showHiddenText by remember(text) {
        mutableStateOf(false)
    }

    // 最终的AnnotatedString
    val annotatedString = remember(text, showHiddenText) {
        val internalText = hiddenPattern.replace(
            input = text,
            transform = { result ->
                val content = result.value.replace("[h]", "").replace("[/h]", "")
                if(showHiddenText) {
                    content
                } else {
                    "<span style=\"background: #666666; color: #666666\">$content</span>"
                }
            }
        )
        HtmlCompat.fromHtml(internalText, HtmlCompat.FROM_HTML_MODE_COMPACT)
            .toAnnotatedString(urlSpanStyle)
    }

    // Ref Dialog
    var showRefDialog by remember {
        mutableStateOf(false)
    }
    var currentRef by remember {
        mutableStateOf<Ref?>(null)
    }
    if(showRefDialog) {
        AlertDialog(
            onDismissRequest = { showRefDialog = false },
            title = {
                Text("引用 No.${currentRef?.id}")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${currentRef?.userHash} | ${TimeUtil.convertTimeToBetterFormat(currentRef?.now)}",
                    )
                    HtmlText(
                        text = currentRef?.content ?: ""
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRefDialog = false
                    }
                ) {
                    Text("确定")
                }
            }
        )
    }

    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val pressIndicator = Modifier.pointerInput(annotatedString) {
        forEachGesture {
            awaitPointerEventScope {
                val event = awaitPointerEvent()
                if(event.type == PointerEventType.Press) {
                    event.changes.first().position.let { offset ->
                        layoutResult.value?.let { layoutResult ->
                            val pos = layoutResult.getOffsetForPosition(offset)
                            // 处理点击文字
                            annotatedString.getStringAnnotations(pos, pos).forEach {
                                // 如果点击的是链接, 则打开链接
                                if(it.tag == "url") {
                                    event.changes.first().consume()
                                    context.openUrl(it.item)
                                }
                                // 点击隐藏的文字, 则显示隐藏的文字
                                if(it.tag == "hidden") {
                                    event.changes.first().consume()
                                    scope.launch {
                                        showHiddenText = true
                                        delay(2000)
                                        showHiddenText = false
                                    }
                                }
                                // 处理点击引用
                                if(it.tag == "reference") {
                                    event.changes.first().consume()
                                    scope.launch {
                                        val id = Regex("\\d+").find(it.item)?.value?.toLong() ?: 0L
                                        val ref = onRequestRef(id)
                                        ref?.let {
                                            currentRef = it
                                            showRefDialog = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    Text(
        modifier = modifier.then(pressIndicator),
        text = annotatedString,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        style = style,
        onTextLayout = {
            layoutResult.value = it
        }
    )
}

fun Spanned.toAnnotatedString(
    urlSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline
    )
): AnnotatedString {
    return buildAnnotatedString {
        val rawText = this@toAnnotatedString.toString()
        val urlSpans = getSpans<URLSpan>()
        val styleSpans = getSpans<StyleSpan>()
        val colorSpans = getSpans<ForegroundColorSpan>()
        val backgroundColorSpan = getSpans<BackgroundColorSpan>()
        val underlineSpans = getSpans<UnderlineSpan>()
        val strikethroughSpans = getSpans<StrikethroughSpan>()
        append(rawText)
        urlSpans.forEach { urlSpan ->
            val start = getSpanStart(urlSpan)
            val end = getSpanEnd(urlSpan)
            addStyle(urlSpanStyle, start, end)
            addStringAnnotation("url", urlSpan.url, start, end)
        }
        colorSpans.forEach { colorSpan ->
            val start = getSpanStart(colorSpan)
            val end = getSpanEnd(colorSpan)
            addStyle(SpanStyle(color = Color(colorSpan.foregroundColor)), start, end)
            if(rawText.substring(start, end).matches(referencePattern)) {
                addStringAnnotation("reference", rawText.substring(start, end), start, end)
            }
        }
        backgroundColorSpan.forEach {
            val start = getSpanStart(it)
            val end = getSpanEnd(it)
            addStyle(SpanStyle(background = Color(it.backgroundColor)), start, end)

            // handle hidden tag
            if(it.backgroundColor == Color(0xFF666666).toArgb()) {
                addStringAnnotation("hidden", "", start, end)
            }
        }
        styleSpans.forEach { styleSpan ->
            val start = getSpanStart(styleSpan)
            val end = getSpanEnd(styleSpan)
            when (styleSpan.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic), start, end)
            }
        }
        underlineSpans.forEach { underlineSpan ->
            val start = getSpanStart(underlineSpan)
            val end = getSpanEnd(underlineSpan)
            addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
        }
        strikethroughSpans.forEach { strikethroughSpan ->
            val start = getSpanStart(strikethroughSpan)
            val end = getSpanEnd(strikethroughSpan)
            addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), start, end)
        }
    }
}