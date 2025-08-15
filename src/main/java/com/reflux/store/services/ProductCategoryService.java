package com.reflux.store.services;
import com.reflux.store.models.ProductCategory;
import com.reflux.store.repositories.ProductCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ProductCategoryService implements ProductCategoryServiceInterface {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService (ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public List<ProductCategory> getCategoryList() {
        return productCategoryRepository.findAll();
    }

    @Override
    public void createCategory(ProductCategory category) {
        ProductCategory savedCategory = productCategoryRepository.findByName(category.getName());
        if(savedCategory != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Category name already exists");
        }

        productCategoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        ProductCategory category = productCategoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        productCategoryRepository.delete(category);
        return "Category Deleted Successfully";
    }

    @Override
    public void updateCategory(ProductCategory category, Long id) {
        productCategoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        category.setId(category.getId());
        productCategoryRepository.save(category);
    }
}
