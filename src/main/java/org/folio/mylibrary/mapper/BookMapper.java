package org.folio.mylibrary.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.folio.mylibrary.domain.dto.AuthorCollection;
import org.folio.mylibrary.domain.dto.AuthorResource;
import org.folio.mylibrary.domain.dto.BookCollection;
import org.folio.mylibrary.domain.dto.BookRequestResource;
import org.folio.mylibrary.domain.dto.BookResource;

import org.folio.mylibrary.domain.entity.Author;
import org.folio.mylibrary.domain.entity.Book;
import org.folio.mylibrary.exceptionn.RecordNotFoundException;
import org.folio.mylibrary.repository.AuthorRepository;
import org.folio.mylibrary.util.StringUtil;
import org.mapstruct.Mapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookMapper {

    public static BookResource mapEntityToDto(Book book) {
        AuthorCollection authors = new AuthorCollection()
            .authors(
                book.getAuthors()
                    .stream()
                    .map(AuthorMapper::mapEntityToDto)
                    .collect(Collectors.toList())
            );

        return new BookResource()
            .id(StringUtil.uuidToStringSafe(book.getId()))
            .title(book.getTitle())
            .authors(authors);
    }

    public static Book mapDtoToEntity(BookResource bookResource) {
        List<Author> authors = bookResource
            .getAuthors()
            .getAuthors()
            .stream()
            .map(AuthorMapper::mapDtoToEntity)
            .collect(Collectors.toList());

        return Book.builder()
            .id(StringUtil.toUuidSafe(bookResource.getId()))
            .title(bookResource.getTitle())
            .authors(authors)
            .build();
    }

    public static Book mapRequestDtoToEntity(BookRequestResource bookRequestResource) {
        return Book.builder()
            .title(bookRequestResource.getTitle())
            .build();
    }
}
