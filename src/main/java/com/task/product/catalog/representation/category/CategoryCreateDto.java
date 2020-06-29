package com.task.product.catalog.representation.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateDto {

    @ApiModelProperty(required = true)
    @NotBlank(message = "Category name should be non-null")
    private String name;

    private String description;

    private UUID parentId;
}
