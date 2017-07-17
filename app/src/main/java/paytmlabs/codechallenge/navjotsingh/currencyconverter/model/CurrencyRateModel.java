package paytmlabs.codechallenge.navjotsingh.currencyconverter.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Model for currency codes and corresponding conversion rates with respect to base currency
 * <p>
 * Created by navjotsingh on 15/07/17.
 */

public class CurrencyRateModel implements Parcelable {

    /**
     * Base currency code
     */
    @SerializedName("base")
    private String base;

    /**
     * Date of the currency conversion rates
     */
    @SerializedName("date")
    private String date;

    /**
     * Map of currency code as key and conversion rate as value
     */
    @SerializedName("rates")
    private HashMap<String, Double> rates;

    /**
     * Method to get the base currency code corresponding to which conversion rates are fetched
     *
     * @return String : base currency code
     */
    public String getBase() {
        return base;
    }

    /**
     * Method to get the date on which the currency conversion rates are fetched
     *
     * @return String : date
     */
    public String getDate() {
        return date;
    }

    /**
     * Method to get the map of currency code as key and conversion rate as value
     *
     * @return HashMap : HashMap of Currency code as key and conversion rate as value
     */
    public HashMap<String, Double> getRates() {
        return rates;
    }

    /**
     * Method to get the map of all currency codes and conversion rate
     *
     * @return HashMap : HashMap of Currency code as key and conversion rate as value
     */
    private HashMap<String, Double> getCurrencyRateMap() {
        HashMap<String, Double> map = new HashMap<>(rates);
        map.put(base, 1d);
        return map;
    }

    /**
     * Method to return Currency Codes including Base Currency Code
     *
     * @return ArrayList<String> : ArrayList of currency codes
     */
    public ArrayList<String> getAllCurrencyCodes() {

        if (rates != null && rates.size() > 0) {

            // Fetch currency codes array list
            ArrayList<String> currencyCodeList = new ArrayList<>(rates.keySet());

            // Add base currency to array list of currency codes
            currencyCodeList.add(base);
            Collections.sort(currencyCodeList);
            return currencyCodeList;
        }
        return new ArrayList<>();
    }

    /**
     * Method to return Currency Codes for currency conversion excluding the currency passed in params
     *
     * @param fromCurrencyCode : Return all currency codes except this
     * @return ArrayList<String> : ArrayList of currency codes
     */
    public ArrayList<String> getConversionCurrencyCodes(String fromCurrencyCode) {

        if (rates != null && rates.size() > 0) {
            // Fetch all currency codes array list
            ArrayList<String> currencyCodeList = getAllCurrencyCodes();

            // Remove fromCurrencyCode to get only the conversion currency codes
            if (currencyCodeList.contains(fromCurrencyCode)) {
                currencyCodeList.remove(fromCurrencyCode);
                return currencyCodeList;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Method to get the currency conversion rate with respect to base currency
     *
     * @param currencyCode {@link String} : Currency code for which conversion rate is required
     * @return Double : Currency conversion rate
     */
    private Double getCurrencyConversionRate(String currencyCode) {
        HashMap<String, Double> rateMap = getCurrencyRateMap();
        if (rateMap != null && rateMap.size() > 0 && rateMap.containsKey(currencyCode)) {
            return rateMap.get(currencyCode);
        }
        return 0d;
    }

    /**
     * Method to get the currency conversion rate from currency code passed in first param
     * to currency code passed in second param
     *
     * @param fromCurrencyCode {@link String} : Return conversion rate considering this currency
     *                         code as base
     * @param toCurrencyCode   {@link String} : Return conversion rate in this currency code
     * @return Double
     */
    public Double getCurrencyConversionRate(String fromCurrencyCode, String toCurrencyCode) {
        Double toCurrencyConversionRate = getCurrencyConversionRate(toCurrencyCode);
        if (toCurrencyConversionRate > 0) {
            return getCurrencyConversionRate(toCurrencyCode) / getCurrencyConversionRate(fromCurrencyCode);
        }
        return 0d;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(base);
        dest.writeString(date);

        // Write map to the parcel
        dest.writeInt(rates.size());
        for (Map.Entry<String, Double> entry : rates.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeDouble(entry.getValue());
        }
    }

    private CurrencyRateModel(Parcel in) {
        base = in.readString();
        date = in.readString();

        // Read map from the parcel
        rates = new HashMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            Double value = in.readDouble();
            rates.put(key, value);
        }
    }

    public static final Creator<CurrencyRateModel> CREATOR = new Creator<CurrencyRateModel>() {
        @Override
        public CurrencyRateModel createFromParcel(Parcel in) {
            return new CurrencyRateModel(in);
        }

        @Override
        public CurrencyRateModel[] newArray(int size) {
            return new CurrencyRateModel[size];
        }
    };
}