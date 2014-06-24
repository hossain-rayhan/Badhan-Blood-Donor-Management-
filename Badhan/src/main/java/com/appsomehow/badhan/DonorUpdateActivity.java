package com.appsomehow.badhan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.appsomehow.badhan.helper.BaseActionBarActivity;
import com.appsomehow.badhan.helper.Constant;
import com.appsomehow.badhan.helper.DateFormatter;
import com.appsomehow.badhan.helper.DbManager;
import com.appsomehow.badhan.helper.DialogHelper;
import com.appsomehow.badhan.helper.DonorInfoValidation;
import com.appsomehow.badhan.helper.Helper;
import com.appsomehow.badhan.model.Donor;
import java.text.ParseException;
import java.util.Calendar;

public class DonorUpdateActivity extends BaseActionBarActivity {

    static final int DATE_DIALOG_ID = 999;
    private Donor donor;
    private TextView etMobile;
    private EditText etName;
    private Spinner spBloodGroup;
    private EditText etLastDonationDate;
    private EditText etNoOfDonation;
    private Button btnUpdate;
    private CheckBox checkBoxEditAll;
    private int year;
    private int month;
    private int day;
    private LinearLayout llName, llBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_update);

        etMobile = (TextView) findViewById(R.id.et_mobile);
        etName = (EditText) findViewById(R.id.et_name);
        spBloodGroup = (Spinner) findViewById(R.id.sp_blood_group);
        etLastDonationDate = (EditText) findViewById(R.id.et_last_donation_date);
        etNoOfDonation = (EditText) findViewById(R.id.et_no_of_donation);
        etLastDonationDate.setInputType(InputType.TYPE_NULL);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        checkBoxEditAll = (CheckBox) findViewById(R.id.cb_edit_all);
        llName = (LinearLayout) findViewById(R.id.tupple_name);
        llBloodGroup = (LinearLayout) findViewById(R.id.tupple_blood_group);

        setUpDonorCurrentInformation();
        setCurrentDateOnDatePicker();
        addListenerOnCheckBox();
        addListenerOnDateField();

        if (Helper.isEditAllChecked) {
            llName.setVisibility(View.VISIBLE);
            llBloodGroup.setVisibility(View.VISIBLE);
        }
        setupButton(btnUpdate);
    }

    private void addListenerOnCheckBox() {
        checkBoxEditAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxEditAll.isChecked()) {
                    Helper.isEditAllChecked = true;
                    llName.setVisibility(View.VISIBLE);
                    llBloodGroup.setVisibility(View.VISIBLE);
                } else {
                    Helper.isEditAllChecked = false;
                    llName.setVisibility(View.GONE);
                    llBloodGroup.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Helper.isEditAllChecked = false;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                Helper.isEditAllChecked = false;
                break;

            default:
                break;
        }
        return true;
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
                if (donationFrequency != null && !donationFrequency.isEmpty()) {
                    donor.setNoOfDonation(Integer.parseInt(donationFrequency));
                }
                if (DonorInfoValidation.isAllInformationFilled(donor)) {
                    updateDonor(donor);
                    Helper.showToast(getBaseContext(),"Updated Successfully");
                    Helper.isEditAllChecked = false;
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

}
