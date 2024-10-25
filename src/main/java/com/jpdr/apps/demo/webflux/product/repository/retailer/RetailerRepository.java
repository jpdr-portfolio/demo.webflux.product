package com.jpdr.apps.demo.webflux.product.repository.retailer;

import com.jpdr.apps.demo.webflux.product.service.dto.retailer.RetailerDto;
import reactor.core.publisher.Mono;

public interface RetailerRepository {

  Mono<RetailerDto> getRetailerById(Integer retailerId);

}
