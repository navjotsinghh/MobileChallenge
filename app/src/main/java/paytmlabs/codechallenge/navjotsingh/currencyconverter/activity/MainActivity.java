package paytmlabs.codechallenge.navjotsingh.currencyconverter.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import paytmlabs.codechallenge.navjotsingh.currencyconverter.R;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.adapter.CurrencyConversionRateRecyclerViewAdapter;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.model.CurrencyRateModel;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.utils.CurrencyConverterConstants;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.utils.CurrencyConverterHelper;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.utils.CurrencyRateResponseListener;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, CurrencyRateResponseListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mEditText;

    private Spinner mSpinner;

    private RecyclerView mCurrencyRatesRecyclerView;

    private GridLayoutManager mGridLayoutManager;

    private CurrencyConversionRateRecyclerViewAdapter mCurrencyRecyclerViewAdapter;

    private CurrencyRateModel currencyRateObject;

    private Context mContext;

    private CurrencyConverterHelper helper;

    private ArrayAdapter spinnerArrayAdapter;

    private Timer timer;

    private String baseCurrencyCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIntentData(getIntent());
        initializeViews();

        // If currency rates not null set data to spinner
        if (currencyRateObject != null) {
            setSpinnerData(currencyRateObject.getAllCurrencyCodes());
            setRecyclerViewData(baseCurrencyCode);
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");

        // Else make API call again
        if (currencyRateObject == null) {
            getLatestCurrencyRates();
        }

        startTimer();
        super.onStart();
    }

    /**
     * Method to get the latest currency rates
     */
    private void getLatestCurrencyRates() {

        helper = new CurrencyConverterHelper(this, this);
        helper.callLatestCurrencyRateApi();
    }

    /**
     * Method to get intent data
     *
     * @param intent {@link Intent}
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            currencyRateObject = intent.getParcelableExtra(CurrencyConverterConstants.INTENT_EXTRA_CURRENCY_RATE_OBJECT);
        }
    }

    /**
     * Method to initialize UI views
     */
    private void initializeViews() {
        Log.d(TAG, "initializing views");

        mContext = this;

        // Initialize Edit TextView
        mEditText = (EditText) findViewById(R.id.currency_conversion_editText);
        // Restrict input type to number and decimal only
        mEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        mEditText.setText("1");
        // Set listener
        mEditText.addTextChangedListener(mEditTextWatcher);

        // Initialize spinner
        mSpinner = (Spinner) findViewById(R.id.currency_code_spinner);
        mSpinner.setOnItemSelectedListener(this);

        // Initialize recycler view
        mCurrencyRatesRecyclerView = (RecyclerView) findViewById(R.id.currency_rates_recyclerView);
    }

    /**
     * Method to start Timer to refresh currency rates after every 30 minutes
     */
    private void startTimer() {
        Log.d(TAG, "starting timer");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {

                // Fetch latest currency rates
                Log.d(TAG, "Timer executed");
                getLatestCurrencyRates();
            }
        }, CurrencyConverterConstants.DELAY_TIME, CurrencyConverterConstants.REFRESH_TIME);
    }

    /**
     * Method to handle the API response for latest currency rates
     *
     * @param currencyRateObj {@link CurrencyRateModel} : Response object
     */
    private void handleLatestCurrencyRateApiResponse(CurrencyRateModel currencyRateObj) {
        Log.d(TAG, "handling API response");

        if (currencyRateObj != null) {
            currencyRateObject = currencyRateObj;
            setSpinnerData(currencyRateObj.getAllCurrencyCodes());
            setRecyclerViewData(baseCurrencyCode);
        }
    }

    /**
     * Method to set data to spinner adapter
     *
     * @param currencyCodesArrayList {@link ArrayList} of Currency codes
     */
    private void setSpinnerData(ArrayList<String> currencyCodesArrayList) {
        Log.d(TAG, "setting spinner data");

        //Creating the ArrayAdapter instance having the country codes
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyCodesArrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        mSpinner.setAdapter(spinnerArrayAdapter);

        if (currencyCodesArrayList.size() > 0) {
            if (TextUtils.isEmpty(baseCurrencyCode)) {
                baseCurrencyCode = currencyCodesArrayList.get(0);
                mSpinner.setSelection(0);
            } else {
                mSpinner.setSelection(currencyCodesArrayList.indexOf(baseCurrencyCode));
            }
        }
    }

    /**
     * Method to set recycler view adapter and data to adapter
     *
     * @param selectedCurrencyCode {@link String} : Selected currency code
     */
    private void setRecyclerViewData(String selectedCurrencyCode) {
        Log.d(TAG, "setting recycler view data");

        if (currencyRateObject != null) {

            String inputValueText = mEditText.getText().toString();

            double inputValue = 1;

            if (!TextUtils.isEmpty(inputValueText))
                inputValue = Double.parseDouble(inputValueText);

            mCurrencyRecyclerViewAdapter = new CurrencyConversionRateRecyclerViewAdapter(
                    currencyRateObject, inputValue, selectedCurrencyCode);
            mGridLayoutManager = new GridLayoutManager(this, 3);
            mCurrencyRatesRecyclerView.setLayoutManager(mGridLayoutManager);
            mCurrencyRatesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mCurrencyRatesRecyclerView.setAdapter(mCurrencyRecyclerViewAdapter);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected");

        baseCurrencyCode = parent.getItemAtPosition(position).toString();

        if (!TextUtils.isEmpty(baseCurrencyCode)) {

            // Update the base currency
            if (helper != null) {
                helper.setBaseCurrencyCode(baseCurrencyCode);
            }
            mCurrencyRecyclerViewAdapter.updateBaseCurrency(baseCurrencyCode);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void postApiSuccessResponse(CurrencyRateModel currencyRateModel) {
        handleLatestCurrencyRateApiResponse(currencyRateModel);
    }

    @Override
    public void postApiFailureResponse(CurrencyRateModel currencyRateModel) {

    }

    private TextWatcher mEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "onTextChanged");

            if (mCurrencyRecyclerViewAdapter != null) {
                String txtMultiplicationFactor = mEditText.getText().toString();
                if (txtMultiplicationFactor.trim().length() > 0) {
                    try {
                        double multiplicationFactor = Double.parseDouble(txtMultiplicationFactor);
                        if (multiplicationFactor > 0) {
                            mCurrencyRecyclerViewAdapter.updateInputValue(multiplicationFactor);
                        } else {
                            mCurrencyRecyclerViewAdapter.updateInputValue(1);

                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    mCurrencyRecyclerViewAdapter.updateInputValue(1);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();

        // Stop timer before leaving the activity
        if (timer != null) {
            Log.d(TAG, "cancelling timer");
            timer.cancel();
            timer.purge();
        }
        if (helper != null) {
            helper = null;
        }
        if (currencyRateObject != null) {
            currencyRateObject = null;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}
