package com.agiletec.plugins.jacms.apsadmin.content;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateTransform {

    public static List<String> changeDateFormat(List<String> dates) {
        List<String> outputList = new ArrayList<>();
        try {
            SimpleDateFormat output = new SimpleDateFormat("yyyyMMdd");
            String formattedDate = null;
            for (String date : dates) {
                formattedDate = null;
                Date inputDate = getDate(date);
                if (inputDate != null) {
                    formattedDate = output.format(inputDate);
                    outputList.add(formattedDate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputList;
    }

    public static void main(String[] args) {
        List<String> dates = changeDateFormat(Arrays.asList("2010/03/30", "15/12/2016", "11-15-2012", "20130720"));
        for (String date : dates) {
            System.out.println(date);
        }
    }

    public static Date getDate(String date) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date dt = formatter.parse(date);
            return dt;
        } catch (ParseException e) {
        }
        formatter = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date dt = formatter.parse(date);
            return dt;
        } catch (ParseException e) {
        }
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dt = formatter.parse(date);
            return dt;
        } catch (ParseException e) {
        }
        return null;
    }
}