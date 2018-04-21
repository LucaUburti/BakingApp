package uby.luca.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import uby.luca.bakingapp.MainActivity;
import uby.luca.bakingapp.R;
import uby.luca.bakingapp.RecipeDetails;
import uby.luca.bakingapp.data.Recipe;

import static uby.luca.bakingapp.data.Ingredient.formatIngredientsList;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {
        //called by UpdateWidgetService when the user chooses a favourite recipe

        String widgetRecipeText;
        String widgetIngredientsText = "";

        if (recipe == null) {
            widgetRecipeText = context.getString(R.string.no_favourite_recipe_yet);
        } else {
            widgetRecipeText = recipe.getName();
            widgetIngredientsText = formatIngredientsList(recipe);
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        PendingIntent pi = setupPendingIntent(context);

        views.setTextViewText(R.id.recipe_widget_tv, widgetRecipeText);
        views.setTextViewText(R.id.ingredients_widget_tv, widgetIngredientsText);
        views.setOnClickPendingIntent(R.id.ingredients_widget_ll, pi);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String recipeName, String formattedIngredients) {
        //called on periodic updates, data is coming from shared prefs

        if (recipeName.equals("")) {
            recipeName = context.getString(R.string.no_favourite_recipe_yet);
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        PendingIntent pi = setupPendingIntent(context);

        views.setTextViewText(R.id.recipe_widget_tv, recipeName);
        views.setTextViewText(R.id.ingredients_widget_tv, formattedIngredients);
        views.setOnClickPendingIntent(R.id.ingredients_widget_ll, pi);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static PendingIntent setupPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //get the text for the widget from shared prefs
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = sp.getString(RecipeDetails.RECIPE_NAME, "");
        String formattedIngredients = sp.getString(RecipeDetails.RECIPE_INGREDIENTS, "");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName, formattedIngredients);
        }
        Log.d("IngredientsWidget", "onUpdate: periodic widget data update with text from from shared prefs");
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

