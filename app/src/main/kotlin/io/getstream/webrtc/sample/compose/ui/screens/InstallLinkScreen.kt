package io.getstream.webrtc.sample.compose.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.AnimatedPreloader
import io.getstream.webrtc.sample.compose.ui.theme.appColorScheme

@Composable
fun InstallLinkScreen(
    onContinue: () -> Unit
) {
    val colors = appColorScheme()
    val isDark = isSystemInDarkTheme()
    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                tonalElevation = 0.dp,
            ) {
                Button(
                    onClick = onContinue,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(48.dp)
                ) {
                    Text("Continue", color = colors.onPrimary, fontWeight = FontWeight.Bold,style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔼 Top 50% for animation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AnimatedPreloader(R.raw.baby_screen1)
            }

            // 🔽 Bottom 50% for text and tip box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Easily install & connect – ",
                    color = colors.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "just share the link!",
                    color = colors.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "The app is not installed on the second device? Just share a link for easy installation and connection!",
                    color = colors.onBackground.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )


                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(48.dp)
                        .background(
                            color = colors.secondary,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = if (isDark) Color.White else Color.Black,
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 8.dp)
                        )

                        Text(
                            text = " SHARE  LINK",
                            color = if (isDark) Color.White else Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }


            }
        }
    }
}
