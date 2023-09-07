package org.folio.mylibrary.repository;

import java.util.UUID;

import org.folio.mylibrary.domain.entity.Book;
import org.folio.spring.cql.JpaCqlRepository;

public interface BookRepository extends JpaCqlRepository<Book, UUID> {
    
}
