package com.dicoding.prayogo.footballLeagueApp.view

import com.dicoding.prayogo.footballLeagueApp.model.Team

interface TeamView {
    fun showLoading()
    fun hideLoading()
    fun showNoFoundText()
    fun hideNoFoundText()
    fun showTeamList(data: List<Team>)
}