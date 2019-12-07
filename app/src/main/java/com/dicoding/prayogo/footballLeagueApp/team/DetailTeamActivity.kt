package com.dicoding.prayogo.footballLeagueApp.team

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
import com.dicoding.prayogo.footballLeagueApp.R
import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.database.FavoriteTeam
import com.dicoding.prayogo.footballLeagueApp.database.teamDatabase
import com.dicoding.prayogo.footballLeagueApp.model.Team
import com.dicoding.prayogo.footballLeagueApp.presenter.TeamPresenter
import com.dicoding.prayogo.footballLeagueApp.test.EspressoIdlingResource
import com.dicoding.prayogo.footballLeagueApp.util.invisible
import com.dicoding.prayogo.footballLeagueApp.util.visible
import com.dicoding.prayogo.footballLeagueApp.view.TeamView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class DetailTeamActivity : AppCompatActivity(), TeamView {
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var teamId: String

    private lateinit var teamBadgeImage: ImageView
    private lateinit var teamNameTextView: TextView
    private lateinit var teamFormedYearTextView: TextView
    private lateinit var teamStadiumTextView: TextView
    private lateinit var locationStadiumTextView: TextView
    private lateinit var teamDescriptionTextView: TextView
    private lateinit var stadiumImage: ImageView
    private lateinit var capacityStadiumTextView: TextView
    private lateinit var stadiumDescriptionTextView: TextView
    private lateinit var jerseyImage: ImageView
    private lateinit var jerseyTextView: TextView
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var team: Team
    private lateinit var noFoundDataTextView: TextView
    private lateinit var presenter: TeamPresenter
    private var request = ApiRepository()
    private var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // UI
        frameLayout {
            noFoundDataTextView = textView {
                text = resources.getString(R.string.no_team_data)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                textSize = 20f
                textColor = Color.BLACK
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
            }
            linearLayout {
                lparams(width = matchParent, height = wrapContent)
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.WHITE

                swipeRefresh = swipeRefreshLayout {
                    setColorSchemeResources(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )

                    scrollView {
                        isVerticalScrollBarEnabled = false
                        relativeLayout {
                            lparams(width = matchParent, height = wrapContent)

                            linearLayout {
                                lparams(width = matchParent, height = wrapContent)
                                padding = dip(10)
                                orientation = LinearLayout.VERTICAL
                                gravity = Gravity.CENTER_HORIZONTAL

                                teamBadgeImage = imageView {}.lparams(
                                    height = dip(80),
                                    width = dip(80)
                                )

                                teamNameTextView = textView {
                                    this.gravity = Gravity.CENTER
                                    typeface = Typeface.DEFAULT_BOLD
                                    textSize = 20f
                                    textColor = ContextCompat.getColor(
                                        context,
                                        R.color.colorAccent
                                    )
                                }.lparams {
                                    topMargin = dip(5)
                                }

                                teamFormedYearTextView = textView {
                                    this.gravity = Gravity.CENTER
                                    textColor = Color.BLACK
                                    typeface = Typeface.DEFAULT_BOLD
                                }

                                teamDescriptionTextView = textView {
                                    textColor = Color.BLACK
                                }.lparams {
                                    topMargin = dip(20)
                                    bottomMargin = dip(20)
                                }

                                stadiumImage = imageView {}.lparams(
                                    height = dip(200),
                                    width = dip(500)
                                )
                                teamStadiumTextView = textView {
                                    this.gravity = Gravity.CENTER
                                    typeface = Typeface.DEFAULT_BOLD
                                    textColor =
                                        ContextCompat.getColor(
                                            context,
                                            R.color.colorPrimaryText
                                        )
                                }
                                locationStadiumTextView = textView {
                                    this.gravity = Gravity.CENTER
                                    textColor = Color.BLACK
                                    typeface = Typeface.DEFAULT_BOLD
                                }
                                capacityStadiumTextView = textView {
                                    this.gravity = Gravity.CENTER
                                    textColor = Color.BLACK
                                    typeface = Typeface.DEFAULT_BOLD
                                }
                                stadiumDescriptionTextView = textView {
                                    textColor = Color.BLACK
                                }.lparams {
                                    topMargin = dip(20)
                                    bottomMargin = dip(20)
                                }
                                jerseyImage = imageView {}.lparams(
                                    height = dip(100),
                                    width = dip(100)
                                )
                                jerseyTextView = textView {
                                    this.gravity = Gravity.CENTER
                                    textColor = Color.BLACK
                                    typeface = Typeface.DEFAULT_BOLD
                                }
                            }
                            progressBar = progressBar {
                            }.lparams {
                                centerHorizontally()
                            }
                        }
                    }
                }
            }
        }

        supportActionBar?.title = resources.getString(R.string.detail_team)
        val intent = intent
        teamId = intent.getStringExtra(TeamActivity.TEAM_ID)
        loadData(teamId)
        favoriteState()
    }

    private fun loadData(teamId: String?) {
        request = ApiRepository()
        gson = Gson()
        presenter = TeamPresenter(this, request, gson)
        EspressoIdlingResource.increment()
        presenter.getTeamDetail(teamId)
        swipeRefresh.onRefresh {
            presenter.getTeamDetail(teamId)
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

            teamDatabase.use {
                insert(
                    FavoriteTeam.TABLE_FAVORITE_TEAM,
                    FavoriteTeam.TEAM_ID to team.teamId,
                    FavoriteTeam.LEAGUE_ID to team.leagueId,
                    FavoriteTeam.TEAM_ID to team.teamId,
                    FavoriteTeam.TEAM_BADGE to team.teamBadge,
                    FavoriteTeam.TEAM_NAME to team.teamName
                )
            }
            swipeRefresh.snackbar(resources.getString(R.string.add_favorite)).show()
        } catch (e: SQLiteConstraintException) {
            swipeRefresh.snackbar(e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            teamDatabase.use {
                delete(
                    FavoriteTeam.TABLE_FAVORITE_TEAM, "(TEAM_ID = {id})",
                    "id" to teamId
                )
            }

            swipeRefresh.snackbar(resources.getString(R.string.removed_favorite)).show()

        } catch (e: SQLiteConstraintException) {
            swipeRefresh.snackbar(e.localizedMessage).show()
        }
    }

    private fun favoriteState() {
        teamDatabase.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
                .whereArgs(
                    "(TEAM_ID = {id})",
                    "id" to teamId
                )
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (favorite.isNotEmpty()) isFavorite = true
        }
    }

    private fun setDataToText(txt: TextView?, data: String?) {
        if (data == "" || data == null) {
            txt?.text = resources.getString(R.string.strip)
        } else {
            txt?.text = data
        }
    }

    private fun loadImage(url: String?, image: ImageView) {
        if (url.isNullOrEmpty()) {
            Picasso.get().load(R.drawable.img_placeholder).fit().into(image)
        } else {
            Picasso.get().load(url).placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_not_found).fit().into(image)
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_added_to_favorites
                )
        else
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_add_to_favorites
                )
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showNoFoundText() {
        noFoundDataTextView.visibility = View.VISIBLE
    }

    override fun hideNoFoundText() {
        noFoundDataTextView.visibility = View.INVISIBLE
    }

    override fun showTeamList(data: List<Team>) {
        if (!EspressoIdlingResource.idlingresource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }
        swipeRefresh.isRefreshing = false
        team = data[0]
        loadImage(team.teamBadge, teamBadgeImage)
        setDataToText(teamNameTextView, team.teamName)
        setDataToText(teamDescriptionTextView, team.teamDescription)
        setDataToText(teamFormedYearTextView, team.formerYear)
        loadImage(team.stadiumImage, stadiumImage)
        setDataToText(teamStadiumTextView, team.stadiumName)
        setDataToText(locationStadiumTextView, team.stadiumLocation)
        setDataToText(
            capacityStadiumTextView,
            team.stadiumCapacity + " " + resources.getString(R.string.capacity)
        )
        setDataToText(stadiumDescriptionTextView, team.stadiumDescription)
        loadImage(team.jerseyImage, jerseyImage)
        setDataToText(jerseyTextView, team.teamName + " " + resources.getString(R.string.jersey))
    }

}
