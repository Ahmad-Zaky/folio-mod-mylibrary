package org.folio.mylibrary.service;

import java.util.List;

import org.folio.mylibrary.domain.dto.BookCollection;
import org.folio.mylibrary.domain.dto.BookRequestResource;
import org.folio.mylibrary.domain.dto.BookResource;

public interface BookService {

    BookCollection getBooks(Integer offset, Integer limit, String sort, String cql);

    BookResource createBook(BookRequestResource bookResource);

    BookResource getBook(String id);

    void updateBook(String id, BookRequestResource bookResource);

    void deleteBook(String id);
    
}
