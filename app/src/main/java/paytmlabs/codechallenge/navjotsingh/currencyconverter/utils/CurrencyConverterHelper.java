package paytmlabs.codechallenge.navjotsingh.currencyconverter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import paytmlabs.codechallenge.navjotsingh.currencyconverter.R;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.api.ApiClient;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.api.ApiInterface;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.model.CurrencyRateModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Helper class for doing all the common stuff
 * <p>
 * Created by navjotsingh on 15/07/17.
 */

public class CurrencyConverterHelper implements Callback {

    private String baseCurrencyCode = "CAD";

    private Context mContext;

    private CurrencyRateResponseListener mCurrencyRateResponseListener;

    private SharedPreferences.Editor editor;

    private static final String TAG = CurrencyConverterHelper.class.getSimpleName();

    public CurrencyConverterHelper(Context mContext, CurrencyRateResponseListener currencyRateResponseListener) {
        this.mContext = mContext;
        this.mCurrencyRateResponseListener = currencyRateResponseListener;
    }

    /**
     * Method to set the base currency code
     *
     * @param currencyCode {@link String}
     */
    public void setBaseCurrencyCode(String currencyCode) {
        Log.d(TAG, "setting base currency code");
        baseCurrencyCode = currencyCode;

    }

    /**
     * Method to call API to fetch the latest currency rates
     */
    public void callLatestCurrencyRateApi() {
        Log.d(TAG, "API call");
        ApiClient apiClient = ApiClient.getInstance(mContext);
        ApiInterface apiService = apiClient.getClient().create(ApiInterface.class);
        Call<CurrencyRateModel> call;
        if (isNetworkAvailable(mContext)) {
            call = apiService.getCurrencyConversionRates(baseCurrencyCode);
        } else {
            call = apiService.getCurrencyConversionRatesFromCacheOnly(baseCurrencyCode);
        }
        call.enqueue(this);

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        if (response.isSuccessful()) {

            mCurrencyRateResponseListener.postApiSuccessResponse((CurrencyRateModel) response.body());

        } else {
            Toast.makeText(mContext, R.string.error_text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull Throwable t) {
        Toast.makeText(mContext, R.string.error_text, Toast.LENGTH_LONG).show();
    }

    /**
     * Method to check the network availability
     *
     * @param context {@link Context}
     * @return boolean
     */
    private static boolean isNetworkAvailable(Context context) {
        Log.d(TAG, "checking for network availability");
        if (context == null) return false;
        ConnectivityManager ConnectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ConnectMgr == null)
            return false;
        NetworkInfo NetInfo = ConnectMgr.getActiveNetworkInfo();
        if (NetInfo == null)
            return false;

        return NetInfo.isConnected();
    }

    /**
     * Method to format number
     *
     * @param number double
     * @return String
     */
    public static String formatNumber(double number) {
        NumberFormat formatter = new DecimalFormat("##,##,##,##,###.######");
        return formatter.format(number);
    }


}
