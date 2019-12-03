package com.dicoding.prayogo.footballLeagueApp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Match(
    @SerializedName("idEvent")
    var eventId: String?,
    @SerializedName("idLeague")
    var leagueId: String?,
    @SerializedName("strSport")
    var sport: String?,
    @SerializedName("strHomeTeam")
    var winTeam: String?,
    @SerializedName("strAwayTeam")
    var loseTeam: String?,
    @SerializedName("intHomeScore")
    var winScore: String?,
    @SerializedName("intAwayScore")
    var loseScore: String?,
    @SerializedName("strHomeGoalDetails")
    var winGoalDetail: String?,
    @SerializedName("strAwayGoalDetails")
    var loseGoalDetail: String?,
    @SerializedName("strHomeRedCards")
    var winRedCard: String?,
    @SerializedName("strAwayRedCards")
    var loseRedCard: String?,
    @SerializedName("strHomeYellowCards")
    var winYellowCard: String?,
    @SerializedName("strAwayYellowCards")
    var loseYellowCard: String?,
    @SerializedName("strHomeLineupGoalkeeper")
    var winGoalKeeper: String?,
    @SerializedName("strAwayLineupGoalkeeper")
    var loseGoalKeeper: String?,
    @SerializedName("strHomeLineupDefense")
    var winDefense: String?,
    @SerializedName("strAwayLineupDefense")
    var loseDefense: String?,
    @SerializedName("strHomeLineupMidfield")
    var winMidField: String?,
    @SerializedName("strAwayLineupMidfield")
    var loseMidField: String?,
    @SerializedName("strHomeLineupForward")
    var winForward: String?,
    @SerializedName("strAwayLineupForward")
    var loseForward: String?,
    @SerializedName("strHomeLineupSubstitutes")
    var winSubstitutes: String?,
    @SerializedName("strAwayLineupSubstitutes")
    var loseSubstitutes: String?,
    @SerializedName("dateEvent")
    val dateMatch: String?,
    @SerializedName("strTime")
    val timeMatch: String?,
    var winBadge: String?,
    var loseBadge: String?
) : Parcelable

data class MatchResponse(
    val events: List<Match>
)

data class SearchMatchResponse(
    val event: List<Match>
)