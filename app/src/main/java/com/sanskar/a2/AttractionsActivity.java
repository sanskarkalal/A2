package com.sanskar.a2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class AttractionsActivity extends AppCompatActivity {

    private static final String ATTRACTIONS_INTENT = "com.sanskar.a1.SHOW_ATTRACTIONS";
    private static final String RESTAURANTS_INTENT = "com.sanskar.a1.SHOW_RESTAURANTS";

    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    private FrameLayout mListContainer;
    private FrameLayout mWebViewContainer;

    private final WebViewFragment mWebViewFragment = new WebViewFragment();
    private FragmentManager mFragmentManager;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        mListContainer = findViewById(R.id.list_fragment_container);
        mWebViewContainer = findViewById(R.id.webview_fragment_container);

        mFragmentManager = getSupportFragmentManager();

        // Set up list fragment with attractions data
        if (savedInstanceState == null) {
            PlacesListFragment listFragment = new PlacesListFragment();
            Bundle args = new Bundle();
            args.putStringArray("names", getResources().getStringArray(R.array.attractions_names));
            args.putStringArray("urls", getResources().getStringArray(R.array.attractions_urls));
            listFragment.setArguments(args);

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.list_fragment_container, listFragment);
            ft.commit();
        }

        // Observe ViewModel to add WebView fragment when item selected
        PlacesViewModel model = new ViewModelProvider(this).get(PlacesViewModel.class);
        model.getSelectedUrl().observe(this, url -> {
            if (url != null && !url.isEmpty() && !mWebViewFragment.isAdded()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.add(R.id.webview_fragment_container, mWebViewFragment);
                ft.addToBackStack(null);
                ft.commit();
                mFragmentManager.executePendingTransactions();
            }
            setLayout();
        });

        // Reset layout when back is pressed
        mFragmentManager.addOnBackStackChangedListener(this::setLayout);

        setLayout();

        // Register broadcast receiver programmatically
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) return;
                if (action.equals(RESTAURANTS_INTENT)) {
                    Intent i = new Intent(AttractionsActivity.this, RestaurantsActivity.class);
                    startActivity(i);
                }
                // If ATTRACTIONS_INTENT, we are already here, do nothing
            }
        };

        mFilter = new IntentFilter();
        mFilter.addAction(ATTRACTIONS_INTENT);
        mFilter.addAction(RESTAURANTS_INTENT);

        registerReceiver(mReceiver, mFilter, Context.RECEIVER_EXPORTED);
    }

    private void setLayout() {
        if (!mWebViewFragment.isAdded()) {
            // List takes full width
            mListContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            mWebViewContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    0, MATCH_PARENT));
        } else {
            // List takes 1/3, WebView takes 2/3
            mListContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    0, MATCH_PARENT, 1f));
            mWebViewContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    0, MATCH_PARENT, 2f));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_attractions) {
            // Already here, do nothing
            return true;
        } else if (id == R.id.menu_restaurants) {
            Intent i = new Intent(this, RestaurantsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}