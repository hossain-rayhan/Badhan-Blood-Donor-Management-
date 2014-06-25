package com.appsomehow.badhan.helper;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import com.appsomehow.badhan.R;

/**
 * Created by rayhan on 6/24/2014.
 */
public class BaseActionBarActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int titleId = getResources().getIdentifier("action_bar_title", "id","android");
        if(titleId == 0)
            titleId = R.id.title;
        Log.e("ab title Id ", ""+titleId);

        /*TextView tvTitle = (TextView) findViewById(titleId);
        tvTitle.setTextAppearance(this, R.style.action_bar_title_style);*/
    }
}
