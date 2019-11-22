package com.dicoding.prayogo.footballLeagueApp.view

import com.dicoding.prayogo.footballLeagueApp.model.League

interface MainView {
    fun showLoading()
    fun hideLoading()
    fun showLeagueList(data: List<League>)
}