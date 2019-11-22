package com.dicoding.prayogo.footballLeagueApp

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.MenuItem
import android.widget.*
import com.dicoding.prayogo.footballLeagueApp.adapter.MatchAdapter
import com.dicoding.prayogo.footballLeagueApp.adapter.PageAdapterMatch
import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.model.League
import com.dicoding.prayogo.footballLeagueApp.model.Match
import com.dicoding.prayogo.footballLeagueApp.model.Team
import com.dicoding.prayogo.footballLeagueApp.presenter.MatchPresenter
import com.dicoding.prayogo.footballLeagueApp.util.invisible
import com.dicoding.prayogo.footballLeagueApp.util.visible
import com.dicoding.prayogo.footballLeagueApp.view.MatchView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.design.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.support.v4.viewPager

class DetailLeagueActivity : AppCompatActivity(),
    MatchView {
    companion object{
        var idLeague:String?=null
        const val match="match"
    }
    private var league: League?=null
    private lateinit var img_logoLeague: ImageView
    private lateinit var tv_nameLeague: TextView
    private lateinit var tv_descLeague: TextView
    private lateinit var tv_noFoundData: TextView
    private lateinit var matchViewPager: ViewPager
    private lateinit var matchTabLayout: TabLayout
    private lateinit var searchView:SearchView
    private lateinit var detailLeagueLayout: LinearLayout
    private lateinit var searchEventsLayout: LinearLayout

    private var listMatch: MutableList<Match> = mutableListOf()
    private var tempListMatch: MutableList<Match> = mutableListOf()
    private var listTeamName: MutableList<String?> = mutableListOf()
    private var templistTeamName: List<String?> = mutableListOf()
    private var index:Int=0
    private lateinit var presenter: MatchPresenter
    private lateinit var adapter: MatchAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvMatch: RecyclerView
    private var request = ApiRepository()
    private var gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUI()
        adapter = MatchAdapter(listMatch) {
            startActivity<DetailMatchActivity>(match to it.eventId)
            val toast = Toast.makeText(applicationContext, it.winTeam, Toast.LENGTH_SHORT)
            toast.show()
        }
        rvMatch.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        menu.clear()
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)

        searchView = menu.findItem(R.id.action_search_menu).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint=resources.getString( R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(query: String): Boolean {
                return if(query.trim().isEmpty()){
                    setUI(false)
                    false
                }else{
                    setUI(true)
                    searchEventMatch(query)
                    true
                }
            }

        })
        return super.onCreateOptionsMenu(menu)
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
        //filter
        for(match in data) {
            if(match.sport=="Soccer") {
                tempListMatch.add(match)
            }
        }

        listTeamName.clear()
        for(i in tempListMatch){
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

    private fun searchEventMatch(team:String?){
        request = ApiRepository()
        gson = Gson()
        presenter =
            MatchPresenter(this, request, gson)
        presenter.getEventMatchList(team)

        swipeRefresh.onRefresh {
            presenter.getEventMatchList(team)
        }
    }

    private fun createUI(){
        frameLayout {
          searchEventsLayout=  linearLayout {
                lparams(width = matchParent, height = wrapContent)
                orientation = LinearLayout.VERTICAL
                topPadding = dip(5)
                leftPadding = dip(16)
                rightPadding = dip(16)

             tv_noFoundData= textView {
                  text=resources.getString(R.string.no_found_data)
                  textAlignment = View.TEXT_ALIGNMENT_CENTER
                  textSize = 20f
                  textColor = Color.BLACK
                  typeface= Typeface.DEFAULT_BOLD
              }.lparams {
                  gravity = Gravity.CENTER
              }
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
            detailLeagueLayout = verticalLayout {
                img_logoLeague = imageView {
                }.lparams {

                    height = dip(80)
                    width = dip(80)
                    gravity = Gravity.CENTER
                    margin = dip(16)
                }

                tv_nameLeague = textView {
                    textSize = 30f
                    textColor = Color.BLACK
                    typeface = Typeface.DEFAULT_BOLD
                    gravity = Gravity.CENTER
                }

                textView {
                    text = getString(R.string.description)
                    textSize = 20f
                    textColor = Color.BLACK
                    textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                }.lparams {
                    leftMargin = dip(16)
                    rightMargin = dip(16)
                    topMargin = dip(16)
                }

                scrollView {
                    lparams(matchParent, 300)
                    isFillViewport = true
                    isVerticalScrollBarEnabled = false
                    verticalLayout {
                        tv_descLeague = textView {
                            textSize = 12f
                            textColor = Color.BLACK
                            textAlignment = View.TEXT_ALIGNMENT_VIEW_START

                        }.lparams {
                            leftMargin = dip(16)
                            rightMargin = dip(16)
                        }
                    }
                }

                coordinatorLayout {
                    lparams(matchParent, matchParent)

                    matchViewPager = viewPager {
                        id = R.id.vp_viewpager
                    }.lparams(width = matchParent)
                    matchTabLayout = themedTabLayout(R.style.ThemeOverlay_AppCompat_Dark) {
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
        }
        matchViewPager.adapter = PageAdapterMatch(
            supportFragmentManager,
            this
        )
        matchTabLayout.setupWithViewPager(matchViewPager)

        searchEventsLayout.visibility=View.INVISIBLE
        val intent = intent
        league = intent.getParcelableExtra(MainActivity.league)
        tv_nameLeague.text = league?.leagueName
        if(league?.leagueDescription==""||league?.leagueDescription==null){
            tv_descLeague.text=resources.getString(R.string.strip)
        }else {
            tv_descLeague.text = league?.leagueDescription
        }
        league?.leagueLogo?.let { Picasso.get().load(it).fit().into(img_logoLeague) }
        idLeague=league?.leagueId
        supportActionBar?.title=league?.leagueName
    }

    private fun setUI(isSearch:Boolean){
        // UI Detail
        if(!isSearch) {
            detailLeagueLayout.visibility=View.VISIBLE
            searchEventsLayout.visibility=View.INVISIBLE
        }
        else{
           detailLeagueLayout.visibility=View.INVISIBLE
            searchEventsLayout.visibility=View.VISIBLE
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when(item.itemId) {
            R.id.action_search_menu -> {
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }
}
