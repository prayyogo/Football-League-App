package com.dicoding.prayogo.footballLeagueApp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dicoding.prayogo.footballLeagueApp.adapter.MatchAdapter
import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.model.Match
import com.dicoding.prayogo.footballLeagueApp.model.Team
import com.dicoding.prayogo.footballLeagueApp.presenter.MatchPresenter
import com.dicoding.prayogo.footballLeagueApp.test.EspressoIdlingResource
import com.dicoding.prayogo.footballLeagueApp.util.invisible
import com.dicoding.prayogo.footballLeagueApp.util.visible
import com.dicoding.prayogo.footballLeagueApp.view.MatchView
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PreviousMatchFragment : Fragment(), MatchView, AnkoComponent<Context> {

    private var listMatch: MutableList<Match> = mutableListOf()
    private var tempListMatch: MutableList<Match> = mutableListOf()
    private var listTeamName: MutableList<String?> = mutableListOf()
    private var tempListTeamName: List<String?> = mutableListOf()
    private var index: Int = 0
    private lateinit var noFoundDataTextView: TextView
    private lateinit var presenter: MatchPresenter
    private lateinit var adapter: MatchAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvMatch: RecyclerView
    private var request = ApiRepository()
    private var gson = Gson()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMatchData()
    }

    private fun getMatchData() {
        adapter = MatchAdapter(listMatch) {
            val intent = Intent(activity, DetailMatchActivity::class.java)
            intent.putExtra(DetailLeagueActivity.MATCH, it.eventId)
            intent.putExtra(DetailLeagueActivity.TYPE_MATCH, false)
            startActivity(intent)
            val toast = Toast.makeText(context, it.winTeam, Toast.LENGTH_SHORT)
            toast.show()
        }
        rvMatch.adapter = adapter

        request = ApiRepository()
        gson = Gson()
        presenter = MatchPresenter(this, request, gson)
      //  EspressoIdlingResource.increment()
        presenter.getPreviousMatchList(DetailLeagueActivity.leagueId)

        swipeRefresh.onRefresh {
            presenter.getPreviousMatchList(DetailLeagueActivity.leagueId)
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showMatchList(data: List<Match>) {
        swipeRefresh.isRefreshing = false
        listMatch.clear()
        listMatch.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun setTeamBadge(data: Team) {
       /* if (!EspressoIdlingResource.idlingresource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }*/
        index++
        for (match in tempListMatch) {
            if (match.winTeam == data.teamName) {
                match.winBadge = data.teamBadge
            }
            if (match.loseTeam == data.teamName) {
                match.loseBadge = data.teamBadge
            }
        }
        if (index >= tempListTeamName.size) {
            showMatchList(tempListMatch)
        }
    }

    override fun getTeamBadge(data: List<Match>) {
      /*  if (!EspressoIdlingResource.idlingresource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }*/
        tempListMatch.clear()
        tempListMatch.addAll(data)
        listTeamName.clear()
        for (i in data) {
            listTeamName.add(i.winTeam)
            listTeamName.add(i.loseTeam)
        }
        tempListTeamName = listTeamName.distinct()

        for (team in tempListTeamName) {
            presenter = MatchPresenter(this, request, gson)
        //    EspressoIdlingResource.increment()
            presenter.getTeamBadge(team)
        }
    }

    override fun showNoFoundText() {
        noFoundDataTextView.visibility = View.VISIBLE
    }

    override fun hideNoFoundText() {
        noFoundDataTextView.visibility = View.INVISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(AnkoContext.create(requireContext()))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {

        frameLayout {
            noFoundDataTextView = textView {
                text = resources.getString(R.string.no_event_match)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                textSize = 20f
                textColor = Color.BLACK
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
            }
            linearLayout {
                lparams(width = matchParent, height = wrapContent)
                orientation = LinearLayout.VERTICAL
                topPadding = dip(30)
                leftPadding = dip(16)
                rightPadding = dip(16)

                swipeRefresh = swipeRefreshLayout {
                    id=R.id.refresh_previous_match_list
                    setColorSchemeResources(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )

                    relativeLayout {
                        lparams(width = matchParent, height = wrapContent)

                        rvMatch = recyclerView {
                            id=R.id.rv_previous_match_list
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
