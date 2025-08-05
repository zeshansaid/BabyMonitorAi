package com.example.babyapplication.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SkipTutorialButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Skip Tutorial")
    }
}
