package com.example.yugivault

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.room.Room
import com.example.yugivault.ui.theme.YuGiVaultTheme
import com.example.yugivault.utils.db.AppDatabase
import com.example.yugivault.utils.entity.Card
import com.example.yugivault.utils.rest.ApiHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dbVault"
        ).build()

        val cardDAO = db.cardDAO()
        val DM = Card(1, "Dark Magician", "Monster", "The ultimate wizard in terms of attack and defense.", 2500, 2100, 7, "Spellcaster", "Dark", "Dark Magician", "SDY-006", "https://storage.googleapis.com/ygoprodeck.com/pics/46986414.jpg", "https://storage.googleapis.com/ygoprodeck.com/pics/46986414.jpg")

        // Observer les changements de données avec LiveData
        cardDAO.getCardById(1).observe(this, Observer { card ->
            card?.let {
                println("Card: ${it.name}, Type: ${it.type}")
            } ?: run {
                println("Card not found for id")
            }
        })

        // Insérer la carte dans la base de données
        GlobalScope.launch {
            //cardDAO.insert(DM)
        }

        val catalogue: ImageButton = findViewById(R.id.imageButton2)
        catalogue.setOnClickListener{
                val intent = Intent(this, MainActivity::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
                startActivity(intent, options.toBundle())
                finish()
        }

        val scan: ImageButton = findViewById(R.id.imageButton3)
        scan.setOnClickListener{
            val intent = Intent(this, MLActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
            startActivity(intent, options.toBundle())
            finish()
        }

        val deck: ImageButton = findViewById(R.id.imageButton)
        deck.setOnClickListener{
            val intent = Intent(this, DeckActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
            startActivity(intent, options.toBundle())
            finish()
        }

        fun onBackPressed() {

        }

//
//        val ButCard: Button = findViewById(R.id.Card)
//            ButCard.setOnClickListener{
//            val intent = Intent(this, DetectedCardActivity::class.java)
//                intent.putExtra("detectedText", tosend)
//            startActivity(intent)
//            finish()
//        }
//
//        val ButCollection: Button = findViewById(R.id.Collection)
//        ButCollection.setOnClickListener{
//            val intent = Intent(this, CardCollection::class.java)
//            startActivity(intent)
//            finish()
//        }





        //val apiUrl = "https://db.ygoprodeck.com/api/v7/cardinfo.php?archetype=Blue-Eyes"
        val apiUrl="https://db.ygoprodeck.com/api/v7/cardinfo.php?id=46986421"

        val apiHandler = ApiHandler(this)


//        apiHandler.getByName("Dark Magician",
//            { response ->
//                // La requête a réussi, traiter la réponse ici
//                tosend=response.toString()
//                Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
//            }
//        ) { error ->
//            // Une erreur s'est produite lors de la requête
//            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
//        }


        /*        setContent {
                    YuGiVaultTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                            Greeting("Android")
                        }
                    }
                }*/
    }
}