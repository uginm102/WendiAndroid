package com.example.wendi // KEEP YOUR PROJECT'S ACTUAL PACKAGE NAME

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class WebHubActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var loadingSpinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_hub)

        webView = findViewById(R.id.myWebView)
        loadingSpinner = findViewById(R.id.loadingSpinner)

        // 1. SAFE PADDING FIX: Prevent top menu/status bar from hiding the webview
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.webContainer)) {
            view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Applies the top status bar height as padding to the container
            view.updatePadding(top = insets.top)
            windowInsets
        }

        // WebView Settings
        webView.settings.javaScriptEnabled = true

        // 2. LOADING ANIMATION LOGIC: Intercept web engine states to show/hide spinner
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Show the spinning wheel when loading begins
                loadingSpinner.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Hide the spinning wheel once the elements finish rendering
                loadingSpinner.visibility = View.GONE
            }

            // --- ADD THIS FUNCTION HERE TO CATCH PLAY STORE INTENTS ---
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    // Check if the URL is trying to open the Play Store or a deep intent link
                    if (url.startsWith("intent:") || url.contains("play.google.com/store")) {
                        try {
                            // Turn the text link into a system Intent command
                            val intent = android.content.Intent.parseUri(url, android.content.Intent.URI_INTENT_SCHEME)
                            if (intent != null) {
                                // Ask the phone's OS to find a native app (like Play Store) to open it
                                startActivity(intent)
                                return true // Tells the WebView: "I handled this link, don't try to load it yourself!"
                            }
                        } catch (e: Exception) {
                            // If the user doesn't have the Play Store app installed, fallback gracefully
                            e.printStackTrace()
                        }
                    }
                }
                return false // For normal sites (like google.com), let the WebView load it normally
            }
        }

        webView.loadUrl("https://wendi.dreamhosters.com/")

        // Back action mapping
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })
    }
}