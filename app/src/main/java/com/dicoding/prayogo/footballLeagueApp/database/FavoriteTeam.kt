package com.dicoding.prayogo.footballLeagueApp.database

data class FavoriteTeam(
    val id: Long?,
    val teamId: String?,
    val leagueId: String?,
    val teamBadge: String?,
    val teamName: String?
) {

    companion object {
        const val LEAGUE_ID: String = "LEAGUE_ID"
        const val TEAM_ID: String = "TEAM_ID"
        const val TEAM_BADGE: String = "TEAM_BADGE"
        const val TEAM_NAME: String = "TEAM_NAME"
        const val TABLE_FAVORITE_TEAM: String = "TABLE_FAVORITE_TEAM"
        const val ID: String = "ID_"
    }
}