package com.musplayer.app;

  import android.app.Activity;
  import android.os.Build;
  import android.os.Bundle;
  import android.view.KeyEvent;
  import android.view.View;
  import android.view.WindowInsets;
  import android.view.WindowInsetsController;
  import android.view.WindowManager;
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
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
          applyImmersiveMode();
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
                  if (url.startsWith("intent://") || url.startsWith("market://")) return true;
                  return false;
              }
          });
          webView.loadUrl("https://amg23-gif.github.io/MUS-player/");
      }

      private void applyImmersiveMode() {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
              WindowInsetsController controller = getWindow().getInsetsController();
              if (controller != null) {
                  controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                  controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
              }
          } else {
              //noinspection deprecation
              getWindow().getDecorView().setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
              );
          }
      }

      @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_BACK && webView != null && webView.canGoBack()) {
              webView.goBack(); return true;
          }
          return super.onKeyDown(keyCode, event);
      }

      @Override protected void onPause() { super.onPause(); if (webView != null) webView.onPause(); }

      @Override
      protected void onResume() {
          super.onResume();
          if (webView != null) webView.onResume();
          applyImmersiveMode();
      }

      @Override
      protected void onDestroy() {
          if (webView != null) {
              webView.stopLoading();
              webView.setWebChromeClient(null);
              webView.setWebViewClient(null);
              webView.destroy();
              webView = null;
          }
          super.onDestroy();
      }
  }