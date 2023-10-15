package beksoft.webview_2023
import android.os.Bundle
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import android.net.LocalServerSocket
import java.net.ServerSocket

class MainActivity : AppCompatActivity() {
    private val port = findAvailablePort()
    private val serverSocket = LocalServerSocket("my-local-server")
    private val webContent = "<html><body><h1>Hej med dig</h1></body></html>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                url: String
            ): WebResourceResponse? {
                // Serve your HTML content from here
                if (url == "http://127.0.0.1:$port/") {
                    val inputStream = webContent.byteInputStream()
                    return WebResourceResponse("text/html", "UTF-8", inputStream)
                }
                return null
            }
        }

        // Start the server
        startServer()

        // Load the WebView with the local server URL
        webView.loadUrl("http://127.0.0.1:$port/")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Close the server socket when the app is destroyed
        serverSocket.close()
    }

    // Find an available port
    private fun findAvailablePort(): Int {
        val socket = ServerSocket(0) // 0 indicates any available port
        val port = socket.localPort
        socket.close()
        return port
    }

    // Start the local server
    private fun startServer() {
        Thread {
            val socket = serverSocket.accept()
            val outputStream = socket.outputStream
            val response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: ${webContent.length}\r\n" +
                    "Connection: close\r\n\r\n" +
                    webContent
            outputStream.write(response.toByteArray())
            outputStream.close()
            socket.close()
        }.start()
    }
}
