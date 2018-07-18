package com.example.ahuja.expensemanager.Adapter_and_dailogs;

import android.icu.util.Calendar;
import android.os.Build;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.sql.Date;

public class AxisFormatter_Week implements IAxisValueFormatter {
    String[] names={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return names[(int)value];
    }
}
