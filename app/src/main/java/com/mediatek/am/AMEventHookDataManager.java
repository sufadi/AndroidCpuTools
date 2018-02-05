/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 *
 * MediaTek Inc. (C) 2016. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */
package com.mediatek.am;

import android.util.Slog;

import java.util.ArrayList;

/**
 * M: Abstract class for data types when AMEventHook#Event happened.
 *
 * {@hide}
 */
public abstract class AMEventHookDataManager {
    private static final String TAG = "AMEventHookDataManager";
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_FLOW = false;

    private ArrayList<Object> mList = new ArrayList<Object>();

    /**
     * Replaces the data.
     *
     * @param args the list of data
     */
    public abstract void set(Object... args);

    /**
     * Replaces the data.
     *
     * @param values the Enum values
     * @param args the list of data
     */
    protected void set(Enum[] values, Object... args) {
        if (DEBUG_FLOW) {
            Slog.d(TAG, "set data", new Throwable());
        }
        for (Enum e : values) {
            set(e.ordinal(), args[e.ordinal()]);
            if (DEBUG) {
                Slog.d(TAG, "set(" + e + ", " + args[e.ordinal()] + ")");
            }
        }
    }

    /**
     * Replaces the element at the specified location
     * with the specified object.
     *
     * @param index the index at which to put the specified object.
     * @param element the object to add.
     * @return the previous element at the index.
     */
    public Object set(int index, Object element) {
        if (mList.size() > index) {
            return mList.set(index, element);
        }
        mList.add(index, element);
        return element;
    }

    /**
     * Retrieves the element at the specified location as an {@code Object}
     * in the Java programming language.
     *
     * @param index the index at which to put the specified object.
     * @return the element at the index.
     */
    public Object get(Enum index) {
        return mList.get(index.ordinal());
    }

    /**
     * Retrieves the element at the specified location as an {@code int}
     * in the Java programming language.
     *
     * @param index the index at which to put the specified object.
     * @return the element at the index.
     */
    public int getInt(Enum index) {
        return (Integer) mList.get(index.ordinal());
    }


    /**
     * Retrieves the element at the specified location as a {@code long}
     * in the Java programming language.
     *
     * @param index the index at which to put the specified object.
     * @return the element at the index.
     */
    public long getLong(Enum index) {
        return (Long) mList.get(index.ordinal());
    }

    /**
     * Retrieves the element at the specified location as a {@code double}
     * in the Java programming language.
     *
     * @param index the index at which to put the specified object.
     * @return the element at the index.
     */
    public double getDouble(Enum index) {
        return (Double) mList.get(index.ordinal());
    }

    /**
     * Retrieves the element at the specified location as a {@code float}
     * in the Java programming language.
     *
     * @param index the index at which to put the specified object.
     * @return the element at the index.
     */
    public float getFloat(Enum index) {
        return (Float) mList.get(index.ordinal());
    }

    /**
     * Retrieves the element at the specified location as a {@code boolean}
     * in the Java programming language.
     *
     * @param index the index at which to put the specified object.
     * @return the element at the index.
     */
    public boolean getBoolean(Enum index) {
        return (Boolean) mList.get(index.ordinal());
    }

    /**
     * Retrieves the element at the specified location as a {@code byte}
     * in the Java programming language.
     *
     * @param index the index at which to put the specified object.
     * @return the element at the index.
     */
    public byte getByte(Enum index) {
        return (Byte) mList.get(index.ordinal());
    }

    /**
     * Retrieves the element at the specified location as a {@code String}
     * in the Java programming language.
     *
     * @param index the index at which to put the specified object.
     * @return the element at the index.
     */
    public String getString(Enum index) {
        return (String) mList.get(index.ordinal());
    }
}
