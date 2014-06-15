package com.appsomehow.badhan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.appsomehow.badhan.helper.ActionBarHelper;
import com.appsomehow.badhan.helper.Constant;
import com.appsomehow.badhan.helper.DateFormatter;
import com.appsomehow.badhan.helper.DbManager;
import com.appsomehow.badhan.helper.DialogHelper;
import com.appsomehow.badhan.helper.DonorInfoValidation;
import com.appsomehow.badhan.helper.Helper;
import com.appsomehow.badhan.model.Donor;
import java.text.ParseException;
import java.util.Calendar;

public class DonorUpdateActivity extends Activity {

    static final int DATE_DIALOG_ID = 999;
    private Donor donor;
    public TextView etMobile,tvName,tvBloodGroup;
    public EditText etName;
    public Spinner spBloodGroup;
    public EditText etLastDonationDate;
    public EditText etNoOfDonation;
    public Button btnUpdate;
    public CheckBox checkBoxEditAll;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_update);
        ActionBarHelper.changeActionBarStyle(this);

        etMobile = (TextView) findViewById(R.id.et_mobile);
        etName = (EditText) findViewById(R.id.et_name);
        spBloodGroup = (Spinner) findViewById(R.id.sp_blood_group);
        etLastDonationDate = (EditText) findViewById(R.id.et_last_donation_date);
        etNoOfDonation = (EditText) findViewById(R.id.et_no_of_donation);
        etLastDonationDate.setInputType(InputType.TYPE_NULL);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        checkBoxEditAll = (CheckBox) findViewById(R.id.cb_edit_all);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvBloodGroup = (TextView) findViewById(R.id.tv_blood_group);

        setUpDonorCurrentInformation();
        setCurrentDateOnDatePicker();
        addListenerOnCheckBox();
        addListenerOnDateField();
        setupButton(btnUpdate);
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
                Intent addNewDonor = new Intent(DonorUpdateActivity.this, AddNewDonorActivity.class);
                startActivity(addNewDonor);
                finish();
                break;

            case R.id.action_donors:
                Intent donorList = new Intent(DonorUpdateActivity.this, DonorListActivity.class);
                startActivity(donorList);
                finish();
                break;

            default:
                break;
        }

        return true;
    }
    private void addListenerOnCheckBox() {
        checkBoxEditAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxEditAll.isChecked()){
                    tvName.setVisibility(View.VISIBLE);
                    etName.setVisibility(View.VISIBLE);
                    tvBloodGroup.setVisibility(View.VISIBLE);
                    spBloodGroup.setVisibility(View.VISIBLE);
                }
                else {
                    tvName.setVisibility(View.GONE);
                    etName.setVisibility(View.GONE);
                    tvBloodGroup.setVisibility(View.GONE);
                    spBloodGroup.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setUpDonorCurrentInformation() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle && bundle.containsKey(Constant.keyDonorMobile)) {
            String donorMobile = bundle.getString(Constant.keyDonorMobile);
            donor = DbManager.getInstance().getDonorWithMobile(donorMobile);

            if (donor != null) {
                etMobile.setText(donor.getMobile().toString());
                spBloodGroup.setSelection(Helper.getBloodGroupIndex(donor.getBloodGroup().toString()));
                etName.setText(donor.getName().toString());
                etLastDonationDate.setText(DateFormatter.getStringFromDate(donor.getLastDonationDate()));
                etNoOfDonation.setText("" + donor.getNoOfDonation());
            }
        }
    }


    private void setupButton(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            Donor donor = new Donor();

            public void onClick(View v) {
                donor.setMobile(etMobile.getText().toString());
                donor.setName(etName.getText().toString());
                donor.setBloodGroup(spBloodGroup.getSelectedItem().toString());
                try {
                    donor.setLastDonationDate(DateFormatter.getDateFromString(etLastDonationDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String donationFrequency = etNoOfDonation.getText().toString();
                if( donationFrequency!= null && !donationFrequency.isEmpty()){
                    donor.setNoOfDonation(Integer.parseInt(donationFrequency));
                }
                if (DonorInfoValidation.isAllInformationFilled(donor)) {
                    updateDonor(donor);
                    finish();
                } else {
                    DialogHelper.openDialog(DonorUpdateActivity.this, "Error", "Please Fill Up all with valid Data !");
                }
            }

        });
    }

    private void updateDonor(Donor donor) {
        DbManager.getInstance().updateDonor(donor);
    }

    private void setCurrentDateOnDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void addListenerOnDateField() {

        etLastDonationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            etLastDonationDate.setText(new StringBuilder().append(day).append("/").append(month + 1)
                    .append("/").append(year)
                    .append(" "));
        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        finish();
    }
}