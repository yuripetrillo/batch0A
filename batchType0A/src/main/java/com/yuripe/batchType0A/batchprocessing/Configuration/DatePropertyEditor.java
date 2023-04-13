package com.yuripe.batchType0A.batchprocessing.Configuration;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePropertyEditor extends PropertyEditorSupport {
    private static final DateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(FORMAT.parse(text));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Could not parse date", e);
        }
    }

    @Override
    public String getAsText() {
        return FORMAT.format((Date) getValue());
    }
}