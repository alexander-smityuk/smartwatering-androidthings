package com.things.smartwatering.driver.pump;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

public interface Pump {
    void setPin(String pinName);

    void water(long duration, TimeUnit timeUnit) throws IOException;

    void water(Boolean water) throws IOException;

    void close() throws IOException;
}
