package com.reflux.store.services;
import com.reflux.store.models.ProductCategory;
import com.reflux.store.payload.ProductCategoryResponse;

public interface ProductCategoryServiceInterface {
    ProductCategoryResponse getCategoryList();
    void createCategory(ProductCategory category);
    String deleteCategory(Long id);
    void updateCategory(ProductCategory category, Long id);
}
