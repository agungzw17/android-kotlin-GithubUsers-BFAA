package com.agung.githubuserapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser (
    var id: Int = 0,
    var avatar: String = "",
    var username: String = "",
    var followers: Int = 0,
    var following: Int = 0,
    var repositories: Int = 0,
    var githubUrl: String = "",
    var company: String? = null,
    var location: String? = null
) : Parcelable