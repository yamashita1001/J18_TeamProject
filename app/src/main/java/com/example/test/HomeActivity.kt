package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {


    // 合計摂取量変数
    companion object {
        var sumWater = 0 // 静的に合計を保持
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // UIに表示
        val homeTextView = findViewById<TextView>(R.id.homeTextView)
        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)
        val inputWaterButton = findViewById<Button>(R.id.inputWaterButton)
        val inputWaterValueTextView = findViewById<TextView>(R.id.inputWaterValueTextView)
//        welcomeTextView.text = "ようこそ、$username さん！"





        // MainActivityからデータを受け取る
        // val username = intent.getStringExtra("USERNAME")


        // InputWaterからデータ受け取り
        val text = intent.getStringExtra("TEXT_KEY")
        // InputWaterから値を継承したものを数値に変換して合計表示
        if (!text.isNullOrEmpty()) {
            val waterValue = text.toIntOrNull() // 数値変換
            if (waterValue != null) {
                sumWater += waterValue // 合計に加算
            }
        }

        // 合計を表示
        inputWaterValueTextView.text = "合計摂取量: $sumWater ml / 2000ml"


        // 入力画面へ遷移
        inputWaterButton.setOnClickListener {
            val intent = Intent(this, InputWaterActivity::class.java)
            startActivity(intent)
        }







    }
}