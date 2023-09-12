package org.folio.mylibrary.service;

import static org.folio.mylibrary.domain.dto.ErrorMessages.AUTHOR_WITH_ID_IS_NOT_FOUND;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.folio.spring.FolioExecutionContext;
import org.folio.mylibrary.domain.dto.AuthorCollection;
import org.folio.mylibrary.domain.dto.AuthorResource;
import org.folio.mylibrary.domain.entity.Author;
import org.folio.mylibrary.domain.entity.Book;
import org.folio.mylibrary.exceptionn.RecordNotFoundException;
import org.folio.mylibrary.mapper.AuthorMapper;
import org.folio.mylibrary.repository.AuthorRepository;
import org.folio.mylibrary.util.StringUtil;
import org.folio.spring.data.OffsetRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final FolioExecutionContext folioExecutionContext;

    @Override
    public AuthorCollection getAuthors(Integer offset, Integer limit, String sort, String cql) {
        var sortDirection = sort.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sort.startsWith("-") ? sort.substring(1) : sort;
        Sort sortOrder = Sort.by(new Sort.Order(sortDirection, sortField));

        Pageable pageable = PageRequest.of(offset / limit, limit, sortOrder);
        
        boolean isBlank = isBlank(cql); 
        Page<Author> authors = isBlank
            ? authorRepository.findAll(pageable)
            : authorRepository.findByCQL(cql, new OffsetRequest(offset, limit));

        return new AuthorCollection()
            .authors(authors.stream().map(AuthorMapper::mapEntityToDto).collect(Collectors.toList()))
            .totalRecords((int) authors.getTotalElements());
    }

    @Override
    public AuthorResource createAuthor(AuthorResource authorResource) {
        var author = AuthorMapper.mapDtoToEntity(authorResource);

        author.setCreatedByUserId(folioExecutionContext.getUserId());

        return AuthorMapper.mapEntityToDto(authorRepository.save(author));
    }

    @Override
    public AuthorResource getAuthor(String id) {
        UUID uuid = StringUtil.toUuidSafe(id);

        return authorRepository.findById(uuid).map(AuthorMapper::mapEntityToDto).orElse(null);
    }

    @Override
    public void updateAuthor(String id, AuthorResource authorResource) {
        UUID uuid = StringUtil.toUuidSafe(id);
        Optional<Author> optionalAuthor = authorRepository.findById(uuid);        
        Author auther = optionalAuthor.orElseThrow(() -> new RecordNotFoundException(String.format(AUTHOR_WITH_ID_IS_NOT_FOUND, id)));
        
        Author updatedAuthor = AuthorMapper.mapDtoToEntity(authorResource);

        auther.setId(uuid);
        auther.merge(updatedAuthor);
        auther.setUpdatedByUserId(folioExecutionContext.getUserId());

        authorRepository.save(auther);
    }

    @Override
    public void deleteAuthor(String id) {
        UUID uuid = StringUtil.toUuidSafe(id);
        if (! authorRepository.existsById(uuid)) {
            throw new RecordNotFoundException(String.format(AUTHOR_WITH_ID_IS_NOT_FOUND, id));
        }

        authorRepository.deleteById(uuid);
    }   
}
