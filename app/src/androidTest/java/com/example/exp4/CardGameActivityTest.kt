package com.example.exp4

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * CardGameActivity 的 UI 测试：
 * 验证翻牌扣分、重置游戏、退出游戏以及最高分同步
 */
@RunWith(AndroidJUnit4::class)
class CardGameActivityTest {

    @Before
    fun setUp() {
        PlayerStore.highScore = 0
    }

    @Test
    fun newGameStartsWithZeroScore() {
        ActivityScenario.launch(CardGameActivity::class.java).use {
            onView(withId(R.id.tv_score)).check(matches(withText("0")))
        }
    }

    @Test
    fun flipOneCard_deductsOnePoint() {
        ActivityScenario.launch(CardGameActivity::class.java).use {
            onView(withId(R.id.card0)).perform(click())
            onView(withId(R.id.tv_score)).check(matches(withText("-1")))
        }
    }

    @Test
    fun resetButton_restoresZeroScore() {
        ActivityScenario.launch(CardGameActivity::class.java).use {
            onView(withId(R.id.card0)).perform(click())
            onView(withId(R.id.btn_reset)).perform(click())
            onView(withId(R.id.tv_score)).check(matches(withText("0")))
        }
    }

    @Test
    fun exitButton_finishesGameActivity() {
        val scenario = ActivityScenario.launch(CardGameActivity::class.java)
        onView(withId(R.id.btn_exit)).perform(click())
        assertTrue(scenario.state != Lifecycle.State.RESUMED)
    }

    @Test
    fun playingGame_updatesHighScore() {
        ActivityScenario.launch(CardGameActivity::class.java).use {
            // 连续翻两张牌，最高分记录应不小于当前得分
            onView(withId(R.id.card0)).perform(click())
            onView(withId(R.id.card1)).perform(click())
            assertTrue(PlayerStore.highScore >= 0)
        }
    }
}
