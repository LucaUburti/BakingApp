package uby.luca.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uby.luca.bakingapp.adapters.RecipeAdapter;
import uby.luca.bakingapp.data.Recipe;
import uby.luca.bakingapp.loaders.RecipeAsyncTaskLoader;

import static uby.luca.bakingapp.adapters.RecipeAdapter.PARCELED_RECIPE;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeOnClickHandler {
    CountingIdlingResource idlingResource = new CountingIdlingResource("loader_call");

    public CountingIdlingResource getMainActivityIdlingResource() {
        return idlingResource;
    }

    Context mContext = this;
    int RECIPELOADER_ID = 100;
    RecipeAdapter recipeAdapter;
    @BindView(R.id.main_rv)
    RecyclerView mainRv;


    private LoaderManager.LoaderCallbacks<ArrayList<Recipe>> recipeLoader = new LoaderManager.LoaderCallbacks<ArrayList<Recipe>>() {
        @Override
        public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
            return new RecipeAsyncTaskLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
            //add to adapter and then set it to the corresponding RecyclerView
            if (data != null) {
                recipeAdapter.add(data);
                mainRv.setAdapter(recipeAdapter);
                Log.d("MainActivity", "recipeAdapter finished loading");
            } else {
                Toast.makeText(mContext, R.string.returned_data_is_null, Toast.LENGTH_SHORT).show();
            }
            Log.d("CountingIdlingResource", "onLoadFinished: decrementing...");
            idlingResource.decrement();
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.d("MainActivity", "Running on tablet? " + getResources().getBoolean(R.bool.isTablet));

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mainRv.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            mainRv.setLayoutManager(new GridLayoutManager(this, 3));
        }
        recipeAdapter = new RecipeAdapter(this, this);

        if (!isOnline()) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("CountingIdlingResource", "loadInBackground: incrementing...");
        idlingResource.increment();
        getSupportLoaderManager().initLoader(RECIPELOADER_ID, null, recipeLoader);
    }

    private boolean isOnline() {        // from Stack Overflow: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void recipeOnClickImplementation(Recipe clickedRecipe) {
        //Toast.makeText(this, clickedRecipe.getName() + " " + clickedRecipe.getServings(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RecipeDetails.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELED_RECIPE, clickedRecipe);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
