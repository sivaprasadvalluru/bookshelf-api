package com.pageturner.bookshelf.book;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank String isbn
) {}
