package com.jpdr.apps.demo.webflux.product.repository;

import com.jpdr.apps.demo.webflux.product.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
  
  Mono<Product> findByIdAndIsActiveTrue(Long productId);
  Flux<Product> findAllByIsActiveTrueOrderById();
  Flux<Product> findByCategoryIdAndIsActiveTrueOrderById(Long categoryId);
  Flux<Product> findBySubCategoryIdAndIsActiveTrueOrderById(Long subCategoryId);
  Flux<Product> findByRetailerIdAndIsActiveTrueOrderById(Long retailerId);
  
}
