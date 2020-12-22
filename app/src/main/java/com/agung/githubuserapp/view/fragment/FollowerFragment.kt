package com.agung.githubuserapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.githubuserapp.R
import com.agung.githubuserapp.adapter.GithubUserAdapter
import com.agung.githubuserapp.viewmodel.FollowerViewModel
import kotlinx.android.synthetic.main.follower_fragment.*
import kotlinx.android.synthetic.main.follower_fragment.rv_github_user

class FollowerFragment : Fragment() {
    private lateinit var followerViewModel: FollowerViewModel
    private lateinit var adapter: GithubUserAdapter

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments= bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.follower_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME) as String
        showRecyclerGithubUsers(username)

    }

    private fun showRecyclerGithubUsers(username: String) {
        adapter = GithubUserAdapter()
        adapter.notifyDataSetChanged()
        rv_github_user.layoutManager = LinearLayoutManager(activity)
        rv_github_user.adapter = adapter

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowerViewModel::class.java)
        followerViewModel.setGithubUsers(requireActivity().application, username)

        followerViewModel.getGithubUsers().observe(viewLifecycleOwner, Observer { githubUserItems ->
            if(githubUserItems !== null) {
                adapter.setData(githubUserItems)
                showLoading(false)
            } else {
                adapter.setData(arrayListOf())
                showLoading(true)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        follower_progress_bar.visibility = if(state) View.VISIBLE else View.GONE
    }
}