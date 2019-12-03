package com.dicoding.prayogo.footballLeagueApp.database

data class FavoriteMatch(
    val id: Long?,
    val eventId: String?,
    val leagueId: String?,
    val dateMatch: String?,
    val winTeam: String?,
    val winScore: String?,
    val winBadge: String?,
    val loseTeam: String?,
    val loseScore: String?,
    val loseBadge: String?
) {

    companion object {
        const val LEAGUE_ID: String = "LEAGUE_ID"
        const val EVENT_ID: String = "EVENT_ID"
        var WIN_TEAM: String = "WIN_TEAM"
        var LOSE_TEAM: String = "LOSE_TEAM"
        var WIN_SCORE: String = "WIN_SCORE"
        var LOSE_SCORE: String = "LOSE_SCORE"
        const val DATE_MATCH: String = "DATE_MATCH"
        var WIN_BADGE: String = "WIN_BADGE"
        var LOSE_BADGE: String = "LOSE_BADGE"
        const val TABLE_FAVORITE_PREVIOUS_MATCH: String = "TABLE_FAVORITE_PREVIOUS_MATCH"
        const val TABLE_FAVORITE_NEXT_MATCH: String = "TABLE_FAVORITE_NEXT_MATCH"
        const val ID: String = "ID_"
    }
}