package com.appsomehow.badhan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.appsomehow.badhan.adapter.DonorListAdapter;
import com.appsomehow.badhan.helper.BaseActionBarActivity;
import com.appsomehow.badhan.helper.DbManager;
import com.appsomehow.badhan.helper.Helper;
import com.appsomehow.badhan.model.Donor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DonorListActivity extends BaseActionBarActivity {

    private DonorListAdapter donorListAdapter;
    private EditText inputSearch;
    private ListView lvDonor;
    private int textLength = 0;
    private List<Donor> donorList, searchedDonorList, donorsToDelete;
    Donor donorPressed;
    private ImageButton cancelSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbManager.init(this);
        setContentView(R.layout.donor_list);

        lvDonor = (ListView) findViewById(R.id.lv_donor);
        inputSearch = (EditText) findViewById(R.id.input_search);
        cancelSearch = (ImageButton) findViewById(R.id.btn_cancel_search);

        donorList = DbManager.getInstance().getAllDonors();
        donorsToDelete = new ArrayList<Donor>();
        donorListAdapter = new DonorListAdapter(this, R.layout.donor_list_item, donorList);
        lvDonor.setAdapter(donorListAdapter);

        lvDonor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                donorPressed = donorListAdapter.getItem(position);
                deleteDonorFromList(donorPressed);
                return true;
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                donorListAdapter.getFilter().filter(cs);
                donorsToDelete.clear();
                textLength = inputSearch.getText().length();
                searchedDonorList.clear();
                donorList = DbManager.getInstance().getAllDonors();
                String searchText = inputSearch.getText().toString();

                for (int i = 0; i < donorList.size(); i++) {
                    Donor currentDonor = donorList.get(i);
                    if (textLength <= currentDonor.getBloodGroup().length()) {
                        if (searchText.equalsIgnoreCase((String) currentDonor.getBloodGroup().subSequence(0, textLength))) {
                            searchedDonorList.add(currentDonor);
                            continue;
                        }
                    }
                    if (textLength <= currentDonor.getMobile().length()) {
                        if (searchText.equalsIgnoreCase((String) currentDonor.getMobile().subSequence(0, textLength))) {
                            searchedDonorList.add(currentDonor);
                            continue;
                        }
                    }
                    if (textLength <= currentDonor.getName().length()) {
                        String token;
                        StringTokenizer st = new StringTokenizer(currentDonor.getName());
                        while (st.hasMoreTokens()) {
                            token = st.nextToken();
                            if (token.length() < textLength)
                                continue;
                            if (searchText.equalsIgnoreCase((String)token.subSequence(0, textLength))) {
                                searchedDonorList.add(currentDonor);
                                break;
                            }
                        }

                    }
                }
                donorListAdapter = new DonorListAdapter(DonorListActivity.this, R.layout.donor_list_item, searchedDonorList);
                lvDonor.setAdapter(donorListAdapter);

            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        cancelSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                inputSearch.setText("");
                populateListAdapter();
            }
        });
    }

    private void deleteDonorFromList(final Donor donorToDelete) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                DonorListActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete donor :" + donorToDelete.getName());
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DbManager.getInstance().deleteDonor(donorToDelete);
                inputSearch.setText("");
                populateListAdapter();
                Helper.showToast(getBaseContext(), "Deleted Successfully !!");
            }

        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        populateListAdapter();
        inputSearch.setText("");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void populateListAdapter() {
        searchedDonorList = DbManager.getInstance().getAllDonors();
        donorListAdapter = new DonorListAdapter(this, R.layout.donor_list_item, searchedDonorList);
        lvDonor.setAdapter(donorListAdapter);

    }
}
