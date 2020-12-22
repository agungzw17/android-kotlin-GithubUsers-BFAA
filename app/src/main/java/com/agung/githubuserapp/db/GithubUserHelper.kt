package com.agung.githubuserapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.agung.githubuserapp.db.DatabaseContract.GithubUserColumns.Companion.TABLE_NAME
import com.agung.githubuserapp.db.DatabaseContract.GithubUserColumns.Companion.USERNAME
import com.agung.githubuserapp.db.DatabaseContract.GithubUserColumns.Companion._ID
import java.sql.SQLException

class GithubUserHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: GithubUserHelper? = null

        fun getInstance(context: Context): GithubUserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: GithubUserHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${BaseColumns._ID} DESC",
            null)
    }

    fun queryByUsername(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null)
    }


    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun updateByUsername(username: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(username))
    }

    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }
}