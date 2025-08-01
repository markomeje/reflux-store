package com.reflux.store.controller;
import com.reflux.store.model.ProductCategory;
import com.reflux.store.service.ProductCategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product/category")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public List<ProductCategory> getCategoryList() {
        return categoryService.getCategoryList();
    }

    @PostMapping("/create")
    public String createCategory(@RequestBody ProductCategory category) {
        categoryService.createCategory(category);
        return "Created Category";
    }
}
