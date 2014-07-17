package com.brandsfever;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.brandsfever.luxury.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;



public class TwitterLogin extends Activity {
    public static final String TAG = TwitterLogin.class.getSimpleName();

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.twitter_login);

        WebView webView = (WebView) findViewById(R.id.twitterlogin);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean result = true;
                if (url != null && url.startsWith(Const.CALLBACK_URL)) {
                    Uri uri = Uri.parse(url);
                    if (uri.getQueryParameter("denied") != null) {
                        setResult(RESULT_CANCELED);
                        finish();
                    } else {
                        String oauthToken = uri.getQueryParameter("oauth_token");
                        String oauthVerifier = uri.getQueryParameter("oauth_verifier");

                        Intent intent = getIntent();
                        intent.putExtra(Const.IEXTRA_OAUTH_TOKEN, oauthToken);
                        intent.putExtra(Const.IEXTRA_OAUTH_VERIFIER, oauthVerifier);

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    result = super.shouldOverrideUrlLoading(view, url);
                }
                return result;
            }
        });
        webView.loadUrl(this.getIntent().getExtras().getString("auth_url"));
    }

	@Override
		public void onStart(){
			super.onStart();
			
			EasyTracker tracker = EasyTracker.getInstance(this);
			tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": twitter/?device=2");
			tracker.send(MapBuilder.createAppView().build());
		}
    
    
}
