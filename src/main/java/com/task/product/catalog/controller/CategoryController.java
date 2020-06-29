package com.task.product.catalog.controller;

import com.task.product.catalog.representation.category.CategoryCreateDto;
import com.task.product.catalog.representation.category.CategoryDto;
import com.task.product.catalog.service.category.CategoryService;
import com.task.product.catalog.representation.category.CategoryUpdateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/categories")
@Api(value = "Category Controller", tags = "Product Category", description = "Product Category Management.")
@Slf4j
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "Creates a new category.")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryCreateDto categoryCreateDto) {
        return ResponseUtils.createResponse(categoryService.createCategory(categoryCreateDto));
    }

    @ApiOperation(value = "Updates the category.")
    @PutMapping(value = "/{categoryId}/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable final UUID categoryId,
            @RequestBody @Valid CategoryUpdateDto categoryUpdateDto
    ) {
        return ResponseUtils.createResponse(categoryService.updateCategory(categoryId, categoryUpdateDto));
    }

    @ApiOperation(value = "Deletes the category.")
    @DeleteMapping(value = "/{categoryId}/", produces = "application/json")
    public void deleteCategory(@PathVariable final UUID categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @ApiOperation(value = "Gets the category by id.")
    @GetMapping(value = "/{categoryId}/", produces = "application/json")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable final UUID categoryId) {
        return ResponseUtils.createResponse(categoryService.getCategory(categoryId));
    }

    @ApiOperation(value = "Gets a list of all the categories.")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseUtils.createResponse(categoryService.getCategories());
    }

    @ApiOperation(value = "Gets the root categories.")
    @GetMapping(value = "/root/", produces = "application/json")
    public ResponseEntity<List<CategoryDto>> getRootCategories() {
        return ResponseUtils.createResponse(categoryService.getRootCategories());
    }

    @ApiOperation(value = "Gets all descendants of a given category node.")
    @GetMapping(value = "/{categoryId}/descendants/", produces = "application/json")
    public ResponseEntity<List<CategoryDto>> getDescendants(@PathVariable final UUID categoryId) {
        return ResponseUtils.createResponse(categoryService.getDescendants(categoryId));
    }

    @ApiOperation(value = "Gets paged list of the children of a given category node.")
    @GetMapping(value = "/{categoryId}/children/", produces = "application/json")
    public ResponseEntity<Page<CategoryDto>> getChildren(
            @PathVariable final UUID categoryId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        return ResponseUtils.createResponse(categoryService.getChildren(categoryId, page, size));
    }

}
