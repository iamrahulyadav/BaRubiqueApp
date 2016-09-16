package com.bestdealfinance.bdfpartner.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bestdealfinance.bdfpartner.R;
import com.bestdealfinance.bdfpartner.application.Constant;
import com.bestdealfinance.bdfpartner.application.Helper;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;

public class YoutubeActivity extends AppCompatActivity {

    private WebView engine;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube);


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.txt_loading)+"...");
        progressDialog.setTitle("0%");
        progressDialog.setCancelable(false);
        progressDialog.show();
        url = getIntent().getStringExtra("link");
        engine = (WebView) findViewById(R.id.youtube_webview);
        engine.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress!=100)
                {
                    progressDialog.setTitle(""+newProgress+"%");
                }
                else
                {
                    progressDialog.dismiss();
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        engine.getSettings().setJavaScriptEnabled(true);

        Tracker mTracker = Helper.getDefaultTracker(this);
        mTracker.setScreenName("Youtube Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        new FlurryAgent.Builder()
                .withLogEnabled(false)
                .build(this, Constant.FLURRY_API_KEY);

        Fabric.with(this, new Crashlytics());


    }

    @Override
    protected void onStart() {
        super.onStart();
        engine.loadUrl(url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        engine.loadUrl("");
    }
}
