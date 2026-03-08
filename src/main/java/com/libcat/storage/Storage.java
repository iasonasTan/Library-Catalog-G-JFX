package com.libcat.storage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.libcat.books.Book;
import com.libcat.books.manager.BookManager;

public final class Storage {
    private static Storage instance;

    public static void init(String path) {
        if(instance!=null)
            throw new IllegalStateException("Already initialized.");
        instance = new Storage(path);
    }

    public static Storage getInstance() {
        if(instance==null)
            throw new IllegalStateException("Not initizlized yet.");
        return instance;
    }

    private final Path mPath;

    private Storage(String path) {
        mPath = Paths.get(path);
    }

    public void loadBooks(BookManager manager) {
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(mPath)) {
            for(Path path: dirStream) {
                Book book = constructBook(path);
                manager.addBook(book);
            }
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private Book constructBook(Path path) throws IOException {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream( path.toFile() ))
        );
        String title = reader.readLine();
        String writer= reader.readLine();
        int id = Integer.parseInt(reader.readLine());
        int pagesCount = Integer.parseInt(reader.readLine());
        return new Book(title, writer, pagesCount, id);
    }
}