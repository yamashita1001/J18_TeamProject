package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InputWaterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inputwater)

        // MainActivityからデータを受け取る
//        val username = intent.getStringExtra("USERNAME")

        // UIに表示（例: TextViewを使う場合）

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val inputCompletedButton = findViewById<Button>(R.id.inputCompletedButton)
//        welcomeTextView.text = "ようこそ、$username さん！"

        inputCompletedButton.setOnClickListener {
            // InputWaterActivityへ遷移
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }







    }
}