package com.example.test

import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.TextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)


        // 戻るボタンの関数
        backButton()

//        //　編集ボタンの画面遷移関数
//        editMyPage()

        // 編集ボタンの処理
        settingEditDialogs()


    }

    private fun backButton() {
        // ツールバーを取得
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar)

        // 戻るボタンを有効化
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 戻るボタンのクリックイベントを処理
        toolbar.setNavigationOnClickListener {
            finish() // 現在のアクティビティを終了して前の画面に戻る
        }
    }

    // 編集ボタンの処理(それぞれのボタンを押した際に切り替わるように設定)
    private fun settingEditDialogs() {
        editDialog(R.id.editNameTextView, R.id.myName, "名前", "name")
        editDialog(R.id.editAgeTextView, R.id.myAge, "年齢", "age")
        editDialog(R.id.editBodyWeightTextView, R.id.myBodyWeight, "体重", "weight")
        editDialog(R.id.editHeightTextView, R.id.myHeight, "身長", "height")
        editDialog(R.id.editGenderWeightTextView, R.id.myGender, "性別", "gender")
    }


    // ダイアログ表示(押したボタンによって引数を変えてそのダイアログを表示)
    private fun editDialog(editButtonId: Int, textViewId: Int, editTitle: String, change: String) {
        val editButton = findViewById<TextView>(editButtonId)
        val targetTextView = findViewById<TextView>(textViewId)



        editButton.setOnClickListener {
            // ラジオボタン形式の性別のみダイアログレイアウト変更
            if (change == "gender") {
                // 性別編集用ダイアログ
                showGenderDialog(targetTextView, editTitle)
            } else {
                val currentValue = targetTextView.text.toString()       // 現在の表示されている値を取得

                // ダイアログ用レイアウト)
                val layout = LinearLayout(this).apply {
                    LinearLayout.VERTICAL     // 縦並び(タイトルの下に入力欄)
                    setPadding(50, 20, 50, 20)  // 座標調整
                }

                // 入力フィールド
                val inputField = EditText(this)
                // 引数によって条件変更(数値や文字の入力変更)
                when (change) {
                    // 名前欄
                    "name" -> {
                        inputField.setText(currentValue)
                        inputField.isSingleLine = true     // 改行不可設定
                        inputField.filters = arrayOf(InputFilter.LengthFilter(10))     // 文字数10文字以下に設定
                    }
                    // 年齢欄
                    "age" -> {
                        // 数値のみ入力可能に設定
                        inputField.inputType = InputType.TYPE_CLASS_NUMBER
                        inputField.setText(currentValue.replace("歳", ""))
                    }
                    // 体重欄
                    "weight" -> {
                        // 数値のみ入力可能(小数点も入力可)
                        inputField.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                        inputField.setText(currentValue.replace("kg", ""))
                    }
                    // 身長欄
                    "height" -> {
                        // 数値のみ入力可能(小数点も入力可)
                        inputField.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                        inputField.setText(currentValue.replace("cm", ""))
                    }

                }
                //　レイアウト適用
                layout.addView(inputField)

                // ダイアログの表示
                AlertDialog.Builder(this)
                    // タイトル
                    .setTitle("$editTitle を編集")
                    // 入力部分
                    .setView(layout)
                    // 変更ボタン(変更部分を適用)
                    .setPositiveButton("変更") { _, _ ->
                        val input = inputField.text.toString()
                        // 引数によって条件変更(入力の条件設定)
                        when (change) {
                            // 名前(1～10文字制限)
                            "name" -> {
                                if (input.length in 1..10) {
                                    targetTextView.text = input
                                } else {
                                    Toast.makeText(this, "1文字以上10文字以下で入力してください", Toast.LENGTH_SHORT).show()
                                }
                            }
                            // 年齢(0～100歳) 歳は固定
                            "age" -> {
                                val age = input.toIntOrNull()
                                if (age != null && age in 0..100) {
                                    targetTextView.text = "$age 歳"
                                } else {
                                    Toast.makeText(this, "0〜100歳の間で入力してください", Toast.LENGTH_SHORT).show()
                                }
                            }
                            // 体重(10.0kg～250.0kg)　kgは固定
                            "weight" -> {
                                val weight = input.toFloatOrNull()
                                if (weight != null && weight in 10.0..250.0) {
                                    targetTextView.text = "$weight kg"
                                } else {
                                    Toast.makeText(this, "10.0〜250.0kgの間で入力してください", Toast.LENGTH_SHORT).show()
                                }
                            }
                            // 身長(50.0cm～250.0cm) cmは固定
                            "height" -> {
                                val height = input.toFloatOrNull()
                                if (height != null && height in 50.0..250.0) {
                                    targetTextView.text = "$height cm"
                                } else {
                                    Toast.makeText(this, "50.0〜250.0cmの間で入力してください", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                    // キャンセルボタン
                    .setNegativeButton("キャンセル", null)
                    // ダイアログ外をタッチしても閉じないように設定
                    .setCancelable(false)

                    .create()

                    // ダイアログを表示
                    .show()
            }
        }
    }

    // 性別用ラジオボタンダイアログレイアウト
    private fun showGenderDialog(targetTextView: TextView, editTitle: String) {
        // 性別リスト
        val genderLists = listOf("男", "女")
        // ラジオボタンを作成
        val radioGroup = RadioGroup(this)
        RadioGroup.VERTICAL       // 縦並び(タイトルの下に縦並びのラジオボタン)
        radioGroup.setPadding(50, 20, 50, 20)  // 座標調整

        // 性別リストからラジオボタン生成
        genderLists.forEach { gender ->
            val radioButton = RadioButton(this)
            radioButton.text = gender
            radioGroup.addView(radioButton)

            // 現在の性別を選択状態に設定
            if (targetTextView.text.toString() == gender) {
                radioButton.isChecked = true
            }
        }

        // ダイアログ表示
        AlertDialog.Builder(this)
            // タイトル
            .setTitle("$editTitle を編集")
            // 入力部分
            .setView(radioGroup)
            // 変更ボタン(変更部分を適用)
            .setPositiveButton("変更") { _, _ ->
                val selectedId = radioGroup.checkedRadioButtonId
                // ラジオボタンが選択されている場合(-1は何も選択されていない状態でNULL)
                if (selectedId != -1) {
                    val selectedRadioButton = radioGroup.findViewById<RadioButton>(selectedId)
                    targetTextView.text = selectedRadioButton.text
                }
            }
            // キャンセルボタン
            .setNegativeButton("キャンセル", null)
            // ダイアログ外をタッチしても閉じないように設定
            .setCancelable(false)
            // ダイアログを表示
            .show()
    }

}