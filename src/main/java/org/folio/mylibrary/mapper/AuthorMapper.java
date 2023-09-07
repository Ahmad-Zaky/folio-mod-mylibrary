package org.folio.mylibrary.mapper;

import java.util.UUID;

import org.folio.mylibrary.domain.dto.AuthorResource;
import org.folio.mylibrary.domain.entity.Author;
import org.folio.mylibrary.exceptionn.RecordNotFoundException;
import org.folio.mylibrary.repository.AuthorRepository;
import org.folio.mylibrary.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;

public class AuthorMapper {
    public static AuthorResource mapEntityToDto(Author author) {
        return new AuthorResource()
            .id(StringUtil.uuidToStringSafe(author.getId()))
            .name(author.getName());
    }

    public static Author mapDtoToEntity(AuthorResource authorResource) {
        return Author.builder()
            .id(StringUtil.toUuidSafe(authorResource.getId()))
            .name(authorResource.getName())
            .build();
    }
}
