package com.agung.githubuserapp.view

import androidx.lifecycle.Observer
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.githubuserapp.R
import com.agung.githubuserapp.adapter.GithubUserAdapter
import com.agung.githubuserapp.db.DatabaseContract.GithubUserColumns.Companion.CONTENT_URI
import com.agung.githubuserapp.helper.MappingHelper
import com.agung.githubuserapp.model.GithubUser
import com.agung.githubuserapp.viewmodel.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.rv_github_user
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: GithubUserAdapter
//    private lateinit var favoriteViewModel: FavoriteViewModel

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        showLoading(true)
        showRecyclerGithubUsers()

        if (savedInstanceState == null) {
//            favoriteViewModel.getGithubUsers().observe(this, Observer { githubUserItems ->
//                if(githubUserItems !== null) {
//                    adapter.setData(githubUserItems)
//                    showLoading(false)
//                } else {
//                    adapter.setData(arrayListOf())
//                    showLoading(true)
//                }
//            })
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<GithubUser>(EXTRA_STATE)
            if (list != null) {
                adapter.listGithubUser = list
            }
        }
    }

    private fun showRecyclerGithubUsers() {
        val actionbar = supportActionBar
        actionbar!!.title = "Favorite"
        actionbar.setDisplayHomeAsUpEnabled(true)

        adapter = GithubUserAdapter()
        adapter.notifyDataSetChanged()
        rv_github_user.layoutManager = LinearLayoutManager(this)
        rv_github_user.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

//        favoriteViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FavoriteViewModel::class.java)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
//                favoriteViewModel.setGithubUsers(contentResolver)
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

//        favoriteViewModel.getGithubUsers().observe(this, Observer { githubUserItems ->
//            if(githubUserItems !== null) {
//                adapter.setData(githubUserItems)
//                showLoading(false)
//            } else {
//                adapter.setData(arrayListOf())
//                showLoading(true)
//            }
//        })

    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val githubUsers = deferredNotes.await()
            if (githubUsers.size > 0) {
                showLoading(false)
                adapter.listGithubUser = githubUsers
            } else {
                adapter.listGithubUser = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listGithubUser)
    }

    private fun showLoading(state: Boolean) {
        favorite_progress_bar.visibility = if(state) View.VISIBLE else View.GONE
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_github_user, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}