package com.reflux.store.controller;
import com.reflux.store.model.ProductCategory;
import com.reflux.store.service.ProductCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/category")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;
    private Long categoryId;

    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public List<ProductCategory> getCategoryList() {
        return categoryService.getCategoryList();
    }

    @PostMapping("/create")
    public String createCategory(@RequestBody ProductCategory category) {
        category.setId(categoryId++);
        categoryService.createCategory(category);
        return "Category Created Successfully";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            String status = categoryService.deleteCategory(id);
            return ResponseEntity.ok(status);
        }catch (ResponseStatusException e) {
            return new ResponseEntity<String>(e.getReason(), e.getStatusCode());
        }
    }
}
