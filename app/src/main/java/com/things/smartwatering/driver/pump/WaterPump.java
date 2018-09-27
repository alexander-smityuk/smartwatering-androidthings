package com.things.smartwatering.driver.pump;

import android.os.Handler;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WaterPump implements Pump {

    private Gpio mRelayGpio;
    private static WaterPump instance;

    private WaterPump() {
    }

    public static WaterPump getInstance() {
        if (instance == null) {
            instance = new WaterPump();
        }
        return instance;
    }

    public void setPin(String pinName) {
        try {
            PeripheralManagerService service = new PeripheralManagerService();
            mRelayGpio = service.openGpio(pinName);
            mRelayGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void water(long duration, TimeUnit timeUnit) {
        try {
            mRelayGpio.setValue(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mRelayGpio.setValue(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, timeUnit.toMillis(duration));
    }

    @Override
    public void water(Boolean water) {
        try {
            mRelayGpio.setValue(water);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (mRelayGpio != null) {
            try {
                mRelayGpio.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mRelayGpio = null;
            }
        }
    }
}
