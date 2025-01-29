package com.example.eindopdracht.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.eindopdracht.model.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBook(Book book);

    @Delete
    void removeBook(Book book);

    @Query("SELECT * FROM books")
    LiveData<List<Book>> getAllBooks();
}
