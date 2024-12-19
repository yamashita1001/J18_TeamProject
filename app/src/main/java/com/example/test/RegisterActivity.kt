package com.example.test // パッケージ名を適切に変更してください

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {


    private lateinit var logindb: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        logindb = LoginDatabase(this)

        val usernameEditText: EditText = findViewById(R.id.username)
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPassword)
        val registerButton: Button = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val userId = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            // 入力抜け処理
            if (userId.isEmpty() || email.isEmpty() || password.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "全てのフィールドを入力してください", Toast.LENGTH_SHORT).show()
            }
            // パスワード不一致
            else if (password != confirmPassword) {
                Toast.makeText(this, "パスワードが一致しません", Toast.LENGTH_SHORT).show()
            }
            // 問題なしの時の処理
            else {
                val success = logindb.addUser(userId, email, password)
                if (success) {
                    // ユーザー登録処理
                    Toast.makeText(this, "登録が完了しました！", Toast.LENGTH_SHORT).show()

                    // 登録後にMainActivityに戻る
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this, "登録に失敗しました。ユーザー名が重複している可能性があります", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
