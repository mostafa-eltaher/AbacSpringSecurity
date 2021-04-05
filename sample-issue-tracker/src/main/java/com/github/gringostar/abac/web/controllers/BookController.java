package com.github.gringostar.abac.web.controllers;

import java.util.List;

import com.github.gringostar.abac.web.model.Book;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/books")
public class BookController {

    public List<Book> getAllBooks() {
        return List.of(new Book("test", "me"));
    }
    
}
