package com.dicoding.prayogo.footballLeagueApp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Standings(
    @SerializedName("name")
    var teamName: String?,
    @SerializedName("played")
    var mainPlayer: String?,
    @SerializedName("goalsfor")
    var goalsFor: String?,
    @SerializedName("goalsagainst")
    var goalsAgainst: String?,
    @SerializedName("goalsdifference")
    var goalsDifference: String?,
    @SerializedName("win")
    var win: String?,
    @SerializedName("loss")
    var lose: String?,
    @SerializedName("draw")
    var draw: String?,
    @SerializedName("total")
    val total: String?
) : Parcelable

data class StandingsResponse(
    val table: List<Standings>
)