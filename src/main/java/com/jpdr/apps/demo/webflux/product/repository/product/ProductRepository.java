package com.jpdr.apps.demo.webflux.product.repository.product;

import com.jpdr.apps.demo.webflux.product.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
  
  Mono<Product> findByIdAndIsActiveTrue(Integer productId);
  Flux<Product> findAllByIsActiveTrue();
  Flux<Product> findByCategoryIdAndIsActiveTrue(Integer categoryId);
  Flux<Product> findByRetailerIdAndIsActiveTrue(Integer retailerId);
  
}
