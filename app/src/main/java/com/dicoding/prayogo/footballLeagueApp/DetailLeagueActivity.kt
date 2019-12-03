package com.dicoding.prayogo.footballLeagueApp

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.Gravity
import com.dicoding.prayogo.footballLeagueApp.adapter.DetailPageAdapter
import com.dicoding.prayogo.footballLeagueApp.model.League
import org.jetbrains.anko.*
import org.jetbrains.anko.design.*
import org.jetbrains.anko.support.v4.viewPager

class DetailLeagueActivity : AppCompatActivity(){
    companion object {
        var leagueId: String? = null
        var league: League? = null
        const val MATCH = "MATCH"
        const val TYPE_MATCH = "TYPE_MATCH"
        const val LEAGUE_ID = "LEAGUE_ID"
    }
    private lateinit var matchViewPager: ViewPager
    private lateinit var matchTabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUI()
    }

    private fun createUI() {
        frameLayout {

                coordinatorLayout {
                    lparams(matchParent, matchParent)

                    matchViewPager = viewPager {
                        id = R.id.vp_viewpager
                    }.lparams(width = matchParent)
                    matchTabLayout = themedTabLayout(R.style.ThemeOverlay_AppCompat_Dark) {
                        id = R.id.tab_layout_detail
                        lparams(width = matchParent)
                        {
                            tabGravity = Gravity.FILL
                            tabMode = TabLayout.MODE_FIXED
                            gravity = Gravity.TOP
                            backgroundColor = Color.parseColor("#303F9F")
                        }
                    }
                }
            }
     /*   matchViewPager.adapter = DetailPageAdapter(
            supportFragmentManager,
            this
        )
        matchTabLayout.setupWithViewPager(matchViewPager)
*/
        val intent = intent
        league = intent.getParcelableExtra(MainActivity.LEAGUE)

        leagueId = league?.leagueId
        supportActionBar?.title = league?.leagueName
    }
}
