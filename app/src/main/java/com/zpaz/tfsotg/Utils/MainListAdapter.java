package com.zpaz.tfsotg.Utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zpaz.tfsotg.Enums.EntityStatus;
import com.zpaz.tfsotg.Interfaces.ListedEntity;
import com.zpaz.tfsotg.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zsolt on 04/03/18.
 */

public class MainListAdapter extends ArrayAdapter<ListedEntity>{

    private final Context context;
    private final List<ListedEntity> list;

    public MainListAdapter(@NonNull Context context, int resource, @NonNull List<ListedEntity> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.main_list_layout, parent, false);

        DateTimeParser time = new DateTimeParser(list.get(position).getTime());

        TextView buildDef = rowView.findViewById(R.id.buildDef);
        buildDef.setText(list.get(position).getDefinition());

        TextView buildStatus = rowView.findViewById(R.id.buildStatus);
        EntityStatus status = list.get(position).getStatus();
        String statusText = " " + String.valueOf(status) + " ";
        buildStatus.setText(statusText);
        buildStatus.setTextColor(Color.WHITE);
        ColoriseStatusText.setTextBgColourMatchStatus(context, buildStatus, status);

        TextView buildNo = rowView.findViewById(R.id.buildNo);
        buildNo.setText(list.get(position).getName());

        TextView queuedBy = rowView.findViewById(R.id.queuedBy);
        int maxLength = 50;
        String queuedByText = list.get(position).getCreatedBy();
        queuedByText = queuedByText.length() > 50 ? queuedByText.substring(0,47) + "..." : queuedByText;
        InputFilter[] shortened = new InputFilter[1];
        shortened[0] = new InputFilter.LengthFilter(maxLength);
        queuedBy.setFilters(shortened);
        queuedBy.setText(queuedByText);

        TextView buildTime = rowView.findViewById(R.id.buildTime);
        buildTime.setText("Created: " + time.getDate() + " - " +time.getTime());

        return rowView;
    }
}
