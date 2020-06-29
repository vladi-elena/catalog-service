package com.task.product.catalog.service.category;

import com.task.product.catalog.domain.model.category.Category;
import com.task.product.catalog.repository.category.CategoryRepository;
import com.task.product.catalog.representation.category.CategoryCreateDto;
import com.task.product.catalog.representation.category.CategoryDto;
import com.task.product.catalog.representation.category.CategoryUpdateDto;
import com.task.product.catalog.service.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация  {@link CategoryService}.
 * <p>
 * Сервис управления категориями товаров.
 * <p>
 * Реализация учитывает:
 * 1) категория может содержать подкатегории
 * 2) количество уровней вложенности не ограничено
 * 3) количество категорий на одном уровне не ограничено
 * <p>
 * Реализовано на основе паттерна 'Closure Table'.
 * Вся информация о категории хранится в таблице 'category'.
 * Вся информация о связях категории с другими категориями - в 'category_closure'.
 * В таблице 'category_closure' для каждого исходного элемента храним всех его потомков.
 * Таблица 'category_closure' создана для оптимизации рекурсивных выборок.
 * Всего один запрос к ней вернет все поддерево для категории в плоском виде.
 * А так как каждый элемент списка содержит id ближайшего родителя, на ui его можно легко превратить в иерархическую структуру.
 * <p>
 * Для того чтобы получить только ближайших потомков определенной категории делается запрос только к первой таблице.
 *
 * @see CategoryService
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;


    private CategoryConverter categoryConverter;

    public CategoryServiceImpl(
            final CategoryRepository categoryRepository,
            final CategoryConverter categoryConverter
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryConverter = categoryConverter;
    }

    @Transactional
    public CategoryDto createCategory(final CategoryCreateDto categoryCreateDto) {
        UUID parentId = categoryCreateDto.getParentId();
        Category parentCategory = null;
        if (parentId != null) {
            parentCategory = getCategoryById(parentId); // Проверка
        }

        Category category = new Category(
                categoryCreateDto.getName(),
                categoryCreateDto.getDescription(),
                parentId
        );
        categoryRepository.save(category);

        /* Сохраняем все связи новой категории согласно паттерну 'Closure Table'. */
        /* Используем нативный query для быстрой и эффективной вставки всех связей. */
        if (parentCategory != null) {
            categoryRepository.saveDescendant(category.getId(), category.getParentId());
        } else {
            categoryRepository.saveDescendant(category.getId());
        }

        return categoryConverter.convertEntityToDto(category);
    }

    public CategoryDto updateCategory(final UUID categoryId, final CategoryUpdateDto categoryUpdateDto) {
        Category category = getCategoryById(categoryId); // Проверка

        category.setName(categoryUpdateDto.getName());
        category.setDescription(categoryUpdateDto.getDescription());

        categoryRepository.save(category);

        return categoryConverter.convertEntityToDto(category);
    }

    public CategoryDto getCategory(final UUID categoryId) {
        return categoryConverter.convertEntityToDto(getCategoryById(categoryId));
    }

    public List<CategoryDto> getCategories() {
        return categoryConverter.convertEntityListToDtoList(categoryRepository.findAll());
    }

    public List<CategoryDto> getDescendants(final UUID categoryId) {
        Category category = getCategoryById(categoryId);
        List<Category> descendants = category.getDescendants()
                .stream()
                .filter(it -> !categoryId.equals(it.getId()))
                .collect(Collectors.toList());
        return categoryConverter.convertEntityListToDtoList(descendants);
    }

    public List<CategoryDto> getRootCategories() {
        return categoryConverter.convertEntityListToDtoList(categoryRepository.findByParentIdIsNull());
    }

    public Page<CategoryDto> getChildren(final UUID categoryId, final Integer page, final Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return categoryConverter.convertEntityPageToDtoPage(categoryRepository.findByParentId(categoryId, pageable));
    }

    public void deleteCategory(final UUID categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
        }
    }

    private Category getCategoryById(final UUID categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Category with Id \"" + categoryId + "\" not found."));
    }
}
