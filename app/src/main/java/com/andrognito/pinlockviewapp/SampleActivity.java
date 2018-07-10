package com.andrognito.pinlockviewapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.View;
import android.view.View.*;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.InputField;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.andrognito.pinlockview.SeparateDeleteButton;

public class SampleActivity extends AppCompatActivity {

    public static final String TAG = "PinLockView";

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private ImageView logo;
    private InputField mInputField;
    private boolean isEnterButtonEnabled = true;
    private SeparateDeleteButton separateDeleteButton;

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
            Toast.makeText(SampleActivity.this, "Pin complete", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sample);
        logo = (ImageView) findViewById(R.id.profile_image);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mInputField = (InputField) findViewById(R.id.input_field);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        separateDeleteButton = (SeparateDeleteButton) findViewById(R.id.separate_delete_button);

        mInputField.setVisibility(View.VISIBLE);
        separateDeleteButton.setVisibility(View.VISIBLE);
        separateDeleteButton.setSeparateDeleteButtonColor(R.color.light_purple);
        separateDeleteButton.setSeparateDeleteButtonPressedColor(Color.WHITE);
        mPinLockView.attachSeparateDeleteButton(separateDeleteButton);

        mPinLockView.attachInputField(mInputField);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();
        mPinLockView.setShowDeleteButton(false);
        mIndicatorDots.setVisibility(View.GONE);

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);

        mPinLockView.setPinLength(4);

        ((RelativeLayout.LayoutParams) mPinLockView.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.input_field);

        logo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                if(isShowing) {
//                    mPinLockView.setVisibility(View.GONE);
//                    mIndicatorDots.setVisibility(View.GONE);
//                    isShowing = false;
//                } else {
//                    mPinLockView.setVisibility(View.VISIBLE);
//                    mIndicatorDots.setVisibility(View.VISIBLE);
//                    isShowing = true;
//                }
                if (isEnterButtonEnabled) {
                    isEnterButtonEnabled = false;
                    mPinLockView.setShowEnterButton(false);
                    mPinLockView.setSwapEnterDeleteButtons(false);
                    mPinLockView.setShowDeleteButton(true);
                    separateDeleteButton.setShowSeparateDeleteButton(false);
                    mInputField.setVisibility(View.GONE);
                    mIndicatorDots.setVisibility(View.VISIBLE);
                    ((RelativeLayout.LayoutParams) mPinLockView.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.indicator_dots);
                } else{
                    isEnterButtonEnabled = true;
                    mPinLockView.setShowEnterButton(true);
                    mPinLockView.setSwapEnterDeleteButtons(true);
                    mPinLockView.setShowDeleteButton(false);
                    separateDeleteButton.setShowSeparateDeleteButton(true);
                    mInputField.setVisibility(View.VISIBLE);
                    mIndicatorDots.setVisibility(View.GONE);
                    ((RelativeLayout.LayoutParams) mPinLockView.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.input_field);
                    mInputField.requestFocus();
                }
            }
        });
    }
}
