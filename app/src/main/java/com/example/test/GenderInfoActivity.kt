package com.example.test

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GenderInfoActivity : AppCompatActivity() {

    // 初期値は無選択
    private var selectGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender_info)

        // UI要素を取得
        val manButton = findViewById<ImageButton>(R.id.manButton)
        val womanButton = findViewById<ImageButton>(R.id.womanButton)
        val saveButton = findViewById<Button>(R.id.savegenderButton)

        // ボタンの位置
//        saveButton.translationX = 300F

//        // 保存された情報をロード
//        loadBodyInfo(ageInput, heightInput, weightInput)


        // 男性ボタンを押したときの処理
        manButton.setOnClickListener {
            selectGenderProcess("man", manButton, womanButton)
        }

        // 女性ボタンを押したときの処理
        womanButton.setOnClickListener {
            selectGenderProcess("woman", womanButton, manButton)
        }

        //  次へボタンのクリックリスナー
        saveButton.setOnClickListener {
            if (selectGender != null) {
                saveBodyInfo(selectGender!!)        // Stringで保存
//                Toast.makeText(this, "情報を保存しました", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HeightInfoActivity::class.java) //身長登録に遷移
                startActivity(intent)
            } else {
                Toast.makeText(this, "性別を選択してください", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 身体情報を保存
    private fun saveBodyInfo(gender: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("gender", gender)
        editor.apply()
    }

    // 選択した時の処理(片方は選択された状態、片方は選択されていない状態)
    private fun selectGenderProcess(gender: String, selectButton: ImageButton, otherButton: ImageButton) {
        // 選択された情報を更新
        selectGender = gender
        // 選択された側
        selectButton.isSelected = true
        // 選択されていない側
        otherButton.isSelected = false
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