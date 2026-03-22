package com.bibliotech.bibliotechproject.dtos;

import lombok.Data;
import java.util.List;

@Data
public class BookDTO {
    private String id;
    private String isbn;
    private String title;
    private int stockDisponible;
    private String authorName; // Cleaner: just the name of the author
}