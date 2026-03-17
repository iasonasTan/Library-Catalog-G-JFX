package com.libcat.storage;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;

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
                Optional<Book> bookOpt = constructBook(path);
                bookOpt.ifPresent(b -> {
                    manager.addBook(b);
                });
            }
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private Optional<Book> constructBook(Path path) throws IOException {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(path.toFile()))
        ) {
            @Override
            public String readLine() throws IOException {
                String line = super.readLine();
                if(line == null)
                    throw new IllegalStateException("No more lines.");
                return line;
            }
        };
        try {
            String title = reader.readLine();
            String writer= reader.readLine();
            String id    = reader.readLine();
            int pagesCount = Integer.parseInt(reader.readLine());
            return Optional.of(new Book(title, writer, pagesCount, id));
        } catch(IllegalStateException ise) {
            return Optional.empty();
        }
    }
}
