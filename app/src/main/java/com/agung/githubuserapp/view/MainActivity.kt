package com.agung.githubuserapp.view

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.githubuserapp.R
import com.agung.githubuserapp.adapter.GithubUserAdapter
import com.agung.githubuserapp.db.DatabaseContract.GithubUserColumns.Companion.CONTENT_URI
import com.agung.githubuserapp.db.GithubUserHelper
import com.agung.githubuserapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: GithubUserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var githubUserHelper: GithubUserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showRecyclerGithubUsers()
        searchGithubUsers()
    }

    private fun showRecyclerGithubUsers() {
        adapter = GithubUserAdapter()
        adapter.notifyDataSetChanged()
        rv_github_user.layoutManager = LinearLayoutManager(this)
        rv_github_user.adapter = adapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.setGithubUsers(applicationContext)

        mainViewModel.getGithubUsers().observe(this, Observer { githubUserItems ->
            if(githubUserItems !== null) {
                adapter.setData(githubUserItems)
                showLoading(false)
            } else {
                adapter.setData(arrayListOf())
                showLoading(true)
            }
        })
    }

    private fun searchGithubUsers() {
        search.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mainViewModel.searchGithubUsers(applicationContext, newText)
                return true
            }
        })
    }

    private fun showLoading(state: Boolean) {
        progress_bar.visibility = if(state) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_setting -> {
                val moveSettingActivity = Intent(this, SettingActivity::class.java)
                startActivity(moveSettingActivity)
            }
            R.id.action_favorite -> {
                val moveFavoriteActivity = Intent(this, FavoriteActivity::class.java)
                startActivity(moveFavoriteActivity)
            }
        }
    }
}