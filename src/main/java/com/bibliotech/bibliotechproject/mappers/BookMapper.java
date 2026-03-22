package com.bibliotech.bibliotechproject.mappers;

import com.bibliotech.bibliotechproject.dtos.BookDTO;
import com.bibliotech.bibliotechproject.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    // We ignore authorName and categoryNames because they come from different collections
    @Mapping(target = "authorName", ignore = true)
    @Mapping(target = "categoryNames", ignore = true)
    BookDTO toDto(Book book);

    Book toEntity(BookDTO dto);
}