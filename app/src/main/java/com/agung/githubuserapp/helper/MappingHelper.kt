package com.agung.githubuserapp.helper

import android.database.Cursor
import com.agung.githubuserapp.db.DatabaseContract
import com.agung.githubuserapp.model.GithubUser

object MappingHelper {
    fun mapCursorToArrayList(githubUsersCursor: Cursor?): ArrayList<GithubUser> {
        val githubUserList = ArrayList<GithubUser>()

        githubUsersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns._ID))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.AVATAR))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.USERNAME))
                githubUserList.add(GithubUser(id, avatar, username))
            }
        }
        return githubUserList
    }

    fun mapCursorToObject(notesCursor: Cursor?): GithubUser {
        var githubUserList = GithubUser()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns._ID))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.AVATAR))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.GithubUserColumns.USERNAME))
            githubUserList = GithubUser(id, avatar, username)
        }
        return githubUserList
    }
}