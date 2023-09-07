package org.folio.mylibrary.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.folio.mylibrary.domain.dto.BookCollection;
import org.folio.mylibrary.domain.dto.BookRequestResource;
import org.folio.mylibrary.domain.dto.BookResource;
import static org.folio.mylibrary.domain.dto.ErrorMessages.BOOK_WITH_ID_IS_NOT_FOUND;
import static org.folio.mylibrary.domain.dto.ErrorMessages.AUTHOR_WITH_ID_IS_NOT_FOUND;

import org.folio.mylibrary.domain.entity.Author;
import org.folio.mylibrary.domain.entity.Book;
import org.folio.mylibrary.exceptionn.RecordNotFoundException;
import org.folio.mylibrary.mapper.BookMapper;
import org.folio.mylibrary.repository.AuthorRepository;
import org.folio.mylibrary.repository.BookRepository;
import org.folio.mylibrary.util.StringUtil;
import org.folio.spring.data.OffsetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public BookCollection getBooks(Integer offset, Integer limit, String sort, String cql) {
        var sortDirection = sort.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.startsWith("-") ? sort.substring(1) : sort;
        Sort sortOrder = Sort.by(new Sort.Order(sortDirection, sortField));

        Pageable pageable = PageRequest.of(offset / limit, limit, sortOrder);
    
        boolean isBlank = isBlank(cql);
        Page<Book> books = isBlank 
            ? bookRepository.findAll(pageable)
            : bookRepository.findByCQL(cql, new OffsetRequest(offset, limit));

        return new BookCollection()
            .books(books.stream().map(BookMapper::mapEntityToDto).collect(Collectors.toList()))
            .totalRecords((int) books.getTotalElements());
    }

    @Override
    public BookResource createBook(BookRequestResource bookRequestResource) {
        Book book = BookMapper.mapRequestDtoToEntity(bookRequestResource);

        List<Author> authors = new ArrayList<>(new LinkedHashSet<>(bookRequestResource.getAuthors()))
            .stream()
            .map(
                (String id) -> authorRepository.findById(StringUtil.toUuidSafe(id)).orElseThrow(
                    () -> new RecordNotFoundException(String.format(AUTHOR_WITH_ID_IS_NOT_FOUND, id))
                )
            )
            .collect(Collectors.toList());
        
        book.addAuthors(authors);

        return BookMapper.mapEntityToDto(bookRepository.save(book));
    }

    @Override
    public BookResource getBook(String id) {
        UUID uuid = StringUtil.toUuidSafe(id);

        return bookRepository.findById(uuid).map(BookMapper::mapEntityToDto).orElse(null);
    }

    @Override
    public void updateBook(String id, BookRequestResource bookRequestResource) {
        UUID uuid = StringUtil.toUuidSafe(id);
        if (! bookRepository.existsById(uuid)) {
            throw new RecordNotFoundException(String.format(BOOK_WITH_ID_IS_NOT_FOUND, id));
        }
        
        Book updatedBook = BookMapper.mapRequestDtoToEntity(bookRequestResource);
        updatedBook.setId(uuid);

        List<Author> authors = new ArrayList<>(new LinkedHashSet<>(bookRequestResource.getAuthors()))
            .stream()
            .map(
                (String authorId) -> authorRepository.findById(StringUtil.toUuidSafe(authorId)).orElseThrow(
                    () -> new RecordNotFoundException(String.format(AUTHOR_WITH_ID_IS_NOT_FOUND, authorId))
                )
            )
            .collect(Collectors.toList());
        
        updatedBook.addAuthors(authors);

        bookRepository.save(updatedBook);
    }

    @Override
    public void deleteBook(String id) {
        UUID uuid = StringUtil.toUuidSafe(id);
        if (! bookRepository.existsById(uuid)) {
            throw new RecordNotFoundException(String.format(BOOK_WITH_ID_IS_NOT_FOUND, id));
        }

        bookRepository.deleteById(uuid);
    }

}
