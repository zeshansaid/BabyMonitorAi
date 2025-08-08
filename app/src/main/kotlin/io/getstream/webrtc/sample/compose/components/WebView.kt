package io.getstream.webrtc.sample.compose.components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPageView(url: String, modifier: Modifier = Modifier) {
  Scaffold { innerPadding ->
    AndroidView(
      modifier = modifier
          .fillMaxSize()
          .padding(innerPadding),
      factory = { context ->
        WebView(context).apply {
          webViewClient = WebViewClient()
          settings.javaScriptEnabled = true
          loadUrl(url)
        }
      }
    )
  }
}
