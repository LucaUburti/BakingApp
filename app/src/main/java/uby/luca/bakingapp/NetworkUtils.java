package uby.luca.bakingapp;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import uby.luca.bakingapp.data.Ingredient;
import uby.luca.bakingapp.data.Step;
import uby.luca.bakingapp.data.Recipe;

public class NetworkUtils {
    public final static String recipeURL = "https://go.udacity.com/android-baking-app-json";


    public static URL buildRecipeURL(String recipeURL) {
        Uri builtUri = Uri.parse(recipeURL).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String readFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();


            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<Recipe> parseRecipeJsonResults(String jsonResults) throws JSONException {
        ArrayList<Recipe> recipesList = new ArrayList<>();


        JSONArray jsonRecipes = new JSONArray(jsonResults);
        for (int i = 0; i < jsonRecipes.length(); i++) {
            JSONObject jsonCurrentRecipe = jsonRecipes.getJSONObject(i);
            String id = jsonCurrentRecipe.optString("id");
            String name = jsonCurrentRecipe.optString("name");
            String servings = jsonCurrentRecipe.optString("servings");
            String image = jsonCurrentRecipe.optString("image");

            JSONArray jsonIngredients = jsonCurrentRecipe.optJSONArray("ingredients");
            ArrayList<Ingredient> ingredientsList = parseIngredients(jsonIngredients);

            JSONArray jsonSteps = jsonCurrentRecipe.optJSONArray("steps");
            ArrayList<Step> stepsList = parseSteps(jsonSteps);

            Recipe recipe = new Recipe(id, name, ingredientsList, stepsList, servings, image);
            recipesList.add(recipe);
        }
        return recipesList;
    }

    private static ArrayList<Ingredient> parseIngredients(JSONArray jsonIngredients) throws JSONException {
        ArrayList<Ingredient> ingredientsList = new ArrayList<>();

        for (int i = 0; i < jsonIngredients.length(); i++) {
            JSONObject jsonCurrentIngredient = jsonIngredients.getJSONObject(i);
            String quantity = jsonCurrentIngredient.optString("quantity");
            String measure = jsonCurrentIngredient.optString("measure");
            String ingredient = jsonCurrentIngredient.optString("ingredient");

            Ingredient recipeIngredient = new Ingredient(quantity, measure, ingredient);
            ingredientsList.add(recipeIngredient);
        }
        return ingredientsList;
    }

    private static ArrayList<Step> parseSteps(JSONArray jsonSteps) throws JSONException {
        ArrayList<Step> stepsList = new ArrayList<>();

        for (int i = 0; i < jsonSteps.length(); i++) {
            JSONObject jsonCurrentStep = jsonSteps.getJSONObject(i);
            String id = jsonCurrentStep.optString("id");
            String shortDescription = jsonCurrentStep.optString("shortDescription");
            String description = jsonCurrentStep.optString("description");
            String videoURL = jsonCurrentStep.optString("videoURL");
            String thumbnailURL = jsonCurrentStep.optString("thumbnailURL");

            Step recipeStep = new Step(id, shortDescription, description, videoURL, thumbnailURL);
            stepsList.add(recipeStep);
        }
        return stepsList;
    }

}
