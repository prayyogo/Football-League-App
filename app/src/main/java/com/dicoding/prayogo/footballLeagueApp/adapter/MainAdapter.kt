package com.dicoding.prayogo.footballLeagueApp.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dicoding.prayogo.footballLeagueApp.R
import com.dicoding.prayogo.footballLeagueApp.model.League
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import com.dicoding.prayogo.footballLeagueApp.R.id.*

class MainAdapter(private val leagues: List<League>, private val listener: (League) -> Unit) :
    RecyclerView.Adapter<LeagueViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        return LeagueViewHolder(
            LeaguesUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(leagues[position], listener)
    }

    override fun getItemCount(): Int = leagues.size

}

class LeaguesUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            gridLayout {
                columnCount = 2
                rowCount = 3
                padding = dip(16)
                verticalLayout {
                    lparams(width = matchParent, height = wrapContent)
                    padding = dip(16)

                    imageView {
                        id = img_league_logo
                    }.lparams {
                        height = dip(80)
                        width = dip(80)
                        gravity = Gravity.CENTER
                        margin = dip(16)
                    }

                    textView {
                        id = tv_league_name
                        textSize = 16f
                        textColor = Color.BLACK
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }.lparams {
                        margin = dip(16)
                    }
                }
            }
        }
    }
}

class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val leagueLogo: ImageView = view.find(img_league_logo)
    private val leagueName: TextView = view.find(tv_league_name)

    fun bindItem(leagues: League, listener: (League) -> Unit) {
        loadImage(leagues.leagueLogo, leagueLogo)
        leagueName.text = leagues.leagueName
        itemView.setOnClickListener {
            listener(leagues)
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