package com.ninabornemann.todo_backend_project.service;

import com.ninabornemann.todo_backend_project.dto.ToDoDto;
import com.ninabornemann.todo_backend_project.models.Status;
import com.ninabornemann.todo_backend_project.models.Todo;
import com.ninabornemann.todo_backend_project.repository.TodoRepo;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Test
    void getAllTodos() {
        TodoRepo mockRepo = mock(TodoRepo.class);
        Todo t1 = new Todo("123", "start Mockito", Status.OPEN);
        Todo t2 = new Todo("234", "conquer Mockito", Status.IN_PROGRESS);
        TodoService service = new TodoService(mockRepo, null, null);

        when(mockRepo.findAll()).thenReturn(List.of(t1, t2));

        List<Todo> actual = service.getAllToDos();

        assertEquals(List.of(t1, t2), actual);
        verify(mockRepo).findAll();
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    void getTodoById() {
        TodoRepo mockRepo = mock(TodoRepo.class);
        Todo t1 = new Todo("123", "start Mockito", Status.OPEN);
        Todo t2 = new Todo("234", "conquer Mockito", Status.IN_PROGRESS);
        TodoService service = new TodoService(mockRepo, null, null);

        when(mockRepo.findById("234")).thenReturn(Optional.of(t2));

        Optional<Todo> actual = service.getToDoById("234");

        assertEquals(Optional.of(t2), actual);
        verify(mockRepo).findById("234");
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    void addTodo() {
        TodoRepo mockRepo = mock(TodoRepo.class);
        IdService mockIdService = mock(IdService.class);
        ChatGPTService mockChatGptService = mock(ChatGPTService.class);
        Todo t1 = new Todo("123", "start Mockito", Status.OPEN);
        Todo t2 = new Todo("234", "conquer Mockito", Status.IN_PROGRESS);
        ToDoDto t21 = new ToDoDto("conquuer Mockito", Status.IN_PROGRESS);

        when(mockRepo.save(t2)).thenReturn(t2);
        when(mockIdService.randomId()).thenReturn("234");
        when(mockChatGptService.checkSpelling(t21.description())).thenReturn(t2.description());

        TodoService service = new TodoService(mockRepo, mockIdService, mockChatGptService);

        Todo actual = service.addToDo(t21);

        assertEquals(t2, actual);
        verify(mockIdService).randomId();
        verify(mockRepo).save(t2);
        verify(mockChatGptService).checkSpelling(t21.description());
        verifyNoMoreInteractions(mockIdService, mockRepo, mockChatGptService);
    }

    @Test
    void updateTodo(){
        TodoRepo mockRepo = mock(TodoRepo.class);
        Todo t1 = new Todo("123", "start Mockito", Status.OPEN);
        ToDoDto t12 = new ToDoDto("conquer Mockito", Status.IN_PROGRESS);
        Todo t2 = new Todo("123","conquer Mockito", Status.IN_PROGRESS);
        TodoService service = new TodoService(mockRepo, null, null);

        when(mockRepo.findById("123")).thenReturn(Optional.of(t1));
        doNothing().when(mockRepo).delete(t1);
        when(mockRepo.save(t2)).thenReturn(t2);

        Todo actual = service.updateToDoById("123", t12);

        assertEquals(t2, actual);
        verify(mockRepo).findById("123");
        verify(mockRepo).delete(t1);
        verify(mockRepo).save(t2);
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    void deleteById() {
        TodoRepo mockRepo = mock(TodoRepo.class);
        Todo t1 = new Todo("123", "start Mockito", Status.OPEN);
        TodoService service = new TodoService(mockRepo, null, null);

        when(mockRepo.findById("123")).thenReturn(Optional.of(t1));
        doNothing().when(mockRepo).delete(t1);
        service.deleteById("123");

        verify(mockRepo).findById("123");
        verify(mockRepo).delete(t1);
        verifyNoMoreInteractions(mockRepo);
    }


}