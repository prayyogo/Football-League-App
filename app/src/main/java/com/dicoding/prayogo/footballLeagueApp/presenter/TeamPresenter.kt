package com.dicoding.prayogo.footballLeagueApp.presenter

import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.api.TheSportDBApi
import com.dicoding.prayogo.footballLeagueApp.model.TeamResponse
import com.dicoding.prayogo.footballLeagueApp.util.CoroutineContextProvider
import com.dicoding.prayogo.footballLeagueApp.view.TeamView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamPresenter(
    private val view: TeamView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getTeamList(idLeague: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getTeamList(idLeague)).await(),
                TeamResponse::class.java
            )
            view.hideLoading()
            if (!data.teams.isNullOrEmpty()) {
                view.showTeamList(data.teams)
                view.hideNoFoundText()
            } else {
                view.showNoFoundText()
            }
        }
    }

    fun getTeamDetail(idTeam: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getTeamDetail(idTeam)).await(),
                TeamResponse::class.java
            )
            view.hideLoading()
            if (!data.teams.isNullOrEmpty()) {
                view.showTeamList(data.teams)
                view.hideNoFoundText()
            } else {
                view.showNoFoundText()
            }
        }
    }


    fun getTeamByName(team: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getTeams(team)).await(),
                TeamResponse::class.java
            )
            view.hideLoading()
            if (!data.teams.isNullOrEmpty()) {
                view.showTeamList(data.teams)
                view.hideNoFoundText()
            } else {
                view.showNoFoundText()
            }
        }
    }
}