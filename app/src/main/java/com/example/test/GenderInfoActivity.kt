package com.example.test

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GenderInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender_info)

        // UI要素を取得
        val genderInput = findViewById<EditText>(R.id.genderInput)
        val saveButton = findViewById<Button>(R.id.savegenderButton)

//        // 保存された情報をロード
//        loadBodyInfo(ageInput, heightInput, weightInput)

        // 保存ボタンのクリックリスナー
        saveButton.setOnClickListener {
            val gender = genderInput.text.toString().toFloatOrNull()

            if (gender != null) {
                saveBodyInfo(gender)
                Toast.makeText(this, "情報を保存しました", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HeightInfoActivity::class.java) //身長登録に遷移
                startActivity(intent)
            } else {
                Toast.makeText(this, "項目を正しく入力してください", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 身体情報を保存
    private fun saveBodyInfo(gender: Float) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("gender", gender)
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