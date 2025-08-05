package com.reflux.store.service;
import com.reflux.store.model.ProductCategory;
import java.util.List;

public interface ProductCategoryServiceInterface {
    List<ProductCategory> getCategoryList();

    void createCategory(ProductCategory category);

    String deleteCategory(Long id);
}
