package com.wasimapps.revenuecalculator;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wasim on 2017-01-17.
 */

public class GetShopifyJsonData extends GetRawData {

    private String LOG_TAG = GetShopifyJsonData.class.getSimpleName();
    private List<Product> mProduct;
    private Uri mDestination;

    public interface OnResultsReadyListener {
        void onResultsReady(List<Product> products);
    }

    private OnResultsReadyListener mResultsListener;

    public GetShopifyJsonData(int page, OnResultsReadyListener resultsListener) {
        super(null);
        createUri(page);
        mProduct = new ArrayList<>();
        mResultsListener = resultsListener;
    }


    public void execute(){
        super.setRawUrl(mDestination.toString());
        DownloadShopifyData downloadShopifyData = new DownloadShopifyData();
        Log.v(LOG_TAG, "Built URI = " + mDestination.toString());
        downloadShopifyData.execute(mDestination.toString());
    }


    //CREATES THE URIs
    public boolean createUri(int page) {
        final String SHOPIFY_BASE_URL = "https://shopicruit.myshopify.com/admin/orders.json";
        final String SHOPIFY_PAGE_PARAM = "page";
        final String SHOPIFY_FIELD_PARAM = "fields";
        final String SHOPIFY_ACCESS_TOKEN = "access_token";


        mDestination = Uri.parse(SHOPIFY_BASE_URL).buildUpon()
                .appendQueryParameter(SHOPIFY_PAGE_PARAM, String.valueOf(page))
                .appendQueryParameter(SHOPIFY_FIELD_PARAM, "total_price_usd")
                .appendQueryParameter(SHOPIFY_ACCESS_TOKEN, "c32313df0d0ef512ca64d5b336a0d7c6")
                .build();

        return mDestination != null;
    }

    public void processResults() {

        if(getDownloadStatus() != DownloadStatus.OK){
            Log.e(LOG_TAG, "Error Downloading Raw Data");
            return;
        }

        final String SH_ORDERS = "orders";
        final String SH_TOTAL_PRICE = "total_price_usd";

        try {

            JSONObject jsonData = new JSONObject(getData());
            JSONArray ordersArray = jsonData.getJSONArray(SH_ORDERS);
            for (int i=0; i<ordersArray.length(); i++ ) {
                JSONObject jsonProduct = ordersArray.getJSONObject(i);
                double totalPrice = jsonProduct.getDouble(SH_TOTAL_PRICE);


                Product productObject = new Product(totalPrice);
                this.mProduct.add(productObject);

            }


        } catch (JSONException jsone) {

            jsone.printStackTrace();
            Log.e(LOG_TAG, "Error Processing JSON data");

        }

        if(mResultsListener != null) {
            mResultsListener.onResultsReady(mProduct);
        }


    }


    public class DownloadShopifyData extends DownloadRawData {
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResults();
        }

        protected String doInBackground(String... prams) {
            return super.doInBackground(prams);
        }



    }

}