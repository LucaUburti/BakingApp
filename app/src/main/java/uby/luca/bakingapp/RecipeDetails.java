package uby.luca.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;
import uby.luca.bakingapp.adapters.StepsAdapter;
import uby.luca.bakingapp.data.Recipe;
import uby.luca.bakingapp.widget.UpdateWidgetService;

import static uby.luca.bakingapp.adapters.RecipeAdapter.PARCELED_RECIPE;
import static uby.luca.bakingapp.adapters.StepsAdapter.STEP_POSITION;
import static uby.luca.bakingapp.data.Ingredient.formatIngredientsList;

public class RecipeDetails extends AppCompatActivity implements StepsAdapter.StepOnClickHandler, StepDetailsFragment.OnStepNavbarClickListener {
    private Recipe recipe;
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_INGREDIENTS = "recipe_ingredients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            recipe = bundle.getParcelable(PARCELED_RECIPE);
            if (recipe != null) {
                RecipeDetailsFragment recipedetailsFragment = (RecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_recipedetails);
                recipedetailsFragment.setRecipe(recipe, this);  //set up data for the static fragment

                if (getResources().getBoolean(R.bool.isTablet)) {
                    if (savedInstanceState == null) {  //don't recreate dynamic fragment after rotation
                        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
                        stepDetailsFragment.setArguments(bundle); //set up data for the dynamic fragment for tablet layout
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().add(R.id.step_details_container, stepDetailsFragment).commit();
                    }

                }

            } else {
                Toast.makeText(this, R.string.error_recipe_details, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void stepOnClickImplementation(int clickedStepPosition) { //step clicked
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELED_RECIPE, recipe);
        bundle.putInt(STEP_POSITION, clickedStepPosition);

        if (getResources().getBoolean(R.bool.isTablet)) {
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            stepDetailsFragment.setArguments(bundle); //set up new data for the dynamic fragment for tablet layout
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.step_details_container, stepDetailsFragment).commit();
        } else {
            Intent intent = new Intent(this, StepDetails.class);
            intent.putExtras(bundle); //set up new data for the step details Activity for non-tablet layout
            startActivity(intent);
        }


    }

    @Override
    public void onStepNavbarClicked(int clickedStep) {  //prev-next clicked
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELED_RECIPE, recipe);
        bundle.putInt(STEP_POSITION, clickedStep);
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.step_details_container, stepDetailsFragment).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_as_favourite:
                Toast.makeText(this, R.string.saving_to_widget, Toast.LENGTH_SHORT).show();

                UpdateWidgetService.startActionUpdateIngredientsWidgets(this, recipe);//update widget with recipe

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this); //store favourite recipe name and ingredients on sharedprefs
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(RECIPE_NAME, recipe.getName());
                editor.putString(RECIPE_INGREDIENTS, formatIngredientsList(recipe)); //storing only minimal info for simplicity
                editor.apply();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
