package com.my.webfluxexample.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private long bookId;
    private String titleKorean;
    private String titleEnglish;
    private String description;
    private String author;
    private String isbn;
    private String publishDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("last_modified_at")
    private LocalDateTime modifiedAt;


    public Book mergeCreateNonNullField(Book source) {
        return Book.builder()
                .bookId(this.bookId)
                .titleKorean(Optional.ofNullable(source.getTitleKorean()).orElse(this.titleKorean))
                .titleEnglish(Optional.ofNullable(source.getTitleEnglish()).orElse(this.titleEnglish))
                .description(Optional.ofNullable(source.getDescription()).orElse(this.description))
                .author(Optional.ofNullable(source.getAuthor()).orElse(this.author))
                .isbn(Optional.ofNullable(source.getIsbn()).orElse(this.isbn))
                .publishDate(Optional.ofNullable(source.getPublishDate()).orElse(this.publishDate))
                .build();
    }
}
