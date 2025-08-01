package com.reflux.store.service;
import com.reflux.store.model.ProductCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCategoryService implements ProductCategoryServiceInterface {

    private final List<ProductCategory> categories = new ArrayList<>();

    @Override
    public List<ProductCategory> getCategoryList() {
        return this.categories;
    }

    @Override
    public void createCategory(ProductCategory category) {
        this.categories.add(category);
    }
}
