package paytmlabs.codechallenge.navjotsingh.currencyconverter.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import paytmlabs.codechallenge.navjotsingh.currencyconverter.R;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.model.CurrencyRateModel;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.utils.CurrencyConverterHelper;

/**
 * Recycler view adapter for currency code and conversion rates
 * <p>
 * Created by navjotsingh on 15/07/17.
 */

public class CurrencyConversionRateRecyclerViewAdapter
        extends RecyclerView.Adapter<CurrencyConversionRateRecyclerViewAdapter.CurrencyViewHolder> {
    private CurrencyRateModel currencyRateModel;
    private double inputValue;
    private String baseCurrencyCode;
    private ArrayList<String> conversionCurrencyCodeList;

    public CurrencyConversionRateRecyclerViewAdapter(CurrencyRateModel currencyRateModel,
                                                     double inputValue,
                                                     String baseCurrencyCode) {
        this.currencyRateModel = currencyRateModel;
        this.inputValue = inputValue;
        this.baseCurrencyCode = baseCurrencyCode;
        this.conversionCurrencyCodeList = currencyRateModel.getConversionCurrencyCodes(baseCurrencyCode);

        // Sort the currency codes name wise
        Collections.sort(this.conversionCurrencyCodeList);
    }

    @Override
    public CurrencyConversionRateRecyclerViewAdapter.CurrencyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_rates_recycler_view_item_layout, parent, false);

        return new CurrencyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        String currencyCodeLabel = conversionCurrencyCodeList.get(position);

        // Get the conversion rate and multiply by input value and format the result
        String currencyCodeRate = CurrencyConverterHelper.formatNumber(inputValue
                * currencyRateModel.getCurrencyConversionRate(baseCurrencyCode, currencyCodeLabel));

        // Set text to views
        holder.conversionRateTextView.setText(currencyCodeRate);
        holder.currencyLabelTextView.setText(currencyCodeLabel);
    }

    @Override
    public int getItemCount() {
        return currencyRateModel.getRates().size();
    }

     class CurrencyViewHolder extends ViewHolder {
        TextView conversionRateTextView;
        TextView currencyLabelTextView;

         CurrencyViewHolder(View itemView) {
            super(itemView);
            conversionRateTextView = (TextView) itemView.findViewById(R.id.rate_textView);
            currencyLabelTextView = (TextView) itemView.findViewById(R.id.currency_textView);

        }
    }

    /**
     * Method to update the input value
     *
     * @param inputValue double
     */
    public void updateInputValue(double inputValue) {
        this.inputValue = inputValue;
        notifyDataSetChanged();
    }

    public void updateBaseCurrency(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.conversionCurrencyCodeList = currencyRateModel.getConversionCurrencyCodes(baseCurrencyCode);
        notifyDataSetChanged();
    }
}
