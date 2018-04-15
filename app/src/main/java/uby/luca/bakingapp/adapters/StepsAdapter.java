package uby.luca.bakingapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import uby.luca.bakingapp.R;
import uby.luca.bakingapp.data.Step;

import static android.content.ContentValues.TAG;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {
    private ArrayList<Step> stepsList;
    public static final String PARCELED_STEP = "parceledStep";
    public static final String STEP_POSITION = "stepPosition";


    StepOnClickHandler stepOnClickHandler;

    public interface StepOnClickHandler {
        void stepOnClickImplementation(int clickedStepPosition);
    }


    public StepsAdapter(ArrayList<Step> stepsList, StepOnClickHandler stepOnClickHandler){
        this.stepsList=stepsList;
        this.stepOnClickHandler=stepOnClickHandler;
    }


    @Override
    public StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View stepsView= LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item, parent, false);
        return new StepsHolder(stepsView);
    }

    @Override
    public void onBindViewHolder(final StepsHolder holder, int position) {
        Step currentStep=stepsList.get(position);
        holder.stepIdTv.setText(currentStep.getId());
        holder.stepTv.setText(currentStep.getShortDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepOnClickHandler.stepOnClickImplementation(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }


    class StepsHolder extends RecyclerView.ViewHolder{
        TextView stepTv;
        TextView stepIdTv;
        StepsHolder(View itemView) {
            super(itemView);
            stepTv =itemView.findViewById(R.id.step_item_tv);
            stepIdTv=itemView.findViewById(R.id.step_id_tv);
        }


    }
}
