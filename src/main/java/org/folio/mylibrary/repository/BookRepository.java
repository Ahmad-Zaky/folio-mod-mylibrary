package org.folio.mylibrary.repository;

import java.util.UUID;

import org.folio.mylibrary.domain.entity.Book;
import org.folio.spring.cql.JpaCqlRepository;
import org.folio.spring.data.OffsetRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface BookRepository extends JpaCqlRepository<Book, UUID> {
    
    @EntityGraph(
        type = EntityGraph.EntityGraphType.FETCH,
        value = "book-authors-graph"
    )
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(
        type = EntityGraph.EntityGraphType.FETCH,
        value = "book-authors-graph"
    )
    Page<Book> findByCQL(String cql, OffsetRequest offset);
}
