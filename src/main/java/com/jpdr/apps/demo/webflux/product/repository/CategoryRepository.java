package com.jpdr.apps.demo.webflux.product.repository;

import com.jpdr.apps.demo.webflux.product.model.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
  
  Mono<Category> findByIdAndIsActiveTrue(Long categoryId);
  Flux<Category> findAllByIsActiveTrue();
  
}
