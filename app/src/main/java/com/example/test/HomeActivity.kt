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
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {

    // 合計摂取量変数
    companion object {
        // 静的に合計を保持
        var sumWaterValue = 0
        var percentWaterValue:Long = 0
        var goalWaterValue: Int? = null // 初期値をnullにして未設定を示す
    }

    private var isExecuted: Boolean = false // アクティビティ内でのみ有効なフラグ


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // goalWaterValueが未設定の場合のみ更新(初回のみ実行)
        if (goalWaterValue == null) {
            // 合計摂取量の計算結果をホームで設定
            val waterSum = intent.getIntExtra("waterIntake", 2000) // デフォルト値2000(適当)
            Log.d("HomeActivity", "受け渡した値: $waterSum")
            goalWaterValue = waterSum       // 目標摂取量に計算結果を格納
            Log.d("HomeActivity", "goalWaterValue: $goalWaterValue")
        } else {
            Log.d("HomeActivity", "goalWaterValue: $goalWaterValue")
        }


        // ツールバーのセットアップ
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // アクションバーのタイトルを消す
        supportActionBar?.title = ""

        // コンポーネントを取得
        val inputWaterButton: FloatingActionButton = findViewById(R.id.inputWaterButton)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val inputWaterValueTextView = findViewById<TextView>(R.id.inputWaterValueTextView)
        val messagetextView = findViewById<TextView>(R.id.messagetextView)
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // InputWaterからデータ受け取り
        val waterValue = intent.getIntExtra("TEXT_KEY", 0)
        // InputWaterから値を継承したものを合計して表示
        if (waterValue != null) {
            Log.d("HomeActivity", "goalWaterValue: $goalWaterValue")
            sumWaterValue += waterValue // 合計に加算
            percentWaterValue = Math.round(sumWaterValue.toDouble() / goalWaterValue!!.toDouble() * 100)
        }


        // 水分摂取合計を表示
        inputWaterValueTextView.text = "$sumWaterValue ml"

        // ----------------------------------------------------------------------------- //
        // - 円グラフ描画 ----------------------------------------------------------------- //
        // ----------------------------------------------------------------------------- //

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
        scheduleTaskExecution(receivedTimestamp, 60 * 5 * 1000)

        // ----------------------------------------------------------------------------- //
        // - 画面遷移 --------------------------------------------------------------------- //
        // ----------------------------------------------------------------------------- //

        // メニュー項目のクリック処理
        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item1 -> {
                    // Home
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.item2 -> {
                    // Graph
                    val intent = Intent(this, IntakeActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.item3 -> {
                    // settings
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        // 水分追加の＋ボタンの処理
        inputWaterButton.setOnClickListener {
            // InputWaterActivityへ遷移
            val intent = Intent(this, InputWaterActivity::class.java)
            startActivity(intent)
        }
    }
}