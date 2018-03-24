package com.things.smartwatering.driver;


import android.hardware.Sensor;

import com.google.android.things.userdriver.UserDriverManager;
import com.google.android.things.userdriver.UserSensor;
import com.google.android.things.userdriver.UserSensorDriver;
import com.google.android.things.userdriver.UserSensorReading;
import com.things.smartwatering.driver.ads1115.Ads1115;

import java.io.IOException;
import java.util.UUID;

public class SoilMoistureSensorDriver implements AutoCloseable {

    private static final String DRIVER_NAME = "Soil Moisture";

    private Ads1115 mDevice;

    private SoilMoistureUserDriver mSoilMoistureUserDriver;

    public SoilMoistureSensorDriver(String bus) {
        mDevice = new Ads1115.Factory().newAds1115(bus, 0x48, Ads1115.Gain.TWO_THIRDS);
    }

    @Override
    public void close() throws IOException {
        unregisterSoilMoistureSensor();
        if (mDevice != null) {
            try {
                mDevice.close();
            } finally {
                mDevice = null;
            }
        }
    }

    public void registerSoilMoistureSensor() {
        if (mDevice == null) {
            throw new IllegalStateException("cannot register closed driver");
        }

        if (mSoilMoistureUserDriver == null) {
            mSoilMoistureUserDriver = new SoilMoistureSensorDriver.SoilMoistureUserDriver();
            UserDriverManager.getManager().registerSensor(mSoilMoistureUserDriver.getUserSensor());
        }
    }

    public void unregisterSoilMoistureSensor() {
        if (mSoilMoistureUserDriver != null) {
            UserDriverManager.getManager().unregisterSensor(mSoilMoistureUserDriver.getUserSensor());
            mSoilMoistureUserDriver = null;
        }
    }

    private class SoilMoistureUserDriver extends UserSensorDriver {

        private static final float DRIVER_MAX_RANGE = 100f;
        private static final float DRIVER_RESOLUTION = 0.00008f;
        private static final float DRIVER_POWER = 280f / 1000.f;
        private static final int DRIVER_VERSION = 1;
        private static final String DRIVER_REQUIRED_PERMISSION = "";

        private boolean mEnabled;
        private UserSensor mUserSensor;

        private UserSensor getUserSensor() {
            if (mUserSensor == null) {
                mUserSensor = new UserSensor.Builder()
                        .setType(Sensor.TYPE_RELATIVE_HUMIDITY)
                        .setName(DRIVER_NAME)
                        .setVersion(DRIVER_VERSION)
                        .setMaxRange(DRIVER_MAX_RANGE)
                        .setResolution(DRIVER_RESOLUTION)
                        .setPower(DRIVER_POWER)
                        .setRequiredPermission(DRIVER_REQUIRED_PERMISSION)
                        .setUuid(UUID.randomUUID())
                        .setDriver(this)
                        .build();
            }
            return mUserSensor;
        }

        @Override
        public UserSensorReading read() throws IOException {
            return new UserSensorReading(new float[]{(mDevice.readVoltage(Ads1115.Channel.ONE) / 4.2f) * 100f});
        }

        @Override
        public void setEnabled(boolean enabled) throws IOException {
            mEnabled = enabled;
        }

        private boolean isEnabled() {
            return mEnabled;
        }
    }
}
