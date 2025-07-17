package com.cairosquad.design_system.basic_component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun ExpandableText(
    text: String,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    collapsedMaxLine: Int = 4,
    showMoreText: String = "... Show More",
    showMoreStyle: TextStyle = TextStyle(fontWeight = FontWeight.W500),
    showMoreColor: Color = color,
    showLessText: String = " Show Less",
    showLessStyle: TextStyle = showMoreStyle,
    showLessColor: Color = color,
    textAlign: TextAlign? = null,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .clickable(clickable) {
                isExpanded = !isExpanded
            }
            .then(modifier)
    ) {
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(300)
                ),
            text = buildAnnotatedString {
                if (clickable) {
                    if (isExpanded) {
                        append(text)
                        withStyle(
                            style = showLessStyle.toSpanStyle().copy(color = showLessColor)
                        ) {
                            append(showLessText)
                        }
                    } else {
                        val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMoreText.length)
                            .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                        append(adjustText)
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
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                }
            },
            style = style,
            textAlign = textAlign,
        )
    }
}


@Preview(device = "spec:width=200dp,height=891dp")
@Composable
fun ExpandableTextPreview() {
    MovioTheme {
        Box(
            Modifier
                .background(Theme.color.surfaces.surface)
                .fillMaxSize(),
        ) {
            ExpandableText(
                text = "Pulled to the far side of the galaxy, where the Federation is 75 years away at maximum warp speed, a Starfleet ship must cooperate with Maquis rebels to find a way home.",
                color = Theme.color.surfaces.onSurface,
                style = Theme.textStyle.label.mediumMedium14,
                showLessStyle = Theme.textStyle.label.smallRegular14,
                showLessColor = Theme.color.surfaces.onSurfaceVariant,
                showMoreStyle = Theme.textStyle.label.smallRegular14,
                showMoreColor = Theme.color.surfaces.onSurfaceVariant,
            )
        }
    }
}