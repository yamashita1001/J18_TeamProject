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
        numPicker.minValue = 13
        numPicker.maxValue = 100

        // numberPickerの初期値
        numPicker.value = 25

        // ループ不可設定
        numPicker.wrapSelectorWheel = false

        // 保存ボタンのクリックリスナー
        saveButton.setOnClickListener {
            val age : Int = numPicker.value          // 身長をString型からInt変換

            // 端末の保存
            saveAgeBodyInfo(age)


            // セーブが出来たらHomeへ
            Toast.makeText(this, "初期情報を保存しました", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, WaterCalculationActivity::class.java)
            startActivity(intent)
        }

    }

    // 最後に初期登録した情報を一括保存(データベース)
    private fun saveAgeBodyInfo(age: Int) {
        val sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)        // 保存されたuserIdを取得
        val password = sharedPreferences.getString("password", null)    // 保存されたパスワードを取得
        val email = intent.getStringExtra("email")                             // メールアドレスの値を取得
        val gender = intent.getStringExtra("gender")    // 性別の取得
        val height = intent.getIntExtra("height", -1)    // 身長の取得
        val weight = intent.getFloatExtra("weight", -1f)    // 体重の取得
        val name = intent.getStringExtra("name")        // ニックネーム名の取得

        // 体重の小数点の四捨五入
        val weightRoundUp = Math.round(weight * 10.0) / 10.0

        // userIdが存在しないかのエラーチェック
        if (userId == null) {
            Toast.makeText(this, "ログイン情報が見つかりません", Toast.LENGTH_SHORT).show()
            return
        }

        // パスワードが存在しないかのエラーチェック
        if (password == null) {
            Toast.makeText(this, "ログイン情報が見つかりません", Toast.LENGTH_SHORT).show()
            return
        }

        // メールアドレスが存在しないかのエラーチェック
        if (email == null) {
            Toast.makeText(this, "メールアドレス情報が見つかりません", Toast.LENGTH_SHORT).show()
            return
        }

        // 身体情報が正しく入力されているかのエラーチェック
        if (height == -1 || weight == -1f || name.isNullOrBlank() || gender == null) {
            Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
            return
        }

        // データベースインスタンス
        val database = LoginDatabase(this)

        // データベースに保存(userId, email, passwordも含めて)
        val isSaved = database.saveUserBodyInfo(userId, email, password, age, gender, weightRoundUp, height, name)

        // 保存成功失敗判定処理
        if (isSaved) {
//            Toast.makeText(this, "データベースに保存しました", Toast.LENGTH_SHORT).show()
        } else {
//            Toast.makeText(this, "保存に失敗しました", Toast.LENGTH_SHORT).show()
        }
    }

}