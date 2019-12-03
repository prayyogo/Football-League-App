package com.dicoding.prayogo.footballLeagueApp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class League(
    @SerializedName("idLeague")
    var leagueId:String?,
    @SerializedName("strBadge")
    var leagueLogo:String?,
    @SerializedName("strLeague")
    var leagueName:String?,
    @SerializedName("strDescriptionEN")
    val leagueDescription: String?
):Parcelable
data class LeagueResponse(
    val countrys: List<League>)