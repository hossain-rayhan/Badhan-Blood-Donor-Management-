package com.appsomehow.badhan;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.view.View.OnClickListener;
import com.appsomehow.badhan.adapter.DonorListAdapter;
import com.appsomehow.badhan.helper.ActionBarHelper;
import com.appsomehow.badhan.helper.DbManager;
import com.appsomehow.badhan.helper.Helper;
import com.appsomehow.badhan.model.Donor;
import java.util.ArrayList;
import java.util.List;

public class DonorListActivity extends Activity {


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
        ActionBarHelper.changeActionBarStyle(this);

        lvDonor = (ListView) findViewById(R.id.lv_donor);
        inputSearch = (EditText) findViewById(R.id.input_search);
        cancelSearch = (ImageButton) findViewById(R.id.btn_cancel_search);

        searchedDonorList = DbManager.getInstance().getAllDonors();
        donorList = DbManager.getInstance().getAllDonors();
        donorsToDelete = new ArrayList<Donor>();
/*
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                donorListAdapter = new DonorListAdapter(DonorListActivity.this, R.layout.donor_list_item, searchedDonorList);
                donorListAdapter.getFilter().filter(cs);
                textLength = inputSearch.getText().length();
                searchedDonorList.clear();
                for (int i = 0; i < donorList.size(); i++) {
                    if (textLength <= donorList.get(i).getBloodGroup().length()) {
                        if (inputSearch.getText().toString().equalsIgnoreCase((String) donorList.get(i).getBloodGroup().subSequence(0, textLength))) {
                            searchedDonorList.add(donorList.get(i));
                            continue;
                        }
                    }
                    if (textLength <= donorList.get(i).getMobile().length()) {
                        if (inputSearch.getText().toString().equalsIgnoreCase((String) donorList.get(i).getMobile().subSequence(0, textLength))) {
                            searchedDonorList.add(donorList.get(i));
                            continue;
                        }
                    }
                    if (textLength <= donorList.get(i).getName().length()) {
                        if (inputSearch.getText().toString().equalsIgnoreCase((String) donorList.get(i).getName().subSequence(0, textLength))) {
                            searchedDonorList.add(donorList.get(i));
                            continue;
                        }
                    }
                }
                lvDonor.setAdapter(new DonorListAdapter(DonorListActivity.this, R.layout.donor_list_item, searchedDonorList));
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
        });*/

        lvDonor.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvDonor.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                donorPressed = (Donor) donorListAdapter.getItem(position);
                if (checked) {
                    donorsToDelete.add(donorPressed);
                } else {
                    donorsToDelete.remove(donorPressed);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.cab_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                donorListAdapter.setCabActivated(true);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        DbManager.getInstance().deleteDonor(donorsToDelete);
                        populateListAdapter();
                        donorListAdapter.setCabActivated(true);
                        Helper.showToast(getBaseContext(), "" + donorsToDelete.size() + " item Deleted !!!");
                        donorsToDelete.clear();
                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                donorListAdapter.setCabActivated(false);
                donorsToDelete.clear();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        populateListAdapter();
        inputSearch.setText("");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("On Config Change: ", "Called");
        ActionBarHelper.changeActionBarStyle(this);
        super.onConfigurationChanged(newConfig);
    }

    private void populateListAdapter() {
        donorList = DbManager.getInstance().getAllDonors();
        donorListAdapter = new DonorListAdapter(this, R.layout.donor_list_item, donorList);
        lvDonor.setAdapter(donorListAdapter);
    }
}
