package com.preons.pranav.QRCodeGenerator.Views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.preons.pranav.QRCodeGenerator.R;
import com.preons.pranav.QRCodeGenerator.utils.StringHandler;

/**
 * Created on 10-03-17 at 23:26 by Pranav Raut.
 * For QRCodeProtection
 */

@SuppressWarnings("unused")
public class STDInformation extends LinearLayout {
    int[] EditTextId = {R.id.name, R.id.add, R.id.email, R.id.mob, R.id.add_no, R.id.pan_no, R.id.bank_no};
    EditText[] editTexts = new EditText[EditTextId.length];
    RadioButton[] radioGroup = new RadioButton[2];
    Spinner spinner;
    stateChangeListener listener = null;
    private boolean[] b = new boolean[editTexts.length];

    public STDInformation(Context context) {
        super(context);
        inti();
    }

    public STDInformation(Context context, AttributeSet attrs) {
        super(context, attrs);
        inti();
    }

    public STDInformation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inti();
    }

    private void inti() {
        LayoutInflater.from(getContext()).inflate(R.layout.stantard_info, this);
        for (int i = 0; i < editTexts.length; i++)
            editTexts[i] = findViewById(EditTextId[i]);
        radioGroup[0] = findViewById(R.id.male);
        radioGroup[1] = findViewById(R.id.female);
        spinner = findViewById(R.id.stat);

        b[1] = true;
        editTexts[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                b[0] = !editTexts[0].getText().toString().isEmpty();
                if (!b[0])
                    editTexts[0].setError("Field Required");
                button();
            }
        });
        editTexts[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                b[2] = s.toString().contains("@") && s.toString().contains(".");
                if (!b[2])
                    editTexts[2].setError("Invalid Email ID");
                button();
            }
        });
        editTexts[3].addTextChangedListener(new TextWatcher() {
            int i1 = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = editTexts[3].getText().toString().length();
                if (editTexts[3].getText().toString().isEmpty()) {
                    i1 = 0;
                    return;
                }
                if ((i - i1) % 3 == 0 && i1 <
                        (editTexts[3].getText().toString().contains("+") ? 3 : 2)) {
                    editTexts[3].append(" ");
                    i1++;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = editTexts[3].getText().toString().length();
                boolean t = (i != 7 && i1 == 1) || (i != 12 && i1 == 2) || (i != 16 && i1 == 3);
                b[3] = !t;
                if (!b[3]) editTexts[3].setError("Invalid Number");
                button();
            }
        });

        editTexts[4].addTextChangedListener(new TextWatcher() {
            int i1 = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = editTexts[4].getText().toString().length();
                if (editTexts[4].getText().toString().isEmpty()) {
                    i1 = 0;
                    return;
                }
                if ((i - i1) % 4 == 0) {
                    editTexts[4].append(" ");
                    i1++;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                b[4] = s.toString().matches("[0-9]{4} [0-9]{4} [0-9]{4}");
                if (!b[4])
                    editTexts[4].setError("Invalid Aadhaar No");
                button();
            }
        });
        editTexts[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                b[5] = editTexts[5].getText().toString().matches("^[A-Z,a-z]{5}\\d{4}[A-Z,a-z]");
                if (!b[5])
                    editTexts[5].setError("Invalid PAN No");
                button();
            }
        });
        editTexts[6].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = editTexts[6].getText().toString().length();
                b[6] = !(i < 11);
                if (!b[6])
                    editTexts[6].setError("Invalid Bank Account");
                button();
            }
        });

        radioGroup[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioGroup[0].toggle();
            }
        });

        radioGroup[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioGroup[1].toggle();
            }
        });
    }

    public String getInformation() {
        String t = StringHandler.STD + radioGroup[0].isChecked();
        for (EditText e : editTexts)
            t += StringHandler.P + e.getText().toString();
        return t + StringHandler.P + spinner.getSelectedItem().toString();
    }

    private void button() {
        boolean f = true;
        b[1] = !editTexts[1].getText().toString().isEmpty();
        for (boolean t : b) f = f && t;
        if (listener != null)
            listener.stateListener(f);
    }

    public stateChangeListener getListener() {
        return listener;
    }

    public void setListener(stateChangeListener listener) {
        this.listener = listener;
    }

    public interface stateChangeListener {
        void stateListener(boolean isEnable);
    }
}
