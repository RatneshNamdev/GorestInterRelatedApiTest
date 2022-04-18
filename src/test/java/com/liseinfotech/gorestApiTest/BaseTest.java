package com.liseinfotech.gorestApiTest;


import com.liseinfotech.gorestApiTest.common.ApplicationProperties;
import org.testng.annotations.BeforeSuite;
import static io.restassured.RestAssured.*;

public class BaseTest {
    public String baseUrl;
    public String tokenNumber;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(){
        baseURI = ApplicationProperties.INSTANCE.getBaseUrl();
        baseUrl = baseURI;
        tokenNumber = ApplicationProperties.INSTANCE.getTokenNumber();
    }
}
