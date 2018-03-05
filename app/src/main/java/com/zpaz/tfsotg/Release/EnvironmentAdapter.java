package com.zpaz.tfsotg.Release;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zpaz.tfsotg.R;
import com.zpaz.tfsotg.Utils.ColorFunctions;

import java.util.List;

/**
 * Created by zsolt on 03/03/18.
 */

public class EnvironmentAdapter extends RecyclerView.Adapter<EnvironmentAdapter.EnvHolder>{

    private Context context;
    private List<ReleaseEnvironment> environmentsList;

    public class EnvHolder extends RecyclerView.ViewHolder{
        public TextView status;
        public TextView runTime;

        public EnvHolder(View view){
            super(view);
            status = view.findViewById(R.id.envStatusView);
            runTime = view.findViewById(R.id.envRunTime);
        }
    }

    public EnvironmentAdapter(Context context, List<ReleaseEnvironment> environmentsList){
        this.context = context;
        this.environmentsList = environmentsList;
    }

    @Override
    public EnvironmentAdapter.EnvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.env_card, parent, false);
        return new EnvHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EnvironmentAdapter.EnvHolder holder, int position) {
        ReleaseEnvironment environment = environmentsList.get(position);
        holder.status = setStatusView(holder.status, environment);
        holder.runTime.setText("time placeholder");
    }

    @Override
    public int getItemCount() {
        return environmentsList.size();
    }

    private TextView setStatusView(TextView statusView, ReleaseEnvironment environment){
        statusView.setText(String.valueOf(environment.getName()));
        statusView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        statusView.setTypeface(null, Typeface.BOLD);
        statusView.setTextColor(Color.WHITE);
        return (TextView)ColorFunctions.setTextBgColourMatchStatus(context, statusView, environment.getStatus());
    }
}
