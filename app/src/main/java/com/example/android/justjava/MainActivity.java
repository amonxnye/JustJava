package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private int NUMBER_OF_COFFEES;
    private Toolbar toolbar;
    private CharSequence charSequence;
    private int duration;
    private int totalToppingPrice;
    private int temp;
    private Toast toast;
    private Context context;
    private int ORDER_ID;
    private String CUST_NAME;
    private TextView summaryTextView;
    private TextView summaryTitleTextView;
    private EditText name;
    private TextView quantityTextView;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private String orderSummary;
    private String TOPPINGS;

    private OrderDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        dataSource = new OrderDataSource(this);
        dataSource.open();

        //List<Order> orderList = dataSource.getAllOrders();

        //initialize global variables
        NUMBER_OF_COFFEES = 0;
        totalToppingPrice = 0;
        temp = new Random().nextInt();

        //find and attach views to references
        name = (EditText) findViewById(R.id.cust_name);
        summaryTextView = (TextView) findViewById(R.id.summary_text_view);
        summaryTitleTextView = (TextView) findViewById(R.id.summary);
        quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        if (NUMBER_OF_COFFEES > 0) {
            disableCheckbox(checkBox1);
            disableCheckbox(checkBox2);
            disableCheckbox(checkBox3);
            summaryTextView.setVisibility(View.VISIBLE);
            summaryTitleTextView.setVisibility(View.VISIBLE);
            CUST_NAME = name.getText().toString();
            summaryTextView.setText(orderSummary());
            showToast(getString(R.string.prepare), Toast.LENGTH_SHORT);

            // save the new order to the database
            dataSource.createOrder(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                    CUST_NAME, ORDER_ID, NUMBER_OF_COFFEES, TOPPINGS,
                    calculatePrice(NUMBER_OF_COFFEES));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); //only email apps can handle this
                    String receiver[] = {getString(R.string.company_email)};
                    intent.putExtra(Intent.EXTRA_EMAIL, receiver);
                    intent.putExtra(intent.EXTRA_TEXT, orderSummary());
                    intent.putExtra(intent.EXTRA_SUBJECT, getString(R.string.order_id) +
                            ORDER_ID + returnNameIfNotNull(CUST_NAME));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse("market://search?q=email"));
                        startActivity(intent1);
                    }
                }
            }, 3000);
            Log.v("MainActivity", "ORDER SUCCESSFUL");
            totalToppingPrice = 0;
        } else {
            showToast(getString(R.string.order_none), Toast.LENGTH_SHORT);
        }
    }

    private String returnNameIfNotNull(String string) {
        if (!(string.isEmpty()))
            return ": " + getString(R.string.order_sub) + string;
        return "";
    }

    private void disableCheckbox(final CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checkBox.setChecked(false);
                else
                    checkBox.setChecked(true);
            }
        });
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        quantityTextView.setText("" + number);
    }

    private int calculatePrice(int quantity) {
        return (quantity * 5) + totalToppingPrice;
    }

    private int calculateTotalToppingPrice(CheckBox checkBox) {
        if (checkBox == checkBox1)
            return 1 * NUMBER_OF_COFFEES;
        else if (checkBox == checkBox2)
            return 2 * NUMBER_OF_COFFEES;
        else
            return 3 * NUMBER_OF_COFFEES;
    }

    private void addIfChecked(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            orderSummary += "\n\t\t\t" + checkBox.getText().toString();
            TOPPINGS += checkBox.getText().toString() + "|";
            totalToppingPrice += calculateTotalToppingPrice(checkBox);
        }
    }

    private String orderSummary() {
        orderSummary = getString(R.string.summary_name) + CUST_NAME;

        ORDER_ID = temp > 0 ? temp : -temp;
        orderSummary += "\n" + getString(R.string.order_id) + ORDER_ID;

        orderSummary += "\n" + getString(R.string.summary_quantity) + NUMBER_OF_COFFEES;
        orderSummary += "\n" + getString(R.string.summary_toppings);
        addIfChecked(checkBox1);
        addIfChecked(checkBox2);
        addIfChecked(checkBox3);
        orderSummary += "\n" + getString(R.string.summary_total) +
                NumberFormat.getCurrencyInstance().format(calculatePrice(NUMBER_OF_COFFEES)) + "\n";
        return orderSummary;
    }

    public void increment(View view) {
        if (NUMBER_OF_COFFEES != 100)
            NUMBER_OF_COFFEES += 1;
        else {
            showToast(getString(R.string.coffee_max), Toast.LENGTH_SHORT);
        }
        displayQuantity(NUMBER_OF_COFFEES);
    }

    public void decrement(View view) {
        if (NUMBER_OF_COFFEES != 0)
            NUMBER_OF_COFFEES -= 1;
        else {
            showToast(getString(R.string.coffee_min), Toast.LENGTH_SHORT);
        }
        displayQuantity(NUMBER_OF_COFFEES);
    }

    public void showToast(CharSequence charSequence, int duration) {
        this.context = getApplicationContext();
        this.charSequence = charSequence;
        this.duration = duration;
        toast = Toast.makeText(context, charSequence, duration);
        toast.show();
    }
}