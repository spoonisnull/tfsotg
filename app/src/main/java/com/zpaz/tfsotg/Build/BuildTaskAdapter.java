package com.zpaz.tfsotg.Build;

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
 * Created by zsolt on 04/03/18.
 */

public class BuildTaskAdapter extends RecyclerView.Adapter<BuildTaskAdapter.BuildTaskHolder>{

    private Context context;
    private List<BuildTask> buildTasks;

    public BuildTaskAdapter(Context context, List<BuildTask> buildTasks) {
        this.context = context;
        this.buildTasks = buildTasks;
    }

    public class BuildTaskHolder extends RecyclerView.ViewHolder{

        private TextView buildStatus;
        private TextView buildRunTime;

        public BuildTaskHolder(View view) {
            super(view);
            buildRunTime = view.findViewById(R.id.buildRunTime);
            buildStatus = view.findViewById(R.id.buildStatusView);
        }
    }

    @Override
    public BuildTaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.build_task_card, parent, false);
        return new BuildTaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BuildTaskAdapter.BuildTaskHolder holder, int position) {
        BuildTask buildTask = buildTasks.get(position);
        holder.buildStatus = setStatusView(holder.buildStatus, buildTask);
        holder.buildRunTime.setText("time placeholder");
    }

    @Override
    public int getItemCount() {
        return buildTasks.size();
    }

    private TextView setStatusView(TextView statusView, BuildTask buildTask){
        statusView.setText(String.valueOf(buildTask.getName()));
        statusView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        statusView.setTypeface(null, Typeface.BOLD);
        statusView.setTextColor(Color.WHITE);
        return (TextView) ColorFunctions.setTextBgColourMatchStatus(context, statusView, buildTask.getState());
    }
}
