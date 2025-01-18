package com.example.test

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class WaterCalculationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_calculation)
        // UI要素を取得
//        val homeButton = findViewById<Button>(R.id.HomeButton)
        // ProgressBarの取得
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Databaseインスタンスの作成
        val db = LoginDatabase(this)

        // SharedPreferencesで保存されたuserIdを取得
        val sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)        // 保存されたuserIdを取得

        // userIdが見つかった場合
        if (userId != null) {
            // 5秒間のProgressBar処理
            progressBar.isIndeterminate = true // プログレスバーを表示
            Handler(Looper.getMainLooper()).postDelayed({
                // ユーザーの身体情報を取得
                val userBodyInfo = db.getUserBodyInfo(userId)

                // プログレスバーを非表示
                progressBar.isIndeterminate = false
                progressBar.visibility = ProgressBar.GONE


                // データベースで身体情報が見つかった場合
                if (userBodyInfo != null) {

                    // 取得した値で水分量計算
                    val waterCalculationValue = waterCalculation(userBodyInfo.height, userBodyInfo.weight, userBodyInfo.age)
                    // 小数点を四捨五入
                    val waterIntake = Math.round(waterCalculationValue).toInt()
                    // ダイアログで計算結果を表示
                    showResultDialog(waterIntake)
                } else {
                    // Toast.makeText(this, "身体情報が見つかりませんでした: $userId", Toast.LENGTH_SHORT).show()
                }
            }, 5000) // 5秒待機
        } else {
            // Toast.makeText(this, "userIdが見つかりませんでした", Toast.LENGTH_SHORT).show()
        }
//        // ボタンのクリックリスナー
//        homeButton.setOnClickListener {
//
//            // 入力後Homeへ
//            // Toast.makeText(this, "初期情報を保存しました", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//        }

    }

    // 水分摂取量計算
    private fun waterCalculation(height: Int, weight: Double, age: Int): Double {

        if (age in 13..21) {
            // 13～21歳: 体重 * 40 - 600(食事水分摂取量)
            return weight * 40 - 600
        } else if (age in 22 .. 54) {
            // 22～54歳: 体重 * 35 - 600(食事水分摂取量)
            return weight * 35 - 600
        } else if (age in 55 .. 64) {
            // 55～64歳: 体重 * 30 - 600(食事水分摂取量)
            return weight * 30 - 600
        } else {
            // 65歳以上: 体重 * 25 - 600(食事水分摂取量)
            return weight * 25 - 600
        }
    }

    // 計算結果をダイアログで表示
    private fun showResultDialog(waterIntake: Int) {
        // Material Designダイアログ(角丸)
        val dialogBuilder = MaterialAlertDialogBuilder(this)
            .setTitle("水分摂取量の結果")
            .setMessage("あなたの1日の推奨水分摂取量は $waterIntake mL です。")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("waterIntake", waterIntake)     // 計算結果をホームへ
                Log.d("WaterCalculation", "Sending waterIntake: $waterIntake")
                startActivity(intent)
            }
            .setCancelable(false)   // ダイアログ外をタップしたとき閉じられないようにする

        // ダイアログのスタイル適用
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.round_dialog)
        dialog.show()
    }

}