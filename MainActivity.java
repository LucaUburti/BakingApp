package uby.luca.bakingapp;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import uby.luca.bakingapp.data.Recipe;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    int RECIPELOADER_ID = 100;
    private LoaderManager.LoaderCallbacks<ArrayList<Recipe>> recipeLoader = new LoaderManager.LoaderCallbacks<ArrayList<Recipe>>() {
        @Override
        public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
            return new RecipeAsyncTaskLoader(context);
        }
        @Override
        public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
            //add to adapter and then set it to the corresponding RecyclerView
            TextView deleteme = findViewById(R.id.deleteme);
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    Recipe recipe = data.get(i);
                    deleteme.append("\n\n\nricetta numero: " + i);
                    deleteme.append("\nid: " + recipe.getId());
                    deleteme.append("\nname: " + recipe.getName());
                    for (int j = 0; j < recipe.getIngredients().size(); j++) {
                        Recipe.Ingredient ingredient = recipe.getIngredients().get(j);
                        deleteme.append("\ning_quantity: " + ingredient.getQuantity());
                        deleteme.append("\ning_measure: " + ingredient.getMeasure());
                        deleteme.append("\ning_ingredient: " + ingredient.getIngredient());
                    }
                    for (int k = 0; k < recipe.getSteps().size(); k++) {
                        Recipe.Step step = recipe.getSteps().get(k);
                        deleteme.append("\nstep_id: " + step.getId());
                        deleteme.append("\nstep_shortDesc: " + step.getShortDescription());
                        deleteme.append("\nstep_desc: " + step.getDescription());
                        deleteme.append("\nstep_video: " + step.getVideoURL());
                        deleteme.append("\nstep_thumbVideo: " + step.getThumbnailURL());
                    }
                    deleteme.append("\nservings: " + recipe.getServings());
                    deleteme.append("\nimage: " + recipe.getImage());
                }
            } else {
                deleteme.setText("empty!?");
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(RECIPELOADER_ID, null, recipeLoader);


    }
}
