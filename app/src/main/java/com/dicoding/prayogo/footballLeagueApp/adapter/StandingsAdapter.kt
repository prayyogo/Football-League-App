package com.dicoding.prayogo.footballLeagueApp.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dicoding.prayogo.footballLeagueApp.R.id.*
import com.dicoding.prayogo.footballLeagueApp.model.Standings
import org.jetbrains.anko.*

class StandingsAdapter(
    private val standings: List<Standings>,
    private val listener: (Standings) -> Unit
) :
    RecyclerView.Adapter<StandingsAdapter.StandingsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            StandingsViewHolder {
        return StandingsViewHolder(
            StandingsUI().createView(
                AnkoContext.create(
                    parent.context,
                    parent
                )
            )
        )
    }

    override fun onBindViewHolder(holder: StandingsViewHolder, position: Int) {
        holder.bindItem(standings[position], listener)
    }

    override fun getItemCount(): Int = standings.size

    class StandingsUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            return with(ui) {
                gridLayout {
                    columnCount = 9
                    useDefaultMargins = true
                    textView {
                        id = tv_team
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.BLACK
                        typeface = Typeface.DEFAULT_BOLD
                        textSize = 15f
                        width = dip(100)
                        gravity = Gravity.CENTER
                    }
                    textView {
                        id = tv_main_played
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.BLACK
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                    }
                    textView {
                        id = tv_goals_for
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.BLACK
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                    }
                    textView {
                        id = tv_goals_against
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.BLACK
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                    }
                    textView {
                        id = tv_goals_difference
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.BLACK
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                    }
                    textView {
                        id = tv_win
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.BLACK
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                    }
                    textView {
                        id = tv_loss
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.BLACK
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                    }
                    textView {
                        id = tv_draw
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.BLACK
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                    }
                    textView {
                        id = tv_total
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textColor = Color.BLACK
                        textSize = 15f
                        width = dip(30)
                        gravity = Gravity.CENTER
                    }
                }
            }
        }

    }

    class StandingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val teamName: TextView = view.find(tv_team)
        private val mainPlayed: TextView = view.find(tv_main_played)
        private val goalsFor: TextView = view.find(tv_goals_for)
        private val goalsAgainst: TextView = view.find(tv_goals_against)
        private val goalsDifference: TextView = view.find(tv_goals_difference)
        private val win: TextView = view.find(tv_win)
        private val loss: TextView = view.find(tv_loss)
        private val draw: TextView = view.find(tv_draw)
        private val total: TextView = view.find(tv_total)

        fun bindItem(teams: Standings, listener: (Standings) -> Unit) {
            setDataToText(teamName, teams.teamName)
            setDataToText(mainPlayed, teams.mainPlayer)
            setDataToText(goalsFor, teams.goalsFor)
            setDataToText(goalsAgainst, teams.goalsAgainst)
            setDataToText(goalsDifference, teams.goalsDifference)
            setDataToText(win, teams.win)
            setDataToText(draw, teams.draw)
            setDataToText(loss, teams.lose)
            setDataToText(total, teams.total)
            itemView.setOnClickListener { listener(teams) }
        }

        private fun setDataToText(txt: TextView?, data: String?) {
            if (data.isNullOrEmpty()) {
                txt?.text = "-"
            } else {
                txt?.text = data
            }
        }
    }
}