package com.reflux.store.services;
import com.reflux.store.exception.ResourceNotFoundException;
import com.reflux.store.models.ProductCategory;
import com.reflux.store.payload.ProductCategoryDto;
import com.reflux.store.payload.ProductCategoryResponse;
import com.reflux.store.repositories.ProductCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ProductCategoryService implements ProductCategoryServiceInterface {

    private final ProductCategoryRepository productCategoryRepository;

    private final ModelMapper modelMapper;

    public ProductCategoryService (ProductCategoryRepository productCategoryRepository, ModelMapper modelMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductCategoryResponse getCategories(Integer page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page != null ? page : 0);
        Page<ProductCategory> categoryPage = productCategoryRepository.findAll(pageable);

        List<ProductCategory> categories = categoryPage.getContent();
        if(categories.isEmpty()) {
            throw new ResourceNotFoundException("No categories found");
        }

        List<ProductCategoryDto> categoryDtos = categories.stream()
            .map(category -> modelMapper.map(category, ProductCategoryDto.class))
            .toList();

        ProductCategoryResponse productCategoryResponse = new ProductCategoryResponse();
        productCategoryResponse.setContent(categoryDtos);
        productCategoryResponse.setPage(categoryPage.getNumber());
        productCategoryResponse.setSize(categoryPage.getSize());
        productCategoryResponse.setTotalElements(categoryPage.getTotalElements());
        productCategoryResponse.setTotalPages(categoryPage.getTotalPages());
        productCategoryResponse.setLastPage(categoryPage.isLast());
        return productCategoryResponse;
    }

    @Override
    public ProductCategoryDto createCategory(ProductCategoryDto productCategoryDto) {
        ProductCategory category = modelMapper.map(productCategoryDto, ProductCategory.class);

        ProductCategory existingCategory = productCategoryRepository.findByName(productCategoryDto.getName());
        if(existingCategory != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Category name already exists");
        }

        ProductCategory savedCategory =  productCategoryRepository.save(category);
        return modelMapper.map(savedCategory, ProductCategoryDto.class);
    }

    @Override
    public ProductCategoryDto deleteCategory(Long id) {
        ProductCategory existingCategory = productCategoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        productCategoryRepository.delete(existingCategory);
        return modelMapper.map(existingCategory, ProductCategoryDto.class);
    }

    @Override
    public ProductCategoryDto updateCategory(ProductCategoryDto productCategoryDto, Long id) {
        ProductCategory existingCategory = productCategoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        ProductCategory category = modelMapper.map(productCategoryDto, ProductCategory.class);
        category.setId(id);
        productCategoryRepository.save(existingCategory);
        return modelMapper.map(existingCategory, ProductCategoryDto.class);
    }
}
