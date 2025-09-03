package com.reflux.store.services;
import com.reflux.store.dto.product.ProductDto;
import com.reflux.store.exception.ResourceNotFoundException;
import com.reflux.store.models.Product;
import com.reflux.store.models.ProductCategory;
import com.reflux.store.repositories.ProductCategoryRepository;
import com.reflux.store.repositories.ProductRepository;
import com.reflux.store.response.product.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService implements ProductServiceInterface {

    private final ProductCategoryRepository productCategoryRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public ProductService(
        ModelMapper modelMapper,
        ProductCategoryRepository productCategoryRepository,
        ProductRepository productRepository
    ) {
        this.modelMapper = modelMapper;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }

    public ProductResponse getProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        ProductResponse  productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    public ProductDto createProduct(Product product, Long productCategoryId) {
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

        product.setProductCategory(productCategory);
        double discount = (product.getDiscount() * 0.01) * product.getPrice();
        double specialPrice = Math.round(product.getPrice() - discount);
        product.setSpecialPrice(specialPrice);
        product.setImage("product.png");

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    public ProductResponse getProductsByCategory(Long productCategoryId) {
        ProductCategory category =  productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

        List<Product> products = productRepository.findByProductCategory(category);
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        ProductResponse  productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }
}
