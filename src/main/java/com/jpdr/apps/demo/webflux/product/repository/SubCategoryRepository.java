package com.jpdr.apps.demo.webflux.product.repository;

import com.jpdr.apps.demo.webflux.product.model.SubCategory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SubCategoryRepository extends ReactiveCrudRepository<SubCategory, Long> {
  
  Mono<SubCategory> findByIdAndIsActiveTrue(Long subCategoryId);
  Flux<SubCategory> findAllByIsActiveTrue();
  
}
