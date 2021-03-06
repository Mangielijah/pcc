package com.pefscomsys.pcc_buea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;


public class ReadingOneActivity extends AppCompatActivity {
    String reading_one;
    TextView reading_title;
    TextView readingContent;
    private ScaleGestureDetector mScaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reading_one);

        Intent intent = getIntent();
        reading_one = intent.getStringExtra("READING_ONE");
        reading_title = findViewById(R.id.reading_one_title);
        reading_title.setText(reading_one);

        readingContent = findViewById(R.id.reading_one_content);

        //check if the scripture is a compound scripture
        if(reading_one.contains("&"))
        {
            CompoundScriptureHandler myText = new CompoundScriptureHandler(reading_one, getApplicationContext());
            readingContent.setText(myText.getFinalResult());
        }
        else
        {
            //now get the scriptureal reading from our scripture class
            ScriptureTextHandler myText = new ScriptureTextHandler(reading_one, getApplicationContext());

            //now set the text content to what is returned from the database
            readingContent.setText(myText.getReading());
        }

        mScaleGestureDetector = new ScaleGestureDetector(this, new ReadingOneActivity.ScaleListener());
        readingContent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getPointerCount() == 1){
                    //stuff for 1 pointer
                }else{ //when 2 pointers are present
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            mScaleGestureDetector.onTouchEvent(event);
                            break;

                        case MotionEvent.ACTION_MOVE:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            mScaleGestureDetector.onTouchEvent(event);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return true;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float size = readingContent.getTextSize();
            float mScaleFactor = detector.getScaleFactor();
            int increase = 0;
            if (mScaleFactor > 0.1f){
                increase = 2;
            }
            else {
                increase = -2;
            }
            size += increase;
            readingContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
