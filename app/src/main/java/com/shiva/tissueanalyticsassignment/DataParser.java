package com.shiva.tissueanalyticsassignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DataParser {
    public static StreamFlowData parseStreamFlowData(String io) {

        //declaration of variables
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        double avgValue = Double.MIN_VALUE;
        int[] streamValueArray;
        double sum=0,each_value=0;
        String siteName,siteCodeValue;

        StreamFlowData streamflowdata = null;
        JSONObject valuearrayObject = null;
        JSONObject jsonObject = null;


        try {
            //create an object to the class to collect the stream flow data
            streamflowdata = new StreamFlowData();
            jsonObject = new JSONObject(io);
            JSONObject rootValue = jsonObject.getJSONObject("value");
            JSONArray timeSeries = rootValue.getJSONArray("timeSeries");
            JSONObject timeSeriesObject = (JSONObject) timeSeries.get(0);
            JSONObject sourceInfo = timeSeriesObject.getJSONObject("sourceInfo");
            JSONArray siteCode= sourceInfo.getJSONArray("siteCode");
            JSONObject siteCodeObject = siteCode.getJSONObject(0);
            JSONArray values= timeSeriesObject.getJSONArray("values");
            JSONObject valuesObject= (JSONObject) values.get(0);
            JSONArray valueArray= valuesObject.getJSONArray("value");

            streamValueArray=new int[valueArray.length()];

            for (int i = 0; i < valueArray.length(); i++) {
                valuearrayObject = valueArray.getJSONObject(i);
                each_value = Double.parseDouble(valuearrayObject.getString("value"));
                /*finding minimum value from the data*/
                minValue = minValue > each_value ? each_value : minValue;
                /*finding maximum value from the data*/
                maxValue = maxValue < each_value ? each_value : maxValue;

                streamValueArray[i] = (int) each_value;
                sum += each_value;
            }
              /*calculating average value from the data*/
            avgValue=sum/valueArray.length();

             /*fetching site name from the data*/
            siteName= sourceInfo.getString("siteName");

            /*fetching site code from the data*/
            siteCodeValue= siteCodeObject.getString("value");


            //calling setter methods on the streamflow object
            streamflowdata.setSiteName(siteName);
            streamflowdata.setSiteCode(siteCodeValue);
            streamflowdata.setAverageValue(avgValue);
            streamflowdata.setMaxValue(maxValue);
            streamflowdata.setMinValue(minValue);
            streamflowdata.setStreamValues(streamValueArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return streamflowdata;
    }
}
