package com.jpdr.apps.demo.webflux.product.repository;

import com.jpdr.apps.demo.webflux.product.model.Retailer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RetailerRepository extends ReactiveCrudRepository<Retailer, Long> {
  
  Mono<Retailer> findByIdAndIsActiveIsTrue(Long id);
  Flux<Retailer> findAllByIsActiveIsTrueOrderById();
  
}
