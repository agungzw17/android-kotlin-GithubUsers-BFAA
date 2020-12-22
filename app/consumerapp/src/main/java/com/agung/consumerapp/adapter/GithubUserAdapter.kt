package com.agung.consumerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agung.consumerapp.R
import com.agung.consumerapp.model.GithubUser
import com.bumptech.glide.Glide

class GithubUserAdapter : RecyclerView.Adapter<GithubUserAdapter.GithubUserViewHolder>() {
    var listGithubUser = ArrayList<GithubUser>()
        set(listGithubUser) {
            if (listGithubUser.size > 0) {
                this.listGithubUser.clear()
            }
            this.listGithubUser.addAll(listGithubUser)

            notifyDataSetChanged()
        }

    fun setData(items: ArrayList<GithubUser>) {
        listGithubUser.clear()
        listGithubUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_github_users,
            parent,
            false
        )
        return GithubUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        val githubUser = listGithubUser[position]

        Glide.with(holder.itemView.context)
            .load(githubUser.avatar)
            .into(holder.imgPhoto)
        holder.tvName.text = githubUser.username

        val mContext = holder.itemView.context
//        holder.itemView.setOnClickListener {
//            val moveDetailActivity = Intent(mContext, DetailActivity::class.java)
//            moveDetailActivity.putExtra(DetailActivity.EXTRA_GITHUB_USER, listGithubUser[position])
//            mContext.startActivity(moveDetailActivity)
//        }
    }

    override fun getItemCount(): Int {
        return listGithubUser.size
    }

    inner class GithubUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.tv_item_image)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
    }
}