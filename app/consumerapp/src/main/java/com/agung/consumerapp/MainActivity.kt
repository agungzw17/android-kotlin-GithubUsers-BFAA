package com.agung.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.consumerapp.adapter.GithubUserAdapter
import com.agung.consumerapp.db.DatabaseContract.GithubUserColumns.Companion.CONTENT_URI
import com.agung.consumerapp.helper.MappingHelper
import com.agung.consumerapp.model.GithubUser
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: GithubUserAdapter
    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Consumer Favorite Github"
        showLoading(true)
        showRecyclerGithubUsers()

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<GithubUser>(EXTRA_STATE)
            if (list != null) {
                adapter.listGithubUser = list
            }
        }
    }

    private fun showRecyclerGithubUsers() {

        adapter = GithubUserAdapter()
        adapter.notifyDataSetChanged()
        rv_github_user.layoutManager = LinearLayoutManager(this)
        rv_github_user.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

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
}
