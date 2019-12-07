package com.dicoding.prayogo.footballLeagueApp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DatabaseHelperTeamFavorite(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "FavoriteTeam.db", null, 1) {
    companion object {
        private var instance: DatabaseHelperTeamFavorite? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseHelperTeamFavorite {
            if (instance == null) {
                instance =
                    DatabaseHelperTeamFavorite(ctx.applicationContext)
            }
            return instance as DatabaseHelperTeamFavorite
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            FavoriteTeam.TABLE_FAVORITE_TEAM, true,
            FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteTeam.TEAM_ID to TEXT + UNIQUE,
            FavoriteTeam.LEAGUE_ID to TEXT,
            FavoriteTeam.TEAM_BADGE to TEXT,
            FavoriteTeam.TEAM_NAME to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteTeam.TABLE_FAVORITE_TEAM, true)
    }
}

val Context.teamDatabase: DatabaseHelperTeamFavorite
    get() = DatabaseHelperTeamFavorite.getInstance(applicationContext)