package com.karlosprojects.characters_presentation

import androidx.core.os.bundleOf
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.karlosprojects.base.BaseUITest
import com.karlosprojects.characters_presentation.character_detail.CharacterDetailFragment
import com.karlosprojects.idleresources.waitUntilViewIsNotDisplayed
import com.karlosprojects.network.FILE_SUCCESS_CHARACTER_DETAIL_RESPONSE
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
class CharacterDetailFragmentTest : BaseUITest(dispatcher = QueueDispatcher()) {

    private var navArguments: Int = 1017100

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        launchFragment()
        hiltRule.inject()
    }

    private val successCharacterDetailResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_CHARACTER_DETAIL_RESPONSE, HttpURLConnection.HTTP_OK)

    private val failureCharacterDetailResponse: MockResponse
        get() = mockResponse(
            FILE_SUCCESS_CHARACTER_DETAIL_RESPONSE,
            HttpURLConnection.HTTP_UNAVAILABLE
        )

    private fun launchFragment() {
        launchFragmentInHiltContainer<CharacterDetailFragment>(
            fragmentArgs = bundleOf(
                "characterId" to navArguments
            )
        )
    }

    @Test
    @SmallTest
    fun when_screen_is_created_should_show_title() {
        enqueueResponses(successCharacterDetailResponse)
        waitUntilViewIsNotDisplayed(withId(R.id.cpCharacterLoading))
        checkViewIsDisplayedByText(viewText = "Character Detail")
    }

    @Test
    @SmallTest
    fun when_detail_is_displayed_should_show_character_detail() {
        enqueueResponses(successCharacterDetailResponse)
        waitUntilViewIsNotDisplayed(withId(R.id.cpCharacterLoading))
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.characterDetailTxtAppearances,
            viewText = R.string.character_detail_appearances_title,
        )
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.characterDetailTxtId,
            viewText = "1017100"
        )
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.characterDetailTxtModified,
            viewText = "2013-09-18"
        )
        checkViewWithIdAndPartialTextIsDisplayed(
            viewId = R.id.characterDetailTxtDescription,
            viewText = "Rick Jones has been Hulk's best bud since day one"
        )
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.characterDetailTxtName,
            viewText = "A-Bomb (HAS)"
        )
    }

    @Test
    @SmallTest
    fun when_detail_is_displayed_and_endpoint_get_error_code_should_show_message() {
        enqueueResponses(failureCharacterDetailResponse)
        waitUntilViewIsNotDisplayed(withId(R.id.cpCharacterLoading))
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.characterDetailTxtAppearances,
            viewText = R.string.character_detail_appearances_title,
        )
        checkSnackBarIsDisplayed("Something bad happened in the multiverse")
    }

    @Test
    @SmallTest
    fun when_detail_is_displayed_should_show_appearances() {
        enqueueResponses(successCharacterDetailResponse)
        waitUntilViewIsNotDisplayed(withId(R.id.cpCharacterLoading))
        checkCustomViewWithIdAndTextIsDisplayedWithParentId(
            parentViewId = R.id.cvSeriesAvailable,
            viewId = R.id.characterDetailTxtAvailableQuantity,
            viewText = "2"
        )
        checkCustomViewWithIdAndTextIsDisplayedWithParentId(
            parentViewId = R.id.cvComicsAvailable,
            viewId = R.id.characterDetailTxtAvailableQuantity,
            viewText = "4"
        )
        checkCustomViewWithIdAndTextIsDisplayedWithParentId(
            parentViewId = R.id.cvStoriesAvailable,
            viewId = R.id.characterDetailTxtAvailableQuantity,
            viewText = "7"
        )
    }

}