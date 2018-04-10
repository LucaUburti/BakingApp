package uby.luca.bakingapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import uby.luca.bakingapp.R;
import uby.luca.bakingapp.data.Step;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {
    private ArrayList<Step> stepsList;

    public StepsAdapter(ArrayList<Step> stepsList){
        this.stepsList=stepsList;
    }


    @Override
    public StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View stepsView= LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item, parent, false);
        return new StepsHolder(stepsView);
    }

    @Override
    public void onBindViewHolder(StepsHolder holder, int position) {
        Step currentStep=stepsList.get(position);
        holder.stepsTv.setText(currentStep.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }


    class StepsHolder extends RecyclerView.ViewHolder{
        TextView stepsTv;
        StepsHolder(View itemView) {
            super(itemView);
            stepsTv=itemView.findViewById(R.id.step_item_tv);
        }

    }
}
