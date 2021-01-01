package com.illiarb.revoluttest.functional.tests

import android.Manifest
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.functional.matchers.ElevationMatcher
import com.illiarb.revoluttest.functional.recyclerview.UiRateItem
import com.illiarb.revoluttest.functional.screens.HomeScreen
import com.illiarb.revoluttest.modules.main.MainActivity
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTests : TestCase(Kaspresso.Builder.simple()) {

    @get:Rule
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @get:Rule
    val activityTestRule = activityScenarioRule<MainActivity>()

    @After
    fun cleanup() {
        activityTestRule.scenario.close()
    }

    /** When app is started loading state should be presented */
    @Test
    fun testLoadingIsDisplayed() = run {
        before { }.after { }.run {
            step("Check loading animation view is displayed and recycler view is gone") {
                HomeScreen {
                    loadingAnimationView.isVisible()
                    recyclerView.isGone()
                }
            }
        }
    }

    /** When app is launched it should show home screen with recycler view visible */
    /** and progress view gone */
    @Test
    fun testHomeScreenInitialState() = run {
        before { }.after { }.run {
            step("Check recycler view is visible and animation progress view is gone") {
                HomeScreen {
                    recyclerView.isVisible()
                    loadingAnimationView.isGone()
                }
            }
        }
    }

    /** When entering new amount in base rate value other rate are updated accordingly */
    @Test
    fun testRatesAreChangingOnTyping() = run {
        before { }.after { }.run {
            step("Check recycler view is visible") {
                HomeScreen {
                    recyclerView.isVisible()
                }
            }

            step("Enter new value in base rate field") {
                HomeScreen {
                    recyclerView.firstChild<UiRateItem> {
                        rateValueEditText(atPosition = 0) {
                            // Set new value to zero
                            it.clearText()
                        }
                    }
                }
            }

            step("Check other rates are updated accordingly") {
                HomeScreen {
                    recyclerView.children<UiRateItem> {
                        rateValueEditText(atPosition = it.ordinal) {
                            it.containsText("0.00")
                        }
                    }
                }
            }
        }
    }

    /** When clicking on the item selected currency should go to top and become active */
    @Test
    fun testSelectedCurrencyGoesToTop() = run {
        val itemToSelectCode = "AUD"

        before { }.after { }.run {
            step("Click on item") {
                HomeScreen {
                    recyclerView.childWith<UiRateItem> {
                        withDescendant {
                            withId(R.id.item_rate_body)
                            withText(itemToSelectCode)
                        }
                    }.click()
                }
            }

            step("Check that clicked item at the top") {
                HomeScreen {
                    recyclerView.firstChild<UiRateItem> {
                        rateCodeWithText(itemToSelectCode) {
                            it.isCompletelyDisplayed()
                        }

                        rateValueEditText(atPosition = 0) {
                            it.isFocused()
                        }
                    }
                }
            }
        }
    }

    /** When recycler view is scrolled down toolbar has elevation */
    @Test
    fun testToolbarHasElevationOnScroll() = run {
        before { }.after { }.run {
            step("Check toolbar has zero elevation") {
                HomeScreen {
                    appBar.matches {
                        withMatcher(ElevationMatcher(predicate = { elevation -> elevation == 0f }))
                    }
                }
            }

            step("Scroll recycler view to an end") {
                HomeScreen {
                    recyclerView.swipeUp()
                }
            }

            step("Check toolbar has elevation") {
                HomeScreen {
                    appBar.matches {
                        withMatcher(ElevationMatcher(predicate = { elevation -> elevation != 0f }))
                    }
                }
            }
        }
    }
}