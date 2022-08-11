package com.karlosprojects.characters_presentation

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.karlosprojects.base.BaseUITest
import com.karlosprojects.characters_presentation.character_overview.CharactersFragment
import com.karlosprojects.idleresources.waitUntilViewIsNotDisplayed
import com.karlosprojects.network.*
import com.karlosprojects.util.checkViewInRecyclerWithIdAndTextIsDisplayed
import com.karlosprojects.util.checkViewIsDisplayedByText
import com.karlosprojects.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class CharactersFragmentTest : BaseUITest(dispatcher = charactersDispatcher) {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        launchFragment()
        hiltRule.inject()
    }

    private fun launchFragment() {
        launchFragmentInHiltContainer<CharactersFragment>()
    }

    @Test
    @SmallTest
    fun when_screen_is_created_should_show_title() {
        checkViewIsDisplayedByText(viewText = "Characters Overview")
    }

    @Test
    @SmallTest
    fun when_characters_is_displayed_should_show_name() {
        waitUntilViewIsNotDisplayed(withId(R.id.cpCharactersLoading))
        checkViewInRecyclerWithIdAndTextIsDisplayed(
            viewId = R.id.rvCharacters,
            viewText = "3-D Man",
            position = 0
        )
        checkViewInRecyclerWithIdAndTextIsDisplayed(
            viewId = R.id.rvCharacters,
            viewText = "A.I.M.",
            position = 2
        )
    }

}

private val charactersDispatcher by lazy {
    object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                SUCCESS_CHARACTERS_PATH -> {
                    mockResponse(
                        FILE_SUCCESS_CHARACTERS_RESPONSE,
                        HttpURLConnection.HTTP_OK
                    )
                }
                SUCCESS_CHARACTER_DETAIL_PATH -> {
                    mockResponse(
                        FILE_SUCCESS_CHARACTER_DETAIL_RESPONSE,
                        HttpURLConnection.HTTP_OK
                    )
                }
                else -> {
                    mockResponse(
                        FILE_SUCCESS_CHARACTERS_RESPONSE,
                        HttpURLConnection.HTTP_OK
                    )
                }
            }
        }
    }
}