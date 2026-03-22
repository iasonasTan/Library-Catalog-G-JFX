package com.libcat.storage;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;

import lib.gui.UI;
import com.libcat.books.Book;
import com.libcat.books.manager.BookManager;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import lib.*;

public final class Storage {
    private static Storage instance;

    public static void init(BookManager manager) {
        if(instance!=null)
            throw new AlreadyInitializedException();
        instance = new Storage(manager);
    }

    public static Storage getInstance() {
        if(instance==null)
            throw new NotInitializedException("Not initizlized yet.");
        return instance;
    }

    private final BookManager mBookManager;

    private Storage(BookManager manager) {
        mBookManager = manager;
    }

    public void loadBooksFromXlxs(String filePath) {
        try(FileInputStream fis=new FileInputStream(new File(filePath))) {
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            for(Row row: sheet) {
                String title = row.getCell(0).getStringCellValue();
                String writer = row.getCell(1).getStringCellValue();
                int pagesCount = (int)row.getCell(2).getNumericCellValue();
                String id = row.getCell(3).getStringCellValue();
                Book book = new Book(title, writer, pagesCount, id);
                mBookManager.addBook(book);
            }
        } catch (IOException ioe) {
            UI.showException(ioe);
        }
    }

    public void loadBooksFromFiles(String filePath) {
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(filePath))) {
            for(Path path: dirStream) {
                Optional<Book> bookOpt = constructBook(path);
                bookOpt.ifPresent(b -> {
                    mBookManager.addBook(b);
                });
            }
        } catch (IOException ioe) {
            UI.showException(ioe);
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
            String title  = reader.readLine();
            String writer = reader.readLine();
            String id     = reader.readLine();
            int pagesCount= Integer.parseInt(reader.readLine());
            return Optional.of(new Book(title, writer, pagesCount, id));
        } catch(IllegalStateException ise) {
            return Optional.empty();
        }
    }
}
