package com.deepseek.meter.ui.login

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.deepseek.meter.ui.localization.LocalAppStrings
import com.deepseek.meter.ui.localization.S
import androidx.core.net.toUri
import kotlinx.coroutines.delay

private const val LOGIN_URL = "https://platform.deepseek.com/sign_in"
private const val TOKEN_HOOK = """
(function() {
  var key = 'userToken';
  function extract() {
    try {
      var raw = localStorage.getItem(key);
      if (!raw) return null;
      var parsed = JSON.parse(raw);
      if (parsed && typeof parsed.value === 'string') return parsed.value;
      if (parsed && typeof parsed.token === 'string') return parsed.token;
      if (typeof parsed === 'string') return parsed;
    } catch (ignored) {}
    return null;
  }
  function notify() {
    var token = extract();
    if (token && token.length > 20 && window.DeepSeekMeterBridge) {
      window.DeepSeekMeterBridge.onSessionToken(token);
    }
  }
  if (!window.__deepSeekMeterTokenHookInstalled) {
    window.__deepSeekMeterTokenHookInstalled = true;
    var originalSetItem = localStorage.setItem.bind(localStorage);
    localStorage.setItem = function(name, value) {
      originalSetItem(name, value);
      if (name === key) notify();
    };
    window.addEventListener('pageshow', notify, false);
    window.addEventListener('popstate', notify, false);
    document.addEventListener('visibilitychange', notify, false);
    setTimeout(notify, 100);
    setTimeout(notify, 1200);
  } else {
    notify();
  }
})();
"""

@Composable
fun LoginScreen(
    authenticating: Boolean,
    errorMessage: String?,
    onSessionToken: (String) -> Unit,
    onCancel: () -> Unit,
) {
    val strings = LocalAppStrings.current
    val webViewState = remember { mutableStateOf<WebView?>(null) }
    val showWebView = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(350)
        showWebView.value = true
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewState.value?.stopLoading()
            webViewState.value?.destroy()
            webViewState.value = null
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(strings[S.loginTitle], style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        Text(
                            strings[S.loginInstruction],
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (errorMessage != null) {
                            Spacer(Modifier.height(2.dp))
                            Text(errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    TextButton(onClick = onCancel) { Text(strings[S.back]) }
                }
            }

            if (authenticating) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularProgressIndicator(modifier = Modifier.height(22.dp))
                    Text(strings[S.validating], style = MaterialTheme.typography.bodyMedium)
                }
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (showWebView.value) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            WebView(context).apply {
                                webViewState.value = this
                                addJavascriptInterface(
                                    SessionTokenBridge({ webViewState.value }, onSessionToken),
                                    "DeepSeekMeterBridge",
                                )
                                configureSecureWebView(this)
                                loadUrl(LOGIN_URL)
                            }
                        },
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CircularProgressIndicator()
                        Text(strings[S.preparing], style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun configureSecureWebView(webView: WebView) {
    WebView.setWebContentsDebuggingEnabled(false)
    CookieManager.getInstance().apply {
        setAcceptCookie(true)
        setAcceptThirdPartyCookies(webView, true)
    }
    webView.settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = true
        cacheMode = WebSettings.LOAD_DEFAULT
        mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        allowFileAccess = false
        allowContentAccess = false
        setSupportMultipleWindows(true)
        javaScriptCanOpenWindowsAutomatically = true
        userAgentString = userAgentString.replace("; wv", "")
    }
    webView.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val scheme = request.url.scheme?.lowercase()
            return scheme != "http" && scheme != "https"
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (url?.contains("platform.deepseek.com") == true) {
                view?.evaluateJavascript(TOKEN_HOOK, null)
            }
        }
    }
}

@SuppressLint("AddJavascriptInterface")
private class SessionTokenBridge(
    private val webViewProvider: () -> WebView?,
    private val onToken: (String) -> Unit,
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun onSessionToken(token: String) {
        mainHandler.post {
            val host = webViewProvider()?.url?.let { it.toUri().host }.orEmpty()
            if (host == "platform.deepseek.com" || host.endsWith(".deepseek.com")) {
                onToken(token)
            }
        }
    }
}
