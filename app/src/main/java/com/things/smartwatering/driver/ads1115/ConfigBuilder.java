package com.things.smartwatering.driver.ads1115;

import static com.things.smartwatering.driver.ads1115.Ads1115.ADS1115_REG_CONFIG_CLAT_NONLAT;
import static com.things.smartwatering.driver.ads1115.Ads1115.ADS1115_REG_CONFIG_CMODE_TRAD;
import static com.things.smartwatering.driver.ads1115.Ads1115.ADS1115_REG_CONFIG_CPOL_ACTVLOW;
import static com.things.smartwatering.driver.ads1115.Ads1115.ADS1115_REG_CONFIG_CQUE_NONE;
import static com.things.smartwatering.driver.ads1115.Ads1115.ADS1115_REG_CONFIG_DR_128SPS;
import static com.things.smartwatering.driver.ads1115.Ads1115.ADS1115_REG_CONFIG_MODE_SINGLE;
import static com.things.smartwatering.driver.ads1115.Ads1115.ADS1115_REG_CONFIG_OS_SINGLE;

@SuppressWarnings("PointlessBitwiseExpression")
public class ConfigBuilder {

    int reader(Ads1115.Gain gain, Ads1115.Channel channel) {
        return ADS1115_REG_CONFIG_CQUE_NONE  // Disable the comparator (default val)
                | ADS1115_REG_CONFIG_CLAT_NONLAT  // Non-latching (default val)
                | ADS1115_REG_CONFIG_CPOL_ACTVLOW  // Alert/Rdy active low   (default val)
                | ADS1115_REG_CONFIG_CMODE_TRAD  // Traditional comparator (default val)
                | ADS1115_REG_CONFIG_DR_128SPS  // 1600 samples per second (default)
                | ADS1115_REG_CONFIG_MODE_SINGLE   // Single-shot mode (default)
                | gain.value
                | channel.value
                | ADS1115_REG_CONFIG_OS_SINGLE;
    }
}
