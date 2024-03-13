package com.example.yugivault

import android.os.Bundle
import android.util.JsonReader
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.yugivault.ui.theme.YuGiVaultTheme
import com.example.yugivault.utils.rest.ApiHandler
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.json.JSONObject
import java.io.StringReader



class DetectedCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detected_card)

        val Name = findViewById<TextView>(R.id.Name)
        val Attribute = findViewById<TextView>(R.id.Attribute)
        val ArtWork = findViewById<ImageView>(R.id.ArtWork)
        val TypeRace = findViewById<TextView>(R.id.TypeRace)
        val Desc = findViewById<TextView>(R.id.Desc)
        val AtkDef = findViewById<TextView>(R.id.AtkDef)

        //val detectedText = intent.getStringExtra("detectedText")
        val detectedText = "Dark Magician"
        println(detectedText)


        val apiHandler = ApiHandler(this)
        var jsonResponse: JSONObject? = null
        apiHandler.getByName("Dark Magician",
            { response ->
                // La requête a réussi, traiter la réponse ici
                println("Réponse de la requête: $response")

                jsonResponse= JSONObject(response.toString())
                Name.text = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("name")
                Attribute.text = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("attribute")
                Desc.text = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("desc")
                TypeRace.text =  "["+jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("frameType")+"/" + jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("race")+"]"
                AtkDef.text = "ATK/ "+jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("atk") + "  DEF /" + jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("def")

                //Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
            }
        ) { error ->
            // Une erreur s'est produite lors de la requête
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        apiHandler.getArtwork(jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("id").toString(), ArtWork,
            {
                // La requête a réussi, traiter la réponse ici

                println("Réponse de la requête: ")
            },{
                // Une erreur s'est produite lors de la requête
                println("Erreur de chargement de l'image")
        })





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