package com.wasimapps.revenuecalculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetShopifyJsonData.OnResultsReadyListener {
    private List<Product> allProducts;

    private String LOG_TAG = MainActivity.class.getSimpleName();



    @Override
    public void onResultsReady(List<Product> products) {
        // allProducts contains USD order totals for all requests that have completed so far
        allProducts.addAll(products);
        Log.v(LOG_TAG, allProducts.size() + " orders retrieved.");
//        for(Product singleProduct : allProducts) {
//            Log.v(LOG_TAG, singleProduct.toString());
//        }

        priceCalc(allProducts);
    }



    public void priceCalc(List<Product> allProducts) {
        double totalOrdersRevenue = 0;
        for(Product singleProduct : allProducts) {
               double orderTotal = singleProduct.getTotalPrice();
               totalOrdersRevenue = totalOrdersRevenue + orderTotal;
            }
        Log.v (LOG_TAG, "Total Orders Revenue is: US$" + String.valueOf(totalOrdersRevenue));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allProducts = new ArrayList<>();


        for (int i = 1; i < 4; i++) {
            GetShopifyJsonData jsonData = new GetShopifyJsonData(i, this);
            jsonData.execute();
        }

    }
}