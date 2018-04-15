package uby.luca.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import uby.luca.bakingapp.data.Recipe;
import uby.luca.bakingapp.data.Step;

import static uby.luca.bakingapp.adapters.RecipeAdapter.PARCELED_RECIPE;
import static uby.luca.bakingapp.adapters.StepsAdapter.PARCELED_STEP;
import static uby.luca.bakingapp.adapters.StepsAdapter.STEP_POSITION;

public class StepDetails extends AppCompatActivity implements StepDetailsFragment.OnStepNavbarClickListener {
    private Step step;
    private Recipe recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        ButterKnife.bind(this);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            recipe = bundle.getParcelable(PARCELED_RECIPE); //passed the whole recipe to be able to easily navigate to next/previous steps
            final int clickedStepPosition = bundle.getInt(STEP_POSITION);
            step = recipe.getSteps().get(clickedStepPosition);


            if (savedInstanceState == null) {  //don't recreate fragments after rotation
                StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
                stepDetailsFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.step_details_container, stepDetailsFragment).commit();
            }

        } else {
            Toast.makeText(this, R.string.error_step_details, Toast.LENGTH_SHORT).show();
        }

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onStepNavbarClicked(int clickedStep) { //prev-next clicked
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELED_RECIPE, recipe);
        bundle.putInt(STEP_POSITION, clickedStep);
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.step_details_container, stepDetailsFragment).commit();
    }
}
