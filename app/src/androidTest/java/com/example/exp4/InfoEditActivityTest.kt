package com.example.exp4

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * InfoEditActivity 的 UI 测试：
 * 验证表单回填、修改保存后内存数据更新、取消不修改数据
 */
@RunWith(AndroidJUnit4::class)
class InfoEditActivityTest {

    @Before
    fun setUp() {
        with(PlayerStore.info) {
            name = "张三"
            gender = "男"
            studentId = "2023010101"
            phone = "13800000000"
            email = "zhangsan@example.com"
            hobbies = mutableListOf("旅游", "游戏")
        }
    }

    @Test
    fun formIsFilledWithCurrentInfo() {
        ActivityScenario.launch(InfoEditActivity::class.java).use {
            onView(withId(R.id.rb_male)).check(matches(isChecked()))
        }
    }

    @Test
    fun saveButton_updatesPlayerInfoInMemory() {
        ActivityScenario.launch(InfoEditActivity::class.java).use {
            onView(withId(R.id.et_name)).perform(replaceText("李四"))
            onView(withId(R.id.et_phone)).perform(replaceText("13900000000"))
            onView(withId(R.id.rb_female)).perform(click())
            onView(withId(R.id.btn_save)).perform(click())
        }
        assertEquals("李四", PlayerStore.info.name)
        assertEquals("13900000000", PlayerStore.info.phone)
        assertEquals("女", PlayerStore.info.gender)
    }

    @Test
    fun cancelButton_doesNotChangePlayerInfo() {
        ActivityScenario.launch(InfoEditActivity::class.java).use {
            onView(withId(R.id.et_name)).perform(replaceText("王五"))
            onView(withId(R.id.btn_cancel)).perform(click())
        }
        assertEquals("张三", PlayerStore.info.name)
    }
}
