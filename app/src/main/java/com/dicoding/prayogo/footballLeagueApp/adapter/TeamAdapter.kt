package com.dicoding.prayogo.footballLeagueApp.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dicoding.prayogo.footballLeagueApp.R
import com.dicoding.prayogo.footballLeagueApp.model.Team
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import com.dicoding.prayogo.footballLeagueApp.R.id.*

class TeamAdapter(private val teams: List<Team>, private val listener: (Team) -> Unit) :
    RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TeamViewHolder {
        return TeamViewHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bindItem(teams[position], listener)
    }

    override fun getItemCount(): Int = teams.size

    class TeamUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            return with(ui) {
                linearLayout {
                    lparams(width = matchParent, height = wrapContent)
                    padding = dip(16)
                    orientation = LinearLayout.HORIZONTAL

                    imageView {
                        id = img_team_badge
                    }.lparams {
                        height = dip(80)
                        width = dip(80)
                    }

                    textView {
                        id = tv_team_name
                        textSize = 20f
                        textColor = Color.BLACK
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams {
                        margin = dip(15)
                    }

                }
            }
        }

    }

    class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val teamBadge: ImageView = view.find(img_team_badge)
        private val teamName: TextView = view.find(tv_team_name)

        fun bindItem(teams: Team, listener: (Team) -> Unit) {
            loadImage(teams.teamBadge, teamBadge)
            teamName.text = teams.teamName
            itemView.setOnClickListener { listener(teams) }
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

