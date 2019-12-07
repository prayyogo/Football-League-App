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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.dicoding.prayogo.footballLeagueApp.R
import com.dicoding.prayogo.footballLeagueApp.adapter.TeamFavoriteAdapter
import com.dicoding.prayogo.footballLeagueApp.database.FavoriteTeam
import com.dicoding.prayogo.footballLeagueApp.database.teamDatabase
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteTeamFragment : Fragment(), AnkoComponent<Context> {

    private var listFavoriteTeams: MutableList<FavoriteTeam> = mutableListOf()
    private lateinit var noFoundDataTextView: TextView
    private lateinit var adapter: TeamFavoriteAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvMatch: RecyclerView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getFavoriteData()
    }

    private fun getFavoriteData() {

        adapter = TeamFavoriteAdapter(listFavoriteTeams) {
            val intent = Intent(activity, DetailTeamActivity::class.java)
            intent.putExtra(TeamActivity.TEAM_ID, it.teamId)
            startActivity(intent)
            val toast = Toast.makeText(context, it.teamName, Toast.LENGTH_SHORT)
            toast.show()
        }
        rvMatch.adapter = adapter
        loadDatabase()

        swipeRefresh.onRefresh {
            progressBar.visibility = View.VISIBLE
            loadDatabase()
        }
    }

    private fun loadDatabase() {
        progressBar.visibility = View.INVISIBLE
        listFavoriteTeams.clear()
        context?.teamDatabase?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (!favorite.isNullOrEmpty()) {
                for (fav in favorite) {
                    if (fav.leagueId == TeamActivity.leagueId) {
                        listFavoriteTeams.add(fav)
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }
        if (listFavoriteTeams.isNullOrEmpty()) {
            noFoundDataTextView.visibility = View.VISIBLE
        } else {
            noFoundDataTextView.visibility = View.INVISIBLE
        }
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
                text = resources.getString(R.string.no_favorite_team)
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
                    id = R.id.refresh_favorite_previous_match_list
                    setColorSchemeResources(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )
                    relativeLayout {
                        lparams(width = matchParent, height = wrapContent)

                        rvMatch = recyclerView {
                            id = R.id.rv_favorite_previous_match_list
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