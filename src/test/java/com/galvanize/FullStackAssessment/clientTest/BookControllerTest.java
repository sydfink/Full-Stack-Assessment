package com.galvanize.FullStackAssessment.clientTest;


import com.galvanize.FullStackAssessment.client.Book;
import com.galvanize.FullStackAssessment.client.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    BookRepository repo;
    @Autowired
    MockMvc mvc;


    //can you add a new book with title, author, and favorite default false?
    @Test
    @Transactional
    @Rollback
    public void addNewBook() throws Exception {
        //send book info inside json body
        this.mvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Her\", \"author\": \"Pierre\", \"favorite\": false}"))
                //test book was created from post
                .andExpect(jsonPath("$.title", is("Her")))
                .andExpect(jsonPath("$.author", is("Pierre")))
                .andExpect(jsonPath("$.favorite", is(false)))
                .andExpect(jsonPath("$.id").isNumber());
    }

    //can you get one single book by id?
    @Test
    @Transactional
    @Rollback
    public void getOneBook() throws Exception {
        //create book to retrieve
        Book test = new Book("Her", "Pierre", true);
        Book record = repo.save(test);

        //send get request
        this.mvc.perform(get("/books/" + record.getId()))
                //test book came back
                .andExpect(jsonPath("$.title", is("Her")))
                .andExpect(jsonPath("$.author", is("Pierre")))
                .andExpect(jsonPath("$.favorite", is(true)))
                .andExpect(jsonPath("$.id").isNumber());
    }


    //can you get all the books in the list?
    @Test
    @Transactional
    @Rollback
    public void getAllBooks() throws Exception {
        //create books to retrieve
        Book test = new Book("Her", "Pierre", true);
        Book record = repo.save(test);
        Book test2 = new Book("Him", "Pierre", false);
        Book record2 = repo.save(test2);

        //send get request
        this.mvc.perform(get("/books"))
                //test that books came back
                .andExpect(jsonPath("$[0].title", is("Her")))
                .andExpect(jsonPath("$[0].author", is("Pierre")))
                .andExpect(jsonPath("$[0].favorite", is(true)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[1].title", is("Him")))
                .andExpect(jsonPath("$[1].author", is("Pierre")))
                .andExpect(jsonPath("$[1].favorite", is(false)))
                .andExpect(jsonPath("$[1].id").isNumber());
    }

    //can you update one book?
    @Test
    @Transactional
    @Rollback
    public void updateOneBook() throws Exception{
        //create book to change
        Book test = new Book("She", "Alex", false);
        Book record = repo.save(test);
        //patch the book with changes
        this.mvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Her\", \"author\": \"Pierre\", \"favorite\": false}"))
                //test that the changes worked
                .andExpect(jsonPath("$.title", is("Her")))
                .andExpect(jsonPath("$.author", is("Pierre")))
                .andExpect(jsonPath("$.favorite", is(false)))
                .andExpect(jsonPath("$.id").isNumber());
    }

    //can you delete one book?
    @Test
    @Transactional
    @Rollback
    public void deleteOneBook() throws Exception{
        //create a book to delete
        Book test = new Book("Delete me", "You", false);
        Book record = repo.save(test);

        //send delete request
        this.mvc.perform(delete("/books/" + record.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Delete Me\", \"author\": \"You\", \"favorite\": false}"))
                //test that info was deleted from db
                .andExpect(jsonPath("$.title").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.id").doesNotHaveJsonPath());
    }

}
