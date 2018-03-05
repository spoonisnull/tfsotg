package com.zpaz.tfsotg.Utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.zpaz.tfsotg.Enums.EntityStatus;
import com.zpaz.tfsotg.R;

/**
 * Created by zsolt on 04/03/18.
 */

public class ColorFunctions {

   public static View setTextBgColourMatchStatus(Context context, View view, EntityStatus status){
        switch (status){
            case failed:
            case rejected:
            case canceled:
            case abandoned:
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.tfsRed));
                break;
            case inProgress:
            case notStarted:
            case skipped:
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.tfsGray));
                break;
            case partiallySucceeded:
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.tfsOrange));
                break;
            case succeeded:
            case completed:
            case active:
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.tfsGreen));
                break;
        }
        return view;
    }
}
