package com.pefscomsys.pcc_buea;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private int NAME_INDEX = 1;
    private int CHAIR_INDEX = 2;
    private int BOX_INDEX = 3;
    private int ADDRESS_INDEX = 4;
    private int TEL_INDEX = 5;
    private int EMAIL_INDEX = 6;

    List<ChurchHistory> histories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_history);

        setTitle(R.string.church_history);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.getCustomView();

        //get the history lists
        //Context context = getApplicationContext();
        getHistoryInfo(this);

        //work with the adapter
        RecyclerView historyView = (RecyclerView) findViewById(R.id.history_recycler);
        RecyclerView addressView = (RecyclerView) findViewById(R.id.address_recycler_v);
        historyView.setNestedScrollingEnabled(false);
        addressView.setNestedScrollingEnabled(false);

        historyView.setHasFixedSize(true);
        addressView.setHasFixedSize(true);

        historyView.setLayoutManager(new LinearLayoutManager(this));
        addressView.setLayoutManager(new LinearLayoutManager(this));

        //create a new instance of the adapter
        ChurchHistoryAdapter historyAdapter = new ChurchHistoryAdapter(histories);
        ChurchAddressAdapter addressAdapter = new ChurchAddressAdapter(getChurchAddresses(), this);

        historyView.setAdapter(historyAdapter);
        addressView.setAdapter(addressAdapter);

        ///church address adapter

        //prepare the recycler view

        //create an address view holder

    }

    private void getHistoryInfo(Context context)
    {


        histories = new ArrayList<ChurchHistory>();

        ChurchHistory currentHistory;

        //do a database call
        ScriptureDBHandler db = new ScriptureDBHandler(context);

        //tty creating a database
        try {
            db.createDataBase();
        } catch (IOException e) {
            Log.d("PCCAPP", "Database error");
            e.printStackTrace();
        }

        //now open the databse
        db.openDataBase();

        String query = "SELECT * FROM church_info ";

        Cursor result = db.myDataBase.rawQuery(query, null);

        while(result.moveToNext())
        {
            currentHistory = new ChurchHistory();

            currentHistory.setName(result.getString(NAME_INDEX));
            currentHistory.setChairPerson(result.getString(CHAIR_INDEX));
            currentHistory.setPobox(result.getString(BOX_INDEX));
            currentHistory.setAddress(result.getString(ADDRESS_INDEX));
            currentHistory.setTel(result.getString(TEL_INDEX));
            currentHistory.setEmail(result.getString(EMAIL_INDEX));

            //ADD THE CURRENT HISTORY TO THE LIST OF HISTORUIES
            histories.add(currentHistory);
        }

        //close the result
        result.close();

        //close the database
        db.close();

    }
    public List<ChurchAddress> getChurchAddresses()
    {
        List<ChurchAddress> addresses  = new ArrayList<>();

        ChurchAddress currentAddress;

        ScriptureDBHandler database = new ScriptureDBHandler(this);

        try {
            database.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String query = "SELECT  * FROM `church_address` ";

        database.openDataBase();


        Cursor result = database.myDataBase.rawQuery(query, null);

        while(result.moveToNext())
        {
            currentAddress = new ChurchAddress();

            currentAddress.setName(result.getString(1));
            currentAddress.setChair(result.getString(2));
            currentAddress.setCo(result.getString(3));
            currentAddress.setPobox(result.getString(4));
            currentAddress.setAddress(result.getString(5));
            currentAddress.setTel(result.getString(6));
            currentAddress.setEmail(result.getString(7));

            addresses.add(currentAddress);
        }

        result.close();

        database.close();

        return addresses;
    }
}
