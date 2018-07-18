package com.example.ahuja.expensemanager;

import com.example.ahuja.expensemanager.database.getDate;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class XaxisFormatter implements IAxisValueFormatter {
    getDate gd=new getDate();
    BarChart barChart;
    String m;

    public XaxisFormatter(BarChart barChart) {
        this.barChart = barChart;
        m=gd.get_full_month().substring(0,3);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int day=Integer.parseInt(gd.getDay())-7;
        String d=String.valueOf(day+(int)value)+","+m;
        if(value==0)
            return null;
        else
            return d;
    }
}
