package com.sanskar.a2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class RestaurantsActivity extends AppCompatActivity {

    private FrameLayout mListContainer;
    private FrameLayout mWebViewContainer;

    private final WebViewFragment mWebViewFragment = new WebViewFragment();
    private FragmentManager mFragmentManager;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        setSupportActionBar(findViewById(R.id.toolbar));

        mListContainer = findViewById(R.id.list_fragment_container);
        mWebViewContainer = findViewById(R.id.webview_fragment_container);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            PlacesListFragment listFragment = new PlacesListFragment();
            Bundle args = new Bundle();
            args.putStringArray("names", getResources().getStringArray(R.array.restaurants_names));
            args.putStringArray("urls", getResources().getStringArray(R.array.restaurants_urls));
            listFragment.setArguments(args);

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.list_fragment_container, listFragment);
            ft.commit();
        }

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

        mFragmentManager.addOnBackStackChangedListener(this::setLayout);
        setLayout();
    }

    private void setLayout() {
        if (!mWebViewFragment.isAdded()) {
            mListContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            mWebViewContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    0, MATCH_PARENT));
        } else {
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
            Intent i = new Intent(this, AttractionsActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.menu_restaurants) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}