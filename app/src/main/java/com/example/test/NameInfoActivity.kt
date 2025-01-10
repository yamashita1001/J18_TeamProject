package com.example.test

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NameInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_info)

        // UI要素を取得
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val saveButton = findViewById<Button>(R.id.savenameButton)

//        // 保存された情報をロード
//        loadBodyInfo(ageInput, heightInput, weightInput)

        // 保存ボタンのクリックリスナー
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().toFloatOrNull()

            if (name != null) {
                saveBodyInfo(name)
                Toast.makeText(this, "情報を保存しました", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java) //ホーム画面に遷移
                startActivity(intent)
            } else {
                Toast.makeText(this, "項目を正しく入力してください", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 身体情報を保存
    private fun saveBodyInfo(name: Float) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("name", name)
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