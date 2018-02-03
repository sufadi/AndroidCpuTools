package com.fadisu.cpurun.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.util.CpuUtils;

public class CpuSettingsFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    private final static String[] GOVERNORS = CpuUtils.getCpuAvailableGovernorsList();

    private View mView;
    private TextView tv_cpu_governor_content;
    private LinearLayout ll_cpu_governor_content;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews();
        initValues();
        initListeners();
        return mView;
    }

    private void initViews() {
        tv_cpu_governor_content = (TextView) mView.findViewById(R.id.tv_cpu_governor_content);
        ll_cpu_governor_content = (LinearLayout) mView.findViewById(R.id.ll_cpu_governor_content);
    }

    private void initValues() {
        tv_cpu_governor_content.setText(CpuUtils.getCpuGovernor());
    }

    private void initListeners() {
        ll_cpu_governor_content.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cpu_governor_content:
                showGovernorDialog();
                break;
        }
    }

    private void showGovernorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(GOVERNORS, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_cpu_governor_content.setText(GOVERNORS[which]);
            }
        });
        builder.show();
    }
}
