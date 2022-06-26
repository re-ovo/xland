package me.rerere.xland.ui.component.widget

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.HtmlCompat
import androidx.core.text.getSpans
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val hiddenPattern = Regex("\\[h].+\\[/h]")

/**
 * Simple Text composable to show the text with html styling from a String.
 * Supported are:
 *
 * &lt;b>Bold&lt;/b>
 *
 * &lt;i>Italic&lt;/i>
 *
 * &lt;u>Underlined&lt;/u>
 *
 * &lt;strike>Strikethrough&lt;/strike>
 *
 * &lt;a href="https://google.de">Link&lt;/a>
 *
 * @see androidx.compose.material.Text
 *
 */
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
    style: TextStyle = LocalTextStyle.current
) {
    val scope = rememberCoroutineScope()

    // 从MD3的Text里抄来的，确保显示效果和MD3一致
    val textColor = style.color.takeOrElse {
        LocalContentColor.current
    }

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

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        style = style.merge(TextStyle(color = textColor)),
        onClick = { pos ->
            // 找到点击位置所有符合的span
            val spans = annotatedString.spanStyles.filter {
                pos in it.start .. it.end
            }
            // 点击的是隐藏的文本，则显示
            if(spans.any { it.item.background == Color(0xFF666666) }) {
                scope.launch {
                    showHiddenText = true
                    delay(2000)
                    showHiddenText = false
                }
            }
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
        val urlSpans = getSpans<URLSpan>()
        val styleSpans = getSpans<StyleSpan>()
        val colorSpans = getSpans<ForegroundColorSpan>()
        val backgroundColorSpan = getSpans<BackgroundColorSpan>()
        val underlineSpans = getSpans<UnderlineSpan>()
        val strikethroughSpans = getSpans<StrikethroughSpan>()
        append(this@toAnnotatedString.toString())
        urlSpans.forEach { urlSpan ->
            val start = getSpanStart(urlSpan)
            val end = getSpanEnd(urlSpan)
            addStyle(urlSpanStyle, start, end)
            addStringAnnotation("url", urlSpan.url, start, end) // NON-NLS
        }
        colorSpans.forEach { colorSpan ->
            val start = getSpanStart(colorSpan)
            val end = getSpanEnd(colorSpan)
            addStyle(SpanStyle(color = Color(colorSpan.foregroundColor)), start, end)
        }
        backgroundColorSpan.forEach {
            val start = getSpanStart(it)
            val end = getSpanEnd(it)
            addStyle(SpanStyle(background = Color(it.backgroundColor)), start, end)
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