package com.example.webviewtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;

import com.example.webviewtest1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    final static String tag = "MainActivity";
    ActivityMainBinding binding;
    JSBridge jsBridge;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        jsBridge = new JSBridge(binding.title);

        final WebSettings settings = binding.webpage.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //settings.setUseWideViewPort(true);
        //settings.setLoadWithOverviewMode(true);
        binding.webpage.setWebViewClient(jsBridge.getWebViewClient());
        binding.webpage.setWebChromeClient(jsBridge.getWebChromeClient());
        binding.webpage.addJavascriptInterface(jsBridge, JSBridge.name);
        binding.webpage.loadUrl("file:///android_asset/online_qa_web.html");

        binding.sendButton.setOnClickListener(v -> {
            v.post(new Runnable() {
                @Override
                public void run() {
                    final String script = String.format("window.changeTitle('Message from native: %s')", binding.input.getText().toString());
                    Log.d(tag, "sending java script: " + script);
                    binding.webpage.evaluateJavascript(script, value -> {
                        binding.title.setText(value);
                    });
                }
            });
        });
    }
}