package com.example.faircon.framework.presentation.components

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.faircon.framework.presentation.theme.blue400
import java.math.BigDecimal

@Composable
fun ProfileDetailText(
    modifier: Modifier = Modifier,
    text: String,
) {
    MyText(
        modifier = modifier,
        text = text,
        fontSize = 14.sp,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
fun MyValueText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 14.sp,
) {
    MyText(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        style = MaterialTheme.typography.h5
    )
}

@Composable
fun MyOverlineText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 16.sp,
) {
    MyText(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        style = MaterialTheme.typography.overline
    )
}

@Composable
fun MyLinkText(
    modifier: Modifier = Modifier,
    text: String
) {
    MyText(
        modifier = modifier,
        text = text,
        color = blue400,
        fontSize = 12.sp,
        style = MaterialTheme.typography.overline
    )
}

@Composable
fun MyText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colors.onBackground,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        overflow = overflow,
        maxLines = maxLines,
        style = style
    )
}