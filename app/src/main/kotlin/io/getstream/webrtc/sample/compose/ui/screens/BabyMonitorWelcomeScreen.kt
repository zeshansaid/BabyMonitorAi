package io.getstream.webrtc.sample.compose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.ui.theme.appColorScheme
import androidx.compose.material.icons.filled.Bolt

@Composable
fun BabyMonitorWelcomeScreen(
    skipClicked: Boolean,
    onSkip: () -> Unit,
    onStartTutorial: () -> Unit
) {
    val colors = appColorScheme()
    val boltColor = if (colors.background == Color(0xFF121212)) Color(0xFFFFEB3B) else colors.primary

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            BottomAppBar(
                containerColor = colors.background
            ) {
                Button(
                    onClick = onStartTutorial,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp)
                ) {
                    Text(
                        "Get Started",
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colors.background)
        ) {
            // Top Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baby_sleep),
                    contentDescription = "Sleeping Baby",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Bottom Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Bold heading
                Text(
                    text = "Welcome! Turn your device into",
                    color = colors.onBackground,
                    style = MaterialTheme.typography.titleLarge
                )

                // Primary bold line
                Text(
                    text = "baby monitor",
                    color = colors.primary,
                    style = MaterialTheme.typography.titleLarge
                )

                // Medium description
                Text(
                    text = "Use any phone, tablet or laptop to have a\nreliable baby monitor, always at hand.",
                    color = colors.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )

                // Bolt info line (Medium)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            color = colors.secondary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Bolt Icon",
                            tint = boltColor,
                            modifier = Modifier
                                .padding(end = 8.dp, top = 4.dp)
                                .size(25.dp)
                        )

                        Text(
                            text = "Fast and easy setup in just 3 minutes\nor less.",
                            color = colors.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                if (skipClicked) {
                    Text(
                        "Tutorial skipped!",
                        color = colors.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

