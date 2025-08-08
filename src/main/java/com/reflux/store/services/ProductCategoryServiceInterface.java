package com.reflux.store.services;
import com.reflux.store.models.ProductCategory;
import java.util.List;

public interface ProductCategoryServiceInterface {
    List<ProductCategory> getCategoryList();

    void createCategory(ProductCategory category);

    String deleteCategory(Long id);

    void updateCategory(ProductCategory category, Long id);
}
