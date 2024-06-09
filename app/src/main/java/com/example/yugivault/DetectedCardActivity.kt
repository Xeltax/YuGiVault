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
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import  com.bumptech.glide.Glide
import  kotlinx.coroutines.Dispatchers
import  kotlinx.coroutines.launch
import  kotlinx.coroutines.withContext
import  okhttp3.OkHttpClient
import  okhttp3.Request
import  okhttp3.Response
import  java.io.File
import  java.io.FileOutputStream
import  java.io.IOException
import  java.io.InputStream
import  androidx.lifecycle.lifecycleScope


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

    private lateinit var Artwork: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detected_card)

        val Name = findViewById<TextView>(R.id.Name)
        val Attribute = findViewById<TextView>(R.id.Attribute)
        Artwork = findViewById<ImageView>(R.id.ArtWork)
        val TypeRace = findViewById<TextView>(R.id.TypeRace)
        val Desc = findViewById<TextView>(R.id.Desc)
        val AtkDef = findViewById<TextView>(R.id.AtkDef)
        val btnYes = findViewById<TextView>(R.id.btnYes)
        val btnNo = findViewById<TextView>(R.id.btnNo)

        val receveText = intent.getStringExtra("detectedText")
         //val detectedText = "magicien sombre"
        val detectedText=  receveText?.replace(" ","%20")
        println(detectedText)


        val apiHandler = ApiHandler(this)
        var jsonResponse: JSONObject? = null
        if (detectedText != null) {
            apiHandler.getByName(detectedText,
                { response ->
                    // La requête a réussi, traiter la réponse ici
                    println("Réponse de la requête: $response")

                    jsonResponse= JSONObject(response.toString())
                    Name.text = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("name")
                    Attribute.text = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("attribute")
                    Desc.text = jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("desc")
                    TypeRace.text =  "["+jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("frameType")+"/" + jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("race")+"]"
                    AtkDef.text = "ATK/ "+jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("atk") + "  DEF /" + jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("def")

                    displayImageFromUrl(apiHandler.API_URL_ARTWORK+jsonResponse?.getJSONArray("data")?.getJSONObject(0)?.getString("id")+".jpg")
                    //Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
                }
            ) { error ->
                // Une erreur s'est produite lors de la requête
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }

        //Bonne carte detecter
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
                if (card != null) {
                    Toast.makeText(this@DetectedCardActivity, "Card already in collection", Toast.LENGTH_LONG).show()
                } else {
                    val newCard = Card(cardInfo.id, cardInfo.name, cardInfo.type, cardInfo.desc, cardInfo.atk, cardInfo.def, cardInfo.level, cardInfo.race, cardInfo.attribute)
                    GlobalScope.launch {  cardDAO.insert(newCard) }
                    Toast.makeText(this@DetectedCardActivity, "Card added to collection", Toast.LENGTH_LONG).show()
                }
            })

            lifecycleScope.launch { val dlImage= downloadImage(apiHandler.getArtworkUrl(cardInfo.id),cardInfo.id) }


            val intent = Intent(this, CardCollection::class.java)
            startActivity(intent)
            finish()
        }//end btnYes


        //Mauvaise carte detect
        btnNo.setOnClickListener{
            Toast.makeText(this, "Card not added to collection, rescan the card please", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MLActivity::class.java)
            startActivity(intent)
            finish()
        }//end btnNo





    }

    private suspend fun fetchCardImageUrl(url: String): String? = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        return@withContext try {
            val response: Response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body?.string()
            if (responseData != null) {
                val json = JSONObject(responseData)
                val cardImages = json.getJSONArray("data").getJSONObject(0).getJSONArray("card_images")
                cardImages.getJSONObject(0).getString("image_url_cropped")
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to fetch image URL", e)
            null
        }
    }

    private suspend fun downloadImage(url: String,id: Int): File = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response: Response = client.newCall(request).execute()
        if (!response.isSuccessful) throw IOException("Failed to download image: $response")

        val inputStream: InputStream = response.body!!.byteStream()
        val file = File(getExternalFilesDir(null), "$id.jpg")

        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }

        file
    }

    private fun displayImageFromFile(file: File) {
        Glide.with(this)
            .load(file)
            .into(Artwork)
    }

    private fun displayImageFromUrl(url: String) {
        Glide.with(this)
            .load(url)
            .into(Artwork)
    }


}
