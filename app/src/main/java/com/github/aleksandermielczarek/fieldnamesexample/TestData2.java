package com.github.aleksandermielczarek.fieldnamesexample;

import com.github.aleksandermielczarek.fieldnames.FieldNames;

/**
 * Created by Aleksander Mielczarek on 13.09.2016.
 */
@FieldNames
public class TestData2 extends TestData {

    private String testData;
    private String testData3;

    public String getTestData3() {
        return testData3;
    }

    public void setTestData3(String testData3) {
        this.testData3 = testData3;
    }

    @Override
    public String getTestData() {
        return testData;
    }

    @Override
    public void setTestData(String testData) {
        this.testData = testData;
    }
}
