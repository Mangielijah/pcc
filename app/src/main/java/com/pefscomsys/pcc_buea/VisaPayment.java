package com.pefscomsys.pcc_buea;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.util.HashMap;
import java.util.Map;

public class VisaPayment extends AppCompatActivity {

    TextView reasonView, amountView;
    private String Reason;
    private int Amount;
    private String year;
    private String type;
    private Button payButton;

    private Context context;

    //my stripe keys
    private final String STRIPE_PUBLIC_KEY = "pk_test_4o4XUuHJFzxi2D5fSbKUIexq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_payment);
        setTitle("Visa Payment");

        context = getApplicationContext();

        reasonView = findViewById(R.id.visa_caption);
        amountView =findViewById(R.id.visa_amount);
        payButton = findViewById(R.id.visa_button);

        //collect data from intent
        Intent paymentIntent = getIntent();
        Reason = paymentIntent.getStringExtra("REASON");
        Amount = paymentIntent.getIntExtra("AMOUNT", 0);

            //extra for diary
        try
        {
            this.type = paymentIntent.getStringExtra("TYPE");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //now check if the payment is for diary
        if(this.type != null )
        {
            if(this.type.equals("DIARY"))
            {
                //then get the year
                this.year = paymentIntent.getStringExtra("YEAR");
            }

        }

        //after getting amuonts
        reasonView.setText(Reason.toUpperCase());

        String amountText = "Amount : $" + String.valueOf(Amount);
        amountView.setText(amountText);

        //when the button is clicked. perform payment here
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //process the card
                //process here
                CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

                Card card = mCardInputWidget.getCard();
                if (card == null) {
                    Toast.makeText(getApplicationContext(), "Failed to process visa", Toast.LENGTH_SHORT);
                }
                else
                {
                    //charge the client directly
                    //create a token
                    Stripe stripe = new Stripe(context, STRIPE_PUBLIC_KEY);

                    stripe.createToken(card, new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                            Toast.makeText(context, "Error Prcessing Card", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(Token token) {
                            //save the token and charge directly
                            Log.d("PCCAPP", token.toString());

                            //MAKE AN AJAX REQUEST TO THE SERVER TO MAKE THE CHAREG
                            //and then parese the response


                        }
                    });

                }
            }
        });

    }
}
