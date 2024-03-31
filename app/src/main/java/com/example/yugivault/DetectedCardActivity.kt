package com.example.yugivault

import android.content.Intent
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
import androidx.room.Room
import com.example.yugivault.ui.theme.YuGiVaultTheme
import com.example.yugivault.utils.db.AppDatabase
import com.example.yugivault.utils.entity.Card
import com.example.yugivault.utils.rest.ApiHandler
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.StringReader


data class CardInfo (
    val id: Int,
    val name: String,
    val type: String,
    val frameType: String,
    val desc: String,
    val atk: Int,
    val def: Int,
    val level: Int,
    val race: String,
    val attribute: String,
)

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
        val btnYes = findViewById<TextView>(R.id.btnYes)
        val btnNo = findViewById<TextView>(R.id.btnNo)

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

        btnYes.setOnClickListener{
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "dbVault"
            ).build()

            val cardDAO = db.cardDAO()

            val cardInfo = CardInfo(
                id = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("id")!!.toInt(),
                name = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("name")!!,
                type = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("type")!!,
                frameType = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("frameType")!!,
                desc = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("desc")!!,
                atk = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("atk")!!.toInt(),
                def = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("def")!!.toInt(),
                level = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("level")!!.toInt(),
                attribute = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("attribute")!!,
                race = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("race")!!
            )
            cardDAO.getCardById(cardInfo.id).observe(this, { card ->
                if (card == null) {
                    val newCard = Card(cardInfo.id, cardInfo.name, cardInfo.type, cardInfo.desc, cardInfo.atk, cardInfo.def, cardInfo.level, cardInfo.race, cardInfo.attribute)
                    GlobalScope.launch {
                        cardDAO.insert(newCard)
                    }
                    Toast.makeText(this, "Card added to collection", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Card already in collection", Toast.LENGTH_LONG).show()
                }
            })
            val intent = Intent(this, CardCollection::class.java)
            startActivity(intent)
            finish()
        }//end btnYes

        btnNo.setOnClickListener{
            Toast.makeText(this, "Card not added to collection, rescan the card please", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MLActivity::class.java)
            startActivity(intent)
            finish()
        }//end btnNo





    }
}
