package com.example.webviewtest1;

import android.net.Uri;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class JSBridge {
    final static String name = "jsbridge";
    final String tag = "JSBridge";
    final String scheme = "jsbtest";
    final String authority = "jstonative";
    final String param = "msg";
    final String prompt_head = "jsbtestprompt://";
    final String console_head = "jsbtestconsole://";
    final TextView textView;

    public JSBridge(TextView view) {
        this.textView = view;
    }

    @JavascriptInterface
    public String messageToNative(final String msg) {
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(msg);
            }
        });
        return "messageToNative success.";
    }

    public WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                Log.d(tag, "shouldOverrideUrlLoading: " + uri.toString());
                if (scheme.equals(uri.getScheme()) && authority.equals(uri.getAuthority())) {
                    final String val = uri.getQueryParameter(param);
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(val);
                        }
                    });
                    return true;
                }
                return false;
            }
        };
    }

    public WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.d(tag, "onJsPrompt: " + message);
                if (message != null && message.startsWith(prompt_head)) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(message.substring(prompt_head.length()));
                        }
                    });
                    result.confirm("jsb using prompt success!");
                    return true;
                }
                return false;
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                final String message = consoleMessage.message();
                Log.d(tag, "onConsoleMessage: " + message);
                if (message != null && message.startsWith(console_head)) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(message.substring(console_head.length()));
                        }
                    });
                    return true;
                }
                return false;
            }
        };
    }
}
