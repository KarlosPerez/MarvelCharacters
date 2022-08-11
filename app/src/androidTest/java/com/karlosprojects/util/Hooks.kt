package com.karlosprojects.util

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.*


fun checkViewIsDisplayedById(@IdRes viewId: Int) {
    onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

fun checkViewIsDisplayedByText(viewText: String) {
    onView(ViewMatchers.withText(viewText)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

fun checkViewWithIdAndTextIsDisplayed(@IdRes viewId: Int, @StringRes viewText: Int) {
    onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.withText(viewText)))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

fun checkViewInRecyclerWithIdAndTextIsDisplayed(
    @IdRes viewId: Int,
    viewText: String,
    position: Int
) {
    onView(ViewMatchers.withId(viewId)).check(
        ViewAssertions.matches(
            recyclerItemAtPosition(
                position,
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(viewText)
                )
            )
        )
    )
}

fun checkViewWithIdAndTextIsDisplayed(@IdRes viewId: Int, viewText: String) {
    onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.withText(viewText)))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

fun performClickByViewId(@IdRes viewId: Int) {
    onView(ViewMatchers.withId(viewId)).perform(click())
}

fun performClickByText(viewText: String) {
    onView(ViewMatchers.withText(viewText)).perform(click())
}