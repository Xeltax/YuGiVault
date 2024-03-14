package com.example.yugivault

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.yugivault.ui.theme.YuGiVaultTheme
import com.example.yugivault.utils.DAO.CardDAO
import com.example.yugivault.utils.DataBase.Vault
import com.example.yugivault.utils.rest.ApiHandler
import org.json.JSONObject


class DetectedCardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detected_card)
        println("On est dans DetectedCardActivity")
        val vault = Vault(this)
        println("On est dans DetectedCardActivity")
        vault.addCard(CardDAO.generateRandomCard())

        println("Fake Data: "+CardDAO.getCard(vault, 1))

        val Name = findViewById<TextView>(R.id.Name)
        val Attribute = findViewById<TextView>(R.id.Attribute)
        val ArtWork = findViewById<ImageView>(R.id.ArtWork)
        val TypeRace = findViewById<TextView>(R.id.TypeRace)
        val Desc = findViewById<TextView>(R.id.Desc)
        val AtkDef = findViewById<TextView>(R.id.AtkDef)

        //val detectedText = intent.getStringExtra("detectedText")
        val detectedText = "Dark Magician"
        println(detectedText)


       /* val apiHandler = ApiHandler(this)
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
        }*/


/*        var idT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("id")
        var nameT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("name")
        var typeT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("type")
        var frameTypeT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("frameType")
        var descT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("desc")
        var atkT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("atk")
        var defT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("def")
        var levelT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("level")
        var raceT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("race")
        var attributeT=jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("attribute")
        var artworkT="000"*/







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