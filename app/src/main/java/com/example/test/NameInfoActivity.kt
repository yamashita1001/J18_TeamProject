package com.example.test

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class NameInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_info)

        // UI要素を取得
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val saveButton = findViewById<Button>(R.id.saveNameButton)

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

        // 保存ボタンのクリックリスナー
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim() // 前後の空白を削除

            // ニックネーム入力チェック
            when {
                name.isBlank() -> {
                    // 空白文字のみもしくは入力されていない場合
                    Toast.makeText(this, "入力してください\n空白のみは無効です", Toast.LENGTH_SHORT).show()
                }
                name.length !in 1..10 -> {
                    // 文字数が1〜10文字以下でない場合
                    Toast.makeText(this, "1文字以上10文字以下で入力してください", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 入力が有効な場合
                    saveBodyInfo(name)
//                    Toast.makeText(this, "情報を保存しました", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AgeInfoActivity::class.java) //ホーム画面に遷移
                    startActivity(intent)
                }
            }
        }
    }

    // 身体情報を保存
    private fun saveBodyInfo(name: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserBodyInfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
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