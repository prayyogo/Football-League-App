package com.dicoding.prayogo.footballLeagueApp.team

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.prayogo.footballLeagueApp.DetailLeagueActivity
import com.dicoding.prayogo.footballLeagueApp.R
import kotlinx.android.synthetic.main.activity_team.*
import com.dicoding.prayogo.footballLeagueApp.R.layout.activity_team
import com.dicoding.prayogo.footballLeagueApp.R.id.*


class TeamActivity : AppCompatActivity() {

    companion object {
        var leagueId: String? = null
        const val TEAM_ID = "TEAM_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_team)
        leagueId = intent.getStringExtra(
            DetailLeagueActivity.LEAGUE_ID
        )

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                teams -> {
                    loadTeamsFragment(savedInstanceState)
                    supportActionBar?.title = resources.getString(R.string.team_list)
                }
                favorites -> {
                    loadFavoritesFragment(savedInstanceState)
                    supportActionBar?.title = resources.getString(R.string.favorite_team)
                }
            }
            true
        }
        bottom_navigation.selectedItemId = teams

    }

    private fun loadTeamsFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_container,
                    TeamListFragment(),
                    TeamListFragment::class.java.simpleName
                )
                .commit()
        }
    }

    private fun loadFavoritesFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_container,
                    FavoriteTeamFragment(),
                    FavoriteTeamFragment::class.java.simpleName
                )
                .commit()
        }
    }
}
