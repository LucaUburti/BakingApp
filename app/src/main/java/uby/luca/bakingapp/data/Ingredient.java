package uby.luca.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Ingredient implements Parcelable {
    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredient(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    protected Ingredient(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public static String formatIngredientsList(Recipe recipe) {
        StringBuilder formattedIngredients = new StringBuilder();
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient currentIngredient = ingredients.get(i);
            formattedIngredients.append(currentIngredient.getIngredient());
            if (i < ingredients.size() - 1) {
                formattedIngredients.append("; ");
            }
        }
        return formattedIngredients.toString();
    }
}