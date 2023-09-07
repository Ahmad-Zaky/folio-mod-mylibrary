package org.folio.mylibrary.service;

import java.util.List;

import org.folio.mylibrary.domain.dto.AuthorCollection;
import org.folio.mylibrary.domain.dto.AuthorResource;

public interface AuthorService {

    AuthorCollection getAuthors(Integer offset, Integer limit, String sort, String cql);
    
    AuthorResource createAuthor(AuthorResource author);

    AuthorResource getAuthor(String id);

    void updateAuthor(String id, AuthorResource author);

    void deleteAuthor(String id);
    
}
