package com.dicoding.prayogo.footballLeagueApp.presenter

import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.api.TheSportDBApi
import com.dicoding.prayogo.footballLeagueApp.model.MatchResponse
import com.dicoding.prayogo.footballLeagueApp.model.SearchMatchResponse
import com.dicoding.prayogo.footballLeagueApp.model.TeamResponse
import com.dicoding.prayogo.footballLeagueApp.view.MatchView
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MatchPresenter  (private val view: MatchView,
                       private val apiRepository: ApiRepository,
                       private val gson: Gson
) {
    fun getPreviousMatchList(idLeague: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getPreviousMatch(idLeague)),
                MatchResponse::class.java
            )
            uiThread {
                view.hideLoading()
                if(data.events!=null) {
                    view.getTeamBadge(data.events)
                    view.hideNoFoundText()
                }else{
                    view.showNoFoundText()
                }
            }
        }
    }

    fun getNextMatchList(idLeague: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getNextMatch(idLeague)),
                MatchResponse::class.java
            )
            uiThread {
                view.hideLoading()
                if(data.events!=null) {
                    view.getTeamBadge(data.events)
                    view.hideNoFoundText()
                }else{
                    view.showNoFoundText()
                }
            }
        }
    }

    fun getTeamBadge(team: String?) {
        view.showLoading()
        var data: TeamResponse?
        doAsync {
             data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getTeams(team)),
                TeamResponse::class.java
            )
            uiThread {
                view.hideLoading()
                if(data?.teams!=null){
                for(i in data?.teams!!) {
                    if (i.teamName == team) {
                        view.setTeamBadge(i)
                        break
                    }
                }
                }
            }
        }
    }
    fun getEventMatchList(team: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getEventMatch(team)),
                SearchMatchResponse::class.java
            )
            uiThread {
                view.hideLoading()
                if(data.event==null){
                    view.showNoFoundText()
                }else{
                    view.hideNoFoundText()
                    view.getTeamBadge(data.event)
                }
            }
        }
    }

    fun getMatchDetail(idEvent: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getDetailMatch(idEvent)),
                MatchResponse::class.java
            )
            uiThread {
                view.hideLoading()
                if(data.events!=null) {
                    view.getTeamBadge(data.events)
                    view.hideNoFoundText()
                }else{
                    view.showNoFoundText()
                }
            }
        }
    }
}