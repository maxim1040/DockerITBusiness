package com.example.eindopdracht.ui.SavedBooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eindopdracht.BookAdapter;
import com.example.eindopdracht.R;
import com.example.eindopdracht.databinding.FragmentSavedbooksBinding;
import com.example.eindopdracht.model.Book;
import com.example.eindopdracht.ui.BookDetails.BookDetailsFragment;


import java.util.ArrayList;
import java.util.List;

public class SavedBooksFragment extends Fragment implements BookAdapter.OnSaveButtonClickListener, BookAdapter.OnBookItemClickListener {

    private FragmentSavedbooksBinding binding;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private SavedBooksViewModel savedBooksViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedBooksViewModel = new ViewModelProvider(this).get(SavedBooksViewModel.class);

        binding = FragmentSavedbooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerViewSavedBooks);
        bookAdapter = new BookAdapter(new ArrayList<>(), this, this);
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        savedBooksViewModel.getSavedBooks().observe(getViewLifecycleOwner(), savedBooks -> {
            // Update RecyclerView adapter with the new list of saved books
            bookAdapter.setBooks(savedBooks);
            // Update the saved state in the adapter
            bookAdapter.setSavedBooks(true); // or false based on your logic
            bookAdapter.notifyDataSetChanged(); // Notify the adapter that the dataset has changed
        });

        bookAdapter.setOnSaveButtonClickListener(SavedBooksFragment.this);

        return root;
    }

    @Override
    public void onSaveButtonClick(Book book) {
        savedBooksViewModel.removeBook(book); // Update ViewModel method to remove a book
    }
    @Override
    public void onBookItemClick(Book book) {
        // Open BookDetailsFragment as a DialogFragment when a book is clicked
        BookDetailsFragment detailsFragment = BookDetailsFragment.newInstance(book);
        detailsFragment.show(getParentFragmentManager(), BookDetailsFragment.TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
