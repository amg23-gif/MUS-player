package com.musplayer.app;

  import android.app.Activity;
  import android.os.Bundle;
  import android.view.KeyEvent;
  import android.view.View;
  import android.webkit.WebChromeClient;
  import android.webkit.WebResourceRequest;
  import android.webkit.WebSettings;
  import android.webkit.WebView;
  import android.webkit.WebViewClient;

  public class MainActivity extends Activity {
      private WebView webView;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          getWindow().getDecorView().setSystemUiVisibility(
              View.SYSTEM_UI_FLAG_FULLSCREEN |
              View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
              View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          );

          setContentView(R.layout.activity_main);
          webView = findViewById(R.id.webview);

          WebSettings settings = webView.getSettings();
          settings.setJavaScriptEnabled(true);
          settings.setMediaPlaybackRequiresUserGesture(false);
          settings.setDomStorageEnabled(true);
          settings.setAllowFileAccess(true);
          settings.setAllowContentAccess(true);
          settings.setLoadWithOverviewMode(true);
          settings.setUseWideViewPort(true);
          settings.setBuiltInZoomControls(false);
          settings.setCacheMode(WebSettings.LOAD_DEFAULT);
          // Allow HTTP streams inside HTTPS pages (fixes mixed content blocking)
          settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
          settings.setUserAgentString(
              "Mozilla/5.0 (Linux; Android 12; MUSPlayer) AppleWebKit/537.36 " +
              "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
          );

          webView.setWebChromeClient(new WebChromeClient());
          webView.setWebViewClient(new WebViewClient() {
              @Override
              public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                  String url = request.getUrl().toString();
                  // Block intent:// URLs — they cause "webpage not available" error
                  if (url.startsWith("intent://") || url.startsWith("market://")) {
                      return true; // Block navigation, do nothing
                  }
                  return false; // Allow all other URLs
              }
          });

          webView.loadUrl("https://amg23-gif.github.io/MUS-player/");
      }

      @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
              webView.goBack();
              return true;
          }
          return super.onKeyDown(keyCode, event);
      }

      @Override
      protected void onPause() {
          super.onPause();
          webView.onPause();
      }

      @Override
      protected void onResume() {
          super.onResume();
          webView.onResume();
          // Re-apply immersive mode on resume
          getWindow().getDecorView().setSystemUiVisibility(
              View.SYSTEM_UI_FLAG_FULLSCREEN |
              View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
              View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          );
      }
  }
  