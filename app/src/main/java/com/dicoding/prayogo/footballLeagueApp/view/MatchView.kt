package com.dicoding.prayogo.footballLeagueApp.view

import com.dicoding.prayogo.footballLeagueApp.model.Match
import com.dicoding.prayogo.footballLeagueApp.model.Team

interface MatchView {
    fun showLoading()
    fun hideLoading()
    fun showNoFoundText()
    fun hideNoFoundText()
    fun showMatchList(data: List<Match>)
    fun getTeamBadge(data: List<Match>)
    fun setTeamBadge(data: Team)
}