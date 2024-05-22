package com.example.proyectofinalmovil

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainUnitTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun register_isCorrect(){
        onView(withId(R.id.tvGoRegister)).perform(click())

        onView(withId(R.id.etRegisterName)).perform(typeText("John Doe"))

        onView(withId(R.id.etRegisterEmail)).perform(typeText("john@doe.com"))

        onView(withId(R.id.etRegisterPassword)).perform(typeText("123456john"))

        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btnRegister)).perform(click())
    }

    @Test
    fun login_isCorrect(){
        onView(withId(R.id.etLoginEmail)).perform(typeText("john@doe.com"))

        onView(withId(R.id.etLoginPassword)).perform(typeText("123456john"))

        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btnLogin)).perform(click())
    }
}