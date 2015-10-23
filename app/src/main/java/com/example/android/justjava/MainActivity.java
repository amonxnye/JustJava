package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private int NUMBER_OF_COFFEES = 0;
    private Toolbar toolbar;
    private CharSequence charSequence;
    private int duration;
    private int totalToppingPrice = 0;
    private Toast toast;
    private Context context;
    private String CUST_NAME;
    private TextView summaryTextView;
    private TextView summaryTitleTextView;
    private EditText name;
    private TextView quantityTextView;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private String orderSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //find and attach views to references
        name = (EditText) findViewById(R.id.cust_name);
        summaryTextView = (TextView) findViewById(R.id.summary_text_view);
        summaryTitleTextView = (TextView) findViewById(R.id.summary);
        quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkbox3);

    /*    //reset stuff
        summaryTextView.setText("");
        summaryTextView.setVisibility(View.INVISIBLE);
        summaryTitleTextView.setVisibility(View.INVISIBLE);
*/
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        summaryTextView.setVisibility(View.VISIBLE);
        summaryTitleTextView.setVisibility(View.VISIBLE);
        CUST_NAME = name.getText().toString();
        summaryTextView.setText(orderSummary());

        showToast("Thank you for your order! Preparing to email your order..", Toast.LENGTH_LONG);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); //only email apps can handle this
                String receiver[] = {"orders@justjava.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, receiver);
                intent.putExtra(intent.EXTRA_SUBJECT, "JustJava order for " + CUST_NAME);
                intent.putExtra(intent.EXTRA_TEXT, orderSummary());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        }, 5000);

        Log.v("MainActivity", "ORDER SUCCESSFUL");

        totalToppingPrice = 0;

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
            totalToppingPrice += calculateTotalToppingPrice(checkBox);
        }
    }

    private String orderSummary() {
        orderSummary = "Name: " + CUST_NAME;
        orderSummary += "\nQuantity: " + NUMBER_OF_COFFEES;
        orderSummary += "\nToppings: ";
        addIfChecked(checkBox1);
        addIfChecked(checkBox2);
        addIfChecked(checkBox3);
        orderSummary += "\nTotal: " + NumberFormat.getCurrencyInstance().format(calculatePrice(NUMBER_OF_COFFEES));
        return orderSummary;
    }

    public void increment(View view) {

        if (NUMBER_OF_COFFEES != 100)
            NUMBER_OF_COFFEES += 1;
        else {
            showToast("Sorry, we don't have that much coffee beans left", Toast.LENGTH_SHORT);
        }

        displayQuantity(NUMBER_OF_COFFEES);

    }

    public void decrement(View view) {

        if (NUMBER_OF_COFFEES != 0)
            NUMBER_OF_COFFEES -= 1;
        else {
            showToast("Now you're going imaginary!!", Toast.LENGTH_SHORT);
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