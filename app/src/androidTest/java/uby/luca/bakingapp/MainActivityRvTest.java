package uby.luca.bakingapp;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityRvTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        CountingIdlingResource mIdlingResource = mActivityRule.getActivity().getMainActivityIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void clickRecyclerView_MainActivity() {
        //click on main recyclerview
        onView(withId(R.id.main_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //check that we have a view containing the text "Ingredients List"
        onView(withId(R.id.ingredients_list_header)).check(matches(withText(mActivityRule.getActivity().getResources().getString(R.string.ingredients_list))));
        //check that we can click on the Step named "Recipe Introduction"
        onView(withId(R.id.recipedetails_steps_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Recipe Introduction")), click()));
    }
}

