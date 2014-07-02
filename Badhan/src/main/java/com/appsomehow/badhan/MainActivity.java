package com.appsomehow.badhan;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Region;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.appsomehow.badhan.helper.ActionBarHelper;
import com.appsomehow.badhan.helper.BaseActionBarActivity;
import com.appsomehow.badhan.helper.CSVToDbHelper;
import com.appsomehow.badhan.helper.Constant;
import com.appsomehow.badhan.helper.DbManager;
import com.appsomehow.badhan.helper.DbTableName;
import com.appsomehow.badhan.helper.PreferenceHelper;
import com.appsomehow.badhan.model.Donor;


public class MainActivity extends BaseActionBarActivity {

    private Button btnSearch;
    private Spinner spBloodGroup;
    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbManager.init(this);
        setContentView(R.layout.activity_main);

        btnSearch = (Button) findViewById(R.id.btn_search);
        spBloodGroup = (Spinner) findViewById(R.id.sp_blood_group);
        preferenceHelper = new PreferenceHelper(this);
        if (!preferenceHelper.getBoolean(Constant.IS_DB_CREATED, false)) {
            CSVToDbHelper.readCSVAndInserIntoDb(this, R.raw.donor, DbTableName.Donor);
            preferenceHelper.setBoolean(Constant.IS_DB_CREATED, true);
        }

        addListenerToSearchButton();
    }

    private void addListenerToSearchButton() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bloodGroup = spBloodGroup.getSelectedItem().toString();
                Intent availableDonor = new Intent(MainActivity.this, AvailableDonorListActivity.class);
                availableDonor.putExtra(Constant.bloodGroup, bloodGroup);
                startActivity(availableDonor);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent addNewDonor = new Intent(MainActivity.this, AddNewDonorActivity.class);
                startActivity(addNewDonor);
                break;

            case R.id.action_donors:
                Intent donorList = new Intent(MainActivity.this, DonorListActivity.class);
                startActivity(donorList);
                break;

            default:
                break;
        }

        return true;
    }

}
