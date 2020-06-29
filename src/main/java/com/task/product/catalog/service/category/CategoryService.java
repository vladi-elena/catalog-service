package com.task.product.catalog.service.category;

import com.task.product.catalog.representation.category.CategoryCreateDto;
import com.task.product.catalog.representation.category.CategoryDto;
import com.task.product.catalog.representation.category.CategoryUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * Сервис управления категориями товаров.
 * Категория может содержать подкатегории.
 */
public interface CategoryService {

    /**
     * Создает новую категорию.
     *
     * @param categoryCreateDto категория
     * @return категорию
     */
    CategoryDto createCategory(CategoryCreateDto categoryCreateDto);

    /**
     * Редактирует категорию.
     * На данный момент реализовано редактирование только полей 'имя' и 'описание'.
     * Нет возможности изменить родительскую категорию.
     *
     * @param categoryId        id категории
     * @param categoryUpdateDto категория
     * @return категорию
     */
    CategoryDto updateCategory(UUID categoryId, CategoryUpdateDto categoryUpdateDto);

    /**
     * Возвращает категорию по ID.
     *
     * @param categoryId id категории
     * @return категорию
     */
    CategoryDto getCategory(UUID categoryId);

    /**
     * Возвращает дочерние элементы данной категории.
     * Возвращаются только прямые потомки (первый уровень родства).
     *
     * @param categoryId id родительской категории
     * @param page       номер страницы
     * @param size       размер страницы
     * @return страница категорий
     */
    Page<CategoryDto> getChildren(UUID categoryId, Integer page, Integer size);

    /**
     * Возвращает всех потомков данной категории.
     * Возвращается все поддерево, у которого корень - данная категория.
     *
     * @param categoryId id категории-предка
     * @return список категорий
     */
    List<CategoryDto> getDescendants(UUID categoryId);

    /**
     * Возвращает все категории (полное дерево).
     *
     * @return список категорий
     */
    List<CategoryDto> getCategories();

    /**
     * Возвращает все корневые категории.
     *
     * @return список категорий
     */
    List<CategoryDto> getRootCategories();

    /**
     * Удаляет категорию по ID.
     *
     * @param categoryId id категории
     */
    void deleteCategory(UUID categoryId);
}
