package com.example.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class InputWaterActivity : AppCompatActivity() {

    private lateinit var buttonGrid: GridLayout
    private val CUSTOM_INPUT_REQUEST_CODE = 1001
    private val customButtons = mutableListOf<Int>() // カスタムボタンの容量を保存

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inputwater)

        buttonGrid = findViewById(R.id.buttonGrid)
        val button120 = findViewById<ImageButton>(R.id.button120)
        val button200 = findViewById<ImageButton>(R.id.button200)
        val button500 = findViewById<ImageButton>(R.id.button500)
        val button1000 = findViewById<ImageButton>(R.id.button1000)
        val button1500 = findViewById<ImageButton>(R.id.button1500)
        val button2000 = findViewById<ImageButton>(R.id.button2000)
        val addCustomButton = findViewById<ImageButton>(R.id.addCustomButton)

        // ツールバーの設定
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, android.R.color.black))

        // 各ボタンのクリックリスナー設定
        button120.setOnClickListener { navigateWithWaterValue(120) }
        button200.setOnClickListener { navigateWithWaterValue(200) }
        button500.setOnClickListener { navigateWithWaterValue(500) }
        button1000.setOnClickListener { navigateWithWaterValue(1000) }
        button1500.setOnClickListener { navigateWithWaterValue(1500) }
        button2000.setOnClickListener { navigateWithWaterValue(2000) }

        // カスタムボタンを追加するためのリスナー
        addCustomButton.setOnClickListener {
            val intent = Intent(this, CustomInputActivity::class.java)
            startActivityForResult(intent, CUSTOM_INPUT_REQUEST_CODE)
        }

        // 保存されているカスタムボタンを復元
        restoreCustomButtons()
    }

    // カスタム容量を受け取る
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CUSTOM_INPUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val waterValue = data?.getIntExtra("CUSTOM_WATER_VALUE", -1) ?: -1
            if (waterValue > 0) {
                addCustomButtonToGrid(waterValue)
            } else {
                Toast.makeText(this, "正しい水分量を取得できませんでした", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 動的にボタンとテキストをGridLayoutに追加し、保存
    private fun addCustomButtonToGrid(waterValue: Int) {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1)
            }
        }

        val newButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(128.dpToPx(context), 128.dpToPx(context))
            setBackgroundResource(0)
            setImageResource(R.drawable.ic_custom_cup) // カスタムボタンの画像を指定
            contentDescription = "${waterValue}ミリリットルボタン"
            scaleType = ImageView.ScaleType.FIT_CENTER
            setOnClickListener {
                navigateWithWaterValue(waterValue)
            }
        }

        val textView = TextView(this).apply {
            text = "${waterValue}ml"
            textSize = 20f // 20dp相当
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.CENTER // layout_gravity="center"
                setMargins(0, 8.dpToPx(context), 0, 24.dpToPx(context)) // layout_marginBottom=24dp
            }
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }

        // ボタンとテキストをコンテナに追加
        container.addView(newButton)
        container.addView(textView)

        // コンテナをGridLayoutに追加
        buttonGrid.addView(container, buttonGrid.childCount - 1)

        // ボタン情報を保存
        saveCustomButton(waterValue)
    }


    // カスタムボタンの容量を保存
    private fun saveCustomButton(waterValue: Int) {
        customButtons.add(waterValue)
        val sharedPreferences = getSharedPreferences("CustomButtons", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("BUTTONS", customButtons.joinToString(","))
        editor.apply()
    }

    // カスタムボタンの復元
    private fun restoreCustomButtons() {
        val sharedPreferences = getSharedPreferences("CustomButtons", MODE_PRIVATE)
        val savedButtons = sharedPreferences.getString("BUTTONS", null) ?: return
        savedButtons.split(",").mapNotNull { it.toIntOrNull() }.forEach {
            addCustomButtonToGrid(it)
        }
    }

    // dpをpxに変換
    fun Int.dpToPx(context: android.content.Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    // 水分量を次の画面に渡す
    private fun navigateWithWaterValue(waterValue: Int) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("TEXT_KEY", waterValue)
        startActivity(intent)
    }
}
