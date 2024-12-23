package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // ログイン用データベースインスタンス生成
    private lateinit var logindb: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logindb = LoginDatabase(this)

        // UI要素を取得
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerLink: Button = findViewById(R.id.registerLink)

        // ログインボタンのクリックリスナー
        loginButton.setOnClickListener {
            val userId = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // ユーザー名とパスワードの検証
//            if (username == "user" && password == "password") {
//                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
//            }
            if (logindb.validateUser(userId, password)) {
                Toast.makeText(this, "ログインしました", Toast.LENGTH_SHORT).show()
                // 次の画面に進む処理を追加
                // HomeActivityに遷移
                val intent = Intent(this, BodyInfoActivity::class.java)
//                intent.putExtra("USERNAME", username) // 必要ならデータを渡す
                startActivity(intent)
            } else {
                Toast.makeText(this, "ユーザー名またはパスワードが間違っています", Toast.LENGTH_SHORT).show()
            }
        }

        // 新規作成ボタンのクリックリスナー
        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
