package com.zemosolabs.storede;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zemosolabs.zetarget.sdk.ZeTarget;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ShopActivity extends Activity implements View.OnClickListener,TextView.OnEditorActionListener,Spinner.OnItemSelectedListener {

    ArrayList<ShoppingItem> items = new ArrayList<ShoppingItem>();
    Typeface sourceSansPro_regular, sourceSansPro_bold;
    DecimalFormat df = new DecimalFormat("#0.00");

    private final int SHOP_SCREEN= 0;
    private final int CART = 1;
    private final int MORE = 2;
    CustomArrayAdapter customArrayAdapter;
    private int screen;

    private double cartTotal;
    private DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        sourceSansPro_regular = Typeface.createFromAsset(getAssets(),"source_sans_pro_regular.ttf");
        sourceSansPro_bold = Typeface.createFromAsset(getAssets(),"source_sans_pro_bold.ttf");
        prepareScreen1();
        calculate();
        ImageView centralButtonNavigationToCart = (ImageView)findViewById(R.id.center_cart_button_imgvw);
        RelativeLayout shopButton = (RelativeLayout)findViewById(R.id.shop_icn_clickable_layout);
        RelativeLayout moreButton = (RelativeLayout)findViewById(R.id.more_icn_clickable_layout);
        centralButtonNavigationToCart.setOnClickListener(this);
        shopButton.setOnClickListener(this);
        moreButton.setOnClickListener(this);
        screen = SHOP_SCREEN;
    }

    private void prepareScreen1() {
        ListView listView = (ListView)findViewById(R.id.listview_for_products);
        ShoppingItem iphone = new ShoppingItem(R.mipmap.img_iphone,"APPLE IPHONE 6",699.99,645.99);
        items.add(iphone);
        ShoppingItem stealLikeAnArtist = new ShoppingItem(R.mipmap.img_steal_like_an_artist_book,"STEAL LIKE AN...",20.00,15.50);
        items.add(stealLikeAnArtist);
        ShoppingItem nikonCoolpix = new ShoppingItem(R.mipmap.img_nikon_cam,"NIKON COOLPIX...",121.90,60.50);
        items.add(nikonCoolpix);

        customArrayAdapter = new CustomArrayAdapter(this,items);
        listView.setAdapter(customArrayAdapter);
    }

    @Override
    public void onClick(View v) {
        final String TAG = "NAVIGATION";
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        switch(v.getId()){
            case R.id.center_cart_button_imgvw:
                if(screen != CART) {
                    Log.i(TAG, "Cart clicked");
                    ZeTarget.logPurchaseAttempted();
                    LinearLayout screen2 = (LinearLayout) findViewById(R.id.screen2);
                    findViewById(R.id.cart_icn_clickable_layout).setVisibility(View.GONE);
                    findViewById(R.id.navigation_holder).setVisibility(View.GONE);
                    inflater.inflate(R.layout.pay_after_shopping, screen2, true);
                    findViewById(R.id.screen1).setVisibility(View.GONE);
                    findViewById(R.id.screen3).setVisibility(View.GONE);
                    screen2.setVisibility(View.VISIBLE);
                    findViewById(R.id.pay_button).setOnClickListener(this);
                    screen = CART;
                }
                break;
            case R.id.more_icn_clickable_layout:
                if(screen!=MORE){
                    Log.i(TAG, " more clicked");
                    LinearLayout screen3 = (LinearLayout) findViewById(R.id.screen3);
                    inflater.inflate(R.layout.profile_screen, screen3, true);
                    findViewById(R.id.screen1).setVisibility(View.GONE);
                    findViewById(R.id.screen2).setVisibility(View.GONE);
                    findViewById(R.id.screen3).setVisibility(View.VISIBLE);
                    findViewById(R.id.cart_icn_clickable_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.navigation_holder).setVisibility(View.VISIBLE);
                    setProfileValues();
                    screen = MORE;
                }
                break;
            case R.id.pay_button:
                /**
                 * logPurchaseCompleted(double grandTotal) is a convenience method of ZeTarget class which logs the event
                 * to the ZeTarget databases so that it can be accessed from your dashboard on www.ZeTarget.com. The method takes
                 * many more parameters in the overloaded versions such as quantity of purchase, currency in which purchase was made,
                 * the amount of tax,shipping,orderId,receiptId etc.
                 *
                 * This method is optional and to be used for generating event data for analysis.
                 */
                ZeTarget.logPurchaseCompleted(cartTotal);
                cartTotal = 0;
                for(int i=0;i<items.size();i++){
                    ShoppingItem currentItem = items.get(i);
                    currentItem.count=0;
                }
                customArrayAdapter.notifyDataSetChanged();
                calculate();
            case R.id.shop_icn_clickable_layout:
                if(screen!=SHOP_SCREEN){
                    Log.i(TAG,"shop clicked");
                    /**
                     * logPurchaseAttempted() is another convenience method in ZeTarget which can be used for logging
                     * purchase attempted events such as add to cart event etc. to keep track of such events.
                     *
                     * This method is optional and to be used for generating event data for analysis.
                     */
                    ZeTarget.logPurchaseAttempted();
                    findViewById(R.id.screen1).setVisibility(View.VISIBLE);
                    findViewById(R.id.cart_icn_clickable_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.navigation_holder).setVisibility(View.VISIBLE);
                    findViewById(R.id.screen2).setVisibility(View.GONE);
                    findViewById(R.id.screen3).setVisibility(View.GONE);
                    screen = SHOP_SCREEN;
                    if(v.getId()==R.id.pay_button){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(R.string.dialog_message)
                                .setTitle(R.string.dialog_title);
                        AlertDialog dialog = builder.create();
                        builder.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                }
                break;
            case R.id.dob_field:
                datePicker.show();

            default:
                break;
        }
    }

    private void setProfileValues() {
        final SharedPreferences sharedPreferences = getSharedPreferences("AppData",MODE_PRIVATE);

        TextView fname = (TextView) findViewById(R.id.fname_field);
        TextView lname = (TextView) findViewById(R.id.lname_field);
        Spinner gender = (Spinner) findViewById(R.id.gender_data);
        final TextView dob = (TextView) findViewById(R.id.dob_field);


        String dobField = sharedPreferences.getString("dob", "nullnull");
        int year = Integer.valueOf(dobField.substring(0, 4));
        int month = Integer.valueOf(dobField.substring(4, 6));
        int day = Integer.valueOf(dobField.substring(6,8));
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month = String.format("%02d", monthOfYear+1);
                String day = String.format("%02d",dayOfMonth);
                sharedPreferences.edit().putString("dob",year+""+month+day);
                dob.setText(day+"/"+month+"/"+year);
            }
        },year,month,day );
        datePicker.getDatePicker().setMinDate(-631152000000l);
        datePicker.getDatePicker().setMaxDate(946684800000l);

        fname.setTypeface(sourceSansPro_regular);
        lname.setTypeface(sourceSansPro_regular);
        dob.setTypeface(sourceSansPro_regular);
        fname.setOnEditorActionListener(this);
        lname.setOnEditorActionListener(this);
        gender.setOnItemSelectedListener(this);
        dob.setOnClickListener(this);

        String genderValue = sharedPreferences.getString("gender", "null").toUpperCase();
        if(genderValue.equalsIgnoreCase("male")){
            gender.setSelection(0);
        }else{
            gender.setSelection(1);
        }
        /**
         * setGender() is one of the methods used to set the user properties. It takes in a string value.
         *
         * It is an optional method used for segmentation of users and targetting promotions more accurately.
         */
        ZeTarget.setGender(genderValue);
        String dobPresentable = dobField.substring(6,8)+"/"+dobField.substring(4,6)+"/"+dobField.substring(0, 4);
        dob.setText(dobPresentable);
        /**
         * setDOB() is one of the methods used to set the user properties. It takes in a string format of the date as "yyyymmdd".
         *
         * It is an optional method used for segmentation of users and targetting promotions more accurately.
         */
        ZeTarget.setDOB(dobField);
        if(sharedPreferences.contains("fname")) {
            String firstNameValue = sharedPreferences.getString("fname", "null");
            fname.setText(firstNameValue);
            /**
             * setFirstName() is one of the methods used to set the user properties. It takes in a string value for first name.
             *
             * It is an optional method used for segmentation of users and targetting promotions more accurately.
             */
            ZeTarget.setFirstName(firstNameValue);
        }
        if(sharedPreferences.contains("lname")) {
            String lastNameValue = sharedPreferences.getString("lname", "null");
            lname.setText(lastNameValue);
            /**
             * setLastName() is one of the methods used to set the user properties. It takes in a string value for last name.
             *
             * It is an optional method used for segmentation of users and targetting promotions more accurately.
             */
            ZeTarget.setLastName(lastNameValue);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch(v.getId()){
            case R.id.fname_field:
                String fname = v.getText().toString();
                editor.putString("fname",fname).apply();
                break;
            case R.id.lname_field:
                String lname = v.getText().toString();
                editor.putString("lname",lname).apply();
                break;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String genderValue = ((TextView)view).getText().toString();
        getSharedPreferences("AppData",MODE_PRIVATE).edit().putString("gender",genderValue).apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class CustomArrayAdapter extends ArrayAdapter<ShoppingItem> {
        private ArrayList<ShoppingItem> items;
        private Context context;

        public CustomArrayAdapter(Context context, ArrayList<ShoppingItem> objects) {
            super(context, R.layout.single_item_layout, objects);
            this.context = context;
            this.items=objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Typeface sourceSansPro_regular = Typeface.createFromAsset(getAssets(),"source_sans_pro_regular.ttf");
            Typeface sourceSansPro_bold = Typeface.createFromAsset(getAssets(),"source_sans_pro_bold.ttf");
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View returnableView = inflater.inflate(R.layout.single_item_layout, parent, false);
            final ShoppingItem currentItem = items.get(position);
            ImageView imgView = (ImageView) returnableView.findViewById(R.id.image_of_item_to_purchase);
            imgView.setImageResource(currentItem.mipmap_id);
            TextView desc = (TextView) returnableView.findViewById(R.id.item_name);
            desc.setText(currentItem.desc);
            TextView ex_price = (TextView) returnableView.findViewById(R.id.item_ex_price);
            ex_price.setText(currentItem.exPriceString);
            ex_price.setPaintFlags(ex_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            TextView current_price = (TextView) returnableView.findViewById(R.id.item_current_price);
            current_price.setText(currentItem.currentPriceString);
            TextView qty_tag = (TextView) returnableView.findViewById(R.id.quantity_tag);
            qty_tag.setTypeface(sourceSansPro_regular);
            current_price.setTypeface(sourceSansPro_bold);
            desc.setTypeface(sourceSansPro_regular);
            ex_price.setTypeface(sourceSansPro_regular);
            ImageView minus = (ImageView)returnableView.findViewById(R.id.minus_symbol);
            ImageView plus = (ImageView)returnableView.findViewById(R.id.plus_symbol);
            final TextView quantity_value = (TextView) returnableView.findViewById(R.id.numeric_qty);
            quantity_value.setText(currentItem.count+"");
            quantity_value.setTypeface(sourceSansPro_regular);
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer newValue = Integer.valueOf((String) quantity_value.getText());
                    if (newValue > 0) {
                        currentItem.decrement();
                        newValue--;
                        ShopActivity.this.calculate();
                    }
                    quantity_value.setText(newValue + "");
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer newValue = Integer.valueOf((String) quantity_value.getText());
                    newValue++;
                    currentItem.increment();
                    ShopActivity.this.calculate();
                    quantity_value.setText(newValue + "");
                }
            });
            return returnableView;
        }
    }

    private void calculate() {
        TextView subtotal_value = (TextView) findViewById(R.id.subtotal_value_text);
        TextView shipping = (TextView) findViewById(R.id.shipping_value_text);
        TextView badge = (TextView)findViewById(R.id.badge_counter);
        TextView total = (TextView) findViewById(R.id.total_value_text);
        double subtotal = 0.0;
        int count = 0;
        for(int i=0;i<items.size();i++){
            ShoppingItem item = items.get(i);
            subtotal+=item.currentPrice*item.count;
            count +=item.count;
        }
        subtotal_value.setText("$" + df.format(subtotal));

        if(subtotal==0){
            shipping.setText("$" + df.format(0));
            total.setText("$" + df.format(0));
            cartTotal = 0;
        }else {
            total.setText("$" + df.format(subtotal + 10));
            shipping.setText("$" + df.format(10));
            cartTotal = subtotal+10;
        }

        if(count==0){
           badge.setVisibility(View.GONE);
        }else if(count>0){
            badge.setText(count+"");
            badge.setVisibility(View.VISIBLE);
        }
    }


}

