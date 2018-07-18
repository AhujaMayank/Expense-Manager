package com.example.ahuja.expensemanager.Adapter_and_dailogs;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class AxisFormatter_month implements IAxisValueFormatter {
    String[] month={"Jan","Feb","Mar","Apr","May","Jun","July","Aug","Sep","Oct","Nov","Dec"};
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return month[(int) value];
    }
}
