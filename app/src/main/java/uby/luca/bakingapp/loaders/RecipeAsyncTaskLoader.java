package uby.luca.bakingapp.loaders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import uby.luca.bakingapp.MainActivity;
import uby.luca.bakingapp.NetworkUtils;
import uby.luca.bakingapp.data.Recipe;

public class RecipeAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Recipe>> {

    private ArrayList<Recipe> cachedData;

    public RecipeAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Recipe> loadInBackground() {
        URL url = NetworkUtils.buildRecipeURL(NetworkUtils.recipeURL);
        String jsonResults = null;
        try {
            jsonResults = NetworkUtils.readFromURL(url);
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
        if (cachedData != null) {
            Log.d("RecipeAsyncTaskLoader", "onStartLoading: using cached data");
            deliverResult(cachedData);
        } else {
            Log.d("RecipeAsyncTaskLoader", "onStartLoading: no cached data available, start loading...");

            forceLoad();
        }
    }

    @Override
    public void deliverResult(@Nullable ArrayList<Recipe> data) {
        cachedData=data;
        super.deliverResult(data);
    }

}
