package com.github.aleksandermielczarek.fieldnamesexample;

import com.github.aleksandermielczarek.fieldnames.FieldNames;

/**
 * Created by Aleksander Mielczarek on 13.09.2016.
 */
@FieldNames
public class TestData {

    public static final String TEST = "test";

    private String testData;
    private String testData2;

    public String getTestData() {
        return testData;
    }

    public void setTestData(String testData) {
        this.testData = testData;
    }

    public String getTestData2() {
        return testData2;
    }

    public void setTestData2(String testData2) {
        this.testData2 = testData2;
    }
}
