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
import com.fadisu.cpurun.bean.ScreenInfo;
import com.fadisu.cpurun.util.BuildHelper;
import com.fadisu.cpurun.util.MemInfoUtil;
import com.fadisu.cpurun.util.ProcCpuStatUtil;
import com.fadisu.cpurun.util.PropInfoUtil;
import com.fadisu.cpurun.util.ScreenUtil;
import com.fadisu.cpurun.util.SystemUtils;

import java.util.ArrayList;
import java.util.List;


public class SystemFragment extends Fragment implements CustomAdapter.LayoutView {

    public static final int UPDATE_UI = 0;

    private Context mContext;
    private List<String> result;
    private CustomAdapter<String> mCustomAdapter;

    private View mView;
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
        mView = inflater.inflate(R.layout.fragment_base, container, false);
        initViews();
        initValues();
        initListeners();
        return mView;
    }

    private void initViews() {
        mListView = (ListView) mView.findViewById(R.id.listview);
    }

    private void initValues() {

        result = new ArrayList<String>();
        result.add(getString(R.string.phone_mode) + BuildHelper.getMode());
        result.add(getString(R.string.phone_product) + BuildHelper.getProduct());
        result.add(getString(R.string.phone_band) + BuildHelper.getBrand());
        result.add(getString(R.string.phone_manufacturer) + BuildHelper.getManufacturer());
        result.add(getString(R.string.android_version) + BuildHelper.getAndroidVersion());
        result.add(getString(R.string.sys_dislpay_id) + BuildHelper.getDisplay());
        result.add(getString(R.string.sys_sdk) + BuildHelper.getCurSDK());
        result.add(getString(R.string.sys_jvm) + PropInfoUtil.getJavaVM());
        result.add(getString(R.string.sys_opengl) + SystemUtils.getOpenGlVersion(mContext));
        result.add(getString(R.string.sys_kernel_architecture) + PropInfoUtil.getKernelArchitecture());
        result.add(getString(R.string.sys_kernel_version) + PropInfoUtil.getKernelVersion());

        ScreenInfo mScreenInfo = ScreenUtil.getScreenInfo(mContext);
        result.add(getString(R.string.sys_screen_real_metrics) + mScreenInfo.screenRealMetrics);
        result.add(getString(R.string.screen_density) + mScreenInfo.densityDpiStr);
        result.add(getString(R.string.screen_size) + mScreenInfo.sizeStr);

        result.add(getString(R.string.sys_internal_storage) + SystemUtils.getRomTotalSize(mContext));
        result.add(getString(R.string.sys_available_internal_storage) + SystemUtils.getRomAvailableSize(mContext));
        result.add(getString(R.string.sys_available_ram) + MemInfoUtil.getMemAvailable());
        result.add(getString(R.string.sys_total_ram) + MemInfoUtil.getMemTotal());

        result.add(getString(R.string.sys_root) + (SystemUtils.isRooted() ? getString(R.string.sys_root_yes) : getString(R.string.sys_root_no)));

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
