package org.folio.mylibrary.repository;

import java.util.UUID;

import org.folio.mylibrary.domain.entity.Author;
import org.folio.spring.cql.JpaCqlRepository;

public interface AuthorRepository extends JpaCqlRepository<Author, UUID> {
    
}
