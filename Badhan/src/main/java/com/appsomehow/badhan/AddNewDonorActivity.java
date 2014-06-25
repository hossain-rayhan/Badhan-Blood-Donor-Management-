package com.appsomehow.badhan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import com.appsomehow.badhan.helper.BaseActionBarActivity;
import com.appsomehow.badhan.helper.Constant;
import com.appsomehow.badhan.helper.DbManager;
import com.appsomehow.badhan.helper.DialogHelper;
import com.appsomehow.badhan.helper.DonorInfoValidation;
import com.appsomehow.badhan.helper.Helper;
import com.appsomehow.badhan.model.Donor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewDonorActivity extends BaseActionBarActivity {

    static final int DATE_DIALOG_ID = 999;

    public EditText etMobile;
    public EditText etName;
    public Spinner spBloodGroup;
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

        etMobile = (EditText) findViewById(R.id.et_mobile);
        etName = (EditText) findViewById(R.id.et_name);
        spBloodGroup = (Spinner) findViewById(R.id.sp_blood_group);
        etLastDonationDate = (EditText) findViewById(R.id.et_last_donation_date);
        etNoOfDonation = (EditText) findViewById(R.id.et_no_of_donation);
        etLastDonationDate.setInputType(InputType.TYPE_NULL);
        setDefaultInfo();
        setCurrentDateOnDatePicker();
        addListenerOnDateField();

        btnAdd = (Button) findViewById(R.id.btn_add);
        setupButton(btnAdd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.donor_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_contact:
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContactIntent, Constant.PICK_CONTACT_REQUEST);
                break;
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    private void setDefaultInfo() {
        etNoOfDonation.setText("" + 0);
    }

    private void setupButton(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            Donor donor = new Donor();

            public void onClick(View v) {
                donor.setMobile(etMobile.getText().toString());
                donor.setName(etName.getText().toString());
                donor.setBloodGroup(spBloodGroup.getSelectedItem().toString());
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date lastDate = sdf.parse(etLastDonationDate.getText().toString());
                    donor.setLastDonationDate(lastDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String donationFrequency = etNoOfDonation.getText().toString();
                if (donationFrequency != null && !TextUtils.isEmpty(donationFrequency)) {
                    donor.setNoOfDonation(Integer.parseInt(donationFrequency));
                }
                if (DonorInfoValidation.isAllInformationFilled(donor)) {
                    createNewDonor(donor);
                    Helper.showToast(getBaseContext(), "Added Successfully !!");
                    finish();

                } else {
                    DialogHelper.openDialog(AddNewDonorActivity.this, "Error", "Please Fill Up all with valid Data !");
                }
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                int columnPhoneNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phoneNumber = cursor.getString(columnPhoneNumber);
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                etMobile.setText(phoneNumber);
                etName.setText(name);

            }
        }
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
