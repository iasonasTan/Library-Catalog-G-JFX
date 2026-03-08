package com.libcat.books;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class Book {
	private final String mTitle;
    private final String mWriter;
    private final int mPages;
    private final long mId;

    public Book(String title, long id) {
        this(title, "unknown", 1, id);
    }

    public Book(String title, String writer, int pagesCount, long id) {
        mTitle = title;
        mWriter = writer;
        mPages = pagesCount;
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }
    
    public String getWriter() {
        return mWriter;
    }
    
    public int getPagesCount() {
        return mPages;
    }
    
    public long getId() {
        return mId;
    }

    @Override
    public String toString() {
        return String.format(
            "Title: %s, Writer: %s, Id: %d", mTitle, mWriter, mId 
        );
    }

	public Node toGuiNode() {
		Node node = new Label(toString());
		return node;
	}
}