package com.agung.githubuserapp.viewmodel

import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agung.githubuserapp.db.DatabaseContract
import com.agung.githubuserapp.helper.MappingHelper
import com.agung.githubuserapp.model.GithubUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel(){
    val listGithubUser = MutableLiveData<ArrayList<GithubUser>>()

    fun setGithubUsers(contentResolver: ContentResolver){
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(DatabaseContract.GithubUserColumns.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val githubUsers = deferredNotes.await()
//            if (githubUsers.size > 0) {
//                showLoading(false)
//                adapter.listGithubUser = githubUsers
//            } else {
//                adapter.listGithubUser = java.util.ArrayList()
//                showSnackbarMessage("Tidak ada data saat ini")
//            }
            listGithubUser.postValue(githubUsers)
        }
    }

    fun getGithubUsers() : LiveData<ArrayList<GithubUser>> {
        return listGithubUser
    }
}