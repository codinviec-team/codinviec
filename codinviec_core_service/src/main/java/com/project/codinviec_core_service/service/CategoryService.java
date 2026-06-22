package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.CategoryDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.SaveUpdateCategoryRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    Page<CategoryDTO> getAllCategoriesPage(PageRequestCustom pageRequestCustom);
    CategoryDTO getCategoryById(Integer id);
    CategoryDTO saveCategory(SaveUpdateCategoryRequest saveUpdateCategoryRequest);
    CategoryDTO updateCategory(Integer idCate, SaveUpdateCategoryRequest saveUpdateCategoryRequest);
    CategoryDTO deleteCategoryById(Integer id);
}
