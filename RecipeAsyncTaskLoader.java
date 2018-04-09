package uby.luca.bakingapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import uby.luca.bakingapp.data.Recipe;

public class RecipeAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Recipe>> {



    public RecipeAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Recipe> loadInBackground() {
        URL url = NetworkUtils.buildRecipeURL(NetworkUtils.recipeURL);
        String jsonResults = null;
        try {
            jsonResults = NetworkUtils.readFromURL(url);
            Log.d("RecipeAsyncTaskLoader", "loadInBackground: "+jsonResults);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonResults == null) {
            return null;
        }
        ArrayList<Recipe> recipeList = null;
        try {
            recipeList = NetworkUtils.parseRecipeJsonResults(jsonResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipeList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
