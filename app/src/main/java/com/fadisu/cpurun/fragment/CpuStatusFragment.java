package com.fadisu.cpurun.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.adapter.CustomAdapter;
import com.fadisu.cpurun.service.CpuMsgService;
import com.fadisu.cpurun.service.ICpuMsgCallBack;
import com.fadisu.cpurun.service.ICpuMsgService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class CpuStatusFragment extends Fragment implements CustomAdapter.LayoutView {

    public static final int UPDATE_UI = 0;

    private Context mContext;
    private List<String> result;
    private CustomAdapter<String> mCustomAdapter;

    private View mView;
    private ListView mListView;

    private Handler mHandler = null;
    private ICpuMsgCallBack mICpuMsgCallBack = new ICpuMsgCallBack.Stub() {

        @Override
        public void updateCpuUsage(List<String> list) throws RemoteException {
            if (null != list) {
                result.clear();
                result.addAll(list);
                mHandler.sendEmptyMessage(UPDATE_UI);
            }
        }
    };

    ;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                ICpuMsgService mICpuMsgService = ICpuMsgService.Stub.asInterface(iBinder);
                mICpuMsgService.registerCallback(mICpuMsgCallBack);
                Log.d("CpuMsgService", "onServiceConnected");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("CpuMsgService", "onServiceDisconnected");
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

    @Override
    public void onResume() {
        bindService();
        super.onResume();
    }

    @Override
    public void onStop() {
        unBindService();
        super.onStop();
    }

    private void initViews() {
        mListView = (ListView) mView.findViewById(R.id.listview);
    }

    private void initValues() {
        mHandler = new MyHandler(this);
        result = new ArrayList<>();
        result.add("ServiceConnection .....");
        mCustomAdapter = new CustomAdapter<String>(result);
        mListView.setAdapter(mCustomAdapter);
        mHandler.sendEmptyMessage(UPDATE_UI);
    }

    private void initListeners() {
        mCustomAdapter.setLayoutView(this);
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

    private void bindService() {
        Intent intent = new Intent(mContext, CpuMsgService.class);
        intent.setAction(CpuMsgService.ACTION_CPU_USAGE_START);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.d("CpuMsgService", "bindService");
    }

    private void unBindService() {
        Intent intent = new Intent(mContext, CpuMsgService.class);
        intent.setAction(CpuMsgService.ACTION_CPU_USAGE_START);
        mContext.unbindService(mConnection);
        Log.d("CpuMsgService", "unBindService");
    }

    private static class MyHandler extends Handler {

        WeakReference<CpuStatusFragment> mActivity;

        public MyHandler(CpuStatusFragment activity) {
            mActivity = new WeakReference<CpuStatusFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CpuStatusFragment activity = mActivity.get();
            switch (msg.what) {
                case UPDATE_UI:
                    activity.mCustomAdapter.updateData((ArrayList<String>) activity.result);
                    break;
                default:
                    break;
            }
        }
    }

    class ViewHolder {
        TextView tv_info;
    }
}
