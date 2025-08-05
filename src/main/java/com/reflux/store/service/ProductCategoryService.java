package com.reflux.store.service;
import com.reflux.store.model.ProductCategory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCategoryService implements ProductCategoryServiceInterface {

    private final List<ProductCategory> categories = new ArrayList<>();

    @Override
    public List<ProductCategory> getCategoryList() {
        return categories;
    }

    @Override
    public void createCategory(ProductCategory category) {
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long id) {
        ProductCategory category = categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        categories.remove(category);
        return "Category Deleted Successfully";
    }
}
