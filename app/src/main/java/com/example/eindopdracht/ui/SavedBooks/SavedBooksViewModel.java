package com.example.eindopdracht.ui.SavedBooks;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.eindopdracht.Dao.BookDao;
import com.example.eindopdracht.database.AppDatabase;
import com.example.eindopdracht.model.Book;
import java.util.List;

public class SavedBooksViewModel extends AndroidViewModel {
    private LiveData<List<Book>> savedBooks;
    private BookDao bookDao;

    public SavedBooksViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(application);
        bookDao = database.bookDao();
        savedBooks = bookDao.getAllBooks();
    }

    public LiveData<List<Book>> getSavedBooks() {
        return savedBooks;
    }

    public void saveBook(Book book) {
        // Execute database operation in a background thread if needed
        new InsertBookAsyncTask(bookDao).execute(book);
    }

    public void removeBook(Book book) {
        // Execute database operation in a background thread if needed
        new RemoveBookAsyncTask(bookDao).execute(book);
    }

    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        private InsertBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.insertBook(books[0]);
            return null;
        }
    }

    private static class RemoveBookAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        private RemoveBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.removeBook(books[0]);
            return null;
        }
    }
}
