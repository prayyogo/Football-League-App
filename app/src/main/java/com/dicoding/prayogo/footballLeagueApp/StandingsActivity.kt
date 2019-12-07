package com.dicoding.prayogo.footballLeagueApp

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.dicoding.prayogo.footballLeagueApp.adapter.StandingsAdapter
import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.model.Standings
import com.dicoding.prayogo.footballLeagueApp.presenter.StandingsPresenter
import com.dicoding.prayogo.footballLeagueApp.test.EspressoIdlingResource
import com.dicoding.prayogo.footballLeagueApp.util.invisible
import com.dicoding.prayogo.footballLeagueApp.util.visible
import com.dicoding.prayogo.footballLeagueApp.view.StandingsView
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class StandingsActivity : AppCompatActivity(),StandingsView {
    private var listStandings: MutableList<Standings> = mutableListOf()
    private lateinit var leagueId:String
    private lateinit var noFoundDataTextView: TextView
    private lateinit var presenter: StandingsPresenter
    private lateinit var adapter: StandingsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvMatch: RecyclerView
    private var request = ApiRepository()
    private var gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUI()
        getStandingsData()
        supportActionBar?.title = resources.getString(R.string.standings_list)
    }

    private fun getStandingsData() {
        hideNoFoundText()
        leagueId = intent.getStringExtra(DetailLeagueActivity.LEAGUE_ID)
        adapter = StandingsAdapter(listStandings){

        }
        rvMatch.adapter = adapter

        request = ApiRepository()
        gson = Gson()
        presenter = StandingsPresenter(this, request, gson)
        EspressoIdlingResource.increment()
        presenter.getStandingsList(leagueId)

        swipeRefresh.onRefresh {
            presenter.getStandingsList(leagueId)
        }
    }
    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showNoFoundText() {
        noFoundDataTextView.visibility = View.VISIBLE
    }

    override fun hideNoFoundText() {
        noFoundDataTextView.visibility = View.INVISIBLE
    }

    override fun showStandingsList(data: List<Standings>) {
        if (!EspressoIdlingResource.idlingresource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }
        swipeRefresh.isRefreshing = false
        listStandings.clear()
        listStandings.addAll(data)
        adapter.notifyDataSetChanged()
    }
    private fun createUI() {
        frameLayout {
            noFoundDataTextView = textView {
                text = resources.getString(R.string.no_found_data)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                textSize = 20f
                textColor = Color.BLACK
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
            }
            linearLayout {
                lparams(width = matchParent, height = wrapContent)
                orientation = LinearLayout.VERTICAL


                gridLayout {
                    backgroundColor = Color.parseColor("#3F51B5")
                    columnCount = 9
                    useDefaultMargins = true
                    textView {
                        text = resources.getString(R.string.team_name)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.WHITE
                        textSize = 15f
                        width = dip(100)
                        gravity = Gravity.CENTER
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        text = resources.getString(R.string.main_played)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.WHITE
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        text = resources.getString(R.string.goals_for)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.WHITE
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        text = resources.getString(R.string.goals_against)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.WHITE
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        text = resources.getString(R.string.goals_difference)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.WHITE
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        text = resources.getString(R.string.win)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.WHITE
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        text = resources.getString(R.string.loss)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.WHITE
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        text = resources.getString(R.string.draw)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.WHITE
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                        setTypeface(typeface, Typeface.BOLD)
                    }
                    textView {
                        text = resources.getString(R.string.total)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.WHITE
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                        setTypeface(typeface, Typeface.BOLD)
                    }
                }

                swipeRefresh = swipeRefreshLayout {
                    id = R.id.refresh_next_match_list
                    setColorSchemeResources(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )
                    relativeLayout {
                        lparams(width = matchParent, height = wrapContent)

                        rvMatch = recyclerView {
                            id = R.id.rv_standings_list
                            lparams(width = matchParent, height = wrapContent)
                            layoutManager = LinearLayoutManager(context)
                        }

                        progressBar = progressBar {
                        }.lparams {
                            centerHorizontally()
                        }
                    }
                }
            }
        }
    }
}
