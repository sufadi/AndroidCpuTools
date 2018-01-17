package com.fadisu.cpurun.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomAdapter<T> extends BaseAdapter {

    public final int SELECT_NULL = -1;

    private int selectPosition = SELECT_NULL;

    private LayoutView mLayoutView;
    private List<T> mObjects = new ArrayList<T>();

    public CustomAdapter(List<T> objects) {
        if (objects != null) {
            mObjects.addAll(objects);
        }
    }

    public CustomAdapter(T[] objects) {
        if (objects != null) {
            mObjects.addAll(Arrays.asList(objects));
        }
    }

    public interface LayoutView {
        public <T> View setView(int position, View convertView, ViewGroup parent);
    }

    public void setLayoutView(LayoutView layoutView) {
        mLayoutView = layoutView;
    }

    public List<T> getAdapterData() {
        return mObjects;
    }

    public void setSelectPosition(int position) {
        selectPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void updateData(ArrayList<T> objects) {
        selectPosition = SELECT_NULL;

        mObjects.clear();
        if (objects != null) {
            mObjects.addAll(objects);
        }

        notifyDataSetChanged();
    }

    public void updateData(T[] objects) {
        selectPosition = SELECT_NULL;

        mObjects.clear();
        if (objects != null) {
            mObjects.addAll(Arrays.asList(objects));
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mLayoutView != null) {
            return mLayoutView.setView(position, convertView, parent);
        } else {
            return null;
        }
    }

}
