package com.libcat.books.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import com.libcat.books.Book;

public final class BookManager {
    public static final BookManager instance = new BookManager();
    private final List<Book> mBooks = new ArrayList<>();

    private BookManager() {
    }

    public void addBook(Book book) {
        mBooks.add(book);
    }

    public void findByWriter(final String name, Consumer<Book> consumer) {
        asyncSearch(b -> {
            String iName = b.getWriter().toLowerCase();
            String lname = name.toLowerCase();
            return iName.contains(lname) || lname.contains(iName) || lname.equals(iName);
        }, consumer);
    }
    
    public void findByTitle(final String name, Consumer<Book> consumer) {
        asyncSearch(b -> {
            String iName = b.getTitle().toLowerCase();
            String lname = name.toLowerCase();
            return iName.contains(lname) || lname.contains(iName) || lname.equals(iName);
        }, consumer);
    }
    
    public void findById(long id, Consumer<Book> consumer) {
        asyncSearch(b -> b.getId() == id, consumer);
    }

    public void asyncSearch(Function<Book, Boolean> func, Consumer<Book> consumer) {
        int threadForN = 500;
        int size = mBooks.size();
        
        int start = 0;
        int end = threadForN;
        var pool = Executors.newCachedThreadPool();
        while(start < size) {  
            pool.execute(new Searcher(func, consumer, start, end));
            start += threadForN;
            end   += threadForN;
        }
        pool.shutdown();
    }

    private final class Searcher implements Runnable {
        private final Function<Book, Boolean> mFunction;
        private final Consumer<Book> mConsumer;
        private final int mStart, mEnd;

        public Searcher(Function<Book,Boolean> func, Consumer<Book> cons, int start, int end) {
            mFunction = func;
            mConsumer = cons;
            mStart    = start;
            mEnd      = end;
            System.out.println("Thread started, range: "+mStart+" to "+mEnd);
        }

        @Override
        public void run() {
            for(int i=mStart; i<mEnd; i++) {
                if(i >= mBooks.size())
                    return;
                Book book = mBooks.get(i);
                if(mFunction.apply(book))
                    mConsumer.accept(book);
            }
        }

        // end of Searcher
    }
}