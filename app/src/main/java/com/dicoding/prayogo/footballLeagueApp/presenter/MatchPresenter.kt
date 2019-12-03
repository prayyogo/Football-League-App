package com.dicoding.prayogo.footballLeagueApp.presenter

import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.api.TheSportDBApi
import com.dicoding.prayogo.footballLeagueApp.model.MatchResponse
import com.dicoding.prayogo.footballLeagueApp.model.SearchMatchResponse
import com.dicoding.prayogo.footballLeagueApp.model.TeamResponse
import com.dicoding.prayogo.footballLeagueApp.util.CoroutineContextProvider
import com.dicoding.prayogo.footballLeagueApp.view.MatchView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MatchPresenter(
    private val view: MatchView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getPreviousMatchList(idLeague: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getPreviousMatch(idLeague)).await(),
                MatchResponse::class.java
            )

            view.hideLoading()
            if (!data.events.isNullOrEmpty()) {
                view.getTeamBadge(data.events)
                view.hideNoFoundText()
            } else {
                view.showNoFoundText()
            }
        }
    }

    fun getNextMatchList(idLeague: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getNextMatch(idLeague)).await(),
                MatchResponse::class.java
            )
            view.hideLoading()
            if (!data.events.isNullOrEmpty()) {
                view.getTeamBadge(data.events)
                view.hideNoFoundText()
            } else {
                view.showNoFoundText()
            }
        }
    }

    fun getTeamBadge(team: String?) {
        view.showLoading()
        var data: TeamResponse?
        GlobalScope.launch(context.main) {
            data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getTeams(team)).await(),
                TeamResponse::class.java
            )
            view.hideLoading()
            val teams = data?.teams
            if (teams != null) {
                for (i in teams) {
                    if (i.teamName == team) {
                        view.setTeamBadge(i)
                        break
                    }
                }
            }
        }
    }

    fun getEventMatchList(team: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getEventMatch(team)).await(),
                SearchMatchResponse::class.java
            )
            view.hideLoading()
            if (data.event.isNullOrEmpty()) {
                view.showNoFoundText()
            } else {
                view.hideNoFoundText()
                view.getTeamBadge(data.event)
            }
        }
    }

    fun getMatchDetail(idEvent: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getDetailMatch(idEvent)).await(),
                MatchResponse::class.java
            )
            view.hideLoading()
            if (!data.events.isNullOrEmpty()) {
                view.getTeamBadge(data.events)
                view.hideNoFoundText()
            } else {
                view.showNoFoundText()
            }
        }
    }
}