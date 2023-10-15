package beksoft.webview_2023
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private var goServerProcess: Process? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Start the Go server
        startGoServer()

        // Initialize WebView
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        // Load the WebView with localhost:8080
        webView.loadUrl("http://localhost:8080")
    }

    override fun onDestroy() {
        super.onDestroy()

        // Stop the Go server when the app is destroyed
        stopGoServer()
    }

    private fun startGoServer() {
        try {
            val commandPath = File(filesDir, "android-server").absolutePath
            val processBuilder = ProcessBuilder("chmod", "755", commandPath)
            processBuilder.start().waitFor()

            val processBuilderServer = ProcessBuilder(commandPath)
            processBuilderServer.redirectErrorStream(true)
            goServerProcess = processBuilderServer.start()
            // Handle the output or any other necessary actions
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun stopGoServer() {
        goServerProcess?.destroy()
    }
}


/*
package beksoft.webview_2023
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript
        webView.loadUrl("https://videndjurs.dk") // Load your website here

        /*
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript

        // Load the local HTML file from res/raw
        // note that if we use file:// then we cant use javascript
        //webView.loadUrl("file:///android_res/raw/index.html")
        webView.loadUrl("https://netflix.com") */


    }
}
*/