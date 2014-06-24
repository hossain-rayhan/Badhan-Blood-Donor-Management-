package com.appsomehow.badhan;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import com.appsomehow.badhan.helper.ActionBarHelper;
import com.appsomehow.badhan.helper.BaseActionBarActivity;
import com.appsomehow.badhan.helper.Constant;
import com.appsomehow.badhan.helper.DbManager;


public class MainActivity extends BaseActionBarActivity {

    public Button btnSearch;
    public Spinner spBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbManager.init(this);
        setContentView(R.layout.activity_main);

        btnSearch = (Button) findViewById(R.id.btn_search);
        spBloodGroup = (Spinner) findViewById(R.id.sp_blood_group);

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
