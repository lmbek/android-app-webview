
package beksoft.webview_2023
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader

class MainActivity_old : AppCompatActivity() {
    private var goServerProcess: Process? = null
    private val requestCode = 1 // Define your requestCode here

    fun playground(){
        Toast.makeText(applicationContext, "Hello, World!", Toast.LENGTH_SHORT).show()
        val file = File(filesDir, "kage-elsker.txt") // Change "filesDir" to the appropriate path if needed

        try {
            val fileWriter = FileWriter(file)
            fileWriter.write("kokosmacron")
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the exception
        }
    }

    fun minTest(){
        val fileName = "android-hello"
        val file = File(filesDir, fileName)

        // Check if the file can be executed
        val canExecute = file.canExecute()
        System.out.println("Can Execute: $canExecute")
        System.out.println("before")

        try {
            // Write some content to the file
            val contentToWrite = "Hello, World!"
            val output = FileOutputStream(file)
            output.write(contentToWrite.toByteArray())
            output.close()

            // Make the file executable
            file.setExecutable(true)

            // Execute the file as a separate process
            val process = Runtime.getRuntime().exec("/data/data/beksoft.webview_2023/files/"+fileName)
            process.run {  }

            // You can interact with the process or read its output here

        } catch (e: IOException) {
            e.printStackTrace()
            // Handle exceptions if needed
        }
        System.out.println("complete")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ask for permission
        //requestPermissionsIfNecessary()

        // Start the Go server
        //startGoServer()
        //startGoHelloWorld()
        runGoExecutableWithShell()
        //val command = "ls /data/data/beksoft.webview_2023/files"
        //val command = "/data/data/beksoft.webview_2023/files/android-hello"
        //val result = runExecutable(command)
        //runGo()
        //playground();
        //minTest();

        // initialize webview localhost
    /*
        // Initialize WebView
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        // Load the WebView with localhost:8080
        webView.loadUrl("http://localhost:8080")
*/

        /*
        // Load the local HTML file from res/raw
        // note that if we use file:// then we cant use javascript
        //webView.loadUrl("file:///android_res/raw/index.html")
        webView.loadUrl("https://netflix.com")
        */
    }

    override fun onDestroy() {
        super.onDestroy()

        // Stop the Go server when the app is destroyed
        stopGoServer()
    }

    fun runGo(){
        val command = "android-hello"
        val processBuilder = ProcessBuilder(command)
        processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE)
        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE)
        val process = processBuilder.start()

        // Get input and output streams
        val input = process.outputStream
        val output = process.inputStream

        // Send data to the Go program
        val inputData = "Hello from Kotlin"
        input.write(inputData.toByteArray())
        input.flush()
        input.close()

        // Read the output from the Go program
        val outputData = output.bufferedReader().readText()
        output.close()

        // Now, outputData contains the response from the Go program
    }

    fun runExecutable(command: String): String {
        System.out.println("kokos1")
        val output = StringBuilder()

        try {
            val processBuilder = ProcessBuilder(*command.split(" ").toTypedArray())
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            System.out.println("kokos2")
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            System.out.println("kokos3")
            System.out.println(output.toString())

            process.waitFor()
            process.destroy()
        } catch (e: IOException) {
            System.out.println("kokosfejl")
            e.printStackTrace()
            return "Error: ${e.message}"
        }
        System.out.println("kokos4")
        return output.toString()
    }

    private fun runGoExecutableWithShell() {
        val commandPath = "/data/data/beksoft.webview_2023/files/android-hello" // Update with the correct path
        try {
            val runtime = Runtime.getRuntime()

            System.out.println("kokos1")
            val processBuilder = ProcessBuilder(commandPath)
            val process = processBuilder.start()
            System.out.println("kokos2")
            val exitCode = process.waitFor()
            System.out.println("kokos3")
            if (exitCode == 0) {
                // The Go executable ran successfully
            } else {
                // Handle any errors or issues
            }
        } catch (e: IOException) {

            e.printStackTrace()
            // Handle the exception
        }
    }

    private fun startGoHelloWorld() {
        try {
            val assetName = "android-hello"
            val assetManager = assets
            val assetInputStream = assetManager.open(assetName)
            val outputFile = File(filesDir, assetName) // Copy to app's internal storage directory
            val output = FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (assetInputStream.read(buffer).also { length = it } > 0) {
                output.write(buffer, 0, length)
            }
            output.close()
            assetInputStream.close()

            // Set execute permission
            val commandPath = outputFile.absolutePath
            val processBuilder = ProcessBuilder("chmod", "777", commandPath)
            processBuilder.start().waitFor()

            // Run the compiled Go executable
            val processBuilderGo = ProcessBuilder(commandPath)
            processBuilderGo.redirectErrorStream(true)
            val goProcess = processBuilderGo.start()
            val inputStream = goProcess.inputStream

            goProcess.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startGoServer() {
        try {
            // Copy the asset to the app's internal storage
            val assetManager = assets
            val assetName = "android-server"
            val assetInputStream = assetManager.open(assetName)
            val outputFile = File(filesDir, assetName)
            val output = FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (assetInputStream.read(buffer).also { length = it } > 0) {
                output.write(buffer, 0, length)
            }
            output.close()
            assetInputStream.close()

            // Set execute permission
            val commandPath = outputFile.absolutePath
            val processBuilder = ProcessBuilder("chmod", "777", commandPath)
            processBuilder.start().waitFor()


            // Run the copied and chmod'ed file
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





    private fun requestPermissionsIfNecessary() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            "your_custom_bluetooth_permission" // Replace with your actual custom permission
        )

        val notGrantedPermissions = permissions.filter {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGrantedPermissions.toTypedArray(), requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, handle accordingly.
                } else {
                    // Permission denied, handle accordingly.
                }
            }
        }
    }
}
