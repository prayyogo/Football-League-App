package com.dicoding.prayogo.footballLeagueApp.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dicoding.prayogo.footballLeagueApp.R
import com.dicoding.prayogo.footballLeagueApp.detail.DetailLeagueFragment
import com.dicoding.prayogo.footballLeagueApp.detail.StandingsMatchFragment
import com.dicoding.prayogo.footballLeagueApp.detail.TeamListFragment

class DetailPageAdapter (fm: FragmentManager, private val context: Context) :
    FragmentPagerAdapter(fm) {

    private val pages = listOf(
        DetailLeagueFragment(),
        TeamListFragment())

    override fun getItem(position: Int): Fragment {
        return pages[position] as Fragment
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.resources?.getString(R.string.detail)
            else -> context.resources?.getString(R.string.standings)
        }.toString()
    }
}