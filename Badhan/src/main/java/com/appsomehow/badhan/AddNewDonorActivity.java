package com.appsomehow.badhan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.appsomehow.badhan.helper.ActionBarHelper;
import com.appsomehow.badhan.helper.DbManager;
import com.appsomehow.badhan.helper.DialogHelper;
import com.appsomehow.badhan.helper.DonorInfoValidation;
import com.appsomehow.badhan.helper.Helper;
import com.appsomehow.badhan.model.Donor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewDonorActivity extends Activity {

    static final int DATE_DIALOG_ID = 999;

    public EditText etMobile;
    public EditText etName;
    public Spinner spBloogGroup;
    public EditText etLastDonationDate;
    public EditText etNoOfDonation;
    public Button btnAdd;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_add);
        ActionBarHelper.changeActionBarStyle(this);

        etMobile = (EditText) findViewById(R.id.et_mobile);
        etName = (EditText) findViewById(R.id.et_name);
        spBloogGroup = (Spinner) findViewById(R.id.sp_blood_group);
        etLastDonationDate = (EditText) findViewById(R.id.et_last_donation_date);
        etNoOfDonation = (EditText) findViewById(R.id.et_no_of_donation);
        etLastDonationDate.setInputType(InputType.TYPE_NULL);
        setDefaultInfo();
        setCurrentDateOnDatePicker();
        addListenerOnDateField();

        btnAdd = (Button) findViewById(R.id.btn_add);
        setupButton(btnAdd);
    }


    private void setDefaultInfo() {
        etNoOfDonation.setText("" + 0);
    }

    private void setupButton(Button btn) {
        final Activity activity = this;
        btn.setOnClickListener(new View.OnClickListener() {
            Donor donor = new Donor();

            public void onClick(View v) {
                donor.setMobile(etMobile.getText().toString());
                donor.setName(etName.getText().toString());
                donor.setBloodGroup(spBloogGroup.getSelectedItem().toString());
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date lastDate = sdf.parse(etLastDonationDate.getText().toString());
                    donor.setLastDonationDate(lastDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String donationFrequency = etNoOfDonation.getText().toString();
                if (donationFrequency != null && !donationFrequency.isEmpty()) {
                    donor.setNoOfDonation(Integer.parseInt(donationFrequency));
                }
                if (DonorInfoValidation.isAllInformationFilled(donor)) {
                    createNewDonor(donor);
                    Helper.showToast(getBaseContext(),"Added Successfully");
                    finish();

                } else {
                    DialogHelper.openDialog(AddNewDonorActivity.this, "Error", "Please Fill Up all with valid Data !");
                }
            }

        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void createNewDonor(Donor donor) {
        DbManager.getInstance().addDonor(donor);
    }

    private void setCurrentDateOnDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        etLastDonationDate.setText("" + day + "/" + (month + 1) + "/" + (year));
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

}
