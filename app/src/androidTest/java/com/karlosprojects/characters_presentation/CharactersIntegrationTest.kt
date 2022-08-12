package com.karlosprojects.characters_presentation

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.karlosprojects.base.BaseUITest
import com.karlosprojects.idleresources.waitUntilViewIsDisplayed
import com.karlosprojects.idleresources.waitUntilViewIsNotDisplayed
import com.karlosprojects.marvelcharacters.MainActivity
import com.karlosprojects.network.FILE_SUCCESS_CHARACTERS_RESPONSE
import com.karlosprojects.network.FILE_SUCCESS_CHARACTER_DETAIL_2_RESPONSE
import com.karlosprojects.network.mockResponse
import com.karlosprojects.util.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class CharactersIntegrationTest : BaseUITest(dispatcher = QueueDispatcher()) {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private val successCharactersResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_CHARACTERS_RESPONSE, HttpURLConnection.HTTP_OK)

    private val successCharacterDetailResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_CHARACTER_DETAIL_2_RESPONSE, HttpURLConnection.HTTP_OK)

    @Test
    @SmallTest
    fun when_screen_is_created_and_click_on_character_should_show_details() {
        enqueueResponses(
            successCharactersResponse,
            successCharacterDetailResponse
        )
        waitUntilViewIsDisplayed(withId(R.id.rvCharacters))
        performClickInViewPositionRecyclerView(
            viewId = R.id.rvCharacters,
            childViewId = R.id.itemCrdCharacter,
            position = 3
        )
        waitUntilViewIsNotDisplayed(withId(R.id.cpCharacterLoading))
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.characterDetailTxtId,
            viewText = "1010699"
        )
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.characterDetailTxtDescription,
            viewText = R.string.character_detail_no_description
        )
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.characterDetailTxtName,
            viewText = "Aaron Stack"
        )
        checkCustomViewWithIdAndTextIsDisplayedWithParentId(
            parentViewId = R.id.cvSeriesAvailable,
            viewId = R.id.characterDetailTxtAvailableQuantity,
            viewText = "3"
        )
        checkCustomViewWithIdAndTextIsDisplayedWithParentId(
            parentViewId = R.id.cvComicsAvailable,
            viewId = R.id.characterDetailTxtAvailableQuantity,
            viewText = "14"
        )
        checkCustomViewWithIdAndTextIsDisplayedWithParentId(
            parentViewId = R.id.cvStoriesAvailable,
            viewId = R.id.characterDetailTxtAvailableQuantity,
            viewText = "27"
        )
    }

    @Test
    @SmallTest
    fun when_click_on_character_and_tap_back_arrow_should_show_characters() {
        enqueueResponses(
            successCharactersResponse,
            successCharacterDetailResponse,
            successCharactersResponse
        )
        waitUntilViewIsDisplayed(withId(R.id.rvCharacters))
        performClickInViewPositionRecyclerView(
            viewId = R.id.rvCharacters,
            childViewId = R.id.itemCrdCharacter,
            position = 3
        )
        waitUntilViewIsNotDisplayed(withId(R.id.cpCharacterLoading))
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.characterDetailTxtName,
            viewText = "Aaron Stack"
        )
        checkCustomViewWithIdAndTextIsDisplayedWithParentId(
            parentViewId = R.id.cvSeriesAvailable,
            viewId = R.id.characterDetailTxtAvailableQuantity,
            viewText = "3"
        )
        performClickOnBackButtonInToolbar(R.id.tbCharacter)
        checkViewInRecyclerWithIdAndTextIsDisplayed(
            viewId = R.id.rvCharacters,
            viewText = "Aaron Stack",
            position = 3
        )
    }
}