package com.illiarb.revoluttest.functional.tests

import android.Manifest
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
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

    @Test
    // When app is launched it should show home screen with recycler view visible
    // and progress view gone
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

    @Test
    // When entering new amount in base rate value other rate are updated accordingly
    fun testRatesAreChangingOnTyping() = run {
        before { }.after { }.run {
            step("Check recycler view is visible and animation progress view is gone") {
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
}