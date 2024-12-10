package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    // 合計摂取量変数
    companion object {
        var sumWater = 0 // 静的に合計を保持
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // ツールバーのセットアップ
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // MainActivityからデータを受け取る
        //val username = intent.getStringExtra("USERNAME")
        //welcomeTextView.text = "ようこそ、$username さん！"


        // UIに表示
        val homeTextView = findViewById<TextView>(R.id.homeTextView)
        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)
        val inputWaterButton = findViewById<Button>(R.id.inputWaterButton)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        val inputWaterValueTextView = findViewById<TextView>(R.id.inputWaterValueTextView)


        // InputWaterからデータ受け取り
        val text = intent.getStringExtra("TEXT_KEY")
        // InputWaterから値を継承したものを数値に変換して合計表示
        if (!text.isNullOrEmpty()) {
            val waterValue = text.toIntOrNull() // 数値変換
            if (waterValue != null) {
                sumWater += waterValue // 合計に加算
            }
        }

        // 合計を表示
        inputWaterValueTextView.text = "合計摂取量: $sumWater ml / 2000ml"



        // ハンバーガーアイコンの設定
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        inputWaterButton.setOnClickListener {
            // InputWaterActivityへ遷移
            val intent = Intent(this, InputWaterActivity::class.java)
            startActivity(intent)
        }

        // メニュー項目のクリック処理
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // ホームが選択されたときの処理
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_settings -> {
                    // 設定が選択されたときの処理
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_intake -> {
                    // 摂取量表示が選択されたときの処理
                    val intent = Intent(this, IntakeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_myPage -> {
                    // マイページが選択されたときの処理
                    val intent = Intent(this, MyPageActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}