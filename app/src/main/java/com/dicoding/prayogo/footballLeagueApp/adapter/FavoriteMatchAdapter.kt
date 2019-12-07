package com.dicoding.prayogo.footballLeagueApp.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dicoding.prayogo.footballLeagueApp.R
import com.dicoding.prayogo.footballLeagueApp.R.id.*
import com.dicoding.prayogo.footballLeagueApp.database.FavoriteMatch
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

class FavoriteMatchAdapter(
    private val favoriteMatches: List<FavoriteMatch>,
    private val listener: (FavoriteMatch) -> Unit
) :
    RecyclerView.Adapter<FavoriteMatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            MatchUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )

    override fun getItemCount(): Int = favoriteMatches.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(favoriteMatches[position], listener)
    }

    class MatchUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            return with(ui) {

                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    topPadding = dip(16)
                    bottomPadding = dip(16)
                    textView {
                        id = tv_match_date
                        backgroundColor = Color.parseColor("#FFB74A")
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textSize = 25f
                        textColor = Color.BLACK
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(width = matchParent, height = matchParent) {
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
                            imageView {
                                id = img_team_win_badge
                                contentDescription = resources.getString(R.string.badge_team)
                                scaleType = ImageView.ScaleType.FIT_XY
                            }.lparams(width = dip(80), height = dip(80)) {
                                gravity = Gravity.CENTER
                            }
                            textView {
                                id = tv_team_win_name
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                            }.lparams {
                                gravity = Gravity.CENTER
                            }
                        }.lparams(width = dip(130))
                        textView {
                            id = tv_team_win_score
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textSize = 25f
                            textColor = Color.BLACK
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams {
                            gravity = Gravity.CENTER
                            marginStart = dip(10)
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
                        textView {
                            id = tv_team_lose_score
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textSize = 25f
                            textColor = Color.BLACK
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams {
                            gravity = Gravity.CENTER
                            marginEnd = dip(10)
                        }
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            setPaddingRelative(dip(16), paddingTop, paddingEnd, paddingBottom)
                            topPadding = dip(16)
                            setPaddingRelative(paddingStart, paddingTop, dip(16), paddingBottom)
                            bottomPadding = dip(16)
                            imageView {
                                id = img_team_lose_badge
                                contentDescription = resources.getString(R.string.badge_team)
                                scaleType = ImageView.ScaleType.FIT_XY
                            }.lparams(width = dip(80), height = dip(80)) {
                                gravity = Gravity.CENTER
                            }
                            textView {
                                id = tv_team_lose_name
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                textSize = 20f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                            }.lparams {
                                gravity = Gravity.CENTER
                            }
                        }.lparams(width = dip(130))
                    }.lparams(width = matchParent, height = matchParent)
                }
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val matchDate: TextView = itemView.find(tv_match_date)

        private val teamWinBadge: ImageView = itemView.find(img_team_win_badge)
        private val teamWinName: TextView = itemView.find(tv_team_win_name)
        private val teamWinScore: TextView = itemView.find(tv_team_win_score)

        private val teamLoseBadge: ImageView = itemView.find(img_team_lose_badge)
        private val teamLoseName: TextView = itemView.find(tv_team_lose_name)
        private val teamLoseScore: TextView = itemView.find(tv_team_lose_score)

        fun bindItem(matches: FavoriteMatch, listener: (FavoriteMatch) -> Unit) {
            matchDate.text = matches.dateMatch
            loadImage(matches.winBadge, teamWinBadge)
            teamWinName.text = matches.winTeam
            setDataToText(teamWinScore, matches.winScore)
            loadImage(matches.loseBadge, teamLoseBadge)
            teamLoseName.text = matches.loseTeam
            setDataToText(teamLoseScore, matches.loseScore)
            itemView.setOnClickListener {
                listener(matches)
            }
        }

        private fun setDataToText(txt: TextView?, data: String?) {
            if (data == "" || data == null) {
                txt?.text = "-"
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
    }
}