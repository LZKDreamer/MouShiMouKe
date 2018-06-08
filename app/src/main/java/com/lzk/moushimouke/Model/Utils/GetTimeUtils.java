package com.lzk.moushimouke.Model.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by huqun on 2018/4/25.
 */

public class GetTimeUtils {
    public String getCurDetailTime(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date date=new Date();
        return dateFormat.format(date);
    }

    public int getCurMonth(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.MONTH)+1;
    }

    public int getCurDay(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public String formatTime(String time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//24小时制
        int minute = calendar.get(Calendar.MINUTE);
        String minuteString = null;
        if (minute>=0&&minute<=9){
            minuteString="0"+minute;
        }else {
            minuteString=String.valueOf(minute);
        }

        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        GetTimeUtils timeUtils=new GetTimeUtils();
        int curMonth=timeUtils.getCurMonth();
        int curDay=timeUtils.getCurDay();
        String postTime;
        if (month==curMonth&&day==curDay){
            postTime = hour + ":" + minuteString;
        }else {
            postTime=month+"-"+day;
        }
        return postTime;
    }
}
