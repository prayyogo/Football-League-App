package com.dicoding.prayogo.footballLeagueApp.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dicoding.prayogo.footballLeagueApp.R
import com.dicoding.prayogo.footballLeagueApp.match.FavoriteNextMatchFragment
import com.dicoding.prayogo.footballLeagueApp.match.FavoritePreviousMatchFragment

class FavoritePageAdapter(fm: FragmentManager, private val context: Context) :
    FragmentPagerAdapter(fm) {

    private val pages = listOf(
        FavoritePreviousMatchFragment(),
        FavoriteNextMatchFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position] as Fragment
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.resources?.getString(R.string.previous_match)
            else -> context.resources?.getString(R.string.next_match)
        }.toString()
    }
}