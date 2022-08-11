package com.karlosprojects.util

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher

fun checkViewIsDisplayedById(@IdRes viewId: Int) {
    onView(withId(viewId))
        .check(matches(isDisplayed()))
}

fun checkViewIsDisplayedByText(viewText: String) {
    onView(withText(viewText)).check(matches(isDisplayed()))
}

fun checkViewWithIdAndTextIsDisplayed(@IdRes viewId: Int, @StringRes viewText: Int) {
    onView(withId(viewId))
        .check(matches(withText(viewText)))
        .check(matches(isDisplayed()))
}

fun checkViewWithIdAndPartialTextIsDisplayed(@IdRes viewId: Int, viewText: String) {
    onView(withId(viewId))
        .check(matches(withText(containsString(viewText))))
        .check(matches(isDisplayed()))
}

fun checkCustomViewWithIdAndTextIsDisplayedWithParentId(
    @IdRes parentViewId: Int,
    @IdRes viewId: Int,
    viewText: String
) {
    onView(
        allOf(
            withId(viewId),
            isDescendantOfA(withId(parentViewId))
        )
    ).check(matches(withText(viewText)));
}

fun checkViewInRecyclerWithIdAndTextIsDisplayed(
    @IdRes viewId: Int,
    viewText: String,
    position: Int
) {
    onView(withId(viewId)).check(
        matches(
            recyclerItemAtPosition(
                position,
                hasDescendant(
                    withText(viewText)
                )
            )
        )
    )
}

fun checkViewWithIdAndTextIsDisplayed(@IdRes viewId: Int, viewText: String) {
    onView(withId(viewId))
        .check(matches(withText(viewText)))
        .check(matches(isDisplayed()))
}

fun performClickByViewId(@IdRes viewId: Int) {
    onView(withId(viewId)).perform(click())
}

fun performClickByText(viewText: String) {
    onView(withText(viewText)).perform(click())
}

fun childAtParent(parentMatcher: Matcher<View>): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Child at position in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent

            return parent is ViewGroup && parentMatcher.matches(parent)
        }
    }
}