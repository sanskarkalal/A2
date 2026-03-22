package com.sanskar.a2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class WebViewFragment extends Fragment {

    private WebView mWebView;
    private PlacesViewModel mModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWebView = view.findViewById(R.id.webView);

        // Keep links opening inside the WebView instead of browser
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        // Get the shared ViewModel and observe URL changes
        mModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        mModel.getSelectedUrl().observe(getViewLifecycleOwner(), url -> {
            if (url != null && !url.isEmpty()) {
                mWebView.loadUrl(url);
            }
        });
    }
}