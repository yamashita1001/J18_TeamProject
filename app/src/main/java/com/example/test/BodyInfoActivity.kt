package com.example.test

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BodyInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actibity_body_info)

        // UI要素を取得
        val ageInput = findViewById<EditText>(R.id.ageInput)
        val heightInput = findViewById<EditText>(R.id.heightInput)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // 保存された情報をロード
        loadBodyInfo(ageInput, heightInput, weightInput)

        // 保存ボタンのクリックリスナー
        saveButton.setOnClickListener {
            val age = ageInput.text.toString().toIntOrNull()
            val height = heightInput.text.toString().toFloatOrNull()
            val weight = weightInput.text.toString().toFloatOrNull()

            if (age != null && height != null && weight != null) {
                saveBodyInfo(age, height, weight)
                Toast.makeText(this, "情報を保存しました", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "全ての項目を正しく入力してください", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 身体情報を保存
    private fun saveBodyInfo(age: Int, height: Float, weight: Float) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("age", age)
        editor.putFloat("height", height)
        editor.putFloat("weight", weight)
        editor.apply()
    }

    // 身体情報をロード
    private fun loadBodyInfo(ageInput: EditText, heightInput: EditText, weightInput: EditText) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
        val age = sharedPreferences.getInt("age", -1)
        val height = sharedPreferences.getFloat("height", -1f)
        val weight = sharedPreferences.getFloat("weight", -1f)

        if (age != -1) ageInput.setText(age.toString())
        if (height != -1f) heightInput.setText(height.toString())
        if (weight != -1f) weightInput.setText(weight.toString())
    }
}
