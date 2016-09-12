/*
 * Copyright (c) 2016 Karim ElDeeb
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

package com.karimeldeeb.adila;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Device information class.
 *
 * <p>Before this class becomes usable, it must be initialized first. Initialization is done
 * automatically, via a static block, the first time you try to access any of its fields or methods.
 *
 * <p>Each device can be uniquely identified by both of its {@link android.os.Build#DEVICE} and
 * {@link android.os.Build#MODEL} fields, that are set by the device manufacturer. I believe, this
 * is the same method Google is using to identify all the devices that are accessing the Play Store.
 *
 * <p>After identifying the device, this class will look in its database for the device information.
 * If found, it will be copied into this class static fields. If not found, then default values will
 * be assigned instead. Even after finding the device class, this class will assign default values
 * to all unused/empty fields to prevent a NPE from crashing your application.
 *
 * <p>While assigning default values is a good idea, it doesn't tell you whether the value returned
 * is empty because the device class was not found, or because the value is not used. For example,
 * {@link #SERIES} field will return an empty String if the device does not belong to a series, or
 * if the device class was not found in the database. To find out if the device class is in the
 * database or not, use {@link #FOUND} field.
 */
public final class Device {
    /** Set to true if the device class was found in the database. */
    public static final boolean FOUND;

    /** The manufacturer of the device. */
    public static final String MANUFACTURER;

    /** The user-friendly name of the device. */
    public static final String NAME;

    /** The full name of the device, including the manufacturer. */
    public static final String FULL_NAME;

    /** The series of the device, if any. */
    public static final String SERIES;

    /**
     * Returns the full information about the device as a JSON string.
     *
     * @return a JSON string with all valid information
     */
    public static String info() {
        JSONObject json = new JSONObject();

        try {
            if (MANUFACTURER.length() != 0) json.put("manufacturer", MANUFACTURER);
            if (NAME.length() != 0) json.put("name", NAME);
            if (SERIES.length() != 0) json.put("series", SERIES);
        } catch (JSONException e) {
            return "{}";
        }

        return json.toString();
    }

    /* ************************************** Internal API ************************************** */

    /** The full path to the class. */
    private static StringBuilder mClassName = new StringBuilder("adila.db.");

    /** The current device class. */
    private static Class mClass;

    /* Initialize this class from one of the classes in the database package. */
    static {
        boolean isFound = true;

        /* Try to find the device class name using "Build.DEVICE" only. */
        try {
            mClass = Class.forName(getClassByDevice());
        } catch (ClassNotFoundException d) {
            /* Try again by adding "Build.MODEL" to the class name. */
            try {
                mClass = Class.forName(getClassByModel());
            } catch (ClassNotFoundException m) {
                isFound = false;
            }
        }

        FOUND = isFound;

        /* Get DATA field from the device class. */
        String data = "";
        if (isFound) try {
            data = (String) mClass.getField("DATA").get(null);
        } catch (NoSuchFieldException e) {
            // Nothing to do
        } catch (IllegalAccessException e) {
            // Nothing to do
        }

        /* Assign values (either default or from a device class) to this class static fields. */
        final String[] field = data.split("\\|", -1);

        MANUFACTURER = (field.length > 0) ? field[0] : "";
        NAME         = (field.length > 1) ? field[1] : "";
        SERIES       = (field.length > 2) ? field[2] : "";

        FULL_NAME = MANUFACTURER + ' ' + NAME;
    }

    /** Suppresses the default constructor. */
    private Device() {
    }

    /**
     * Converts {@link android.os.Build#DEVICE} into a valid class name.
     *
     * <p>To be able to find the class name at runtime we must convert {@code Build.DEVICE} into a
     * valid Java class name.
     *
     * <p>A valid Java class name should start with a letter and not a number, and should use
     * (a-z, A-Z, 0-9, _) characters. But device manufacturers can, and usually, use any characters
     * they want for {@code Build.DEVICE} value (e.g. ^ - + '), and sometimes start with a number.
     *
     * <p>This method will convert all characters that shouldn't be used in a class name to valid
     * ones. You must name all the classes in {@link adila.db} package in the same way, so they
     * could be found by this class.
     *
     * <p>Steps:
     * <ol>
     *     <li>If Build.DEVICE starts with a number, add an underscore before it
     *     <li>If a character in Build.DEVICE is not "a-z, A-Z, or 0-9", convert it to hexadecimal
     *     <li>Convert all to lowercase
     * </ol>
     *
     * @return the full path of the class name
     */
    private static String getClassByDevice() {
        final String device = Build.DEVICE;

        if (device == null || device.length() == 0) {
            return "UNKNOWN";
        }

        // A valid Java class name cannot start with a number
        if (Character.isDigit(device.charAt(0))) {
            mClassName.append('_');
        }

        for (int i = 0, n = device.length(); i < n; i++) {
            if(Character.isLetterOrDigit(device.charAt(i))) {
                mClassName.append(device.toLowerCase(Locale.US).charAt(i));
            } else {
                mClassName.append(Integer.toHexString((int) device.charAt(i)));
            }
        }

        return mClassName.toString();
    }

    /**
     * Converts {@link android.os.Build#MODEL} into a valid class name.
     *
     * <p>Most of the time, {@code Build.DEVICE} is enough to find the device class. However, some
     * manufacturers use the same name for more than one device if, for example, many devices belong
     * to the same series, or if the same device has many variants. In this case, this class will
     * use both {@code Build.DEVICE} and {@code Build.MODEL} and try again to find the device class.
     *
     * <p>This method will be called immediately after {@link #getClassByDevice()} if the device
     * class was not found by {@code Build.DEVICE} only. When called, it will separate both
     * {@code Build.DEVICE} and {@code Build.MODEL} with an underscore, then use the same way from
     * {@link #getClassByDevice()} method to convert {@code Build.MODEL} into a valid class name.
     *
     * @return the full path of the class name
     */
    private static String getClassByModel() {
        final String model = Build.MODEL;

        if (model == null || model.length() == 0) {
            return "UNKNOWN";
        }

        // Separate Build.MODEL from Build.DEVICE with an underscore
        mClassName.append('_');

        for (int i = 0, n = model.length(); i < n; i++) {
            if(Character.isLetterOrDigit(model.charAt(i))) {
                mClassName.append(model.toLowerCase(Locale.US).charAt(i));
            } else {
                mClassName.append(Integer.toHexString((int) model.charAt(i)));
            }
        }

        return mClassName.toString();
    }
}
