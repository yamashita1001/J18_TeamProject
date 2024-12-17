package com.example.test

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.utils.ColorTemplate
import android.os.Handler
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeActivity : AppCompatActivity() {

    // 合計摂取量変数
    companion object {
        // 静的に合計を保持
        var sumWaterValue = 0
        var goalWaterValue = 2000
        var percentWaterValue:Long = 0

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


        // コンポーネントを取得
        val inputWaterButton: FloatingActionButton = findViewById(R.id.inputWaterButton)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        val inputWaterValueTextView = findViewById<TextView>(R.id.inputWaterValueTextView)
        val messagetextView = findViewById<TextView>(R.id.messagetextView)

        // InputWaterからデータ受け取り
        val waterValue = intent.getIntExtra("TEXT_KEY", 0)
        // InputWaterから値を継承したものを合計して表示
        if (waterValue != null) {
            sumWaterValue += waterValue // 合計に加算
            percentWaterValue = Math.round(sumWaterValue.toDouble() / goalWaterValue.toDouble() * 100)
        }


        // 水分摂取合計を表示
        inputWaterValueTextView.text = "合計摂取量: $sumWaterValue ml / 2000ml"

        // 水分摂取量の円グラフ
        // コンポーネントを取得
        val pieChart: PieChart = findViewById(R.id.pieChart)

        // グラフのデータを設定(float型)
        val value: ArrayList<PieEntry> = ArrayList()
        // 割合を表示(上:摂取した割合 下:残りの割合)
        value.add(PieEntry((percentWaterValue.toFloat()), ""))
        // 100%以下は割合を変更
        if(0 < (100F - percentWaterValue.toFloat())) {
            value.add(PieEntry(100F - percentWaterValue.toFloat(), ""))
        }
        // 100%以上になった場合は割合を0に固定
        else {
            value.add(PieEntry(0F,""))
        }


        // 表示する凡例の色を設定
        val color: ArrayList<Int> = ArrayList()
        color.add(Color.rgb(8,255,238))
        color.add(Color.rgb(230,230,230))


        // 真ん中に現在の摂取量の割合を表示(%)
        pieChart.centerText = "水分摂取量\n $percentWaterValue %/100%"
        pieChart.setCenterTextSize(20f) // テキストサイズ

        // chartに設定
        val dataSet = PieDataSet(value, "waterValue")
        dataSet.colors = color
        pieChart.data = PieData(dataSet)

        // 円グラフ非表示設定
        // グラフの一部非表示
        dataSet.setDrawValues(false) // ラベルの非表示
        dataSet.setDrawIcons(false) // アイコンも非表示
        // グラフ左下の凡例非表示
        pieChart.legend.isEnabled = false
        // description非表示
        pieChart.description.isEnabled = false

        // グラフ回転不可設定
        pieChart.isRotationEnabled = false

        // 凡例の文字色変更
        dataSet.setDrawValues(false) // ラベルを非表示に設定
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 14f

        // グラフの更新
        pieChart.invalidate()

        // ----------------------------------------------------------------------------- //
        // - 一言メッセージ表示機能 -------------------------------------------------------- //
        // ----------------------------------------------------------------------------- //

        // 指定した時間（ミリ秒後）に処理を実行するメソッド
        // 引数：delayMillis: Long → 遅延のミリ秒数を指定
        fun scheduleTaskExecution(actionTimestamp: Long, delayMillis:Long) {
            val handler = Handler()

            handler.postDelayed({
                val currentTime = System.currentTimeMillis()
                val timeElapsed = currentTime - actionTimestamp

                messagetextView.text = if (timeElapsed >= delayMillis) {
                    "水分摂取を推奨します"
                } else {
                    "水分摂取から $timeElapsed 分経過しました"
                }
            }, delayMillis)
        }

        // 追加ボタン押した時の時間受取
        val receivedTimestamp = intent.getLongExtra("TIMESTAMP_KEY", 0L)
        // 第二引数経過後に関数呼び出し
        scheduleTaskExecution(receivedTimestamp, 1 * 5 * 1000)

        // ----------------------------------------------------------------------------- //
        // ----------------------------------------------------------------------------- //
        // ----------------------------------------------------------------------------- //


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


        // ハンバーガーアイコンの設定
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 水分追加の＋ボタンの処理
        inputWaterButton.setOnClickListener {
            // InputWaterActivityへ遷移
            val intent = Intent(this, InputWaterActivity::class.java)
            startActivity(intent)
        }
    }
}