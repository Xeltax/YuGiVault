package com.example.yugivault

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.yugivault.ui.theme.YuGiVaultTheme

class DetectedCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detected_card)

        // Récupérer les données textuelles passées en tant qu'extra
        val detectedText = intent.getStringExtra("detectedText")
        val tv : TextView = findViewById(R.id.textView)
        tv.text = detectedText
        // Utiliser les données textuelles comme nécessaire
       // textView.text = detectedText
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    YuGiVaultTheme {
        Greeting3("Android")
    }
}