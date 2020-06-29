package com.task.product.catalog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Converter<E, D> {

    public abstract D convertEntityToDto(E entity);

    public abstract E convertDtoToEntity(D dto);

    public List<D> convertEntityListToDtoList(final List<E> entityList) {
        return entityList.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public Page<D> convertEntityPageToDtoPage(final Page<E> entityPage) {
        List<D> dtoList = convertEntityListToDtoList(entityPage.getContent());
        if (!dtoList.isEmpty()) {
            return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
        }
        return null;
    }
}

