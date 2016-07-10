package com.bananacoding.expressologin;

import android.support.annotation.StringRes;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by teerapong on 7/7/2016 AD.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void emailIsEmpty() {
        onView(withId(R.id.tv_email)).perform(clearText());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withId(R.id.tv_email)).check(matches(withError(getString(R.string.error_field_required))));
    }

    @Test
    public void passwordIsEmpty() {
        onView(withId(R.id.tv_email)).perform(typeText("email@email.com"), closeSoftKeyboard());
        onView(withId(R.id.tv_password)).perform(clearText());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withId(R.id.tv_password)).check(matches(withError(getString(R.string.error_field_required))));
    }

    @Test
    public void emailIsInvalid() {
        onView(withId(R.id.tv_email)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withId(R.id.tv_email)).check(matches(withError(getString(R.string.error_invalid_email))));
    }

    @Test
    public void passwordIsTooShort() {
        onView(withId(R.id.tv_email)).perform(typeText("email@email.com"), closeSoftKeyboard());
        onView(withId(R.id.tv_password)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withId(R.id.tv_password)).check(matches(withError(getString(R.string.error_invalid_password))));
    }

    @Test
    public void loginFailed() {
        onView(withId(R.id.tv_email)).perform(typeText("incorrect@email.com"), closeSoftKeyboard());
        onView(withId(R.id.tv_password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText(getString(R.string.error_login_failed)))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginSuccessfully_shouldShowWelcomeMessage() {
        onView(withId(R.id.tv_email)).perform(typeText("user@email.com"), closeSoftKeyboard());
        onView(withId(R.id.tv_password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withId(R.id.tv_welcome)).check(matches(withText("Hi user@email.com!")));
    }

    @Test
    public void loginSuccessfully_shouldShowToast() {
        onView(withId(R.id.tv_email)).perform(typeText("user@email.com"), closeSoftKeyboard());
        onView(withId(R.id.tv_password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText(getString(R.string.login_successfully)))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    private String getString(@StringRes int resourceId) {
        return activityTestRule.getActivity().getString(resourceId);
    }

    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (item instanceof EditText) {
                    return ((EditText)item).getError().toString().equals(expected);
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Not found error message" + expected + ", find it!");
            }
        };
    }
}