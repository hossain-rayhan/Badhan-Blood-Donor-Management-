package com.appsomehow.badhan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.KeyListener;
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
    private TextView tvDisplayName;
    private Donor donorToUpdate;
    private EditText etMobile, etName, etLastDonationDate, etNoOfDonation, etAddress, etPreferredArea, etComment;
    private Spinner spBloodGroup;
    private Button btnUpdate;
    private CheckBox checkBoxEditAll;
    private int year;
    private int month;
    private int day;
    private LinearLayout llMobile, llBloodGroup, llAddress, llPreferredArea, llComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_update);

        tvDisplayName = (TextView)findViewById(R.id.tv_display_name);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etName = (EditText) findViewById(R.id.et_name);
        spBloodGroup = (Spinner) findViewById(R.id.sp_blood_group);
        etLastDonationDate = (EditText) findViewById(R.id.et_last_donation_date);
        etNoOfDonation = (EditText) findViewById(R.id.et_no_of_donation);
        etLastDonationDate.setInputType(InputType.TYPE_NULL);
        etAddress = (EditText) findViewById(R.id.et_address);
        etPreferredArea = (EditText) findViewById(R.id.et_preferred_area);
        etComment = (EditText) findViewById(R.id.et_comment);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        checkBoxEditAll = (CheckBox) findViewById(R.id.cb_edit_all);
        llMobile = (LinearLayout) findViewById(R.id.tupple_mobile);
        llBloodGroup = (LinearLayout) findViewById(R.id.tupple_blood_group);
        llAddress = (LinearLayout) findViewById(R.id.tupple_address);
        llPreferredArea = (LinearLayout) findViewById(R.id.tupple_preferred_area);
        llComment = (LinearLayout) findViewById(R.id.tupple_comment);


        setUpDonorCurrentInformation();
        setCurrentDateOnDatePicker();
        addListenerOnCheckBox();
        addListenerOnDateField();

        if (Helper.isEditAllChecked) {
            llMobile.setVisibility(View.VISIBLE);
            llBloodGroup.setVisibility(View.VISIBLE);
            llAddress.setVisibility(View.VISIBLE);
            llPreferredArea.setVisibility(View.VISIBLE);
            llComment.setVisibility(View.VISIBLE);
            etName.setVisibility(View.VISIBLE);
            tvDisplayName.setVisibility(View.GONE);
        }
        setupButton(btnUpdate);
    }

    private void addListenerOnCheckBox() {
        checkBoxEditAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxEditAll.isChecked()) {
                    Helper.isEditAllChecked = true;
                    llMobile.setVisibility(View.VISIBLE);
                    llBloodGroup.setVisibility(View.VISIBLE);
                    llAddress.setVisibility(View.VISIBLE);
                    llPreferredArea.setVisibility(View.VISIBLE);
                    llComment.setVisibility(View.VISIBLE);
                    etName.setVisibility(View.VISIBLE);
                    tvDisplayName.setVisibility(View.GONE);
                } else {
                    Helper.isEditAllChecked = false;
                    llMobile.setVisibility(View.GONE);
                    llBloodGroup.setVisibility(View.GONE);
                    llAddress.setVisibility(View.GONE);
                    llPreferredArea.setVisibility(View.GONE);
                    llComment.setVisibility(View.GONE);
                    etName.setVisibility(View.GONE);
                    tvDisplayName.setVisibility(View.VISIBLE);
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
        if (null != bundle && bundle.containsKey(Constant.keyDonor)) {
            int donorId = bundle.getInt(Constant.keyDonor);
            donorToUpdate = DbManager.getInstance().getDonorWithId(donorId);

            if (donorToUpdate != null) {
                tvDisplayName.setText(donorToUpdate.getName().toString());
                etMobile.setText(donorToUpdate.getMobile().toString());
                spBloodGroup.setSelection(Helper.getBloodGroupIndex(donorToUpdate.getBloodGroup().toString()));
                etName.setText(donorToUpdate.getName().toString());
                etLastDonationDate.setText(DateFormatter.getStringFromDate(donorToUpdate.getLastDonationDate()));
                etNoOfDonation.setText("" + donorToUpdate.getNoOfDonation());
                etAddress.setText(donorToUpdate.getAddress());
                etPreferredArea.setText(donorToUpdate.getPreferredArea());
                etComment.setText(donorToUpdate.getComment());
            }
        }
    }


    private void setupButton(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            Donor donor = donorToUpdate;

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
                if (donationFrequency != null && !TextUtils.isEmpty(donationFrequency)) {
                    donor.setNoOfDonation(Integer.parseInt(donationFrequency));
                }
                donor.setAddress(etAddress.getText().toString());
                donor.setPreferredArea(etPreferredArea.getText().toString());
                donor.setComment(etComment.getText().toString());

                if (DonorInfoValidation.isAllInformationFilled(donor)) {
                    updateDonor(donor);
                    Helper.showToast(getBaseContext(), "Updated Successfully");
                    Helper.isEditAllChecked = false;
                    finish();
                } else {
                    DialogHelper.openDialog(DonorUpdateActivity.this, "Error", "Please fill up mandatory field with valid Data !");
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
