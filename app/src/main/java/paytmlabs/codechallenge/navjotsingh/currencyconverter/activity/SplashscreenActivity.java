package paytmlabs.codechallenge.navjotsingh.currencyconverter.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import paytmlabs.codechallenge.navjotsingh.currencyconverter.R;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.model.CurrencyRateModel;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.utils.CurrencyConverterConstants;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.utils.CurrencyConverterHelper;
import paytmlabs.codechallenge.navjotsingh.currencyconverter.utils.CurrencyRateResponseListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashscreenActivity extends AppCompatActivity implements CurrencyRateResponseListener {

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        mContext = this;

        startAnimations();
        final CurrencyConverterHelper helper = new CurrencyConverterHelper(this, this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                helper.callLatestCurrencyRateApi();
            }
        }, 1500);
    }


    private void startAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.splash_view);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.clearAnimation();
        progressBar.startAnimation(anim);

    }


    @Override
    public void postApiSuccessResponse(CurrencyRateModel currencyRateModel) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(CurrencyConverterConstants.INTENT_EXTRA_CURRENCY_RATE_OBJECT, currencyRateModel);
        startActivity(intent);
        finish();
    }

    @Override
    public void postApiFailureResponse(CurrencyRateModel currencyRateModel) {

    }
}
