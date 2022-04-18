package com.liseinfotech.gorestApiTest.common;

import java.io.IOException;
import java.util.Properties;

public enum ApplicationProperties {

    INSTANCE;
    private final Properties properties;

    ApplicationProperties(){
        properties = new Properties();

        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }

    public String getBaseUrl(){
        return properties.getProperty("baseUrl");
    }

    public String getTokenNumber(){
        return properties.getProperty("tokenNumber");
    }
}
