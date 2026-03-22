package com.bibliotech.bibliotechproject.mappers;

import com.bibliotech.bibliotechproject.dtos.BookDTO;
import com.bibliotech.bibliotechproject.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    // We convert the Book entity to the DTO
    BookDTO toDto(Book book);

    // We convert the DTO back to the Entity
    Book toEntity(BookDTO dto);
}