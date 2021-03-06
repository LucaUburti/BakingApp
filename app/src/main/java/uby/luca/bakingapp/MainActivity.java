package uby.luca.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
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
    @Nullable
    private SimpleIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    private final Context mContext = this;
    private final int RECIPELOADER_ID = 100;
    private RecipeAdapter recipeAdapter;

    @BindView(R.id.main_rv)
    RecyclerView mainRv;
    @BindView(R.id.main_activity)
    ConstraintLayout mainActivity;


    private LoaderManager.LoaderCallbacks<ArrayList<Recipe>> recipeLoader = new LoaderManager.LoaderCallbacks<ArrayList<Recipe>>() {
        @Override
        public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
            return new RecipeAsyncTaskLoader(mContext);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
            //add to adapter and then set it to the corresponding RecyclerView
            if (data != null) {
                recipeAdapter.add(data);
                mainRv.setAdapter(recipeAdapter);
                Log.d("LoaderCallbacks", "onLoadFinished: recipeAdapter finished loading");
            } else {
                Toast.makeText(mContext, R.string.returned_data_is_null, Toast.LENGTH_SHORT).show();
            }
            if (idlingResource != null) {
                idlingResource.setIdleState(true);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {

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
            //Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
            Snackbar.make(mainActivity, R.string.not_connected, Snackbar.LENGTH_LONG).show();
        } else {
            if (idlingResource != null) {
                idlingResource.setIdleState(false);
            }
            getSupportLoaderManager().initLoader(RECIPELOADER_ID, null, recipeLoader);
        }
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
