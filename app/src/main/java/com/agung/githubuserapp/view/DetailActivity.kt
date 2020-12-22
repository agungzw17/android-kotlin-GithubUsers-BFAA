package com.agung.githubuserapp.view

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.agung.githubuserapp.R
import com.agung.githubuserapp.adapter.SectionsPagerAdapter
import com.agung.githubuserapp.db.DatabaseContract
import com.agung.githubuserapp.db.DatabaseContract.GithubUserColumns.Companion.CONTENT_URI
import com.agung.githubuserapp.db.GithubUserHelper
import com.agung.githubuserapp.helper.MappingHelper
import com.agung.githubuserapp.model.GithubUser
import com.agung.githubuserapp.viewmodel.MainDetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var mainDetailViewModel: MainDetailViewModel
    private var statusFavorite: Boolean = false

    private lateinit var githubUserHelper: GithubUserHelper

    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        githubUserHelper = GithubUserHelper.getInstance(applicationContext)
        githubUserHelper.open()

        showDetailGithubUser()
    }

    private fun showDetailGithubUser() {
        val extraGithubUser = intent.getParcelableExtra(EXTRA_GITHUB_USER) as GithubUser?

        val actionbar = supportActionBar
        actionbar!!.title = extraGithubUser?.username
        actionbar.setDisplayHomeAsUpEnabled(true)

        val tvName: TextView = findViewById(R.id.tv_item_name)
        val tvFollower: TextView = findViewById(R.id.tv_item_followers)
        val tvFollowing: TextView = findViewById(R.id.tv_item_following)
        val tvRepositories: TextView = findViewById(R.id.tv_item_repositories)
        val tvGithubUrl: TextView = findViewById(R.id.tv_item_githubUrl)
        val imgPhoto: ImageView = findViewById(R.id.tv_item_image)

        Glide.with(this).load(extraGithubUser?.avatar).into(imgPhoto)
        tvName.text = extraGithubUser?.username

        val getUsername = extraGithubUser?.username.toString()
        mainDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainDetailViewModel::class.java
        )
        mainDetailViewModel.setGithubUsers(applicationContext, getUsername)

        mainDetailViewModel.getGithubUsers().observe(this, Observer { githubUserItems ->
            tvFollower.text = githubUserItems.followers.toString()
            tvFollowing.text = githubUserItems.following.toString()
            tvRepositories.text = githubUserItems.repositories.toString()
            tvGithubUrl.text = githubUserItems.githubUrl
        })

        showSectionPagerAdapter(getUsername)

        statusFavorite = checkStatusFavorite(getUsername)
        changeFloatingIcon(statusFavorite)

        btn_favorite.setOnClickListener {
            val username = extraGithubUser?.username
            val avatar = extraGithubUser?.avatar

            val values = ContentValues()
            values.put(DatabaseContract.GithubUserColumns.USERNAME, username)
            values.put(DatabaseContract.GithubUserColumns.AVATAR, avatar)
            if (!statusFavorite) {
                statusFavorite = true
                btn_favorite.setImageResource(R.drawable.ic_favorite)
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, "Satu item berhasil dijadikan favorit", Toast.LENGTH_SHORT).show()

            } else {
                statusFavorite = false
                btn_favorite.setImageResource(R.drawable.ic_favorite_deactivate)
                contentResolver.delete(Uri.parse("$CONTENT_URI/$username"), null, null)
                Toast.makeText(this, "Telah dikeluarkan dari daftar favorit", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun checkStatusFavorite(username: String):Boolean{
        var statusFavorite = false
        val cursor = contentResolver.query(Uri.parse("$CONTENT_URI/$username"), null, null, null, null)
        if (cursor != null) {
            val users = MappingHelper.mapCursorToArrayList(cursor)
            cursor.close()
            statusFavorite = users.size > 0
        }
        return statusFavorite
    }

    private fun changeFloatingIcon(statusFavorite: Boolean) {
        if(statusFavorite){
            btn_favorite.setImageResource(R.drawable.ic_favorite)
        } else {
            btn_favorite.setImageResource(R.drawable.ic_favorite_deactivate)
        }
    }

    private fun showSectionPagerAdapter(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}