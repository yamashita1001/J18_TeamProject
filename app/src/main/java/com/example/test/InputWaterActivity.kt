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
import androidx.appcompat.widget.Toolbar

class InputWaterActivity : AppCompatActivity() {

    // 水分量に応じた画像リソースを返す関数
    private fun getWaterImageResource(waterValue: Int): Int {
        return when (waterValue) {
            120 -> R.drawable.ic_120ml
            200 -> R.drawable.ic_200ml
            500 -> R.drawable.ic_500ml
            1000 -> R.drawable.ic_1000ml
            1500 -> R.drawable.ic_1500ml
            2000 -> R.drawable.ic_2000ml
            else -> { /* 何もしない */ 0 }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inputwater)

        // UIに表示
        val inputCompletedButton = findViewById<Button>(R.id.inputCompletedButton)
        val inputNumberText = findViewById<EditText>(R.id.inputNumberText)
        val button120 = findViewById<ImageButton>(R.id.button120)
        val button200 = findViewById<ImageButton>(R.id.button200)
        val button500 = findViewById<ImageButton>(R.id.button500)
        val button1000 = findViewById<ImageButton>(R.id.button1000)
        val button1500 = findViewById<ImageButton>(R.id.button1500)
        val button2000 = findViewById<ImageButton>(R.id.button2000)

        // 最近使用した項目ボタンを3枠取得
        val recent1 = findViewById<ImageButton>(R.id.recent1)
        val recent2 = findViewById<ImageButton>(R.id.recent2)
        val recent3 = findViewById<ImageButton>(R.id.recent3)

        // ツールバーを取得
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar)

        // 戻るボタンを有効化
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 戻るボタンのクリックイベントを処理
        toolbar.setNavigationOnClickListener {
            finish() // 現在のアクティビティを終了して前の画面に戻る
        }

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
                saveToRecentList(waterValue)
                startActivity(intent)
            }
        }

        // 各プリセットボタンリスナー
        button120.setOnClickListener { navigateWithWaterValue(120) }
        button200.setOnClickListener { navigateWithWaterValue(200) }
        button500.setOnClickListener { navigateWithWaterValue(500) }
        button1000.setOnClickListener { navigateWithWaterValue(1000) }
        button1500.setOnClickListener { navigateWithWaterValue(1500) }
        button2000.setOnClickListener { navigateWithWaterValue(2000) }

        // よく使う項目のボタンに設定
        displayRecentItems(recent1, recent2, recent3)
    }

    // ホーム画面へ水分量を持って遷移する関数
    private fun navigateWithWaterValue(waterValue: Int) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("TEXT_KEY", waterValue)
        saveToRecentList(waterValue)
        startActivity(intent)
    }

    // よく使う項目リストをSharedPreferencesから取得
    private fun loadRecentList(): List<Pair<Int, Int>> {
        val prefs = getSharedPreferences("recent_prefs", MODE_PRIVATE)
        val recentMap = prefs.all.filterKeys { it.startsWith("water_value_") }
        val recentList = recentMap.map { Pair(it.key.substringAfter("water_value_").toInt(), it.value as Int) }
        return recentList.sortedByDescending { it.second } // 使用回数で降順に並べ替え
    }

    // 水分量をSharedPreferencesに保存
    private fun saveToRecentList(newValue: Int) {
        val prefs = getSharedPreferences("recent_prefs", MODE_PRIVATE)
        val editor = prefs.edit()

        // 使用回数を増加
        val key = "water_value_$newValue"
        val currentCount = prefs.getInt(key, 0)
        editor.putInt(key, currentCount + 1)

        editor.apply()
    }

    // よく使う項目を表示
    private fun displayRecentItems(vararg buttons: ImageButton) {
        val recentList = loadRecentList()
        buttons.forEachIndexed { index, button ->
            if (index < recentList.size) {
                val waterValue = recentList[index].first
                button.setImageResource(getWaterImageResource(waterValue)) // 画像を動的に設定
                button.tag = waterValue // 水分量をタグに保存
                button.visibility = View.VISIBLE

                button.setOnClickListener {
                    navigateWithWaterValue(waterValue)
                }
            } else {
                button.visibility = View.INVISIBLE
            }
        }
    }
}
