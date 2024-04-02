package com.example.yugivault

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityOptionsCompat

class DeckActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.deck_main)

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

    }
}