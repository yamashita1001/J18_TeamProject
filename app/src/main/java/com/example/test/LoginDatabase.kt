package com.example.test

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LoginDatabase(private val context: Context) : SQLiteOpenHelper(context, "WaterDatabase", null, 7) {

    // ---------------------------------------------------------------------- //
    // テーブルを作成する関数
    // ---------------------------------------------------------------------- //
    override fun onCreate(db: SQLiteDatabase?) {
        // Usersテーブル作成するSQL文
        val createUsersTableQuery = """
            CREATE TABLE IF NOT EXISTS Users (
                userId TEXT NOT NULL UNIQUE PRIMARY KEY, 
                name TEXT,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL UNIQUE,
                age INTEGER,
                gender TEXT,
                height INTEGER,
                weight INTEGER
            )
        """
        db?.execSQL(createUsersTableQuery) // Usersテーブル作成

        // Loginテーブル作成するSQL文
        val createLoginTableQuery = """
            CREATE TABLE IF NOT EXISTS Login ( 
                userId TEXT UNIQUE PRIMARY KEY,
                lastlogin DATETIME,
                FOREIGN KEY (userId) REFERENCES Users(userId)
            ) 
        """
        db?.execSQL(createLoginTableQuery) // Loginテーブル作成

        // Historyテーブル作成するSQL文
        val createHistoryTableQuery = """
            CREATE TABLE IF NOT EXISTS History (
                historyId INTEGER PRIMARY KEY AUTOINCREMENT,
                userId TEXT NOT NULL UNIQUE,
                day DATE NOT NULL,
                waterIntake INTEGER NOT NULL,
                FOREIGN KEY (userId) REFERENCES Users(userId)
            )
        """
        db?.execSQL(createHistoryTableQuery) // Historyテーブル作成
    }

    // ---------------------------------------------------------------------- //
    // バージョン変更時に実行
    // ---------------------------------------------------------------------- //
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Users, Login, History テーブルを削除
//        db?.execSQL("DROP TABLE IF EXISTS Users")
//        db?.execSQL("DROP TABLE IF EXISTS Login")
//        db?.execSQL("DROP TABLE IF EXISTS History")
//        val dbFile = context.getDatabasePath("UserDatabase")
//        if (dbFile.exists()) {
//            dbFile.delete()  // データベースファイルを削除
//        }

        // 新しいテーブルを作成
        onCreate(db)
    }

    // ---------------------------------------------------------------------- //
    // DBにユーザー追加
    // ---------------------------------------------------------------------- //
    fun addUser(userId: String, email: String, password: String): Boolean {

        val db = writableDatabase       // 編集可能なデータベースインスタンスを格納

        val values = ContentValues().apply {
            put("userId", userId)           // カラム名"user_id"にuser_idを代入
            put("email", email)             // カラム名"email"にemailを代入
            put("password", password)       // カラム名"password"にpasswordを代入
        }
        // SQLにデータ挿入
        val result = db.insert("Users", null, values)
        db.close()
        return result != -1L
    }

    // ---------------------------------------------------------------------- //
    // ユーザー検証
    // ---------------------------------------------------------------------- //
    fun validateUser(userId: String, password: String): Boolean {
        val db = readableDatabase   // 読取可能なデータベースインスタンスを格納
        val query = "SELECT * FROM Users WHERE userId = ? AND password = ?"
        // sqlクエリの?に値を入れて実行
        val cursor = db.rawQuery(query, arrayOf(userId, password))
        // 一行以上返されたらtrueを格納
        val isValid = cursor.count > 0
        cursor.close()
        db.close()
        return isValid
    }

    // ---------------------------------------------------------------------- //
    // userIdからログイン時のメールアドレス取得
    // ---------------------------------------------------------------------- //
    fun getEmailByUserId(userId: String): String? {
        val db = this.readableDatabase
        val query = "SELECT email FROM Users WHERE userId = ?"
        val cursor = db.rawQuery(query, arrayOf(userId))
        var email: String? = null

        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
        }

        cursor.close()
        db.close()
        return email
    }

    // ---------------------------------------------------------------------- //
    // 初回身体情報保存
    // ---------------------------------------------------------------------- //
    fun saveUserBodyInfo(userId: String, email: String, password: String, age: Int, gender: String, weight: Double, height: Int, name: String): Boolean {
        val db = writableDatabase       // 編集可能なデータベースインスタンスを格納
        val values = ContentValues().apply {
            put("userId", userId)
            put("email", email)
            put("password", password)
            put("name", name)
            put("age", age)
            put("gender", gender)
            put("weight", weight)
            put("height", height)

        }
        // SQLにデータ挿入
        val result = db.insertWithOnConflict("Users", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return result != -1L
    }

    // ---------------------------------------------------------------------- //
    // 身体情報の取得
    // ---------------------------------------------------------------------- //
    data class UserBodyInfo(
        val height: Int,
        val weight: Double,
        val age: Int
    )
    // userIdから身体情報を取得
    fun getUserBodyInfo(userId: String): UserBodyInfo? {
        val db = this.readableDatabase
        val query = "SELECT height, weight, age FROM Users WHERE userId = ?"
        val cursor = db.rawQuery(query, arrayOf(userId))
        var userBodyInfo: UserBodyInfo? = null

        // 身長・体重・年齢をuserBodyInfoに格納
        if (cursor.moveToFirst()) {
            val height = cursor.getInt(cursor.getColumnIndexOrThrow("height"))
            val weight = cursor.getDouble(cursor.getColumnIndexOrThrow("weight"))
            val age = cursor.getInt(cursor.getColumnIndexOrThrow("age"))
            userBodyInfo = UserBodyInfo(height, weight, age)
        }

        cursor.close()
        db.close()
        // 取得した情報をデータクラスとして返す
        return userBodyInfo
    }
}