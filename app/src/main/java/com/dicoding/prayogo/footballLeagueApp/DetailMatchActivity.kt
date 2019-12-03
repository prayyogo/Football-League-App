package com.dicoding.prayogo.footballLeagueApp

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.database.database
import com.dicoding.prayogo.footballLeagueApp.database.FavoriteMatch
import com.dicoding.prayogo.footballLeagueApp.model.Match
import com.dicoding.prayogo.footballLeagueApp.model.Team
import com.dicoding.prayogo.footballLeagueApp.presenter.MatchPresenter
import com.dicoding.prayogo.footballLeagueApp.test.EspressoIdlingResource
import com.dicoding.prayogo.footballLeagueApp.util.invisible
import com.dicoding.prayogo.footballLeagueApp.util.visible
import com.dicoding.prayogo.footballLeagueApp.view.MatchView
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class DetailMatchActivity : AppCompatActivity(), MatchView {

    private var dataMatch: MutableList<Match> = mutableListOf()
    private var match: Match? = null
    private var nextMatch: Boolean? = null
    private lateinit var eventId: String
    private var listTeamName: MutableList<String?> = mutableListOf()
    private lateinit var dateMatchTextView: TextView
    private lateinit var timeMatchTextView: TextView
    private var index: Int = 0
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var noFoundDataTextView: TextView
    private lateinit var presenter: MatchPresenter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var request = ApiRepository()
    private var gson = Gson()

    private lateinit var badgeWinTeamImage: ImageView
    private lateinit var nameWinTeamTextView: TextView
    private lateinit var scoreWinTeamTextView: TextView
    private lateinit var goalDetailWinTeamTextView: TextView
    private lateinit var yellowCardWinTeamTextView: TextView
    private lateinit var redCardWinTeamTextView: TextView
    private lateinit var goalKeeperWinTeamTextView: TextView
    private lateinit var defenseWinTeamTextView: TextView
    private lateinit var midFieldWinTeamTextView: TextView
    private lateinit var fowardWinTeamTextView: TextView
    private lateinit var substituteWinTeamTextView: TextView

    private lateinit var badgeLoseTeamImage: ImageView
    private lateinit var nameLoseTeamTextView: TextView
    private lateinit var scoreLoseTeamTextView: TextView
    private lateinit var goalDetailLoseTeamTextView: TextView
    private lateinit var yellowCardLoseTeamTextView: TextView
    private lateinit var redCardLoseTeamTextView: TextView
    private lateinit var goalKeeperLoseTeamTextView: TextView
    private lateinit var defenseLoseTeamTextView: TextView
    private lateinit var midFieldLoseTeamTextView: TextView
    private lateinit var fowardLoseTeamTextView: TextView
    private lateinit var substituteLoseTeamTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //UI
        frameLayout {
            noFoundDataTextView = textView {
                text = resources.getString(R.string.no_event_match)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                textSize = 20f
                textColor = Color.BLACK
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
            }
            linearLayout {
                lparams(width = matchParent, height = wrapContent)
                orientation = LinearLayout.VERTICAL
                topPadding = dip(30)
                leftPadding = dip(16)
                rightPadding = dip(16)

                swipeRefresh = swipeRefreshLayout {
                    setColorSchemeResources(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )
                    relativeLayout {
                        lparams(width = matchParent, height = wrapContent)
                        progressBar = progressBar {
                        }.lparams {
                            centerHorizontally()
                        }
                    }
                }
            }
            linearLayout {
                orientation = LinearLayout.VERTICAL
                bottomPadding = dip(16)

                dateMatchTextView = textView {
                    backgroundColor = Color.parseColor("#FFB74A")
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    textSize = 25f
                    textColor = Color.BLACK
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(width = matchParent, height = wrapContent) {
                    gravity = Gravity.CENTER
                }
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    setPaddingRelative(dip(16), paddingTop, paddingEnd, paddingBottom)
                    topPadding = dip(16)
                    setPaddingRelative(paddingStart, paddingTop, dip(16), paddingBottom)
                    bottomPadding = dip(16)
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        setPaddingRelative(dip(16), paddingTop, paddingEnd, paddingBottom)
                        topPadding = dip(16)
                        setPaddingRelative(paddingStart, paddingTop, dip(16), paddingBottom)
                        bottomPadding = dip(16)
                        badgeWinTeamImage = imageView {
                            contentDescription = resources.getString(R.string.badge_team)
                            scaleType = ImageView.ScaleType.FIT_XY
                        }.lparams(width = dip(80), height = dip(80)) {
                            gravity = Gravity.CENTER
                        }
                        nameWinTeamTextView = textView {
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textSize = 20f
                            textColor = Color.BLACK
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams {
                            gravity = Gravity.CENTER
                        }
                    }.lparams(width = dip(130))
                    scoreWinTeamTextView = textView {
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textSize = 25f
                        textColor = Color.BLACK
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams {
                        gravity = Gravity.CENTER
                        marginStart = dip(10)
                        marginEnd = dip(10)
                    }
                    textView {
                        text = resources.getString(R.string.colon)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textSize = 25f
                        textColor = Color.BLACK
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams {
                        gravity = Gravity.CENTER
                        marginStart = dip(10)
                        marginEnd = dip(10)
                    }
                    scoreLoseTeamTextView = textView {
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textSize = 25f
                        textColor = Color.BLACK
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams {
                        gravity = Gravity.CENTER
                        marginStart = dip(10)
                        marginEnd = dip(10)
                    }
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        setPaddingRelative(dip(16), paddingTop, paddingEnd, paddingBottom)
                        topPadding = dip(16)
                        setPaddingRelative(paddingStart, paddingTop, dip(16), paddingBottom)
                        bottomPadding = dip(16)
                        badgeLoseTeamImage = imageView {
                            contentDescription = resources.getString(R.string.badge_team)
                            scaleType = ImageView.ScaleType.FIT_XY
                        }.lparams(width = dip(80), height = dip(80)) {
                            gravity = Gravity.CENTER
                        }
                        nameLoseTeamTextView = textView {
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textSize = 20f
                            textColor = Color.BLACK
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams {
                            gravity = Gravity.CENTER
                        }
                    }.lparams(width = dip(130))
                }.lparams(width = matchParent, height = wrapContent)
                // Information
                textView {
                    backgroundColor = Color.parseColor("#F8D762")
                    text = resources.getString(R.string.information)
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    textSize = 35f
                    textColor = Color.BLACK
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(width = matchParent) {
                    gravity = Gravity.CENTER
                }
                scrollView {
                    lparams(matchParent, wrapContent)
                    isFillViewport = true
                    isVerticalScrollBarEnabled = false
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        textView {
                            text = resources.getString(R.string.time)
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textSize = 30f
                            textColor = Color.BLACK
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams {
                            gravity = Gravity.CENTER
                        }
                        timeMatchTextView = textView {
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textSize = 20f
                            setTypeface(typeface, Typeface.BOLD)
                        }.lparams {
                            gravity = Gravity.CENTER
                        }
                        gridLayout {
                            columnCount = 3
                            rowCount = 4
                            useDefaultMargins = true
                            goalDetailWinTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            textView {
                                backgroundColor = Color.parseColor("#F8CB9F")
                                text = resources.getString(R.string.goals_detail)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f //sp
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                                width = dip(100)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            goalDetailLoseTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            yellowCardWinTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            textView {
                                backgroundColor = Color.parseColor("#F8CB9F")
                                text = resources.getString(R.string.yellow_card)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                                width = dip(100)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            yellowCardLoseTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            redCardWinTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            textView {
                                backgroundColor = Color.parseColor("#F8CB9F")
                                text = resources.getString(R.string.red_card)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                                width = dip(100)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            redCardLoseTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            goalKeeperWinTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            textView {
                                backgroundColor = Color.parseColor("#F8CB9F")
                                text = resources.getString(R.string.goal_keeper)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                                width = dip(100)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            goalKeeperLoseTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            defenseWinTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            textView {
                                backgroundColor = Color.parseColor("#F8CB9F")
                                text = resources.getString(R.string.defense)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                                width = dip(100)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            defenseLoseTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            midFieldWinTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            textView {
                                backgroundColor = Color.parseColor("#F8CB9F")
                                text = resources.getString(R.string.mid_field)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                                width = dip(100)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            midFieldLoseTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            //Forward
                            fowardWinTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            textView {
                                backgroundColor = Color.parseColor("#F8CB9F")
                                text = resources.getString(R.string.forward)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                                width = dip(100)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            fowardLoseTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                setTypeface(typeface, Typeface.NORMAL)
                                textColor = Color.BLACK
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }

                            substituteWinTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            textView {
                                backgroundColor = Color.parseColor("#F8CB9F")
                                text = resources.getString(R.string.substitutes)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                                width = dip(100)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            substituteLoseTeamTextView = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                        }.lparams {
                            margin = dip(10)
                        }
                    }.lparams(width = wrapContent)
                }
            }
        }

        supportActionBar?.title = resources.getString(R.string.detail_match)
        val intent = intent
        eventId = intent.getStringExtra(DetailLeagueActivity.MATCH)
        nextMatch = intent.getBooleanExtra(DetailLeagueActivity.TYPE_MATCH, false)
        loadData(eventId)
        favoriteState()
    }

    private fun setDataToText(txt: TextView?, data: String?) {
        if (data == "" || data == null) {
            txt?.text = resources.getString(R.string.strip)
        } else {
            txt?.text = data
        }
    }

    private fun loadData(eventId: String?) {
        request = ApiRepository()
        gson = Gson()
        presenter = MatchPresenter(this, request, gson)
        EspressoIdlingResource.increment()
        presenter.getMatchDetail(eventId)

        swipeRefresh.onRefresh {
            presenter.getMatchDetail(eventId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_match_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (!isFavorite) {
                    addToFavorite()
                } else {
                    removeFromFavorite()
                }
                isFavorite = !isFavorite
                setFavorite()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite() {
        try {
            if (nextMatch == false) {
                database.use {
                    insert(
                        FavoriteMatch.TABLE_FAVORITE_PREVIOUS_MATCH,
                        FavoriteMatch.EVENT_ID to match?.eventId,
                        FavoriteMatch.LEAGUE_ID to match?.leagueId,
                        FavoriteMatch.DATE_MATCH to match?.dateMatch,
                        FavoriteMatch.WIN_TEAM to match?.winTeam,
                        FavoriteMatch.WIN_SCORE to match?.winScore,
                        FavoriteMatch.WIN_BADGE to match?.winBadge,
                        FavoriteMatch.LOSE_TEAM to match?.loseTeam,
                        FavoriteMatch.LOSE_SCORE to match?.loseScore,
                        FavoriteMatch.LOSE_BADGE to match?.loseBadge
                    )
                }
            } else {
                database.use {
                    insert(
                        FavoriteMatch.TABLE_FAVORITE_NEXT_MATCH,
                        FavoriteMatch.EVENT_ID to match?.eventId,
                        FavoriteMatch.LEAGUE_ID to match?.leagueId,
                        FavoriteMatch.DATE_MATCH to match?.dateMatch,
                        FavoriteMatch.WIN_TEAM to match?.winTeam,
                        FavoriteMatch.WIN_SCORE to match?.winScore,
                        FavoriteMatch.WIN_BADGE to match?.winBadge,
                        FavoriteMatch.LOSE_TEAM to match?.loseTeam,
                        FavoriteMatch.LOSE_SCORE to match?.loseScore,
                        FavoriteMatch.LOSE_BADGE to match?.loseBadge
                    )
                }
            }
            swipeRefresh.snackbar(resources.getString(R.string.add_favorite_match)).show()
        } catch (e: SQLiteConstraintException) {
            swipeRefresh.snackbar(e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            if (nextMatch == false) {
                database.use {
                    delete(
                        FavoriteMatch.TABLE_FAVORITE_PREVIOUS_MATCH, "(EVENT_ID = {id})",
                        "id" to eventId
                    )
                }
            } else {
                database.use {
                    delete(
                        FavoriteMatch.TABLE_FAVORITE_NEXT_MATCH, "(EVENT_ID = {id})",
                        "id" to eventId
                    )
                }
            }
            swipeRefresh.snackbar(resources.getString(R.string.removed_favorite_match)).show()

        } catch (e: SQLiteConstraintException) {
            swipeRefresh.snackbar(e.localizedMessage).show()
        }
    }

    private fun favoriteState() {
        if (nextMatch == false) {
            database.use {
                val result = select(FavoriteMatch.TABLE_FAVORITE_PREVIOUS_MATCH)
                    .whereArgs(
                        "(EVENT_ID = {id})",
                        "id" to eventId
                    )
                val favorite = result.parseList(classParser<FavoriteMatch>())
                if (favorite.isNotEmpty()) isFavorite = true
            }
        } else {
            database.use {
                val result = select(FavoriteMatch.TABLE_FAVORITE_NEXT_MATCH)
                    .whereArgs(
                        "(EVENT_ID = {id})",
                        "id" to eventId
                    )
                val favorite = result.parseList(classParser<FavoriteMatch>())
                if (favorite.isNotEmpty()) isFavorite = true
            }
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showMatchList(data: List<Match>) {
        swipeRefresh.isRefreshing = false
        dateMatchTextView.text = match?.dateMatch
        timeMatchTextView.text = match?.timeMatch
        match?.winBadge?.let {
            Picasso.get().load(it).fit().placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_not_found).into(badgeWinTeamImage)
        }
        match?.loseBadge?.let {
            Picasso.get().load(it).fit().placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_not_found).into(badgeLoseTeamImage)
        }
        nameWinTeamTextView.text = match?.winTeam
        nameLoseTeamTextView.text = match?.loseTeam
        setDataToText(scoreWinTeamTextView, match?.winScore)
        setDataToText(scoreLoseTeamTextView, match?.loseScore)
        //Detail
        //Win team
        setDataToText(goalDetailWinTeamTextView, match?.winGoalDetail)
        setDataToText(yellowCardWinTeamTextView, match?.winYellowCard)
        setDataToText(redCardWinTeamTextView, match?.winRedCard)
        setDataToText(goalKeeperWinTeamTextView, match?.winGoalKeeper)
        setDataToText(defenseWinTeamTextView, match?.winDefense)
        setDataToText(midFieldWinTeamTextView, match?.winMidField)
        setDataToText(fowardWinTeamTextView, match?.winForward)
        setDataToText(substituteWinTeamTextView, match?.winSubstitutes)
        //Lose team
        setDataToText(goalDetailLoseTeamTextView, match?.loseGoalDetail)
        setDataToText(yellowCardLoseTeamTextView, match?.loseYellowCard)
        setDataToText(redCardLoseTeamTextView, match?.loseRedCard)
        setDataToText(goalKeeperLoseTeamTextView, match?.loseGoalKeeper)
        setDataToText(defenseLoseTeamTextView, match?.loseDefense)
        setDataToText(midFieldLoseTeamTextView, match?.loseMidField)
        setDataToText(fowardLoseTeamTextView, match?.loseForward)
        setDataToText(substituteLoseTeamTextView, match?.loseSubstitutes)
    }

    override fun setTeamBadge(data: Team) {
        if (!EspressoIdlingResource.idlingresource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }
        index++
        if (match?.winTeam == data.teamName) {
            match?.winBadge = data.teamBadge
        }
        if (match?.loseTeam == data.teamName) {
            match?.loseBadge = data.teamBadge
        }

        if (index >= listTeamName.size) {
            showMatchList(dataMatch)
        }
    }

    override fun getTeamBadge(data: List<Match>) {
        if (!EspressoIdlingResource.idlingresource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }
        dataMatch.clear()
        dataMatch.addAll(data)
        match = data[0]
        listTeamName.clear()
        listTeamName.add(match?.winTeam)
        listTeamName.add(match?.loseTeam)
        for (team in listTeamName) {
            presenter = MatchPresenter(this, request, gson)
            EspressoIdlingResource.increment()
            presenter.getTeamBadge(team)
        }
    }

    override fun showNoFoundText() {
        noFoundDataTextView.visibility = View.VISIBLE
    }

    override fun hideNoFoundText() {
        noFoundDataTextView.visibility = View.INVISIBLE
    }
}
