package com.fadisu.cpurun.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.adapter.CustomAdapter;
import com.fadisu.cpurun.util.BuildHelper;
import com.fadisu.cpurun.util.CpuUtils;
import com.fadisu.cpurun.util.ProcCpuInfo;

import java.util.ArrayList;
import java.util.List;


public class CoreInfoFragment extends Fragment implements CustomAdapter.LayoutView {

    public static final int UPDATE_UI = 0;

    private Context mContext;
    private List<String> result;
    private CustomAdapter<String> mCustomAdapter;

    private View mView;
    private TextView mInfoTv;
    private ListView mListView;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    mCustomAdapter.updateData((ArrayList<String>) result);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_core, container, false);
        initViews();
        initValues();
        initListeners();
        return mView;
    }

    private void initViews() {
        mInfoTv = (TextView) mView.findViewById(R.id.tv_info);
        mListView = (ListView) mView.findViewById(R.id.listview);
    }

    private void initValues() {
        mInfoTv.setText(ProcCpuInfo.getArchitecture());

        result = new ArrayList<String>();
        result.add("手机制造商: " + BuildHelper.getProduct());
        result.add("系统定制商: " + BuildHelper.getBrand());
        result.add("硬件制造商: " + BuildHelper.getManufacturer());
        result.add("平台信息: " + BuildHelper.getHardWare());
        result.add("型号: " + BuildHelper.getMode());
        result.add("Android 系统版本: " + BuildHelper.getAndroidVersion());
        result.add("CPU 核数: " + CpuUtils.getNumCpuCores());
        result.add("CPU 位数: " + (CpuUtils.isCpu64() ? "64 bit" : "32 bite"));
        result.add("CPU 指令集: " + BuildHelper.getCpuAbi());
        result.add("CPU Processor: " + ProcCpuInfo.getProcessor());

        mCustomAdapter = new CustomAdapter<String>(result);
        mListView.setAdapter(mCustomAdapter);
        mHandler.sendEmptyMessage(UPDATE_UI);
    }

    private void initListeners() {
        mCustomAdapter.setLayoutView(this);
    }

    class ViewHolder {
        TextView tv_info;
    }

    @Override
    public <T> View setView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_base, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_info.setText(result.get(position));

        return convertView;
    }
}
