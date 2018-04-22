package uby.luca.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import uby.luca.bakingapp.adapters.StepsAdapter;
import uby.luca.bakingapp.data.Ingredient;
import uby.luca.bakingapp.data.Recipe;
import uby.luca.bakingapp.data.Step;

import static android.content.ContentValues.TAG;

public class RecipeDetailsFragment extends Fragment {
    @BindView(R.id.recipedetails_recipe_tv)
    TextView recipeTv;
    @BindView(R.id.recipedetails_servings_tv)
    TextView servingsTv;
    @BindView(R.id.recipedetails_ingredients_tv)
    TextView ingredientsTv;
    @BindView(R.id.recipedetails_steps_rv)
    RecyclerView stepsRv;
    @BindView(R.id.recipedetails_recipe_iv)
    ImageView recipeIv;

    private final Context mContext = getActivity();

//    OnStepClickListener onStepClickListener;
//    public interface OnStepClickListener {
//        void onStepClickImplementation(int stepId);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try{
//            onStepClickListener=(OnStepClickListener) context;
//        } catch (ClassCastException e){
//            throw new ClassCastException();
//        }
//    }

    //Recipe recipe;

    public void setRecipe(Recipe recipe, StepsAdapter.StepOnClickHandler stepOnClickHandler) {
        //this.recipe = recipe;
        recipeTv.setText(recipe.getName());
        servingsTv.setText(R.string.servings);
        recipeIv.setContentDescription(recipe.getName());
        if (!TextUtils.isEmpty(recipe.getImage())) {
            Picasso.with(mContext)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.food_stock_bg)
                    .error(R.drawable.food_stock_bg)
                    .into(recipeIv);
        } else {
            recipeIv.setImageResource(R.drawable.food_stock_bg);
        }

        servingsTv.append(recipe.getServings());

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient currentIngredient = recipe.getIngredients().get(i);
            ingredientsTv.append(currentIngredient.getIngredient() + ", " + currentIngredient.getQuantity() + " " + currentIngredient.getMeasure());
            ingredientsTv.append("\n");
        }

        for (int j = 0; j < recipe.getSteps().size(); j++) {
            Step currentStep = recipe.getSteps().get(j);
            Log.d(TAG, "step " + j + " " + currentStep.getShortDescription());
        }
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        stepsRv.setLayoutManager(layoutManager);
        //stepsRv.setNestedScrollingEnabled(true);
        StepsAdapter stepsAdapter = new StepsAdapter(recipe.getSteps(), stepOnClickHandler);
        stepsRv.setAdapter(stepsAdapter);




    }


    public RecipeDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, rootView);


        return rootView;
    }
}
