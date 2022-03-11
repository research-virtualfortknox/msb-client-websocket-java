/*
 * Copyright (c) 2019 Fraunhofer Institute for Manufacturing Engineering and Automation (IPA).
 * Authors: Daniel Schel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package de.fhg.ipa.vfk.msb.client.api;

import java.util.Date;

public class AllTypes {

    private String string_type;
    private int int_type;
    private long long_type;
    private Date date_type;
    private double double_type;
    private float float_type;
    private boolean bool_type;

    public String getString_type() {
        return string_type;
    }

    public void setString_type(String string_type) {
        this.string_type = string_type;
    }

    public int getInt_type() {
        return int_type;
    }

    public void setInt_type(int count_type) {
        this.int_type = count_type;
    }

    public long getLong_type() {
        return long_type;
    }

    public void setLong_type(long long_type) {
        this.long_type = long_type;
    }

    public Date getDate_type() {
        return date_type;
    }

    public void setDate_type(Date date_type) {
        this.date_type = date_type;
    }

    public double getDouble_type() {
        return double_type;
    }

    public void setDouble_type(double double_type) {
        this.double_type = double_type;
    }

    public float getFloat_type() {
        return float_type;
    }

    public void setFloat_type(float float_type) {
        this.float_type = float_type;
    }

    public boolean isBool_type() {
        return bool_type;
    }

    public void setBool_type(boolean bool_type) {
        this.bool_type = bool_type;
    }

    public static AllTypes getTestEntity() {
        AllTypes AT = new AllTypes();
        AT.setBool_type(false);
        AT.setInt_type(222);
        AT.setLong_type(333L);
        AT.setDate_type(new Date());
        AT.setDouble_type(45.15415d);
        AT.setFloat_type(12.325f);
        AT.setString_type("26000");
        return AT;
    }

}

