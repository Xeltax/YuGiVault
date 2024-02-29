package com.example.yugivault

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.yugivault.databinding.ActivityMlBinding
import com.example.yugivault.ui.theme.YuGiVaultTheme
import com.example.yugivault.utils.OverlayView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit
private lateinit var roiRect: Matrix

class MLActivity :  ComponentActivity(),ImageAnalysis.Analyzer {
    private lateinit var viewBinding: ActivityMlBinding

    val lifecycleOwner: LifecycleOwner = ProcessLifecycleOwner.get()

    private lateinit var cameraExecutor: ExecutorService
    fun sendDataToNewActivity(detectedText: String) {
        // Créer un Intent pour démarrer la nouvelle activité
        Log.v("text", "blockText: $detectedText")
        println("On rentre dans la fonction")
        val intent = Intent(this, DetectedCardActivity::class.java)
        println("On a créé l'intent")
        intent.putExtra("detectedText", detectedText)
        println("On a ajouté l'extra")
        // Démarrer la nouvelle activité
        startActivity(intent)
        println("On a démarré l'activité")
        finish()
        println("On a fini")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMlBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

// Récupérez les dimensions de votre PreviewView
// Récupérez votre PreviewView à partir de votre layout XML
        val viewFinder: PreviewView = findViewById(R.id.viewFinder)
        val overlayView: OverlayView = findViewById(R.id.overlayView)


// Définissez les dimensions de votre ROI
// Récupérez les dimensions de votre PreviewView
        val previewWidth = viewFinder.width.toFloat()
        val previewHeight = viewFinder.height.toFloat()

// Définissez les dimensions de votre ROI
        val roiWidth = 200f // Largeur de la ROI
        val roiHeight = 200f // Hauteur de la ROI

        // Définissez les dimensions de votre ROI comme précédemment
        val roiLeft = (previewWidth /2) + roiWidth
        val roiTop = (previewHeight /2) + roiHeight
        overlayView.setROI(roiLeft, roiTop, roiLeft , roiTop )
        //roiRect = Rect(roiLeft.toInt(), roiTop.toInt(), (roiLeft + roiWidth).toInt(), (roiTop + roiHeight).toInt())
        roiRect = Matrix().apply{
            setRectToRect(
                RectF(roiLeft, roiTop, roiLeft + roiWidth, roiTop + roiHeight),
                RectF(0f, 0f, previewWidth, previewHeight),
                Matrix.ScaleToFit.FILL
            )
        }

        /*// Calculez les coordonnées pour placer la ROI au centre de l'écran
                val roiLeft = (previewWidth /2) + roiWidth
                val roiTop = (previewHeight / 2) + roiHeight
                val roiRight = roiLeft + roiWidth
                val roiBottom = roiTop + roiHeight

        // Configurez la ROI dans votre vue d'overlay
                overlayView.setROI(roiLeft, roiTop, roiRight, roiBottom)*/




        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, this)
                    }

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    fun stopCamera() {
        cameraExecutor.shutdown()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private var analysisActive = true
    var blockText = ""
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (!analysisActive) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees,
                roiRect)


            val result = recognizer.process(image)
                .addOnSuccessListener { result ->
                    val resultText = result.text
                    for (block in result.textBlocks) {
                        blockText = block.text
                        Log.v("text", "blockText: $blockText")
                        if (!blockText.equals("")) {
                            println("On rentre dans le if")
                            analysisActive = false
                            break;
                        }

                    }
                }
                .addOnFailureListener { e ->
                    println(e.message)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                    if(blockText != "") {
                        stopCamera()
                        sendToDetectedCardActivity()
                    }

                }
        }
    }//analyze

    fun sendToDetectedCardActivity() {
        sendDataToNewActivity(blockText)
    }
}



