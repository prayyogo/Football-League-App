package com.dicoding.prayogo.footballLeagueApp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DatabaseHelperFavoriteMatch(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "FavoriteMatch.db", null, 1) {
    companion object {
        private var instance: DatabaseHelperFavoriteMatch? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseHelperFavoriteMatch {
            if (instance == null) {
                instance =
                    DatabaseHelperFavoriteMatch(ctx.applicationContext)
            }
            return instance as DatabaseHelperFavoriteMatch
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            FavoriteMatch.TABLE_FAVORITE_PREVIOUS_MATCH, true,
            FavoriteMatch.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteMatch.EVENT_ID to TEXT + UNIQUE,
            FavoriteMatch.LEAGUE_ID to TEXT,
            FavoriteMatch.DATE_MATCH to TEXT,
            FavoriteMatch.WIN_TEAM to TEXT,
            FavoriteMatch.WIN_SCORE to TEXT,
            FavoriteMatch.WIN_BADGE to TEXT,
            FavoriteMatch.LOSE_TEAM to TEXT,
            FavoriteMatch.LOSE_SCORE to TEXT,
            FavoriteMatch.LOSE_BADGE to TEXT
        )
        db.createTable(
            FavoriteMatch.TABLE_FAVORITE_NEXT_MATCH, true,
            FavoriteMatch.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteMatch.EVENT_ID to TEXT + UNIQUE,
            FavoriteMatch.LEAGUE_ID to TEXT,
            FavoriteMatch.DATE_MATCH to TEXT,
            FavoriteMatch.WIN_TEAM to TEXT,
            FavoriteMatch.WIN_SCORE to TEXT,
            FavoriteMatch.WIN_BADGE to TEXT,
            FavoriteMatch.LOSE_TEAM to TEXT,
            FavoriteMatch.LOSE_SCORE to TEXT,
            FavoriteMatch.LOSE_BADGE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteMatch.TABLE_FAVORITE_PREVIOUS_MATCH, true)
        db.dropTable(FavoriteMatch.TABLE_FAVORITE_NEXT_MATCH, true)
    }
}

val Context.matchDatabase: DatabaseHelperFavoriteMatch
    get() = DatabaseHelperFavoriteMatch.getInstance(applicationContext)