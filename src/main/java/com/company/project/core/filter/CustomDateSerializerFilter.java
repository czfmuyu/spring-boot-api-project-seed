package com.company.project.core.filter;

import com.company.project.core.untils.TimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : xiaomo
 */
public class CustomDateSerializerFilter extends JsonSerializer<Date> {

    @Override
    public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.DEFAULT_FORMAT2);
        jsonGenerator.writeString(sdf.format(value));
    }
}