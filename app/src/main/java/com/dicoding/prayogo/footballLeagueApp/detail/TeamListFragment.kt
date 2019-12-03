package com.dicoding.prayogo.footballLeagueApp.detail


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout

class TeamListFragment : Fragment(), AnkoComponent<Context> {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(AnkoContext.create(requireContext()))

    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        frameLayout {

        }

    }
}
