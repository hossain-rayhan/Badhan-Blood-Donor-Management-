package com.appsomehow.badhan;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import com.appsomehow.badhan.adapter.AvailableDonorListAdapter;
import com.appsomehow.badhan.helper.ActionBarHelper;
import com.appsomehow.badhan.helper.Constant;
import com.appsomehow.badhan.helper.DateFormatter;
import com.appsomehow.badhan.helper.DbManager;
import com.appsomehow.badhan.helper.Helper;
import com.appsomehow.badhan.model.Donor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AvailableDonorListActivity extends Activity {
    private AvailableDonorListAdapter availableDonorListAdapter;
    private ListView lvSearchedDonor;
    private TextView tvNoDonorAvailable;
    private List<Donor> availableDonorList, donorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbManager.init(this);
        ActionBarHelper.changeActionBarStyle(this);
        setContentView(R.layout.available_donor_list);

        tvNoDonorAvailable = (TextView) findViewById(R.id.tv_no_donor_available);
        donorList = DbManager.getInstance().getAllDonors();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle && bundle.containsKey(Constant.bloodGroup)) {
            String bGroup = bundle.getString(Constant.bloodGroup);
            availableDonorList = getAvailableDonor(bGroup);
            if (availableDonorList.size() <= 0)
                tvNoDonorAvailable.setVisibility(View.VISIBLE);
        }

        availableDonorListAdapter = new AvailableDonorListAdapter(AvailableDonorListActivity.this, R.layout.available_donor_list_item, availableDonorList);
        lvSearchedDonor = (ListView) findViewById(R.id.lv_searched_donor);
        lvSearchedDonor.setAdapter(availableDonorListAdapter);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public List<Donor> getAvailableDonor(String bloodGroup) {
        List<Donor> availableList = new ArrayList<Donor>();
        for (int i = 0; i < donorList.size(); i++) {
            if (donorList.get(i).getBloodGroup().equals(bloodGroup) && isFourMonthOver(donorList.get(i).getLastDonationDate())) {
                availableList.add(donorList.get(i));
            }
        }
        return availableList;
    }
    private boolean isFourMonthOver(Date lastDonationDate){
        Date today = new Date();
        Calendar todayC = Calendar.getInstance();
        todayC.setTime(today);

        Calendar lastDonationC = Calendar.getInstance();
        lastDonationC.setTime(lastDonationDate);
        lastDonationC.add(Calendar.MONTH, 4);

        DateFormatter.CalendarDateWithoutTimeComparator comparator = new DateFormatter.CalendarDateWithoutTimeComparator();
        int available = comparator.compare(todayC, lastDonationC);
        if (available>= 0)
            return true;
        return false;
    }
}
