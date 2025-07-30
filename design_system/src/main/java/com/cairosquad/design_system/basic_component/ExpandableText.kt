package com.cairosquad.design_system.basic_component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.cairosquad.design_system.R

@Composable
fun ExpandableText(
    text: String,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier,
    collapsedMaxLine: Int = 4,
    showMoreText: String = stringResource(R.string.more_with_dotes_behind),
    showMoreStyle: TextStyle =TextStyle(fontWeight = FontWeight.W500),
    showMoreColor: Color = color,
    showLessText: String =stringResource(R.string.less_with_dotes_behind),
    showLessStyle: TextStyle = showMoreStyle,
    showLessColor: Color = color,
    textAlign: TextAlign? = null,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier.clickable(enabled = isOverflowing) {
            isExpanded = !isExpanded
        }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = tween(300)),
            text = buildAnnotatedString {
                if (isOverflowing) {
                    if (isExpanded) {
                        append(text)
                        withStyle(
                            style = showLessStyle.toSpanStyle().copy(color = showLessColor)
                        ) {
                            append(showLessText)
                        }
                    } else {
                        val showMoreLength = showMoreText.length + 3
                        val safeEnd = (lastCharIndex - showMoreLength).coerceAtMost(text.length).coerceAtLeast(0)
                        val visibleText = text.substring(0, safeEnd)
                            .dropLastWhile { it.isWhitespace() || it == '.' }
                        append(visibleText)

                        withStyle(
                            style = showMoreStyle.toSpanStyle().copy(color = showMoreColor)
                        ) {
                            append(showMoreText)
                        }
                    }
                } else {
                    append(text)
                }
            },
            color = color,
            style = style,
            textAlign = textAlign,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            onTextLayout = {
                if (!isExpanded && it.hasVisualOverflow) {
                    isOverflowing = true
                    lastCharIndex = it.getLineEnd(collapsedMaxLine - 1)
                }
            }
        )
    }
}
