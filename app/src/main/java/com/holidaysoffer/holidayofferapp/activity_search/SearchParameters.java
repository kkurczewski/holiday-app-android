package com.holidaysoffer.holidayofferapp.activity_search;

import java.io.Serializable;
import java.math.BigDecimal;

public class SearchParameters implements Serializable {

    private static final String DATE_SEPARATOR = "-";

    private final int day;
    private final int month;
    private final int year;
    private final BigDecimal price;
    private final int persons;
    private final int mealPreference;
    private final int climateFlag;

    SearchParameters(int day, int month, int year, BigDecimal price, int persons, int mealPreference, int climateFlag) {
        this.price = price;
        this.persons = persons;
        this.mealPreference = mealPreference;
        this.climateFlag = climateFlag;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getPersons() {
        return persons;
    }

    public int getMealPreference() {
        return mealPreference;
    }

    public int getClimateFlag() {
        return climateFlag;
    }

    public String getDate() {
        return year + DATE_SEPARATOR + month + DATE_SEPARATOR + day;
    }

    int getDay() {
        return day;
    }

    int getMonth() {
        return month;
    }

    int getYear() {
        return year;
    }

}
