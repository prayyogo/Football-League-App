package com.dicoding.prayogo.footballLeagueApp

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.dicoding.prayogo.footballLeagueApp.api.ApiRepository
import com.dicoding.prayogo.footballLeagueApp.model.Match
import com.dicoding.prayogo.footballLeagueApp.model.Team
import com.dicoding.prayogo.footballLeagueApp.presenter.MatchPresenter
import com.dicoding.prayogo.footballLeagueApp.util.invisible
import com.dicoding.prayogo.footballLeagueApp.util.visible
import com.dicoding.prayogo.footballLeagueApp.view.MatchView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class DetailMatchActivity : AppCompatActivity(),MatchView {

    private var dataMatch: MutableList<Match> = mutableListOf()
    private var match: Match?=null
    private var eventId:String?=null
    private var listTeamName: MutableList<String?> = mutableListOf()
    private lateinit var tv_dateMatch: TextView
    private lateinit var tv_timeMatch: TextView
    private var index:Int=0
    private lateinit var tv_noFoundData: TextView
    private lateinit var presenter: MatchPresenter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var request = ApiRepository()
    private var gson = Gson()
    // Variable win team
    private lateinit var img_badgeWinTeam: ImageView
    private lateinit var tv_nameWinTeam: TextView
    private lateinit var tv_scoreWinTeam: TextView
    private lateinit var tv_goalDetailWinTeam: TextView
    private lateinit var tv_yellowCardWinTeam: TextView
    private lateinit var tv_redCardWinTeam: TextView
    private lateinit var tv_goalKeeperWinTeam: TextView
    private lateinit var tv_defenseWinTeam: TextView
    private lateinit var tv_midFieldWinTeam: TextView
    private lateinit var tv_fowardWinTeam: TextView
    private lateinit var tv_substituteWinTeam: TextView
    // Variable lose team
    private lateinit var img_badgeLoseTeam: ImageView
    private lateinit var tv_nameLoseTeam: TextView
    private lateinit var tv_scoreLoseTeam: TextView
    private lateinit var tv_goalDetailLoseTeam: TextView
    private lateinit var tv_yellowCardLoseTeam: TextView
    private lateinit var tv_redCardLoseTeam: TextView
    private lateinit var tv_goalKeeperLoseTeam: TextView
    private lateinit var tv_defenseLoseTeam: TextView
    private lateinit var tv_midFieldLoseTeam: TextView
    private lateinit var tv_fowardLoseTeam: TextView
    private lateinit var tv_substituteLoseTeam: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //UI
        frameLayout {
            tv_noFoundData = textView {
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

                tv_dateMatch = textView {
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
                        img_badgeWinTeam = imageView {
                            contentDescription = resources.getString(R.string.badge_team)
                            scaleType = ImageView.ScaleType.FIT_XY
                        }.lparams(width = dip(80), height = dip(80)) {
                            gravity = Gravity.CENTER
                        }
                        tv_nameWinTeam = textView {
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textSize = 20f
                            textColor = Color.BLACK
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams {
                            gravity = Gravity.CENTER
                        }
                    }.lparams(width = dip(130))
                    tv_scoreWinTeam = textView {
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
                    tv_scoreLoseTeam = textView {
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
                        img_badgeLoseTeam = imageView {
                            contentDescription = resources.getString(R.string.badge_team)
                            scaleType = ImageView.ScaleType.FIT_XY
                        }.lparams(width = dip(80), height = dip(80)) {
                            gravity = Gravity.CENTER
                        }
                        tv_nameLoseTeam = textView {
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
                        tv_timeMatch = textView {
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
                            tv_goalDetailWinTeam = textView {
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
                            tv_goalDetailLoseTeam = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            tv_yellowCardWinTeam = textView {
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
                            tv_yellowCardLoseTeam = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            tv_redCardWinTeam = textView {
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
                            tv_redCardLoseTeam = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            tv_goalKeeperWinTeam = textView {
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
                            tv_goalKeeperLoseTeam = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            tv_defenseWinTeam = textView {
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
                            tv_defenseLoseTeam = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            tv_midFieldWinTeam = textView {
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
                            tv_midFieldLoseTeam = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                textColor = Color.BLACK
                                setTypeface(typeface, Typeface.NORMAL)
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }
                            //Forward
                            tv_fowardWinTeam = textView {
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
                            tv_fowardLoseTeam = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 18f
                                setTypeface(typeface, Typeface.NORMAL)
                                textColor = Color.BLACK
                                width = dip(135)
                                gravity = Gravity.CENTER
                            }

                            tv_substituteWinTeam = textView {
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
                            tv_substituteLoseTeam = textView {
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


        supportActionBar?.title =resources.getString(R.string.detail_match)
        val intent = intent
        eventId = intent.getStringExtra(DetailLeagueActivity.match)
        loadData(eventId)
    }
    private fun setDataToText(txt:TextView?, data:String?){
        if(data==""||data==null){
            txt?.text=resources.getString(R.string.strip)
        }else{
            txt?.text=data
        }
    }
    private fun loadData(eventId:String?){
        request = ApiRepository()
        gson = Gson()
        presenter =
            MatchPresenter(this, request, gson)
        presenter.getMatchDetail(eventId)

        swipeRefresh.onRefresh {
            presenter.getMatchDetail(eventId)
        }
    }
    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }
    override fun showMatchList(data: List<Match>) {
        swipeRefresh.isRefreshing = false
        tv_dateMatch.text=match?.dateMatch
        tv_timeMatch.text=match?.timeMatch
        match?.winBadge?.let { Picasso.get().load(it).fit().into(img_badgeWinTeam) }
        match?.loseBadge?.let { Picasso.get().load(it).fit().into(img_badgeLoseTeam)}
        tv_nameWinTeam.text=match?.winTeam
        tv_nameLoseTeam.text=match?.loseTeam
        setDataToText(tv_scoreWinTeam,match?.winScore)
        setDataToText(tv_scoreLoseTeam,match?.loseScore)
        //Detail
        //Win team
        setDataToText(tv_goalDetailWinTeam,match?.winGoalDetail)
        setDataToText(tv_yellowCardWinTeam,match?.winYellowCard)
        setDataToText(tv_redCardWinTeam,match?.winRedCard)
        setDataToText(tv_goalKeeperWinTeam,match?.winGoalKeeper)
        setDataToText(tv_defenseWinTeam,match?.winDefense)
        setDataToText(tv_midFieldWinTeam,match?.winMidField)
        setDataToText(tv_fowardWinTeam,match?.winForward)
        setDataToText(tv_substituteWinTeam,match?.winSubstitutes)
        //Lose team
        setDataToText(tv_goalDetailLoseTeam,match?.loseGoalDetail)
        setDataToText(tv_yellowCardLoseTeam,match?.loseYellowCard)
        setDataToText(tv_redCardLoseTeam,match?.loseRedCard)
        setDataToText(tv_goalKeeperLoseTeam,match?.loseGoalKeeper)
        setDataToText(tv_defenseLoseTeam,match?.loseDefense)
        setDataToText(tv_midFieldLoseTeam,match?.loseMidField)
        setDataToText(tv_fowardLoseTeam,match?.loseForward)
        setDataToText(tv_substituteLoseTeam,match?.loseSubstitutes)
    }

    override fun setTeamBadge(data: Team) {
        index++
            if(match?.winTeam==data.teamName){
                match?.winBadge=data.teamBadge
            }
            if(match?.loseTeam==data.teamName){
                match?.loseBadge=data.teamBadge
            }

        if(index>=listTeamName.size){
            showMatchList(dataMatch)
        }
    }
    override fun getTeamBadge(data: List<Match>) {
        dataMatch.clear()
        dataMatch.addAll(data)
        match=data[0]
        listTeamName.clear()
        listTeamName.add(match?.winTeam)
        listTeamName.add(match?.loseTeam)
        for(team in listTeamName) {
            presenter =
                MatchPresenter(this, request, gson)
            presenter.getTeamBadge(team)
        }
    }
    override fun showNoFoundText() {
        tv_noFoundData.visibility=View.VISIBLE
    }
    override fun hideNoFoundText() {
        tv_noFoundData.visibility=View.INVISIBLE
    }
}
