package com.stepDefinitions;
import com.my_enumerations.ResSpecs_request;
import com.utils_pack.PropertiesFileImpl;

import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class BaseTestSetup {

    @Before
    public void setup() {
        RestAssured.baseURI = PropertiesFileImpl.getDataFromPropertyFile(ResSpecs_request.RESURI_REQUEST);
        RestAssured.basePath = PropertiesFileImpl.getDataFromPropertyFile(ResSpecs_request.BASEPATH_REQUEST);
    }
}