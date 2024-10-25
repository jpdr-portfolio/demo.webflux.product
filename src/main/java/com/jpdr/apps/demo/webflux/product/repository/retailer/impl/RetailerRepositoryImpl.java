package com.jpdr.apps.demo.webflux.product.repository.retailer.impl;

import com.jpdr.apps.demo.webflux.product.exception.retailer.RetailerNotFoundException;
import com.jpdr.apps.demo.webflux.product.exception.retailer.RetailerRepositoryException;
import com.jpdr.apps.demo.webflux.product.repository.retailer.RetailerRepository;
import com.jpdr.apps.demo.webflux.product.service.dto.retailer.RetailerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class RetailerRepositoryImpl implements RetailerRepository {
  
  private final WebClient webClient;
  
  public RetailerRepositoryImpl(@Qualifier(value = "retailerWebClient") WebClient webClient ){
    this.webClient = webClient;
  }
  
  
  @Override
  public Mono<RetailerDto> getRetailerById(Integer retailerId) {
    return this.webClient.get()
      .uri("/retailers/{retailerId}", retailerId)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .retrieve()
      .onStatus(HttpStatus.NOT_FOUND::equals,
        response -> response.createException()
          .map(error -> new RetailerNotFoundException(retailerId,error)))
      .onStatus(HttpStatusCode::isError,
        response -> response.createException()
          .map(error -> new RetailerRepositoryException(retailerId, error))
      )
      .bodyToMono(RetailerDto.class);
  }
  
}
