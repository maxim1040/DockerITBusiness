package com.example.eindopdracht;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eindopdracht.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private boolean isSavedBooks;
    private List<Book> books;
    private OnSaveButtonClickListener onSaveButtonClickListener;
    private OnBookItemClickListener onBookItemClickListener;

    public BookAdapter(List<Book> books, OnSaveButtonClickListener onSaveButtonClickListener, OnBookItemClickListener onBookItemClickListener) {
        this.books = books;
        this.onSaveButtonClickListener = onSaveButtonClickListener;
        this.onBookItemClickListener = onBookItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        // Bind data to views
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewAuthor.setText(book.getAuthor());
        holder.textViewDescription.setText(book.getDescription());

        // Check if the imageUrl is not empty or null before loading with Picasso
        if (!TextUtils.isEmpty(book.getImageUrl())) {
            Picasso.get()
                    .load(book.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.imageViewCover);
        } else {
            // Handle the case where imageUrl is empty or null
            // You can set a placeholder or hide the ImageView as per your requirement
            holder.imageViewCover.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Set button text based on the saved status
        if (isSavedBooks) {
            holder.buttonSave.setText("Unsave");
        } else {
            holder.buttonSave.setText("Save");
        }

        holder.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle Save button click
                if (onSaveButtonClickListener != null) {
                    onSaveButtonClickListener.onSaveButtonClick(book);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle item click
                if (onBookItemClickListener != null) {
                    onBookItemClickListener.onBookItemClick(book);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public void setSavedBooks(boolean savedBooks) {
        this.isSavedBooks = savedBooks;
    }

    public void setOnSaveButtonClickListener(OnSaveButtonClickListener onSaveButtonClickListener) {
        this.onSaveButtonClickListener = onSaveButtonClickListener;
    }

    public void setOnBookItemClickListener(OnBookItemClickListener onBookItemClickListener) {
        this.onBookItemClickListener = onBookItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button buttonSave;
        TextView textViewTitle;
        TextView textViewAuthor;
        TextView textViewDescription;
        ImageView imageViewCover;

        ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
            buttonSave = itemView.findViewById(R.id.buttonSave);
        }
    }

    // Define the OnSaveButtonClickListener interface
    public interface OnSaveButtonClickListener {
        void onSaveButtonClick(Book book);
    }

    // Define the OnBookItemClickListener interface
    public interface OnBookItemClickListener {
        void onBookItemClick(Book book);
    }
}
