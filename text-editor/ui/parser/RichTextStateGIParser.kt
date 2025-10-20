package voidthinking.texteditor.ui.parser

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import com.mohamedrejeb.richeditor.model.RichTextState
import voidthinking.texteditor.util.color.toHex

object RichTextStateGIParser {
    private data class SpanItem(
        val start: Int,
        val end: Int,
        val italic: Boolean = false,
        val color: Color = Color.Unspecified,
    ) {
        fun merge(
            start: Int,
            end: Int,
            style: SpanStyle,
        ) = SpanItem(
            start = start,
            end = end,
            italic = style.fontStyle?.let {
                it == FontStyle.Italic
            } ?: italic,
            color = style.color.takeIf { it.isSpecified } ?: color,
        )
    }

    fun decode(richTextState: RichTextState) = buildString {
        val annotatedString = richTextState.annotatedString
        val spanStyles = mutableListOf(SpanItem(start = 0, end = annotatedString.length))
        annotatedString.spanStyles.forEach { range ->
            val start = range.start
            val end = range.end

            val newSpans = mutableListOf<SpanItem>()
            for (span in spanStyles) {
                if (span.end <= start || span.start >= end) {
                    newSpans.add(span)
                } else {
                    if (span.start < start) {
                        newSpans.add(span.copy(start = span.start, end = start))
                    }
                    val overlapStart = maxOf(span.start, start)
                    val overlapEnd = minOf(span.end, end)
                    newSpans.add(span.merge(overlapStart, overlapEnd, range.item))
                    if (span.end > end) {
                        newSpans.add(span.copy(start = end, end = span.end))
                    }
                }
            }
            spanStyles.clear()
            spanStyles.addAll(newSpans)
        }

        val paragraphEnds = mutableListOf<Int>()
        var currentEnd = 0
        annotatedString.paragraphStyles.forEach { paragraph ->
            if (currentEnd < paragraph.start) {
                // Process a default paragraph
                paragraphEnds.add(paragraph.start)
            }
            paragraphEnds.add(paragraph.end)
            currentEnd = paragraph.end
        }
        if (currentEnd < annotatedString.length) {
            paragraphEnds.add(annotatedString.length)
        }

        var italic = false
        var color = Color.Unspecified
        var currentEndIndex = 0
        for (span in spanStyles) {
            if (span.italic != italic) {
                if (span.italic) {
                    append("<i>")
                } else {
                    append("</i>")
                }
                italic = span.italic
            }
            if (span.color != color) {
                if (color.isSpecified) {
                    append("</color>")
                }
                if (span.color.isSpecified) {
                    append("<color=#${span.color.toHex()}>")
                }
                color = span.color
            }
            var lastPartStart = span.start
            while (currentEndIndex < paragraphEnds.size) {
                val currentEnd = paragraphEnds[currentEndIndex]
                if (currentEnd > span.end) {
                    break
                }
                if (currentEnd > 0 && annotatedString[currentEnd - 1] == ' ' && currentEnd - lastPartStart > 0) {
                    append(annotatedString.subSequence(lastPartStart, currentEnd - 1))
                } else {
                    append(annotatedString.subSequence(lastPartStart, currentEnd))
                }
                if (currentEndIndex != paragraphEnds.size - 1) {
                    append("\\n")
                }
                lastPartStart = currentEnd.coerceAtMost(span.end)
                currentEndIndex++
            }
            append(annotatedString.subSequence(lastPartStart, span.end))
        }
        if (italic) {
            append("</i>")
        }
        if (color.isSpecified) {
            append("</color>")
        }
    }
}

fun RichTextState.toGI() = RichTextStateGIParser.decode(this)
