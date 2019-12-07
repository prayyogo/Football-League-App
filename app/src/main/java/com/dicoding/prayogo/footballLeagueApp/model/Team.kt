package com.dicoding.prayogo.footballLeagueApp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    @SerializedName("idTeam")
    var teamId: String?,
    @SerializedName("strTeam")
    var teamName: String?,
    @SerializedName("idLeague")
    var leagueId: String?,
    @SerializedName("strTeamBadge")
    var teamBadge: String?,
    @SerializedName("intFormedYear")
    var formerYear: String?,
    @SerializedName("strDescriptionEN")
    var teamDescription: String?,
    @SerializedName("strStadiumThumb")
    var stadiumImage: String?,
    @SerializedName("strStadium")
    var stadiumName: String?,
    @SerializedName("strStadiumLocation")
    var stadiumLocation: String?,
    @SerializedName("intStadiumCapacity")
    var stadiumCapacity: String?,
    @SerializedName("strStadiumDescription")
    var stadiumDescription: String?,
    @SerializedName("strTeamJersey")
    var jerseyImage: String?
) : Parcelable

data class TeamResponse(
    val teams: List<Team>
)