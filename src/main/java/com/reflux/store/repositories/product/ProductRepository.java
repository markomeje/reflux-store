package com.reflux.store.repositories.product;
import com.reflux.store.models.Product;
import com.reflux.store.models.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory productCategory);
    List<Product> findByNameLikeIgnoreCase(String name);
    Product findByName(String name);
}
