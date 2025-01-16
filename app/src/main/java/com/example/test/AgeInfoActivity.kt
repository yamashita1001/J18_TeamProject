package com.example.test

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class AgeInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age_info)

        // UI要素を取得
        val saveButton = findViewById<Button>(R.id.saveButton)
        val numPicker = findViewById<NumberPicker>(R.id.numPicker)

        // ツールバーを取得
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar)

        // アプリ名を非表示
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 戻るボタンを有効化
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 戻るボタンのクリックイベントを処理
        toolbar.setNavigationOnClickListener {
            finish() // 現在のアクティビティを終了して前の画面に戻る
        }

        // 矢印の色を黒に設定
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, android.R.color.black))

//        // 保存された情報をロード
//        loadBodyInfo(ageInput, heightInput, weightInput)

        // 年齢の下限上限設定
        numPicker.minValue = 5
        numPicker.maxValue = 100

        // numberPickerの初期値
        numPicker.value = 25

        // ループ不可設定
        numPicker.wrapSelectorWheel = false

        // 保存ボタンのクリックリスナー
        saveButton.setOnClickListener {
            val age : Int = numPicker.value          // 身長をString型からInt変換

            // 一旦端末の保存(後にデータベース)
            saveAgeBodyInfo(age)
            val sharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)

            // 判定処理用
            val savedAge = sharedPreferences.getInt("age", -1)

            // セーブが出来たらHomeへ
            if (savedAge == age) {
                Toast.makeText(this, "初期情報を保存しました", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "保存に失敗しました", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // 身長情報を保存
    private fun saveAgeBodyInfo(age: Int) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("age", age)
        editor.apply()
    }

//    // 身体情報をロード
//    private fun loadBodyInfo(ageInput: EditText, heightInput: EditText, weightInput: EditText) {
//        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
//        val age = sharedPreferences.getInt("age", -1)
//        val height = sharedPreferences.getFloat("height", -1f)
//        val weight = sharedPreferences.getFloat("weight", -1f)
//
//        if (age != -1) ageInput.setText(age.toString())
//        if (height != -1f) heightInput.setText(height.toString())
//        if (weight != -1f) weightInput.setText(weight.toString())
//    }
}