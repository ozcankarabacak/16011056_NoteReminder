package com.example.a16011056_notereminder.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteUtils {

    public static String dateFromLong(long time){
        DateFormat format = new SimpleDateFormat("hh:mm aaa\nEEE, dd MMM yyyy", Locale.US);
        return format.format(new Date(time));

    }
}
