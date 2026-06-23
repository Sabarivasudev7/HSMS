package com.abc.servicerequestservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    @JsonAlias("categoryId")
    private Long id;

    @JsonAlias("categoryName")
    private String name;

    private String description;
}
