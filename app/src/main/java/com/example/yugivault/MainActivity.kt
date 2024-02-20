package com.example.yugivault

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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
import com.example.yugivault.utils.rest.ApiHandler


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{
                val intent = Intent(this, MLActivity::class.java)
                startActivity(intent)
                finish()
        }




        val apiUrl = "https://db.ygoprodeck.com/api/v7/cardinfo.php?archetype=Blue-Eyes"

        val apiHandler = ApiHandler(this)

        apiHandler.getByName("Dark Magician",
            { response ->
                // La requête a réussi, traiter la réponse ici
                println("Réponse de la requête: $response")
                Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
            },
            { error ->
                // Une erreur s'est produite lors de la requête
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        )



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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YuGiVaultTheme {
        Greeting("Android")
    }
}