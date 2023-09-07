package org.folio.mylibrary.controller;

import org.folio.mylibrary.rest.resource.AuthorsApi;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.folio.mylibrary.domain.dto.AuthorCollection;
import org.folio.mylibrary.domain.dto.AuthorResource;
import org.folio.mylibrary.domain.entity.Author;
import org.folio.mylibrary.service.AuthorService;
import org.folio.mylibrary.service.BookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@Log4j2
@RestController
@RequestMapping(value = "/")
public class AuthorController implements AuthorsApi {
    private final AuthorService authorService;
    
    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    public ResponseEntity<AuthorCollection> getAuthors(
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId,
        Integer offset,
        Integer limit,
        String sort,
        @Valid String cql
    ) {
        return ResponseEntity.ok(authorService.getAuthors(offset, limit, sort, cql));
    }

    @Override
    public ResponseEntity<AuthorResource> createAuthor(
        AuthorResource author,
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId
    ) {
        AuthorResource newAuthor = authorService.createAuthor(author); 

        URI authorUrl = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newAuthor.getId())
            .toUri();

        return ResponseEntity.created(authorUrl).body(newAuthor);
    }

    @Override
    public ResponseEntity<AuthorResource> getAuthor(
        String id,
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId
    ) {
        var author = authorService.getAuthor(id);

        return author == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(author);
    }

    @Override
    public ResponseEntity<Void> updateAuthor(
        String id,
        AuthorResource author,
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId
    ) {
        authorService.updateAuthor(id, author);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteAuthor(
        String id,
        String xOkapiUrl,
        String xOkapiTenant,
        String xOkapiToken,
        String xOkapiModuleId
    ) {
        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }
}
