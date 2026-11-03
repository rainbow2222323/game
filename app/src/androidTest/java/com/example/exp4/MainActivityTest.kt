package com.example.exp4

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * MainActivity 的 UI 测试：
 * 验证玩家信息展示、跳转到“信息修改”“翻牌游戏”两个二级界面，
 * 以及菜单中的“关于”对话框。
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setUp() {
        PlayerStore.info.name = "张三"
        PlayerStore.info.studentId = "2023010101"
        PlayerStore.highScore = 0
    }

    @Test
    fun displaysPlayerBasicInfo() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.tv_name)).check(matches(withText("张三")))
            onView(withId(R.id.tv_gender)).check(matches(withText("男")))
            onView(withId(R.id.tv_student_id)).check(matches(withText("2023010101")))
            onView(withId(R.id.tv_phone)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_high_score)).check(matches(withText("0")))
        }
    }

    @Test
    fun clickEditInfoButton_opensInfoEditActivity() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btn_edit_info)).perform(click())
            // 信息修改界面的表单控件应该显示出来
            onView(withId(R.id.et_name)).check(matches(isDisplayed()))
            onView(withId(R.id.btn_save)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun clickCardGameButton_opensCardGameActivity() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btn_card_game)).perform(click())
            // 翻牌游戏界面的得分区和 RecyclerView 应该显示出来
            onView(withId(R.id.tv_score)).check(matches(isDisplayed()))
            onView(withId(R.id.rv_cards)).check(matches(isDisplayed()))
            assertTrue(PlayerStore.info.name == "张三")
        }
    }

    @Test
    fun clickAboutMenu_showsAboutDialog() {
        ActivityScenario.launch(MainActivity::class.java).use {
            openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
            onView(withText(R.string.menu_about)).perform(click())
            onView(withText(R.string.about_title)).check(matches(isDisplayed()))
            val message = ApplicationProvider.getApplicationContext<Context>()
                .resources.getStringArray(R.array.about_message_lines)
                .joinToString("\n")
            onView(withText(message)).check(matches(isDisplayed()))
            onView(withText(R.string.btn_ok)).perform(click())
        }
    }
}
