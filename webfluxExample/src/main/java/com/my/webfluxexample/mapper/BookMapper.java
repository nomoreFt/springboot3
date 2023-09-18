package com.my.webfluxexample.mapper;

import com.my.webfluxexample.dto.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import reactor.core.publisher.Mono;

import com.my.webfluxexample.domain.Book;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book bookPostToBook(BookDto.Post requestBody);
    Book bookPatchToBook(BookDto.Patch requestBody);
    BookDto.Response bookToResponse(Book book);
    List<BookDto.Response> booksToResponse(List<Book> books);

}
