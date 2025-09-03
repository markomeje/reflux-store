package com.reflux.store.services;
import com.reflux.store.dto.product.ProductCategoryDto;
import com.reflux.store.response.product.ProductCategoryResponse;

public interface ProductCategoryServiceInterface {
    ProductCategoryResponse getCategories(Integer page);
    ProductCategoryDto createCategory(ProductCategoryDto productCategoryDto);
    ProductCategoryDto deleteCategory(Long id);
    ProductCategoryDto updateCategory(ProductCategoryDto productCategoryDto, Long id);
}
