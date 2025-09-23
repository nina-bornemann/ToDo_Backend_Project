package com.ninabornemann.todo_backend_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninabornemann.todo_backend_project.dto.ToDoDto;
import com.ninabornemann.todo_backend_project.models.Status;
import com.ninabornemann.todo_backend_project.models.Todo;
import com.ninabornemann.todo_backend_project.repository.TodoRepo;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepo repo;

    @Autowired
    private MockRestServiceServer mockServer;

    @DirtiesContext
    @Test
    void getAllToDos_shouldReturnStatus_isOk() throws Exception {
        Todo t1 = new Todo("123", "start Spring", Status.OPEN);
        repo.save(t1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                    [
                                        {
                                          "id": "123",
                                          "description": "start Spring",
                                          "status": "OPEN"
                                        }
                                    ]
                                """
                ));
    }

    @DirtiesContext
    @Test
    void addTodo_shouldReturnStatus_isCreated() throws Exception {
        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                                           {
                                             "choices":
                                             [
                                                {
                                                   "message" :
                                                   {
                                                    "role": "user",
                                                    "content": "test Spring"
                                                   }
                                                }
                                             ]
                                           }
                                        """, MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "description": "test Spring",
                                    "status": "OPEN"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "description": "test Spring",
                            "status": "OPEN"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @DirtiesContext
    @Test
    void addTodo_shouldCheckSpelling_withChatGPT() throws Exception {
        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                                           {
                                             "choices":
                                             [
                                                {
                                                   "message" :
                                                   {
                                                    "role": "user",
                                                    "content": "conquer Mockito"
                                                   }
                                                }
                                             ]
                                           }
                                        """, MediaType.APPLICATION_JSON));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "description": "conqueer Mockito",
                                    "status": "OPEN"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                    "description": "conquer Mockito",
                                    "status": "OPEN"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @DirtiesContext
    @Test
    void getTodoById_shouldReturnStatus_isOk() throws Exception {
        Todo t1 = new Todo("123", "start Spring", Status.OPEN);
        repo.save(t1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                          "id": "123",
                          "description": "start Spring",
                          "status": "OPEN"
                        }
                        """));
    }

    @DirtiesContext
    @Test
    void getTodoById_shouldThrow_IllegalArgumentException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/2323"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                              {
                                                                "errorMessage": "No To-Do was found under this id."
                                                              }
                                                              """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instant").isNotEmpty());
    }

    @DirtiesContext
    @Test
    void updateToDoById_shouldReturn_isOk() throws Exception {
        Todo t1 = new Todo("123", "start Spring", Status.OPEN);
        repo.save(t1);
        ToDoDto dto = new ToDoDto("is still working?", Status.DONE);

        String json = new ObjectMapper().writeValueAsString(dto);
        System.out.println(json);

        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                                           {
                                             "choices":
                                             [
                                                {
                                                   "message" :
                                                   {
                                                    "role": "user",
                                                    "content": "is still working?"
                                                   }
                                                }
                                             ]
                                           }
                                        """, MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/todo/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": "123",
                            "description": "is still working?",
                            "status": "DONE"
                        }
                        """));
    }

    @DirtiesContext
    @Test
    void updateToDoById_shouldCheckSpelling_withChatGpt() throws Exception {
        Todo t1 = new Todo("123", "start Spring", Status.OPEN);
        repo.save(t1);
        ToDoDto dto = new ToDoDto("conqueer Mockito", Status.DONE);
        String json = new ObjectMapper().writeValueAsString(dto);
        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                                           {
                                             "choices":
                                             [
                                                {
                                                   "message" :
                                                   {
                                                    "role": "user",
                                                    "content": "conquer Mockito"
                                                   }
                                                }
                                             ]
                                           }
                                        """, MediaType.APPLICATION_JSON));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                    "description": "conquer Mockito",
                                    "status": "DONE"
                                }
                                """));
    }

    @DirtiesContext
    @Test
    void updateToDoById_shouldThrow_ResponseStatusException() throws Exception {
        Todo t1 = new Todo("123", "start Spring", Status.OPEN);
        repo.save(t1);
        ToDoDto dto = new ToDoDto("is still working?", Status.DONE);

        String json = new ObjectMapper().writeValueAsString(dto);
        System.out.println(json);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/2323")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                              {
                                                                "errorMessage": "404 NOT_FOUND \\"No To-Do was found under this id.\\""
                                                              }
                                                              """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instant").isNotEmpty());
    }

    @DirtiesContext
    @Test
    void deleteById_shouldReturn_noContent() throws Exception {
        Todo t1 = new Todo("123", "start Spring", Status.OPEN);
        repo.save(t1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/123"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @DirtiesContext
    @Test
    void deleteById_shouldThrow_ResponseStatusException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/2424"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                                {
                                                                  "errorMessage": "404 NOT_FOUND \\"No To-Do was found under this id.\\""
                                                                }
                                                                """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instant").isNotEmpty());
    }

}