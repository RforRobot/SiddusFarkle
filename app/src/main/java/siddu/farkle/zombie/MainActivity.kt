package siddu.farkle.zombie

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.allowContentAccess = true

        // Bind the native Android bridge object accessible as window.AndroidBridge in JS
        webView.addJavascriptInterface(WebAppInterface(this), "AndroidBridge")

        webView.webViewClient = object : WebViewClient() {}

        // Load the local HTML file from the assets folder
        webView.loadUrl("file:///android_asset/farkle_universe_trilogy.html")

        // Intercept Android Back Button so it doesn't immediately close the game
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Trigger a save in JS before leaving
                webView.evaluateJavascript("if (typeof saveGame === 'function') { saveGame(); }", null)
                finish()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        // Auto-save whenever player switches apps or screen turns off
        webView.evaluateJavascript("if (typeof saveGame === 'function') { saveGame(); }", null)
    }
}