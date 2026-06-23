package com.abc.assignmentservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    @JsonAlias("categoryId")
    private Long id;

    @JsonAlias("categoryName")
    private String name;

    private String description;
    private Boolean active;
}
