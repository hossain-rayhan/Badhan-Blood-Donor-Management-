package com.appsomehow.badhan.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.appsomehow.badhan.DonorUpdateActivity;
import com.appsomehow.badhan.R;
import com.appsomehow.badhan.helper.Constant;
import com.appsomehow.badhan.helper.DateFormatter;
import com.appsomehow.badhan.model.Donor;
import java.util.List;

public class DonorListAdapter extends ArrayAdapter<Donor> {
    Context context;
    int layoutResourceId;
    private boolean isCabActivated = false;

    public DonorListAdapter(Context context, int layout, List<Donor> donors) {
        super(context, layout, donors);
        this.context = context;
        layoutResourceId = layout;
    }

    private class ViewHolder {
        TextView mobile;
        TextView name;
        TextView bloodGroup;
        TextView lastDonationDate;
        TextView noOfDonation;
        ImageButton btnCallDonor;
        ImageButton btnSmsDonor;
        ImageButton btnUpdateDonor;
    }

    @Override
    public int getViewTypeCount() {
        if (getCount() != 0)
            return getCount();
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Donor donor = getItem(position);

        View inflatedView = LayoutInflater.from(context).inflate(
                layoutResourceId, parent, false);

        ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflatedView;

            holder = new ViewHolder();

            holder.mobile = (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.bloodGroup = (TextView) convertView
                    .findViewById(R.id.tv_blood_group);
            holder.lastDonationDate = (TextView) convertView.findViewById(R.id.tv_last_donation_date);
            holder.noOfDonation = (TextView) convertView
                    .findViewById(R.id.tv_no_of_donation);
            holder.btnCallDonor = (ImageButton) convertView.findViewById(R.id.btn_call_donor);
            holder.btnUpdateDonor = (ImageButton) convertView.findViewById(R.id.btn_update_donor);
            holder.btnSmsDonor = (ImageButton) convertView.findViewById(R.id.btn_sms_donor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mobile.setText(donor.getMobile());
        holder.name.setText(donor.getName());
        holder.bloodGroup.setText(donor.getBloodGroup());
        holder.lastDonationDate.setText(DateFormatter.getStringFromDate(donor.getLastDonationDate()));
        holder.noOfDonation.setText("" + donor.getNoOfDonation());


        final ViewHolder finalHolder = holder;

        if (isCabActivated) {
            holder.btnCallDonor.setEnabled(false);
            holder.btnSmsDonor.setEnabled(false);
            holder.btnUpdateDonor.setEnabled(false);
        } else {
            holder.btnCallDonor.setEnabled(true);
            holder.btnSmsDonor.setEnabled(true);
            holder.btnUpdateDonor.setEnabled(true);
        }
        holder.btnCallDonor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + finalHolder.mobile.getText().toString()));
                context.startActivity(intent);
            }
        });

        holder.btnSmsDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("address", finalHolder.mobile.getText().toString());
                sendIntent.putExtra("sms_body", "Can U please donate blood.. Urgent !!");
                sendIntent.setType("vnd.android-dir/mms-sms");
                context.startActivity(sendIntent);
            }
        });

        holder.btnUpdateDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donorUpdate = new Intent(context, DonorUpdateActivity.class);
                donorUpdate.putExtra(Constant.keyDonorMobile, finalHolder.mobile.getText().toString());
                context.startActivity(donorUpdate);
            }
        });

        return convertView;
    }

    public void setCabActivated(boolean isCabActivated) {
        this.isCabActivated = isCabActivated;
    }
}
