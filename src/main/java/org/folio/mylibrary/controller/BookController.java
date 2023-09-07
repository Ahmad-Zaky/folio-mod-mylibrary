package org.folio.mylibrary.controller;

import org.folio.mylibrary.rest.resource.BooksApi;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.folio.mylibrary.domain.dto.BookCollection;
import org.folio.mylibrary.domain.dto.BookRequestResource;
import org.folio.mylibrary.domain.dto.BookResource;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.folio.mylibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import liquibase.pro.packaged.A;

@Log4j2
@RestController
@RequestMapping(value = "/")
public class BookController implements BooksApi {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public ResponseEntity<BookCollection> getBooks(
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId,    
        Integer offset,
        Integer limit,
        String sort,
        @Valid String cql
    ) {
        return ResponseEntity.ok(bookService.getBooks(offset, limit, sort, cql));
    }

    @Override
    public ResponseEntity<BookResource> createBook(
        BookRequestResource book,
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId
    ) {
        BookResource newBook = bookService.createBook(book); 

        URI bookUrl = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newBook.getId())
            .toUri();

        return ResponseEntity.created(bookUrl).body(newBook);
    }

    @Override
    public ResponseEntity<BookResource> getBook(
        String id,
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId
    ) {
        var book = bookService.getBook(id);

        return book == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(book);
    }

    @Override
    public ResponseEntity<Void> updateBook(
        String id,
        BookRequestResource bookResource,
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId
    ) {
        bookService.updateBook(id, bookResource);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteBook(
        String id,
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId
    ) {
        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }   
}
