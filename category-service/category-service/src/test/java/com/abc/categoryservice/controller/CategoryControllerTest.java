package com.abc.categoryservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.abc.categoryservice.dto.CategoryRequest;
import com.abc.categoryservice.dto.CategoryResponse;
import com.abc.categoryservice.dto.CategoryStatusDTO;
import com.abc.categoryservice.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService service;

    @Test
    void testCreateCategory_Admin_Success() throws Exception {
        CategoryRequest request = new CategoryRequest("Plumbing", "Pipe Repairs", 150.0, true);
        CategoryResponse response = new CategoryResponse(1L, "Plumbing", "Pipe Repairs", 150.0, true);

        when(service.createCategory(any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/categories")
                .header("X-Role", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.name").value("Plumbing"))
                .andExpect(jsonPath("$.categoryName").value("Plumbing"))
                .andExpect(jsonPath("$.description").value("Pipe Repairs"))
                .andExpect(jsonPath("$.basePrice").value(150.0))
                .andExpect(jsonPath("$.active").value(true));

        verify(service, times(1)).createCategory(any(CategoryRequest.class));
    }

    @Test
    void testCreateCategory_Customer_Forbidden() throws Exception {
        CategoryRequest request = new CategoryRequest("Plumbing", "Pipe Repairs", 150.0, true);

        mockMvc.perform(post("/api/categories")
                .header("X-Role", "CUSTOMER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verify(service, never()).createCategory(any(CategoryRequest.class));
    }

    @Test
    void testCreateCategory_Unauthenticated_Unauthorized() throws Exception {
        CategoryRequest request = new CategoryRequest("Plumbing", "Pipe Repairs", 150.0, true);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(service, never()).createCategory(any(CategoryRequest.class));
    }

    @Test
    void testGetAllCategories_Authenticated_Success() throws Exception {
        CategoryResponse response = new CategoryResponse(1L, "Plumbing", "Pipe Repairs", 150.0, true);
        List<CategoryResponse> list = Collections.singletonList(response);

        when(service.getAllCategories()).thenReturn(list);

        mockMvc.perform(get("/api/categories")
                .header("X-Role", "CUSTOMER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].categoryId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Plumbing"))
                .andExpect(jsonPath("$[0].categoryName").value("Plumbing"));

        verify(service, times(1)).getAllCategories();
    }

    @Test
    void testGetAllCategories_Unauthenticated_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isUnauthorized());

        verify(service, never()).getAllCategories();
    }

    @Test
    void testGetCategoryById_Authenticated_Success() throws Exception {
        CategoryResponse response = new CategoryResponse(1L, "Plumbing", "Pipe Repairs", 150.0, true);

        when(service.getCategoryById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/categories/{id}", 1L)
                .header("X-Role", "TECHNICIAN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.name").value("Plumbing"))
                .andExpect(jsonPath("$.categoryName").value("Plumbing"));

        verify(service, times(1)).getCategoryById(1L);
    }

    @Test
    void testGetCategoryById_Unauthenticated_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/categories/{id}", 1L))
                .andExpect(status().isUnauthorized());

        verify(service, never()).getCategoryById(anyLong());
    }

    @Test
    void testUpdateCategory_Admin_Success() throws Exception {
        CategoryRequest request = new CategoryRequest("Plumbing", "Updated Description", 160.0, true);
        CategoryResponse response = new CategoryResponse(1L, "Plumbing", "Updated Description", 160.0, true);

        when(service.updateCategory(eq(1L), any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/categories/{id}", 1L)
                .header("X-Role", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.basePrice").value(160.0));

        verify(service, times(1)).updateCategory(eq(1L), any(CategoryRequest.class));
    }

    @Test
    void testUpdateCategory_Customer_Forbidden() throws Exception {
        CategoryRequest request = new CategoryRequest("Plumbing", "Updated Description", 160.0, true);

        mockMvc.perform(put("/api/categories/{id}", 1L)
                .header("X-Role", "CUSTOMER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verify(service, never()).updateCategory(anyLong(), any(CategoryRequest.class));
    }

    @Test
    void testDeleteCategory_Admin_Success() throws Exception {
        doNothing().when(service).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/{id}", 1L)
                .header("X-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category deleted successfully"));

        verify(service, times(1)).deleteCategory(1L);
    }

    @Test
    void testDeleteCategory_Customer_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}", 1L)
                .header("X-Role", "CUSTOMER"))
                .andExpect(status().isForbidden());

        verify(service, never()).deleteCategory(anyLong());
    }

    @Test
    void testUpdateCategoryStatus_Admin_Success() throws Exception {
        CategoryStatusDTO statusDTO = new CategoryStatusDTO(false);
        CategoryResponse response = new CategoryResponse(1L, "Plumbing", "Pipe Repairs", 150.0, false);

        when(service.updateCategoryStatus(eq(1L), any(CategoryStatusDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/api/categories/{id}/status", 1L)
                .header("X-Role", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.active").value(false));

        verify(service, times(1)).updateCategoryStatus(eq(1L), any(CategoryStatusDTO.class));
    }

    @Test
    void testUpdateCategoryStatus_Customer_Forbidden() throws Exception {
        CategoryStatusDTO statusDTO = new CategoryStatusDTO(false);

        mockMvc.perform(patch("/api/categories/{id}/status", 1L)
                .header("X-Role", "CUSTOMER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isForbidden());

        verify(service, never()).updateCategoryStatus(anyLong(), any(CategoryStatusDTO.class));
    }
}

