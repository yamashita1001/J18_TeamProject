package com.example.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InputWaterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inputwater)

        // UIに表示
        val inputCompletedButton = findViewById<Button>(R.id.inputCompletedButton)
        val inputNumberText = findViewById<EditText>(R.id.inputNumberText)
        val button120 = findViewById<ImageButton>(R.id.button120)
        val button500 = findViewById<ImageButton>(R.id.button500)
        val button1000 = findViewById<ImageButton>(R.id.button1000)

        // welcomeTextView.text = "ようこそ、$username さん！"

        // Homeへ遷移(入力した値を持って)
        inputCompletedButton.setOnClickListener {
            // EditTextから水分摂取量を取得
            val inputText = inputNumberText.text.toString()
            val waterValue = inputText.toIntOrNull() // 数値変換

            // 入力なし
            if (waterValue == null) {
                Toast.makeText(this, "数値を入力してください", Toast.LENGTH_SHORT).show()
            }
            // 1以上6000以下で判定(致死量6000mlのため)
            else if (waterValue < 1 || waterValue > 6000) {
                Toast.makeText(this, "1以上6000以下の値を入力してください", Toast.LENGTH_SHORT).show()
            }
            else {
                // 値が範囲内ならホーム画面へ遷移
                val intent = Intent(this, HomeActivity::class.java)
                // intent変数をつなげる(第一引数はキー，第二引数は渡したい変数)
                intent.putExtra("TEXT_KEY", waterValue)
                startActivity(intent)
            }
        }

        button120.setOnClickListener {
            // 数値を足して直接ホームに戻るか下の手入力テキストエリアに一旦表示するか
            // 一旦手入力のテキストエリアに数値を入れる
            inputNumberText.setText("120")
        }

        button500.setOnClickListener {
            inputNumberText.setText("500")
        }

        button1000.setOnClickListener {
            inputNumberText.setText("1000")
        }
    }
}