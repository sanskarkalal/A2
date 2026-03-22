package com.sanskar.a2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

public class PlacesListFragment extends ListFragment {

    private PlacesViewModel mModel;
    private String[] mNames;
    private String[] mUrls;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get data passed in via Bundle
        Bundle args = getArguments();
        if (args != null) {
            mNames = args.getStringArray("names");
            mUrls = args.getStringArray("urls");
        }

        // Set up the list adapter
        setListAdapter(new ArrayAdapter<>(requireActivity(),
                R.layout.fragment_list_item, R.id.list_item_text, mNames));

        // Single selection mode
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Get the shared ViewModel
        mModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);

        // Restore previously selected item on rotation
        mModel.getSelectedIndex().observe(getViewLifecycleOwner(), index -> {
            if (index != null && index >= 0) {
                getListView().setItemChecked(index, true);
            }
        });
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int pos, long id) {
        // Highlight selected item
        getListView().setItemChecked(pos, true);
        // Update ViewModel with selected index and URL
        mModel.selectPlace(pos, mUrls[pos]);
    }
}