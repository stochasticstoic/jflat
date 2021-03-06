package com.tecacet.jflat.util;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalTime;
import org.junit.Test;

import com.tecacet.jflat.conversion.LocalTimeConverter;

public class LocalTimeConverterTest {

    @Test
    public void testConvert() {
       LocalTimeConverter converter = new LocalTimeConverter("hh:mm a");
       LocalTime t =  converter.convert( "9:21 PM");
       assertEquals("21:21:00.000",t.toString());
    }
 
}
