package com.dicoding.prayogo.footballLeagueApp.view

import com.dicoding.prayogo.footballLeagueApp.model.Standings

interface StandingsView {
    fun showLoading()
    fun hideLoading()
    fun showNoFoundText()
    fun hideNoFoundText()
    fun showStandingsList(data: List<Standings>)
}