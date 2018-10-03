package com.pefscomsys.pcc_buea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

public class ReadingTwoActivity extends AppCompatActivity {
    String reading_two;
    TextView reading_title;
    TextView readingContent;
    private ScaleGestureDetector mScaleGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reading_two);

        Intent intent = getIntent();
        reading_two = intent.getStringExtra("READING_TWO");
        reading_title = findViewById(R.id.reading_two_title);
        reading_title.setText(reading_two);

        readingContent = findViewById(R.id.reading_two_content);


        if(reading_two.contains("&"))
        {
            CompoundScriptureHandler myText = new CompoundScriptureHandler(reading_two, getApplicationContext());
            readingContent.setText(myText.getFinalResult());
        }
        else
        {
            //now get the scriptureal reading from our scripture class
            ScriptureTextHandler myText = new ScriptureTextHandler(reading_two, getApplicationContext());

            //now set the text content to what is returned from the database
            readingContent.setText(myText.getReading());
        }
        mScaleGestureDetector = new ScaleGestureDetector(this, new ReadingTwoActivity.ScaleListener());
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
