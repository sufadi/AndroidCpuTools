package com.fadisu.cpurun.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.adapter.CustomAdapter;
import com.fadisu.cpurun.service.CpuMsgService;
import com.fadisu.cpurun.service.ICpuMsgCallBack;
import com.fadisu.cpurun.service.ICpuMsgService;
import com.fadisu.cpurun.util.CustomToast;
import com.fadisu.cpurun.util.ProcCpuInfo;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class CpuStatusFragment extends Fragment implements CustomAdapter.LayoutView, View.OnClickListener {

    public static final int UPDATE_UI = 0;

    public static final String CPU_INFO = ProcCpuInfo.getProcessor();

    private Context mContext;
    private List<String> result;
    private CustomAdapter<String> mCustomAdapter;

    private View mView;
    private Button mFloatBtn;
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
        mView = inflater.inflate(R.layout.fragment_cpu_status, container, false);
        initViews();
        initValues();
        initListeners();
        return mView;
    }

    @Override
    public void onResume() {
        if (!isCanDrawOverlays(mContext)) {
            requestAlertWindowPermission();
        }
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
        mFloatBtn = (Button) mView.findViewById(R.id.btn_float_window);
    }

    private void initValues() {
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        mHandler = new MyHandler(this);
        result = new ArrayList<>();
        result.add("ServiceConnection .....");
        mCustomAdapter = new CustomAdapter<String>(result);
        mListView.setAdapter(mCustomAdapter);
        mHandler.sendEmptyMessage(UPDATE_UI);
    }

    private void initListeners() {
        mFloatBtn.setOnClickListener(this);
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

                    if (null != activity.mResultTv) {
                        String info = "";
                        for (String value : activity.result) {
                            info = info + value + "\n";
                        }
                        info = info + CPU_INFO;
                        activity.mResultTv.setText(info);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    class ViewHolder {
        TextView tv_info;
    }

    private TextView mResultTv;
    private WindowManager mWm;
    private WindowManager.LayoutParams mParams;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_float_window:
                String info = mFloatBtn.getText().toString();
                if (info.equals(getString(R.string.title_float_window_open))) {
                    showFloatWindow();
                    mFloatBtn.setText(getString(R.string.title_float_window_close));
                } else if (info.equals(getString(R.string.title_float_window_close))) {
                    hideFloatWindow();
                    mFloatBtn.setText(getString(R.string.title_float_window_open));
                }
                break;
        }
    }

    private void showFloatWindow() {
        mResultTv = new TextView(mContext);
        mResultTv.setBackgroundColor(mContext.getColor(R.color.float_bg));
        mResultTv.setTextColor(mContext.getColor(R.color.white));
        mResultTv.setOnTouchListener(new View.OnTouchListener() {
            int lastX = 0;
            int lastY = 0;
            int paramX = 0;
            int paramY = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) motionEvent.getRawX();
                        lastY = (int) motionEvent.getRawY();
                        paramX = mParams.x;
                        paramY = mParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) motionEvent.getRawX() - lastX;
                        int dy = (int) motionEvent.getRawY() - lastY;
                        mParams.x = paramX + dx;
                        mParams.y = paramY + dy;
                        // update float window
                        mWm.updateViewLayout(mResultTv, mParams);
                        break;
                }
                return true;
            }
        });

        mParams = new WindowManager.LayoutParams();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 悬浮窗的核心
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.format = PixelFormat.TRANSPARENT;

        mWm.addView(mResultTv, mParams);
    }

    private void hideFloatWindow() {
        mWm.removeView(mResultTv);
    }

    private static final int REQUEST_CODE = 1;

    //判断权限
    private boolean isCanDrawOverlays(Context context) {
        Boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Settings.class;
                Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                result = (Boolean) canDrawOverlays.invoke(null, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //申请权限
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);

        CustomToast.showToast(mContext, mContext.getString(R.string.toast_float_tip), Toast.LENGTH_LONG);
    }

}
