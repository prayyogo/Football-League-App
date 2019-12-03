package com.dicoding.prayogo.footballLeagueApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import com.dicoding.prayogo.footballLeagueApp.R.color.*
import com.dicoding.prayogo.footballLeagueApp.adapter.MainAdapter
import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.model.League
import com.dicoding.prayogo.footballLeagueApp.presenter.MainPresenter
import com.dicoding.prayogo.footballLeagueApp.test.EspressoIdlingResource
import com.dicoding.prayogo.footballLeagueApp.util.invisible
import com.dicoding.prayogo.footballLeagueApp.util.visible
import com.dicoding.prayogo.footballLeagueApp.view.MainView
import com.google.gson.Gson
import org.jetbrains.anko.support.v4.onRefresh

class MainActivity : AppCompatActivity(), MainView {
    companion object {
        const val LEAGUE = "LEAGUE"
    }

    private var listLeague: MutableList<League> = mutableListOf()
    private lateinit var presenter: MainPresenter
    private lateinit var adapter: MainAdapter
    private lateinit var rvLeague: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var spinner: Spinner
    private lateinit var countryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            spinner = spinner {
                id = R.id.spinner
            }
            swipeRefresh = swipeRefreshLayout {
                id=R.id.refresh_league_list
                setColorSchemeResources(
                    colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
                )

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)

                    rvLeague = recyclerView {
                        id = R.id.rv_league_list
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = GridLayoutManager(context, 2)
                    }

                    progressBar = progressBar {
                    }.lparams {
                        centerHorizontally()
                    }
                }
            }
        }
        getLeagueData()

    }

    private fun getLeagueData() {
        val spinnerItems = resources.getStringArray(R.array.country_name)
        val spinnerAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerItems
        )
        spinner.adapter = spinnerAdapter

        adapter = MainAdapter(listLeague) {
            startActivity<DetailLeagueActivity>(LEAGUE to it)
            val toast = Toast.makeText(applicationContext, it.leagueName, Toast.LENGTH_SHORT)
            toast.show()
        }
        rvLeague.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = MainPresenter(this, request, gson)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                countryName = spinner.selectedItem.toString()
                EspressoIdlingResource.increment()
                presenter.getLeagueList(countryName)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        swipeRefresh.onRefresh {
            presenter.getLeagueList(countryName)
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showLeagueList(data: List<League>) {
        if (!EspressoIdlingResource.idlingresource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }
        swipeRefresh.isRefreshing = false
        listLeague.clear()
        listLeague.addAll(data)
        adapter.notifyDataSetChanged()
    }
}