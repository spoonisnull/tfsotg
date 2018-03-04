package com.zpaz.tfsotg.Utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.zpaz.tfsotg.Enums.EntityStatus;
import com.zpaz.tfsotg.R;

/**
 * Created by zsolt on 04/03/18.
 */

public class ColoriseStatusText {

   public static TextView setTextBgColourMatchStatus(Context context, TextView textView, EntityStatus status){
        switch (status){
            case failed:
            case rejected:
            case canceled:
            case abandoned:
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.tfsRed));
                break;
            case inProgress:
            case notStarted:
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.tfsGray));
                break;
            case partiallySucceeded:
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.tfsOrange));
                break;
            case succeeded:
            case active:
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.tfsGreen));
                break;
        }
        return textView;
    }
}
