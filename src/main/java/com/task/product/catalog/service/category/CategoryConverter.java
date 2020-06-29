package com.task.product.catalog.service.category;

import com.task.product.catalog.representation.category.CategoryDto;
import com.task.product.catalog.service.Converter;
import com.task.product.catalog.domain.model.category.Category;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoryConverter extends Converter<Category, CategoryDto> {

    @Override
    public CategoryDto convertEntityToDto(final Category entity) {
        if (entity != null) {
            CategoryDto dto = new CategoryDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }
        return null;
    }

    @Override
    public Category convertDtoToEntity(final CategoryDto dto) {
        if (dto != null) {
            Category entity = new Category();
            BeanUtils.copyProperties(dto, entity);
            entity.setId(UUID.randomUUID());
            return entity;
        }
        return null;
    }
}
