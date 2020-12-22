package com.agung.githubuserapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DatabaseHelper(context: Context) :  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_GITHUB_USERS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $DatabaseContract.GithubUserColumns.TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbgithubusersapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_GITHUB_USERS = "CREATE TABLE ${DatabaseContract.GithubUserColumns.TABLE_NAME}" +
                " (${DatabaseContract.GithubUserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.GithubUserColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseContract.GithubUserColumns.USERNAME} TEXT NOT NULL)"
    }
}