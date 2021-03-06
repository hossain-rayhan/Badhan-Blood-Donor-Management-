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
import com.appsomehow.badhan.R;
import com.appsomehow.badhan.helper.DateFormatter;
import com.appsomehow.badhan.model.Donor;

import java.util.List;

public class AvailableDonorListAdapter extends ArrayAdapter<Donor> {
    Context context;
    int layoutResourceId;

    public AvailableDonorListAdapter (Context context, int layout, List<Donor> donors) {
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
        TextView address;
        TextView preferredArea;
        TextView comment;
        ImageButton btnCallDonor;
        ImageButton btnSmsDonor;

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

        ViewHolder holder;
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
            holder.address = (TextView)convertView.findViewById(R.id.tv_address);
            holder.preferredArea = (TextView)convertView.findViewById(R.id.tv_preferred_area);
            holder.comment = (TextView)convertView.findViewById(R.id.tv_comment);
            holder.btnCallDonor = (ImageButton) convertView.findViewById(R.id.btn_call_donor);
            holder.btnSmsDonor = (ImageButton) convertView.findViewById(R.id.btn_sms_donor);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.mobile.setText(donor.getMobile());
        holder.name.setText(donor.getName());
        holder.bloodGroup.setText(donor.getBloodGroup());
        holder.lastDonationDate.setText(DateFormatter.getStringFromDate(donor.getLastDonationDate()));
        holder.noOfDonation.setText("" + donor.getNoOfDonation());
        holder.address.setText(donor.getAddress());
        holder.preferredArea.setText(donor.getPreferredArea());
        holder.comment.setText(donor.getComment());

        final ViewHolder finalHolder = holder;
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

        return convertView;
    }
}