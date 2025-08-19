package com.reflux.store.services;
import com.reflux.store.payload.ProductCategoryDto;
import com.reflux.store.payload.ProductCategoryResponse;

public interface ProductCategoryServiceInterface {
    ProductCategoryResponse getCategories(Integer page);
    ProductCategoryDto createCategory(ProductCategoryDto productCategoryDto);
    ProductCategoryDto deleteCategory(Long id);
    ProductCategoryDto updateCategory(ProductCategoryDto productCategoryDto, Long id);
}
