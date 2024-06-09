package com.example.yugivault.utils.rest

import android.content.Context
import android.util.JsonReader
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.IOException
import java.io.StringReader

class ApiHandler(private val context: Context) {

    private val API_URL = "https://db.ygoprodeck.com/api/v7/cardinfo.php"
    val API_URL_ARTWORK= "https://images.ygoprodeck.com/images/cards_cropped/"
    private val FR = "&language=fr"

    // Fonction pour faire une requête GET à une API distante
    fun makeApiRequest(url: String, onSuccess: (JSONObject) -> Unit, onError: (String) -> Unit) {
        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // La requête a réussi, traiter la réponse ici
                try {
                    // Utiliser un JsonReader pour traiter la réponse de manière incrémentielle
                    val jsonString = response.toString()
                    val jsonReader = JsonReader(StringReader(jsonString))

                    // Appeler la fonction onSuccess avec les données extraites
                    onSuccess(JSONObject(jsonString))
                } catch (e: Exception) {
                    onError("Erreur de traitement JSON: ${e.message}")
                }
            },
            { error ->
                // Une erreur s'est produite lors de la requête
                onError(error.message ?: "Erreur inconnue")
            }
        )

        // Ajouter la requête à la file d'attente
        requestQueue.add(jsonObjectRequest)
    }

    fun getAll(onSuccess: (JSONObject) -> Unit, onError: (String) -> Unit) {
        makeApiRequest(API_URL, onSuccess, onError)
    }

    fun getById(id: String, onSuccess: (JSONObject) -> Unit, onError: (String) -> Unit) {
        makeApiRequest(API_URL + "?id=${id}", onSuccess, onError)
    }

    fun getByName(name: String, onSuccess: (JSONObject) -> Unit, onError: (String) -> Unit) {
        makeApiRequest(API_URL + "?name=${name}"+FR, onSuccess, onError)
    }

    fun getByArchetype(name: String, onSuccess: (JSONObject) -> Unit, onError: (String) -> Unit) {
        makeApiRequest(API_URL + "?archetype=${name}", onSuccess, onError)
    }
    fun getByArtwork(id: Int,onSuccess: (JSONObject) -> Unit, onError: (String) -> Unit){
        makeApiRequest(API_URL_ARTWORK+id+".jpg",onSuccess,onError)
    }

    fun getArtworkUrl(id: Int): String {
        return API_URL_ARTWORK+"$id.jpg"
    }



    private fun readJsonValue(reader: JsonReader, key: String): String? {
        try {
            reader.beginObject() // Commencer la lecture de l'objet JSON

            while (reader.hasNext()) {
                val name = reader.nextName()
                if (name == key) {
                    // Trouvé la clé recherchée, lire et retourner la valeur associée
                    return reader.nextString()
                } else {
                    // Ignorer la valeur actuelle, car elle n'est pas celle que nous recherchons
                    reader.skipValue()
                }
            }

            reader.endObject() // Fin de la lecture de l'objet JSON
        } catch (e: IOException) {
            e.printStackTrace()
            // Gérer les erreurs de lecture ici
        }

        return null // Retourner null si la clé n'est pas trouvée ou s'il y a une erreur
    }
}
