package com.dicoding.prayogo.footballLeagueApp.match

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import com.dicoding.prayogo.footballLeagueApp.DetailLeagueActivity
import com.dicoding.prayogo.footballLeagueApp.R
import com.dicoding.prayogo.footballLeagueApp.adapter.FavoritePageAdapter
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedTabLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.viewPager

class FavoriteMatchActivity : AppCompatActivity() {
    companion object {
        var leagueId: String? = null
    }

    private lateinit var matchViewPager: ViewPager
    private lateinit var matchTabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueId = intent.getStringExtra(DetailLeagueActivity.LEAGUE_ID)
        supportActionBar?.elevation = 0F
        coordinatorLayout {
            lparams(matchParent, matchParent)

            matchViewPager = viewPager {
                id = R.id.vp_viewpager
            }.lparams(width = matchParent)
            matchTabLayout = themedTabLayout(R.style.ThemeOverlay_AppCompat_Dark) {
                id = R.id.tab_layout_favorite
                lparams(width = matchParent)
                {
                    tabGravity = Gravity.FILL
                    tabMode = TabLayout.MODE_FIXED
                    gravity = Gravity.TOP
                    backgroundColor = Color.parseColor("#303F9F")
                }
            }
        }
        matchViewPager.adapter = FavoritePageAdapter(
            supportFragmentManager,
            this
        )
        matchTabLayout.setupWithViewPager(matchViewPager)
        supportActionBar?.title = resources.getString(R.string.favorite_match)
    }
}
