package com.dicoding.prayogo.footballLeagueApp.presenter

import com.dicoding.prayogo.footballLeagueApp.TestContextProvider
import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.model.*
import com.dicoding.prayogo.footballLeagueApp.view.MatchView
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MatchPresenterTest {
    @Mock
    private lateinit var view: MatchView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var presenter: MatchPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun getPreviousMatchList() {
        val matches: MutableList<Match> = mutableListOf()
        val response = MatchResponse(matches)
        val idLeague = "4328"

        runBlocking {
            Mockito.`when`(apiRepository.doRequest(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    MatchResponse::class.java
                )
            ).thenReturn(response)

            presenter.getPreviousMatchList(idLeague)

            Mockito.verify(view).showLoading()
            if (!matches.isNullOrEmpty()) {
                Mockito.verify(view).getTeamBadge(matches)
                Mockito.verify(view).hideNoFoundText()
            } else {
                Mockito.verify(view).showNoFoundText()
            }
            Mockito.verify(view).hideLoading()
        }
    }

    @Test
    fun getNextMatchList() {
        val matches: MutableList<Match> = mutableListOf()
        val response = MatchResponse(matches)
        val idLeague = "4328"

        runBlocking {
            Mockito.`when`(apiRepository.doRequest(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    MatchResponse::class.java
                )
            ).thenReturn(response)

            presenter.getNextMatchList(idLeague)

            Mockito.verify(view).showLoading()
            if (!matches.isNullOrEmpty()) {
                Mockito.verify(view).getTeamBadge(matches)
                Mockito.verify(view).hideNoFoundText()
            } else {
                Mockito.verify(view).showNoFoundText()
            }
            Mockito.verify(view).hideLoading()
        }
    }

    @Test
    fun getTeamBadge() {
        val teams: MutableList<Team> = mutableListOf()
        val response = TeamResponse(teams)
        val teamName = "Arsenal"

        runBlocking {
            Mockito.`when`(apiRepository.doRequest(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    TeamResponse::class.java
                )
            ).thenReturn(response)

            presenter.getTeamBadge(teamName)

            Mockito.verify(view).showLoading()
            if (!teams.isNullOrEmpty()) {
                for (i in teams) {
                    if (i.teamName == teamName) {
                        Mockito.verify(view).setTeamBadge(i)
                        break
                    }
                }
            }
            Mockito.verify(view).hideLoading()
        }
    }

    @Test
    fun getEventMatchList() {
        val matches: MutableList<Match> = mutableListOf()
        val response = SearchMatchResponse(matches)
        val teamName = "Arsenal"

        runBlocking {
            Mockito.`when`(apiRepository.doRequest(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    SearchMatchResponse::class.java
                )
            ).thenReturn(response)

            presenter.getEventMatchList(teamName)

            Mockito.verify(view).showLoading()
            if (!matches.isNullOrEmpty()) {
                Mockito.verify(view).getTeamBadge(matches)
                Mockito.verify(view).hideNoFoundText()
            } else {
                Mockito.verify(view).showNoFoundText()
            }
            Mockito.verify(view).hideLoading()
        }
    }

    @Test
    fun getMatchDetail() {
        val matches: MutableList<Match> = mutableListOf()
        val response = MatchResponse(matches)
        val eventId = "441613"

        runBlocking {
            Mockito.`when`(apiRepository.doRequest(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    MatchResponse::class.java
                )
            ).thenReturn(response)

            presenter.getMatchDetail(eventId)

            Mockito.verify(view).showLoading()
            if (!matches.isNullOrEmpty()) {
                Mockito.verify(view).getTeamBadge(matches)
                Mockito.verify(view).hideNoFoundText()
            } else {
                Mockito.verify(view).showNoFoundText()
            }
            Mockito.verify(view).hideLoading()
        }
    }
}