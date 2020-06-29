package com.task.product.catalog.representation.category;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private UUID id;

    private String name;

    private String description;

    private UUID parentId;

}
