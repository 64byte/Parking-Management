package com.story.parking.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.story.parking.AppConfig;
import com.story.parking.R;
import com.story.parking.RequestManager;
import com.story.parking.activity.abstracts.BaseActivity;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentActivity extends BaseActivity {
    private EditText mEditTextCardNum;
    private EditText mEditTextExpiration;
    private EditText mEditTextCVV;
    private Button mButtonCheckout;

    private String cardNum;

    public PaymentActivity() {
        setContentViewResId(R.layout.activity_payment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mEditTextCardNum = (EditText)findViewById(R.id.edittext_cardnum);
        mEditTextExpiration = (EditText)findViewById(R.id.edittext_expiration);
        mEditTextCVV = (EditText)findViewById(R.id.edittext_cvv);
        mButtonCheckout = (Button)findViewById(R.id.button_checkout);

        mEditTextCardNum.addTextChangedListener(new TextWatcher() {
            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // chech that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrecntString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });

        mEditTextExpiration.addTextChangedListener(new TextWatcher() {
            private static final int TOTAL_SYMBOLS = 5; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 4; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 3; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = '/';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // chech that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrecntString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });

        mButtonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequestPay();
            }
        });
    }

    private void doRequestPay() {
        mEditTextCardNum.setError(null);
        mEditTextExpiration.setError(null);
        mEditTextCVV.setError(null);

        String cardNum = mEditTextCardNum.getText().toString();
        String exp = mEditTextExpiration.getText().toString();
        String cvv = mEditTextCVV.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (cardNum.isEmpty()) {
            mEditTextCardNum.setError("필수 입력 항목입니다.");
            focusView = mEditTextCardNum;
            cancel = true;
        } else if (!isCardNumberVaild(cardNum)) {
            mEditTextCardNum.setError("카드 번호 입력 형식이 아닙니다.");
            focusView = mEditTextCardNum;
            cancel = true;
        }

        if (exp.isEmpty()) {
            mEditTextExpiration.setError("필수 입력 항목입니다.");
            focusView = mEditTextExpiration;
            cancel = true;
        } else if (!isCardExpirationVaild(exp)) {
            mEditTextExpiration.setError("유효 기간 입력 형식이 아닙니다.");
            focusView = mEditTextExpiration;
            cancel = true;
        }

        if (cvv.isEmpty()) {
            mEditTextCVV.setError("필수 입력 항목입니다.");
            focusView = mEditTextCVV;
            cancel = true;
        } else if (!isCardCvvVaild(cvv)) {
            mEditTextCVV.setError("보안 번호 입력 형식이 아닙니다.");
            focusView = mEditTextCVV;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            RequestManager.getInstance().doRequestPay(PaymentActivity.this, "", "", "", new RequestManager.OnJSON() {
                @Override
                public boolean onJSON(JSONObject json) {
                    if (json != null) {
                        //   doRequestExitCar();
                    }

                    return false;
                }
            });
        }
    }

    private void doRequestExitCar() {
        RequestManager.getInstance().doRequestExitCar(this, AppConfig.getInstance().getUser().getCarNum(), AppConfig.getInstance().getCheckoutKey(), new RequestManager.OnJSON() {
            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {

                }

                return false;
            }
        });
    }

    private boolean isCardNumberVaild(String cardnumber) {
        Pattern pattern;
        Matcher matcher;
        final String CARDNUMBER_PATTERN = "^4[0-9]{3}\\s?[0-9]{4}\\s?[0-9]{4}\\s?(?:[0-9]{4})?$";
        pattern = Pattern.compile(CARDNUMBER_PATTERN);
        matcher = pattern.matcher(cardnumber);

        return matcher.matches();
    }

    private boolean isCardExpirationVaild(String exp) {
        Pattern pattern;
        Matcher matcher;
        final String CARDEXP_PATTERN = "^(0[1-9]|1[0-2])\\/?([0-9]{4}|[0-9]{2})$";
        pattern = Pattern.compile(CARDEXP_PATTERN);
        matcher = pattern.matcher(exp);

        return matcher.matches();
    }

    private boolean isCardCvvVaild(String cvv) {
        Pattern pattern;
        Matcher matcher;
        final String CARDCVV_PATTERN = "^([0-9]){3}$";
        pattern = Pattern.compile(CARDCVV_PATTERN);
        matcher = pattern.matcher(cvv);

        return matcher.matches();
    }

    public static void Open(BaseActivity baseActivity) {
        baseActivity.startActivity(new Intent(baseActivity, PaymentActivity.class));
    }
}
