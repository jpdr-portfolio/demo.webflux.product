package com.jpdr.apps.demo.webflux.product.repository;

import com.jpdr.apps.demo.webflux.product.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
  
  Mono<Product> findByIdAndIsActiveTrue(Long productId);
  Flux<Product> findAllByIsActiveTrue();
  Flux<Product> findByCategoryIdAndIsActiveTrue(Long categoryId);
  Flux<Product> findBySubCategoryIdAndIsActiveTrue(Long subCategoryId);
  Flux<Product> findByRetailerIdAndIsActiveTrue(Long retailerId);
  
}
