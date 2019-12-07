package com.dicoding.prayogo.footballLeagueApp.team


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.dicoding.prayogo.footballLeagueApp.*
import com.dicoding.prayogo.footballLeagueApp.adapter.TeamAdapter
import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.model.Team
import com.dicoding.prayogo.footballLeagueApp.presenter.TeamPresenter
import com.dicoding.prayogo.footballLeagueApp.test.EspressoIdlingResource
import com.dicoding.prayogo.footballLeagueApp.util.invisible
import com.dicoding.prayogo.footballLeagueApp.util.visible
import com.dicoding.prayogo.footballLeagueApp.view.TeamView
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamListFragment : Fragment(), TeamView, AnkoComponent<Context> {

    private var listTeam: MutableList<Team> = mutableListOf()
    private lateinit var noFoundDataTextView: TextView
    private lateinit var presenter: TeamPresenter
    private lateinit var adapter: TeamAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvMatch: RecyclerView
    private lateinit var searchView: SearchView
    private var request = ApiRepository()
    private var gson = Gson()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        getTeamData()
    }

    private fun getTeamData() {
        hideNoFoundText()
        adapter = TeamAdapter(listTeam) {
            val intent = Intent(activity, DetailTeamActivity::class.java)
            intent.putExtra(TeamActivity.TEAM_ID, it.teamId)
            startActivity(intent)
            val toast = Toast.makeText(context, it.teamName, Toast.LENGTH_SHORT)
            toast.show()
        }
        rvMatch.adapter = adapter

        request = ApiRepository()
        gson = Gson()
        presenter = TeamPresenter(this, request, gson)
        EspressoIdlingResource.increment()
        presenter.getTeamList(TeamActivity.leagueId)

        swipeRefresh.onRefresh {
            presenter.getTeamList(TeamActivity.leagueId)
        }
    }

    private fun searchTeamData(team: String) {
        adapter = TeamAdapter(listTeam) {
            val intent = Intent(activity, DetailTeamActivity::class.java)
            intent.putExtra(TeamActivity.TEAM_ID, it.teamId)
            startActivity(intent)
            val toast = Toast.makeText(context, it.teamName, Toast.LENGTH_SHORT)
            toast.show()
        }
        rvMatch.adapter = adapter

        request = ApiRepository()
        gson = Gson()
        presenter = TeamPresenter(this, request, gson)
        EspressoIdlingResource.increment()
        presenter.getTeamByName(team)

        swipeRefresh.onRefresh {
            presenter.getTeamList(TeamActivity.leagueId)
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

    override fun showTeamList(data: List<Team>) {
        if (!EspressoIdlingResource.idlingresource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }
        swipeRefresh.isRefreshing = false
        listTeam.clear()
        listTeam.addAll(data)
        adapter.notifyDataSetChanged()
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.team_list_menu, menu)

        searchView = menu.findItem(R.id.action_search_menu).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = resources.getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return if (query.trim().isEmpty()) {
                    getTeamData()
                    false
                } else {
                    searchTeamData(query)
                    true
                }
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(AnkoContext.create(requireContext()))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {

        frameLayout {
            noFoundDataTextView = textView {
                text = resources.getString(R.string.no_team_data)
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
                            id = R.id.rv_team_list
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
