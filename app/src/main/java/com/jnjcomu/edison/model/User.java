package com.jnjcomu.edison.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author CodeRi <ruto1924@gmail.com>
 * @since 2017-05-06
 */

public class User {
    @SerializedName("name")
    private String name;

    @SerializedName("grade")
    private int grade;

    @SerializedName("clazz")
    private int clazz;

    @SerializedName("number")
    private int number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getClazz() {
        return clazz;
    }

    public void setClazz(int clazz) {
        this.clazz = clazz;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
