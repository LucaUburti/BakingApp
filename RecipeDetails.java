package uby.luca.bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import uby.luca.bakingapp.adapters.StepsAdapter;
import uby.luca.bakingapp.data.Recipe;
import uby.luca.bakingapp.data.Step;

import static uby.luca.bakingapp.adapters.RecipeAdapter.PARCELED_RECIPE;
import static uby.luca.bakingapp.adapters.StepsAdapter.PARCELED_STEP;
import static uby.luca.bakingapp.adapters.StepsAdapter.STEP_POSITION;

public class RecipeDetails extends AppCompatActivity implements StepsAdapter.StepOnClickHandler {
    private Recipe recipe;


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
                recipedetailsFragment.setRecipe(recipe, this);


            } else {
                Toast.makeText(this, R.string.error_recipe_details, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void stepOnClickImplementation(int clickedStepPosition) {


        Intent intent = new Intent(this, StepDetails.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELED_RECIPE, recipe);
        bundle.putInt(STEP_POSITION, clickedStepPosition);

        intent.putExtras(bundle);
        startActivity(intent);

    }
}
