package com.andrognito.pinlockview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class InputField extends AppCompatEditText {

    public InputField(Context context) {
        super(context);

        disableKeyboard(context);
        setupPasswordDots();
    }

    public InputField(Context context, AttributeSet attrs) {
        super(context, attrs);

        disableKeyboard(context);
        setupPasswordDots();
    }

    public InputField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        disableKeyboard(context);
        setupPasswordDots();
    }

    private void disableKeyboard(Context context) {
        this.setCursorVisible(true);
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.setInputType(InputType.TYPE_NULL);
        this.setKeyListener(null);
        this.setRawInputType(InputType.TYPE_CLASS_TEXT);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /* do nothing */
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
                /* do nothing */
            }
        });
    }

    private void setupPasswordDots() {
        final InputField inputField = this;
        TextWatcher pinWatcher = new TextWatcher() {

            private Handler pinHandler = new Handler();

            int oldLength = 0;
            boolean editing = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editing) return; // Do nothing if editing field within the watcher, to prevent infinite loops

                pinHandler.removeCallbacksAndMessages(null);

                Runnable hideText = new Runnable() {
                    @Override
                    public void run() {
                        editing = true;
                        inputField.setText(hideString(inputField.getText().toString()));
                        inputField.setSelection(inputField.getText().length());
                        editing = false;
                    }
                };

                if (oldLength < s.toString().length()) { // Briefly display last digit if PIN increases
                    oldLength = s.length();

                    editing = true;
                    inputField.setText(almostHideString(inputField.getText().toString()));
                    inputField.setSelection(inputField.getText().length());
                    editing = false;

                    pinHandler.postDelayed(hideText, 1500);
                } else { // Otherwise just hide PIN if deleting
                    oldLength = s.length();

                    hideText.run();
                }
            }

        };

        this.addTextChangedListener(pinWatcher);
    }

    public String almostHideString(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length() - 1; ++i) {
            stringBuilder.append(DOT);
        }
        stringBuilder.append(s.charAt(s.length() - 1));

        return stringBuilder.toString();
    }

    public String hideString(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            stringBuilder.append(DOT);
        }

        return stringBuilder.toString();
    }

    private static char DOT = '\u2022';
}
