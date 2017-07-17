package paytmlabs.codechallenge.navjotsingh.currencyconverter.utils;

import paytmlabs.codechallenge.navjotsingh.currencyconverter.model.CurrencyRateModel;

/**
 * Implement this to get callback methods for Currency Rate API Response
 * <p>
 * Created by navjotsingh on 15/07/17.
 */

public interface CurrencyRateResponseListener {
    void postApiSuccessResponse(CurrencyRateModel currencyRateModel);

    void postApiFailureResponse(CurrencyRateModel currencyRateModel);
}
