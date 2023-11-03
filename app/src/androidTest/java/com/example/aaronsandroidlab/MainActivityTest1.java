package com.example.aaronsandroidlab;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest1 {

    /**
     * Activity Scenario
     */
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Check if password is set to 12345 and if Text view's text matches with the text
     */
    @Test
    public void mainActivityTest1() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText2 = onView(withId(R.id.passwordText));
        appCompatEditText2.perform(replaceText("12345"), closeSoftKeyboard());


        ViewInteraction materialButton = onView(withId(R.id.loginBtn));
        materialButton.perform(click());


        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass !")));
    }


    /**
     * Test to Find any missing upper cases
     */
    @Test
    public void testFindMissingUppercase(){

        //Find the passwordText
        ViewInteraction appCompactEditText = onView(withId(R.id.passwordText));

        //Type password: password123#$*
        appCompactEditText.perform(replaceText("password123#$*"));

        //Find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn));
        materialButton.perform(click());


        //Find the text view
        ViewInteraction textView = onView(withId(R.id.textView));

        //Check if text matches
        textView.check(matches(withText("You shall not pass !")));

    }

    /**
     * Test to Find any missing upper lower cases
     */
    @Test
    public void testFindMissingLowercase(){

        //Find the password text
        ViewInteraction appCompactEditText = onView(withId(R.id.passwordText));

        //Type password: password123#$*
        appCompactEditText.perform(replaceText("PASSWORD123#$*"));

        //Find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn));
        materialButton.perform(click());


        //Find the text view
        ViewInteraction textView = onView(withId(R.id.textView));

        //Check if text matches
        textView.check(matches(withText("You shall not pass !")));

    }


    /**
     * Test to Find any missing numbers
     */
    @Test
    public void testFindMissingNumber(){
        ViewInteraction appCompactEditText = onView(withId(R.id.passwordText));

        //Type password: Password#$*
        appCompactEditText.perform(replaceText("Password#$*"));

        //Find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn));
        materialButton.perform(click());


        //Find the text view
        ViewInteraction textView = onView(withId(R.id.textView));

        //Check if text matches
        textView.check(matches(withText("You shall not pass !")));
    }

    /**
     * Test to Find any missing special characters
     */
    @Test
    public void testFindMissingSpecials(){
        ViewInteraction appCompactEditText = onView(withId(R.id.passwordText));

        //Type password: password123
        appCompactEditText.perform(replaceText("password123"));

        //Find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn));
        materialButton.perform(click());


        //Find the text view
        ViewInteraction textView = onView(withId(R.id.textView));

        //Check if text matches
        textView.check(matches(withText("You shall not pass !")));
    }

    /**
     * Test to see if all requirements are met
     */
    @Test
    public void testAllRequirementsMet(){
        ViewInteraction appCompactEditText = onView(withId(R.id.passwordText));

        //Type password: Password123#$*
        appCompactEditText.perform(replaceText("Password123#$*"));

        //Find the button
        ViewInteraction materialButton = onView(withId(R.id.loginBtn));
        materialButton.perform(click());


        //Find the text view
        ViewInteraction textView = onView(withId(R.id.textView));

        //Check if text matches
        textView.check(matches(withText("Your password is complex enough")));
    }

}
