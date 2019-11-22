package com.dicoding.prayogo.footballLeagueApp.presenter

import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.api.TheSportDBApi
import com.dicoding.prayogo.footballLeagueApp.model.LeagueResponse
import com.dicoding.prayogo.footballLeagueApp.view.MainView
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainPresenter (private val view: MainView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson
) {
    fun getLeagueList(country: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getLeague(country)),
                LeagueResponse::class.java
            )
            uiThread {
                view.hideLoading()
                view.showLeagueList(data.countrys)
            }
        }
    }
}