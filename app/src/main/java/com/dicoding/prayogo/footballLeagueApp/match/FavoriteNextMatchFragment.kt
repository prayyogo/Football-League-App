package com.dicoding.prayogo.footballLeagueApp.match


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
import com.dicoding.prayogo.footballLeagueApp.DetailLeagueActivity
import com.dicoding.prayogo.footballLeagueApp.R
import com.dicoding.prayogo.footballLeagueApp.adapter.FavoriteMatchAdapter
import com.dicoding.prayogo.footballLeagueApp.database.FavoriteMatch
import com.dicoding.prayogo.footballLeagueApp.database.matchDatabase
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteNextMatchFragment : Fragment(), AnkoComponent<Context> {
    private var listFavoriteMatches: MutableList<FavoriteMatch> = mutableListOf()
    private lateinit var noFoundDataTextView: TextView
    private lateinit var favoriteAdapter: FavoriteMatchAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvMatch: RecyclerView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getFavoriteData()
    }

    private fun getFavoriteData() {

        favoriteAdapter = FavoriteMatchAdapter(listFavoriteMatches) {
            val intent = Intent(activity, DetailMatchActivity::class.java)
            intent.putExtra(DetailLeagueActivity.MATCH, it.eventId)
            startActivity(intent)
            val toast = Toast.makeText(context, it.winTeam, Toast.LENGTH_SHORT)
            toast.show()
        }
        rvMatch.adapter = favoriteAdapter
        loadDatabase()

        swipeRefresh.onRefresh {
            progressBar.visibility = View.VISIBLE
            loadDatabase()
        }
    }

    private fun loadDatabase() {
        progressBar.visibility = View.INVISIBLE
        listFavoriteMatches.clear()
        context?.matchDatabase?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteMatch.TABLE_FAVORITE_NEXT_MATCH)
            val favorite = result.parseList(classParser<FavoriteMatch>())
            if (!favorite.isNullOrEmpty()) {
                for (fav in favorite) {
                    if (fav.leagueId == FavoriteMatchActivity.leagueId) {
                        listFavoriteMatches.add(fav)
                    }
                }
            }
            favoriteAdapter.notifyDataSetChanged()
        }
        if (listFavoriteMatches.isNullOrEmpty()) {
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
                text = resources.getString(R.string.no_favorite_match)
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
                    id = R.id.refresh_favorite_next_match_list
                    setColorSchemeResources(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )
                    relativeLayout {
                        lparams(width = matchParent, height = wrapContent)

                        rvMatch = recyclerView {
                            id = R.id.rv_favorite_next_match_list
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
