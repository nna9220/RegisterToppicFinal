package com.web.config;

import com.web.entity.RegistrationPeriod;

import java.util.Date;
import java.util.List;

public class CompareTime {
    private static boolean isCurrentTimeInInterval(Date start, Date end) {
        Date currentTime = new Date();
        return currentTime.after(start) && currentTime.before(end);
    }

    public static boolean isCurrentTimeInPeriodStudent(List<RegistrationPeriod> periodList) {
        Date currentTime = new Date();
        if (periodList.size() >= 2) {
            RegistrationPeriod period1 = periodList.get(0);
            Date start1 = period1.getRegistrationTimeStart();
            Date end1 = period1.getRegistrationTimeEnd();
            RegistrationPeriod period2 = periodList.get(1);
            Date start2 = period2.getRegistrationTimeStart();
            Date end2 = period2.getRegistrationTimeEnd();
            return isCurrentTimeInInterval(start1, end1) || isCurrentTimeInInterval(start2, end2);
        }
        return false;
    }
}
