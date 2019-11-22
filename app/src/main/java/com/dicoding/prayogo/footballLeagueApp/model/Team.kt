package com.dicoding.prayogo.footballLeagueApp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team (
    @SerializedName("idTeam")
    var teamId: String? = null,
    @SerializedName("strTeam")
    var teamName: String? = null,
    @SerializedName("idLeague")
    var teamIdLeague: String? = null,
    @SerializedName("strTeamBadge")
    var teamBadge: String? = null
): Parcelable

data class TeamResponse(
    val teams: List<Team>
)