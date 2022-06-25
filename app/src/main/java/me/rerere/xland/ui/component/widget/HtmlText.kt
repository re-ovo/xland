package me.rerere.xland.ui.component.widget

import android.graphics.Typeface
import android.os.Build.VERSION.SDK_INT
import android.text.Html
import android.text.Spanned
import android.text.style.*
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.core.text.getSpans

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
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val annotatedString = if (SDK_INT <24) {
        Html.fromHtml(text)
    } else {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    }.toAnnotatedString(urlSpanStyle)

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { pos ->
                layoutResult.value?.let { layoutResult ->
                    val position = layoutResult.getOffsetForPosition(pos)
                    annotatedString.getStringAnnotations(position, position)
                        .firstOrNull()
                        ?.let { sa ->
                            if (sa.tag == "url") { // NON-NLS
                                kotlin.runCatching {
                                    uriHandler.openUri(sa.item)
                                }.onFailure {
                                    Toast.makeText(context, "打开失败: ${sa.item}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }
            })
        },
        text = annotatedString,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        },
        style = style
    )
}

fun CharSequence.toAnnotatedString(
    urlSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline
    )
): AnnotatedString {
    return if (this is Spanned) {
        this.toAnnotatedString(urlSpanStyle)
    } else {
        buildAnnotatedString {
            append(this@toAnnotatedString.toString())
        }
    }
}

fun Spanned.toAnnotatedString(
    urlSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline
    )
): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString.toString())
        val urlSpans = getSpans<URLSpan>()
        val styleSpans = getSpans<StyleSpan>()
        val colorSpans = getSpans<ForegroundColorSpan>()
        val underlineSpans = getSpans<UnderlineSpan>()
        val strikethroughSpans = getSpans<StrikethroughSpan>()
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