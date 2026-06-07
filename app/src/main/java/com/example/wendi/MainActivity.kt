package com.example.wendi

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the WebView by its ID
        webView = findViewById(R.id.myWebView)

        // Force links and redirects to open in the WebView instead of a browser app
        webView.webViewClient = WebViewClient()

        // Enable JavaScript (Optional but highly recommended for modern websites)
        webView.settings.javaScriptEnabled = true

        // Load the webpage
        webView.loadUrl("https://wendi.dreamhosters.com/")
    }

    // Optional: Handle the physical back button to go back in web history
    // rather than instantly closing the app
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}