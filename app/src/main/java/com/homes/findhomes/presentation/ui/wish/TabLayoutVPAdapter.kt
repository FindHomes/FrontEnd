package com.homes.findhomes.presentation.ui.wish

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabLayoutVPAdapter(activity : FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> RecentViewFragment()
            1 -> FavoriteFragment()
            else -> SearchHistoryFragment()
        }
    }
}