package uby.luca.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import uby.luca.bakingapp.data.Recipe;

import static uby.luca.bakingapp.adapters.RecipeAdapter.PARCELED_RECIPE;

public class UpdateWidgetService extends IntentService {
    private Recipe recipe = null;
    public static final String ACTION_UPDATE_INGREDIENTS = "action_widget_update_ingredients";

    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("UpdateWidgetService", "onHandleIntent: setting widget data after the user has chosen a favourite recipe");
        if (intent != null) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                recipe = bundle.getParcelable(PARCELED_RECIPE);
            }
            if (ACTION_UPDATE_INGREDIENTS.equals(action)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
                for (int appWidgetId : appWidgetIds) {
                    IngredientsWidget.updateAppWidget(this, appWidgetManager, appWidgetId, recipe);
                }
            }
        }


    }

    public static void startActionUpdateIngredientsWidgets(Context context, Recipe recipe) { //called immediately when choosing a favourite recipe
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELED_RECIPE, recipe);
        intent.putExtras(bundle);
        context.startService(intent);
    }
}
