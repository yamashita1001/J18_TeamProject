package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        // ハンバーガーメニュー(ナビゲーションビュー)の設定関数
        setupNavigationView()

        //　マイページボタンの画面遷移関数
        myPageChange()


    }

    // ハンバーガーメニュー(ナビゲーションビュー)の設定関数
    private fun setupNavigationView(){
        // ツールバーのセットアップ
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // ハンバーガーメニュー(ナビゲーションビュー)のコンポーネントを取得
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        // メニュー項目のクリック処理
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // ホームが選択されたときの処理
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_settings -> {
                    // 設定が選択されたときの処理
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_intake -> {
                    // 摂取量表示が選択されたときの処理
                    val intent = Intent(this, IntakeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_myPage -> {
                    // 摂取量表示が選択されたときの処理
                    val intent = Intent(this, MyPageActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        // ハンバーガーアイコンの設定
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    //　編集ボタンの画面遷移関数
    private fun myPageChange() {
        // 編集ボタンのコンポーネント取得
        val myPageChange = findViewById<LinearLayout>(R.id.myPageChange)
        //　押した時の画面遷移(マイページ)
        myPageChange.setOnClickListener {
            // EditMyPageActivityへ遷移
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
    }

}