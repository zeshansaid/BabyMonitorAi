package io.getstream.webrtc.sample.compose.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R

// Set of Material typography styles to start with
val Typography = Typography(
  body1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
  )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
     */
)


val robotoFont = FontFamily(
  Font(R.font.roboto_regular, FontWeight.Normal)
)

//val lexendFont = FontFamily(
//  Font(R.font.lexend_regular,  FontWeight.Normal),
//  Font(R.font.lexend_bold,  FontWeight.Bold),
//  Font(R.font.lexend_extrabold,  FontWeight.ExtraBold)
//)