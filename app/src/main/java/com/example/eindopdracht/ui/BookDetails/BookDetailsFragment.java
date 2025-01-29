package com.example.eindopdracht.ui.BookDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.eindopdracht.R;
import com.example.eindopdracht.model.Book;

public class BookDetailsFragment extends DialogFragment {

    private static final String ARG_BOOK = "arg_book";
    public static final String TAG = "BookDetailsFragment";
    private ImageView imageViewCover;
    private TextView textViewTitle, textViewAuthor, textViewPublisher,
            textViewPublishDate, textViewGenre, textViewRating, textViewLanguage, textViewDescription;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        imageViewCover = view.findViewById(R.id.imageViewCover);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewAuthor = view.findViewById(R.id.textViewAuthor);
        textViewPublisher = view.findViewById(R.id.textViewPublisher);
        textViewPublishDate = view.findViewById(R.id.textViewPublishDate);
        textViewGenre = view.findViewById(R.id.textViewGenre);
        textViewRating = view.findViewById(R.id.textViewRating);
        textViewLanguage = view.findViewById(R.id.textViewLanguage);
        textViewDescription = view.findViewById(R.id.textViewDescription);

        // Find the "Close" button and set an OnClickListener
        Button closeButton = view.findViewById(R.id.buttonClose);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the close button is clicked
                dismiss();
            }
        });

        // Get book details from arguments
        Bundle args = getArguments();
        if (args != null) {
            Book book = args.getParcelable(ARG_BOOK);

            // Set data to views
            if (book != null) {
                Glide.with(requireContext())
                        .load(book.getImageUrl())
                        .into(imageViewCover);

                textViewTitle.setText("Title: " + book.getTitle());
                textViewAuthor.setText("Author: " + book.getAuthor());
                textViewPublisher.setText("Publisher: " + book.getPublisher());
                textViewPublishDate.setText("Publish Date: " + book.getPublishDate());
                textViewGenre.setText("Genre: " + book.getGenre());
                textViewRating.setText("Rating: " + book.getRating());
                textViewLanguage.setText("Language: " + book.getLanguage());
                textViewDescription.setText("Description: " + book.getDescription());
            }
        }
    }

    // Ensure the fragment is shown as a dialog
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }
}
