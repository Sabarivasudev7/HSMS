package cap_stone_project.caregory_service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cap_stone_project.caregory_service.model.CategoryDTO;
import cap_stone_project.caregory_service.service.CategoryService;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @MockitoBean
    private CategoryService cateService;

    @Autowired
    private MockMvc mockMvc;
    
    
    @Test
    void testShowAllCategories() throws Exception {

        List<CategoryDTO> categories = new ArrayList<>();

        categories.add( new CategoryDTO( "Plumbing","Pipe Services", 500, true));

        categories.add( new CategoryDTO("Electrical", "Electrical Services", 700, true));

        when(cateService.showAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/category/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                .value(2));
    }

    
    
    @Test
    void testFindCategoryById() throws Exception {

        CategoryDTO dto = new CategoryDTO("Plumbing", "Pipe Services", 500, true);

        when(cateService.findCateById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName")
                .value("Plumbing"));
    }
    
    
}