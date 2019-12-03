package com.dicoding.prayogo.footballLeagueApp

import android.support.design.widget.TabLayout
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dicoding.prayogo.footballLeagueApp.R.id.*
import com.dicoding.prayogo.footballLeagueApp.test.EspressoIdlingResource
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

    @Test
    fun testAppBehaviour() {
        onView(withId(spinner))
            .check(matches(isDisplayed()))
        onView(withId(spinner)).perform(click())
        onView(withText("Spain")).perform(click())
        onView(withId(rv_league_list))
            .check(matches(isDisplayed()))
        onView(withId(rv_league_list)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                1
            )
        )
        onView(withId(rv_league_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click())
        )
        Thread.sleep(1000)
        onView(withId(tab_layout)).perform(selectTabAtPosition(1))
        onView(withId(rv_next_match_list))
            .check(matches(isDisplayed()))
        onView(withId(rv_next_match_list)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                2
            )
        )

        onView(withId(tab_layout)).perform(selectTabAtPosition(0))

        onView(withId(rv_previous_match_list))
            .check(matches(isDisplayed()))
        onView(withId(rv_previous_match_list)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                5
            )
        )

        onView(withId(rv_previous_match_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click())
        )

        onView(withId(add_to_favorite))
            .check(matches(isDisplayed()))
        onView(withId(add_to_favorite)).perform(click())
        onView(withText(R.string.add_favorite_match))
            .check(matches(isDisplayed()))
        android.support.test.espresso.Espresso.pressBack()

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
        onView(withText(R.string.match_favorites)).perform(click())
        onView(withId(tab_layout_favorite)).perform(selectTabAtPosition(1))

        onView(withId(tab_layout_favorite)).perform(selectTabAtPosition(0))

        onView(withId(rv_favorite_previous_match_list))
            .check(matches(isDisplayed()))
        onView(withId(rv_favorite_previous_match_list)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        )

        onView(withId(rv_favorite_previous_match_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(add_to_favorite))
            .check(matches(isDisplayed()))
        onView(withId(add_to_favorite)).perform(click())
        onView(withText(R.string.removed_favorite_match))
            .check(matches(isDisplayed()))
        android.support.test.espresso.Espresso.pressBack()
        onView(withId(refresh_favorite_previous_match_list))
            .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))
        android.support.test.espresso.Espresso.pressBack()

        onView(withId(action_search_menu)).perform(click())
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("brisbane"))
        Thread.sleep(3000)
        onView(withId(android.support.design.R.id.search_src_text)).perform(clearText())
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("arsenal"))
        Thread.sleep(3000)
        onView(withId(android.support.design.R.id.search_src_text)).perform(clearText())
        android.support.test.espresso.Espresso.pressBack()
    }

    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() =
                allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }

    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController?, view: View?) {
                action.perform(uiController, view)
            }
        }
    }
}