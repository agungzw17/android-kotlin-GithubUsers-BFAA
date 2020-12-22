package com.agung.githubuserapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.agung.githubuserapp.db.DatabaseContract.AUTHORITY
import com.agung.githubuserapp.db.DatabaseContract.GithubUserColumns.Companion.CONTENT_URI
import com.agung.githubuserapp.db.DatabaseContract.GithubUserColumns.Companion.TABLE_NAME
import com.agung.githubuserapp.db.GithubUserHelper

class GithubUserProvider : ContentProvider() {

    companion object {
        private const val GITHUBUSER = 1
        private const val GITHUBUSER_USERNAME = 2
        private lateinit var githubUserHelper: GithubUserHelper

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, GITHUBUSER)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", GITHUBUSER_USERNAME)
        }
    }

    override fun onCreate(): Boolean {
        githubUserHelper = GithubUserHelper.getInstance(context as Context)
        githubUserHelper.open()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            GITHUBUSER -> cursor = githubUserHelper.queryAll()
            GITHUBUSER_USERNAME -> cursor = githubUserHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (GITHUBUSER) {
            sUriMatcher.match(uri) -> githubUserHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        val updated: Int = when (GITHUBUSER_USERNAME) {
            sUriMatcher.match(uri) -> githubUserHelper.updateByUsername(uri.lastPathSegment.toString(),contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (GITHUBUSER_USERNAME) {
            sUriMatcher.match(uri) -> githubUserHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}