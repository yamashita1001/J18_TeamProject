package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // MainActivityからデータを受け取る
//        val username = intent.getStringExtra("USERNAME")

        // UIに表示
        val homeTextView = findViewById<TextView>(R.id.homeTextView)
        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)
        val inputWaterButton = findViewById<Button>(R.id.inputWaterButton)
//        welcomeTextView.text = "ようこそ、$username さん！"

        inputWaterButton.setOnClickListener {
            // InputWaterActivityへ遷移
            val intent = Intent(this, InputWaterActivity::class.java)
            startActivity(intent)
        }







    }
}