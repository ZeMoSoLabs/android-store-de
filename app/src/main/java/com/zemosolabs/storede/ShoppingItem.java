package com.zemosolabs.storede;

import java.text.DecimalFormat;

/**
 * Created by vedaprakash on 29/6/15.
 */
public class ShoppingItem {
    int mipmap_id;
    String desc,exPriceString,currentPriceString;
    DecimalFormat df = new DecimalFormat("#0.00");
    double currentPrice;
    double exPrice;
    int count=0;

    public ShoppingItem(int mipmap_id, String desc,double exPrice, double currentPrice){
        this.mipmap_id = mipmap_id;
        this.desc = desc;
        this.exPrice = exPrice;
        this.currentPrice = currentPrice;
        exPriceString = "$"+df.format(exPrice);
        currentPriceString = "$"+df.format(currentPrice);
    }

    void increment(){
        count++;
    }
    void decrement(){
        if(count>0){
            count--;
        }
    }
}
