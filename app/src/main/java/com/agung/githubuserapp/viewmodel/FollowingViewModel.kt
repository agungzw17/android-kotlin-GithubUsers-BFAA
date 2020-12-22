package com.agung.githubuserapp.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agung.githubuserapp.model.GithubUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel : ViewModel() {
    val listGithubUser = MutableLiveData<ArrayList<GithubUser>>()

    fun setGithubUsers(context: Context, username: String){
        val listItems = ArrayList<GithubUser>()

        val url = "https://api.github.com/users/$username/following"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token d530a4b7749f868240ef2ddd609a24ae1f2768d2")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let {
                    String(it)
                }
                try {
                    val list = JSONArray(result)
                    for (i in 0 until list.length()) {
                        val githubUser = list.getJSONObject(i)
                        val githubUserItem = GithubUser()
                        githubUserItem.id = githubUser.getInt("id")
                        githubUserItem.avatar = githubUser.getString("avatar_url")
                        githubUserItem.username = githubUser.getString("login")
                        listItems.add(githubUserItem)
                    }

                    listGithubUser.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getGithubUsers() : LiveData<ArrayList<GithubUser>> {
        return listGithubUser
    }
}