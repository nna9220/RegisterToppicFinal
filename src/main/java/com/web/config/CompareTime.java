package com.web.config;

import com.web.entity.RegistrationPeriod;
import com.web.entity.RegistrationPeriodLectuer;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CompareTime {
    private static Date parseDateString(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        return (Date) dateFormat.parse(dateString);
    }
    private static boolean isCurrentTimeInInterval(Date start, Date end) {
        SimpleDateFormat dateFormatOutput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

        try {
            Date formattedDate = new Date(System.currentTimeMillis());
            System.out.println("Test: " + formattedDate);

            // Chuyển đổi chuỗi ngày giờ thành chuỗi theo định dạng mới
            String formattedDateString = dateFormatOutput.format(formattedDate);

            // Chuyển đổi chuỗi thành Date
            Date formattedDateAsDate = (Date) dateFormatOutput.parse(formattedDateString);
            System.out.println(formattedDateAsDate);

            // So sánh
            String startAsString = dateFormatOutput.format(start);
            String endAsString = dateFormatOutput.format(end);

            return formattedDateString.compareTo(startAsString) >= 0 && formattedDateString.compareTo(endAsString) <= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            // Xử lý nếu có lỗi khi chuyển đổi chuỗi thành Date
            return false;
        }
    }

    public static boolean isCurrentTimeInPeriodStudent(List<RegistrationPeriod> periodList) {
        Date currentTime = new Date(System.currentTimeMillis());
        if (periodList.size() >= 2) {
            RegistrationPeriod period1 = periodList.get(0);
            Date start1 = period1.getRegistrationTimeStart();
            Date end1 = period1.getRegistrationTimeEnd();
            RegistrationPeriod period2 = periodList.get(1);
            Date start2 = period2.getRegistrationTimeStart();
            Date end2 = period2.getRegistrationTimeEnd();

            System.out.println("start 1: " + start1);
            System.out.println("end 1: " + end1);
            System.out.println("start 2: " + start2);
            System.out.println("end 2: " + end2);
            System.out.println("current: " + currentTime);

            return isCurrentTimeInInterval(start1, end1) || isCurrentTimeInInterval(start2, end2);
        }
        return false;
    }
    public static boolean isCurrentTimeInPeriodSLecturer(List<RegistrationPeriodLectuer> periodList) {
        Date currentTime = new Date(System.currentTimeMillis());
        if (periodList.size() >= 2) {
            RegistrationPeriodLectuer period1 = periodList.get(0);
            Date start1 = period1.getRegistrationTimeStart();
            Date end1 = period1.getRegistrationTimeEnd();
            RegistrationPeriodLectuer period2 = periodList.get(1);
            Date start2 = period2.getRegistrationTimeStart();
            Date end2 = period2.getRegistrationTimeEnd();

            System.out.println("start 1: " + start1);
            System.out.println("end 1: " + end1);
            System.out.println("start 2: " + start2);
            System.out.println("end 2: " + end2);
            System.out.println("current: " + currentTime);

            return isCurrentTimeInInterval(start1, end1) || isCurrentTimeInInterval(start2, end2);
        }
        return false;
    }
}
