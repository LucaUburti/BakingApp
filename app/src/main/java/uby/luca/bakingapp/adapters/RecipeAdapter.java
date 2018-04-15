package uby.luca.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uby.luca.bakingapp.R;
import uby.luca.bakingapp.data.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {
    public static final String PARCELED_RECIPE = "parceledRecipe";

    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private Context mContext;
    RecipeOnClickHandler recipeOnClickHandler;

    public RecipeAdapter(Context context, RecipeOnClickHandler recipeOnClickHandler) {
        this.mContext = context;
        this.recipeOnClickHandler=recipeOnClickHandler;
    }

    public interface RecipeOnClickHandler {
        void recipeOnClickImplementation(Recipe clickedRecipe);
    }

    public void add(Recipe recipe) {
        recipeList.add(recipe);
        notifyItemInserted(recipeList.size() - 1);
    }

    public void add(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    @Override
    public RecipeAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeTv.setText(recipe.getName());

        holder.recipeIv.setContentDescription(recipe.getName());
        if (!recipe.getImage().isEmpty()) {
            Picasso.with(mContext)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.food_stock_bg)
                    .error(R.drawable.food_stock_bg)
                    .into(holder.recipeIv);
        } else{
            holder.recipeIv.setImageResource(R.drawable.food_stock_bg);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeTv;
        ImageView recipeIv;

        public RecipeHolder(View recipeView) {
            super(recipeView);
            recipeTv = recipeView.findViewById(R.id.recipe_tv);
            recipeIv = recipeView.findViewById(R.id.recipe_iv);
            recipeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(mContext, recipeList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
            recipeOnClickHandler.recipeOnClickImplementation(recipeList.get(getAdapterPosition()));

        }
    }
}
