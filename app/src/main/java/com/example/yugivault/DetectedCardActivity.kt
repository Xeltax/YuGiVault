package com.example.yugivault

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
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
        val Name = findViewById<TextView>(R.id.Name)
        val Attribute = findViewById<TextView>(R.id.Attribute)
        val ArtWork = findViewById<ImageView>(R.id.ArtWork)
        val TypeRace = findViewById<TextView>(R.id.TypeRace)
        val Desc = findViewById<TextView>(R.id.Desc)
        val AtkDef = findViewById<TextView>(R.id.AtkDef)


        val CardInfo = detectedText?.substringBefore("card_images")
        println("CardInfo: $CardInfo")
        val CardName = CardInfo.toString()?.substringAfter(" \"name\" ")?.substringBefore(",")
        println("CardName: $CardName")
        val CardAttribute = CardInfo?.substringAfter("attribute ")?.substringBefore(",")
        val CardType = CardInfo?.substringAfter("frameType: ")?.substringBefore(",")
        val CardRace = CardInfo?.substringAfter("race: ")?.substringBefore(",")
        val CardAtk = CardInfo?.substringAfter("atk: ")?.substringBefore(",")
        val CardDef = CardInfo?.substringAfter("def: ")?.substringBefore(",")


        Name.text = CardName
        Attribute.text = CardAttribute
        TypeRace.text = "[$CardType / $CardRace]"
        AtkDef.text = "$CardAtk / $CardDef"



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