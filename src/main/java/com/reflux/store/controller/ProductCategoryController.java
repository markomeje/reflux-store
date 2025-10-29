package com.reflux.store.controller;
import com.reflux.store.config.PaginationConstants;
import com.reflux.store.dto.product.ProductCategoryDto;
import com.reflux.store.response.product.ProductCategoryResponse;
import com.reflux.store.services.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product/category")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public ResponseEntity<ProductCategoryResponse> getCategories(
        @RequestParam(name = "page", defaultValue = PaginationConstants.PAGE_NUMBER, required = false)
        Integer page
    ) {
        ProductCategoryResponse productCategoryResponse = categoryService.getCategories(page);
        return new ResponseEntity<>(productCategoryResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductCategoryDto> createCategory(@Valid @RequestBody ProductCategoryDto productCategoryDto) {
        ProductCategoryDto savedProductCategoryDto = categoryService.createCategory(productCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductCategoryDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ProductCategoryDto> deleteCategory(@PathVariable Long id) {
        ProductCategoryDto deletedCategory = categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(deletedCategory);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductCategoryDto> updateCategory(
        @Valid
        @RequestBody ProductCategoryDto productCategoryDto,
        @PathVariable Long id
    ) {
        ProductCategoryDto savedCategory = categoryService.updateCategory(productCategoryDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(savedCategory);
    }

}
