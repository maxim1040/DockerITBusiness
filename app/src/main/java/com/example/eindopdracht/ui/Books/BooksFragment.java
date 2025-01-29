package com.example.eindopdracht.ui.Books;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eindopdracht.BookAdapter;
import com.example.eindopdracht.R;
import com.example.eindopdracht.model.Book;
import com.example.eindopdracht.network.VolleySingleton;
import com.example.eindopdracht.ui.BookDetails.BookDetailsFragment;
import com.example.eindopdracht.ui.SavedBooks.SavedBooksViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class BooksFragment extends Fragment implements BookAdapter.OnSaveButtonClickListener, BookAdapter.OnBookItemClickListener {

    private static final String TAG = BooksFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private SearchView searchView;
    private SavedBooksViewModel savedBooksViewModel;

    @Override
    public void onSaveButtonClick(Book book) {
        savedBooksViewModel.saveBook(book);
    }

    @Override
    public void onBookItemClick(Book book) {
        showBookDetailsDialog(book);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBooks);
        bookAdapter = new BookAdapter(new ArrayList<>(), this, this);
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedBooksViewModel = new ViewModelProvider(this).get(SavedBooksViewModel.class);
        searchView = view.findViewById(R.id.searchView);
        setupSearchView();

        // Set the OnSaveButtonClickListener and OnBookItemClickListener
        bookAdapter.setOnSaveButtonClickListener(this);

        // Initially load some books (you may want to load popular or featured books)
        loadBooks("Android");

        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission, e.g., trigger API call
                searchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query changes, e.g., filter local data
                // You can choose to implement auto-suggestions or other behaviors here
                return true;
            }
        });
    }

    private void loadBooks(String query) {
        // Load initial set of books (you may want to load popular or featured books)
        searchBooks(query);
    }

    private void searchBooks(String query) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Book> books = parseBooksResponse(response);
                        bookAdapter.setBooks(books);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley request failed", error);
                    }
                });

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest);
    }

    private List<Book> parseBooksResponse(JSONObject response) {
        List<Book> books = new ArrayList<>();

        try {
            JSONArray items = response.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");

                String id = generateUniqueId();
                String title = volumeInfo.optString("title", "No Title");
                String author = "No Author";
                String description = volumeInfo.optString("description", "No Description");
                String imageUrl = "";

                if (volumeInfo.has("authors")) {
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    if (authorsArray.length() > 0) {
                        author = authorsArray.getString(0);
                    }
                }

                if (volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    imageUrl = imageLinks.optString("thumbnail", "");
                }

                String publisher = volumeInfo.optString("publisher", "No Publisher");
                String publishDate = volumeInfo.optString("publishedDate", "No Publish Date");
                String genre = "";  // Google Books API does not provide genre directly
                String rating = volumeInfo.optString("averageRating", "No Rating");
                String language = volumeInfo.optString("language", "No Language");

                Log.d(TAG, "Publisher: " + publisher);
                Log.d(TAG, "Publish Date: " + publishDate);
                Log.d(TAG, "Genre: " + genre);
                Log.d(TAG, "Rating: " + rating);
                Log.d(TAG, "Language: " + language);

                Book book = new Book(id, title, author, description, imageUrl, publisher, publishDate, genre, rating, language);
                book.setPublisher(publisher);
                book.setPublishDate(publishDate);
                book.setGenre(genre);
                book.setRating(rating);
                book.setLanguage(language);

                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return books;
    }

    private String generateUniqueId() {
        long timestamp = System.currentTimeMillis();
        UUID uuid = UUID.randomUUID();
        String randomPart = uuid.toString().replace("-", "");
        return timestamp + randomPart;
    }

    private void showBookDetailsDialog(Book book) {
        BookDetailsFragment detailsFragment = BookDetailsFragment.newInstance(book);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        detailsFragment.show(fragmentManager, BookDetailsFragment.TAG);
    }
}
