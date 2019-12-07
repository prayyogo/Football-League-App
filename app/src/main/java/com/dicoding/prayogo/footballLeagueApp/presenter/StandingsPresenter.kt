package com.dicoding.prayogo.footballLeagueApp.presenter

import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.api.TheSportDBApi
import com.dicoding.prayogo.footballLeagueApp.model.StandingsResponse
import com.dicoding.prayogo.footballLeagueApp.util.CoroutineContextProvider
import com.dicoding.prayogo.footballLeagueApp.view.StandingsView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StandingsPresenter(
    private val view: StandingsView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getStandingsList(idLeague: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getStandingsList(idLeague)).await(),
                StandingsResponse::class.java
            )
            view.hideLoading()
            if (data.table.isNullOrEmpty()) {
                view.showNoFoundText()
            } else {
                view.showStandingsList(data.table)
            }
        }
    }
}