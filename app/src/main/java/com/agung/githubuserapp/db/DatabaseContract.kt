package com.agung.githubuserapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.agung.githubuserapp"
    const val SCHEME = "content"

    class GithubUserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_users"
            const val _ID = "_id"
            const val AVATAR = "avatar"
            const val USERNAME = "username"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}