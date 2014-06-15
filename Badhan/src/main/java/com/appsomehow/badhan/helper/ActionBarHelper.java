package com.appsomehow.badhan.helper;

import android.app.Activity;
import android.widget.TextView;
import com.appsomehow.badhan.R;

public class ActionBarHelper {
    public static void changeActionBarStyle(Activity activity) {
        int titleId = activity.getResources().getIdentifier("action_bar_title", "id","android");
        TextView tvTitle = (TextView) activity.findViewById(titleId);
        tvTitle.setTextAppearance(activity, R.style.action_bar_title_style);
    }
}
