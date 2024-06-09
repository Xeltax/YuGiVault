package com.example.yugivault

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.yugivault.databinding.ActivityMlBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.app.AlertDialog
import android.widget.EditText


typealias LumaListener = (luma: Double) -> Unit

class MLActivity :  ComponentActivity(),ImageAnalysis.Analyzer {
    private lateinit var viewBinding: ActivityMlBinding

    val lifecycleOwner: LifecycleOwner = ProcessLifecycleOwner.get()
    private val detectedTexts= mutableListOf<String>()

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var analyzeButton: Button
    private var analysisActive = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMlBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        analyzeButton= findViewById(R.id.analyzeButton)
        analyzeButton.setOnClickListener {
            analysisActive =true
        }



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
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val result = recognizer.process(image)
                .addOnSuccessListener { result ->
                    val resultText = result.text

                    for (block in result.textBlocks) {
                        println("entré for")
                        blockText = block.text
                        println("Texte détecté: $blockText")
                        if(blockText.isNotEmpty()&& detectedTexts.size < 3){
                            println("entré IF")
                            detectedTexts.add(blockText)
                            if (detectedTexts.size ==3) {
                                println ("On a 3 textes")
                                analysisActive = false
                                val confidenceScore = compareTexts(detectedTexts)
                                println("Confidence score: $confidenceScore")
                                sendToDetectedCardActivity(confidenceScore)
                                break;
                            }
                        }
                        println("sortie if")
                    }
                    println("sorrtie for")
                    println("Contenu du tableau detectedTexts: $detectedTexts")
                }
                .addOnFailureListener { e ->
                    println(e.message)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                    println("On a fermé l'image")
                }
        }
    }//analyze


/*    fun sendToDetectedCardActivity(detectedText: String) {
        println("texte détecté: $detectedText")
        val intent = Intent(this, DetectedCardActivity::class.java)
        intent.putExtra("detectedText", detectedText)
        startActivity(intent)
        finish()
    }*/

    fun sendToDetectedCardActivity(confidenceScore: Float) {
        val detectedText = if (confidenceScore == 1f) {
            detectedTexts[0]
        } else {
            // Compter la fréquence de chaque texte
            val textFrequency = detectedTexts.groupingBy { it }.eachCount()
            // Trier par fréquence décroissante et prendre les plus fréquents
            val mostFrequentTexts = textFrequency.entries
                .sortedByDescending { it.value }
                .map { it.key }
                .toList()
            showDetectedTextDialog(mostFrequentTexts)
        }
    }




    private fun compareTexts(texts: List<String>): Float {
        var score = 0f
        val numberOfComp =3; // compare 3 texts
        for (i in texts.indices) {
            for (j in i + 1 until texts.size) {
                score += calculateSimilarity(texts[i], texts[j])
            }
        }
        return score / numberOfComp
    }

    private fun calculateSimilarity(text1: String, text2: String): Float {
        val words1 = text1.split("\\s+".toRegex()).map { it.trim().lowercase() }
        val words2 = text2.split("\\s+".toRegex()).map { it.trim().lowercase() }
        val commonWords = words1.intersect(words2).size
        val totalWords = (words1 + words2).toSet().size
        return if (totalWords == 0) 0f else commonWords.toFloat() / totalWords
    }

    private fun showDetectedTextDialog(detectedTexts: List<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Textes détectés")

        // Convertir les textes en tableau de chaînes pour les afficher dans la liste de dialogues
        val items = detectedTexts.toTypedArray()

        builder.setItems(items) { _, which ->
            // Gérer le clic sur un texte détecté
            val selectedText = items[which]
            // Passer le texte sélectionné à l'activité suivante
            val intent = Intent(this, DetectedCardActivity::class.java)
            intent.putExtra("detectedText", selectedText)
            startActivity(intent)
        }
        // Ajouter l'option "Ajouter mon propre texte"
        builder.setPositiveButton("Saisir le nom de la carte, si option propose incorrect") { _, _ ->
            showCustomTextDialog()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showCustomTextDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mettre le nom de la carte ")
        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            val enteredText = input.text.toString()
            // Passer le texte entré à l'activité suivante
            val intent = Intent(this, DetectedCardActivity::class.java)
            intent.putExtra("detectedText", enteredText)
            startActivity(intent)
        }
        builder.setNegativeButton("Annuler") { dialog, which -> dialog.cancel() }

        builder.show()
    }



}



