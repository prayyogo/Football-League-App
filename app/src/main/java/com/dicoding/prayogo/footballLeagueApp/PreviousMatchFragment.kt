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
import com.dicoding.prayogo.footballLeagueApp.util.invisible
import com.dicoding.prayogo.footballLeagueApp.util.visible
import com.dicoding.prayogo.footballLeagueApp.view.MatchView
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PreviousMatchFragment : Fragment(), MatchView,AnkoComponent<Context>  {

    private var listMatch: MutableList<Match> = mutableListOf()
    private var tempListMatch: MutableList<Match> = mutableListOf()
    private var listTeamName: MutableList<String?> = mutableListOf()
    private var templistTeamName: List<String?> = mutableListOf()
    private var index:Int=0
    private lateinit var tv_noFoundData: TextView
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

    private fun getMatchData(){
        adapter = MatchAdapter(listMatch) {
            val intent = Intent(activity, DetailMatchActivity::class.java)
            intent.putExtra(DetailLeagueActivity.match, it.eventId)
            startActivity(intent)
            val toast = Toast.makeText(context, it.winTeam, Toast.LENGTH_SHORT)
            toast.show()
        }
        rvMatch.adapter = adapter

        request = ApiRepository()
        gson = Gson()
        presenter =
            MatchPresenter(this, request, gson)
        presenter.getPreviousMatchList(DetailLeagueActivity.idLeague)

        swipeRefresh.onRefresh {
            presenter.getPreviousMatchList(DetailLeagueActivity.idLeague)
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
        index++
        for(match in tempListMatch){
            if(match.winTeam==data.teamName){
                match.winBadge=data.teamBadge
            }
            if(match.loseTeam==data.teamName){
                match.loseBadge=data.teamBadge
            }
        }
        if(index>=templistTeamName.size){
            showMatchList(tempListMatch)
        }
    }
    override fun getTeamBadge(data: List<Match>) {
        tempListMatch.clear()
        tempListMatch.addAll(data)
       listTeamName.clear()
        for(i in data){
            listTeamName.add(i.winTeam)
            listTeamName.add(i.loseTeam)
        }
        templistTeamName=listTeamName.distinct()

        for(team in templistTeamName) {
            presenter =
                MatchPresenter(this, request, gson)
            presenter.getTeamBadge(team)
        }
    }

    override fun showNoFoundText() {
        tv_noFoundData.visibility=View.VISIBLE
    }
    override fun hideNoFoundText() {
        tv_noFoundData.visibility=View.INVISIBLE
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
            tv_noFoundData = textView {
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
                    setColorSchemeResources(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )

                    relativeLayout {
                        lparams(width = matchParent, height = wrapContent)

                        rvMatch = recyclerView {
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
