package com.freisia.github.adapter

import android.content.Context
import android.view.View
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.freisia.github.R
import com.freisia.github.detail.follower.FollowersFragment
import com.freisia.github.detail.following.FollowingFragment

class SectionPagerAdapter(private val mContext: Context,fm : FragmentManager,private val username:
                            String?,private val followerInt: Int?,private val followingInt: Int?) : FragmentPagerAdapter(fm,
                            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    @StringRes
    private val tabTitles = intArrayOf(R.string.tab1,R.string.tab2)
    private var fragmentList: ArrayList<Fragment>? = ArrayList()

    override fun getItem(position: Int): Fragment {
        if(position ==0){
            fragmentList!!.add(position,FollowersFragment(
                position,
                username,
                followerInt
            ))
            FollowersFragment.setFrag(
                fragmentList!![position] as FollowersFragment
            )
        }
        else if(position == 1){
            fragmentList!!.add(position,FollowingFragment(
                position,
                username,
                followingInt
            ))
            FollowingFragment.setFrag(
                fragmentList!![position] as FollowingFragment
            )
        }
        return fragmentList!![position]
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }

    @Suppress("DEPRECATION")
    override fun instantiateItem(container: View, position: Int): Any {
        fragmentList!![position] = super.instantiateItem(container, position) as Fragment
        when(position){
            0 -> FollowersFragment.setFrag(
                fragmentList!![position] as FollowersFragment
            )
            1 -> FollowingFragment.setFrag(
                fragmentList!![position] as FollowingFragment
            )
        }
        return fragmentList!![position]
    }



    override fun getCount(): Int {
        return 2
    }
}