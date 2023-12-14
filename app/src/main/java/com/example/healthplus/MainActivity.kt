package com.example.healthplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.b1)
        val editText = findViewById<EditText>(R.id.b2)
        val Mytext = findViewById<TextView>(R.id.b3)
        button.setOnClickListener{
            Mytext.text = ApiConnection.getApiAnswer(editText.text.toString())
        }

        //hej
        //hejdå

    }
}