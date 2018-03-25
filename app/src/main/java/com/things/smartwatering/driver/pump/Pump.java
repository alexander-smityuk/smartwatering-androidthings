package com.things.smartwatering.driver.pump;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

public interface Pump {
    void water(long duration, TimeUnit timeUnit) throws IOException;

    void close() throws IOException;
}
