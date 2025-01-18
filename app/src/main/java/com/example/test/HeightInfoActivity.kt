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

class HeightInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_height_info)

        // AgeInfoActivityまで持っていく値
        val email = intent.getStringExtra("email")      // メールアドレスの値を取得
        val gender = intent.getStringExtra("gender")    // 性別の取得

        // UI要素を取得
        val saveButton = findViewById<Button>(R.id.saveButton)
        val numPicker1 = findViewById<NumberPicker>(R.id.numPicker1)
        val numPicker2 = findViewById<NumberPicker>(R.id.numPicker2)
        val numPicker3 = findViewById<NumberPicker>(R.id.numPicker3)

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

        // 体重の下限上限設定
        numPicker1.minValue = 20
        numPicker1.maxValue = 200

        // 体重の小数点
        numPicker2.minValue = 0
        numPicker2.maxValue = 9

        // 身長の下限上限設定
        numPicker3.minValue = 50
        numPicker3.maxValue = 250

        // numberPickerの初期値
        numPicker1.value = 60   // 体重
        numPicker2.value = 0    // 体重(小数点)
        numPicker3.value = 170  // 身長

        // ループ不可設定
        numPicker1.wrapSelectorWheel = false
        numPicker2.wrapSelectorWheel = false
        numPicker3.wrapSelectorWheel = false


        // 保存ボタンのクリックリスナー
        saveButton.setOnClickListener {
            val height : Int = numPicker3.value          // 身長をString型からInt変換
            val weightLeft : Int = numPicker1.value      // 体重の左(整数)をString型からInt変換
            val weightRight : Int = numPicker2.value     // 体重の右(小数)をString型からInt変換
            val weightRightFloat : Float = weightRight.toFloat() / 10           // 小数点を10で割って計算
            val weight : Float = weightLeft.toFloat() + weightRightFloat        // 体重を計算してFloat型に変換

            // 一旦端末の保存(後にデータベース)
            saveHeightBodyInfo(height)
            saveWeightBodyInfo(weight)
            val sharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)

            // 判定処理用
            val savedHeight = sharedPreferences.getInt("height", -1)
            val savedWeight = sharedPreferences.getFloat("weight", -1f)

            // セーブが出来たら次へ
            if (savedHeight == height && savedWeight == weight) {
//                Toast.makeText(this, "情報を保存しました", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, NameInfoActivity::class.java)
                // 以下最後のAgeInfoActivityまで持っていく
                intent.putExtra("email", email)     // メールアドレスの継承
                intent.putExtra("gender", gender)   // 性別の継承
                intent.putExtra("height", height)   // 身長の継承
                intent.putExtra("weight", weight)   // 体重の継承

                startActivity(intent)
            } else {
                Toast.makeText(this, "保存に失敗しました", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // 身長情報を保存
    private fun saveHeightBodyInfo(height: Int) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("height", height)
        editor.apply()
    }

    // 体重情報を保存
    private fun saveWeightBodyInfo(weight: Float) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("weight", weight)
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