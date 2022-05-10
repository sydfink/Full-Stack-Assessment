package com.galvanize.FullStackAssessment.client;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//I know you shouldn't use the wildcard but it was the only way reqbin would work -_-
@CrossOrigin(origins = "*", methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PATCH})
public class BookController {
    BookRepository br;

    public BookController(BookRepository br) {
        this.br = br;
    }

    @PostMapping("/books")
    public Book addNewBook(@RequestBody Book book){
        //System.out.println(book);
        return br.save(book);
    }

    @GetMapping("/books")
    public Iterable<Book> findAllBooks(){
        return br.findAll();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> findOneBook(@PathVariable Long id){
        //try to get the book by id
        try {
            Book b = br.findById(id).get();
            //if book found return book and status ok
            return new ResponseEntity<>(b, HttpStatus.OK);
        } catch (Exception e){
            //if no book found return bad request
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/books/{id}")
    public Book updateOneBook(@PathVariable Long id, @RequestBody Map<String, String> replace){
        //get the book you need to change
        Book b = br.findById(id).get();
        //for each value - replace with user input
        replace.forEach((k,v) -> {
            if(k.equals("title")){
                b.setTitle(v);
            } else if(k.equals("author")){
                b.setAuthor(v);
            } else if(k.equals("favorite")){
                b.setFavorite(Boolean.valueOf(v));
            }
        });
        //return the updated book
        return br.save(b);
    }


    @DeleteMapping("/books/{id}")
    public void deleteOneBook(@PathVariable Long id){
        br.deleteById(id);
    }


}
