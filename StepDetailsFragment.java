package uby.luca.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import uby.luca.bakingapp.data.Recipe;
import uby.luca.bakingapp.data.Step;

import static uby.luca.bakingapp.adapters.RecipeAdapter.PARCELED_RECIPE;
import static uby.luca.bakingapp.adapters.StepsAdapter.STEP_POSITION;

public class StepDetailsFragment extends Fragment {
    @BindView(R.id.step_details_shortdesc_tv)
    TextView stepDetailsShortdescTv;
    @BindView(R.id.step_details_desc_tv)
    TextView stepDetailsDescTv;
    @BindView(R.id.step_detail_prev_tv)
    TextView prevTv;
    @BindView(R.id.step_detail_next_tv)
    TextView nextTv;

    OnStepNavbarClickListener onStepNavbarClickListener;
    public interface OnStepNavbarClickListener {
        void onStepNavbarClicked(int clickedStep);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onStepNavbarClickListener = (OnStepNavbarClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException();
        }
    }
    public StepDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, rootView);

        Recipe recipe = getArguments().getParcelable(PARCELED_RECIPE);
        final int clickedStepPosition = getArguments().getInt(STEP_POSITION);
        final Step step;
        if (recipe != null) {
            step = recipe.getSteps().get(clickedStepPosition);
            stepDetailsShortdescTv.setText(step.getShortDescription());
            stepDetailsDescTv.setText(step.getDescription());


            if (clickedStepPosition == 0) {
                prevTv.setVisibility(View.GONE);
            } else {
                prevTv.setVisibility(View.VISIBLE);
                prevTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onStepNavbarClickListener.onStepNavbarClicked(clickedStepPosition-1);
                    }
                });
            }

            if (clickedStepPosition == recipe.getSteps().size() - 1) {
                nextTv.setVisibility(View.GONE);
            } else {
                nextTv.setVisibility(View.VISIBLE);
                nextTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onStepNavbarClickListener.onStepNavbarClicked(clickedStepPosition+1);
                    }
                });
            }

        }


        return rootView;


    }
}
