package com.agung.githubuserapp.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.agung.githubuserapp.R
import com.agung.githubuserapp.view.fragment.FollowerFragment
import com.agung.githubuserapp.view.fragment.FollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    var username: String? = null
    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.followers, R.string.following)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment.newInstance(username.toString())
            1 -> fragment = FollowingFragment.newInstance(username.toString())
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }

}