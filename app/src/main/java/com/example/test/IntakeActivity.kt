package com.example.test

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter


class IntakeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intake)

        // コンポーネント取得
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val StartimageView = findViewById<ImageView>(R.id.StartimageView)
        val FinishimageView = findViewById<ImageView>(R.id.FinishimageView)
        val StartText = findViewById<TextView>(R.id.StartText)
        val FinishText = findViewById<TextView>(R.id.FinishText)
        val barChart = findViewById<BarChart>(R.id.barChart)
        val GraphSearchButton = findViewById<Button>(R.id.GraphSearchbutton)
        val BarChartTitle = findViewById<TextView>(R.id.barChartTitle)

        // ----------------------------------------------------------------------------- //
        // - カレンダー表示 --------------------------------------------------------------- //
        // ----------------------------------------------------------------------------- //

        // 現在の日付を取得
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // 開始日カレンダー画像クリックリスナー
        StartimageView.setOnClickListener {
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // 月と日が２桁の場合0を削除
                val formattedMonth = if (selectedMonth + 1 < 10) "0${selectedMonth + 1}" else (selectedMonth + 1).toString()
                val formattedDay = if (selectedDay < 10) "0${selectedDay}" else (selectedDay).toString()

                StartText.text = "$formattedMonth/$formattedDay"
            }, year, month, day).show()
        }

        // 終了日ボタンのクリックリスナー
        FinishimageView.setOnClickListener {
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = if (selectedMonth + 1 < 10) "0${selectedMonth + 1}" else (selectedMonth + 1).toString()
                val formattedDay = if (selectedDay < 10) "0${selectedDay}" else (selectedDay).toString()

                FinishText.text = "$formattedMonth/$formattedDay"
            }, year, month, day).show()
        }

        // ----------------------------------------------------------------------------- //
        // - 棒グラフ描画 ----------------------------------------------------------------- //
        // ----------------------------------------------------------------------------- //

        GraphSearchButton.setOnClickListener() {

            // データ作成
            // y軸（水分摂取量）
            val values = listOf(
                1520, 1740, 1800, 1930, 2150, 1600, 1870, 2100, 1960,
                2200, 1850, 1980, 1760, 2250, 1620, 1720, 2060, 2350,
                1580, 2450, 1600, 2140, 1980, 1880, 2120, 1730, 2550,
                1600, 1940, 2500, 1910
            )
            // x軸（日付）
            val startDay = StartText.text.substring(StartText.text.indexOf("/") + 1).toInt()
            val finishDay = FinishText.text.substring(FinishText.text.indexOf("/") + 1).toInt()

            // BarEntryリストを作成
            val entries = mutableListOf<BarEntry>()
            for (i in values.indices) {
                val day = startDay + i
                if (day <= finishDay) {
                    entries.add(BarEntry(day.toFloat(), values[i].toFloat()))
                }
            }

            // データセットを作成
            val dataSet = BarDataSet(entries, "日別水分摂取量")
            dataSet.colors = listOf(
                android.graphics.Color.parseColor("#1E90FF"),
                android.graphics.Color.parseColor("#008080")
            ).let { colors -> List(entries.size) { colors[it % 2] } }

            // データをBarChartにセット
            val barData = BarData(dataSet)
            barChart.data = barData

            // 軸の設定
            val xAxis = barChart.xAxis
            xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f // 日付単位
            xAxis.setDrawGridLines(false) // グリッド線を非表示

            // y軸
            barChart.axisLeft.axisMinimum = 0f
            barChart.axisLeft.axisMaximum = 3000f
            barChart.axisRight.isEnabled = false

            // 細かい整形
            barChart.description.isEnabled = false // Descriptionラベルを非表示
            barChart.visibility = View.VISIBLE
            barChart.legend.isEnabled = false // 凡例（ラベルと色の□）を非表示

            // アニメーション
            barChart.animateY(2000) // 棒が波のように上昇するアニメーション
            dataSet.setGradientColor(Color.parseColor("#1E90FF"), Color.parseColor("#FFFFFF")) // アニメーションのループで透明度を動かす（例）
            barChart.animateY(3000, Easing.EaseInOutElastic)

            // 背景の設定
            barChart.setDrawGridBackground(true)
            barChart.setGridBackgroundColor(Color.parseColor("#E0F7FA"))

            // グラフの更新
            BarChartTitle.text = "水分摂取量"
            barChart.invalidate()
        }

        // ----------------------------------------------------------------------------- //
        // - 画面遷移 --------------------------------------------------------------------- //
        // ----------------------------------------------------------------------------- //

        // メニュー項目のクリック処理
        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item1 -> {
                    // Home
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
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
    }

    class DateValueFormatter(private val dates: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val index = value.toInt()
            return if (index >= 0 && index < dates.size) {
                dates[index]
            } else {
                ""
            }
        }
    }
}
