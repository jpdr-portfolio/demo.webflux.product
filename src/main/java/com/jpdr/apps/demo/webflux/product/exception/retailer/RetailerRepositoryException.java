package com.jpdr.apps.demo.webflux.product.exception.retailer;

public class RetailerRepositoryException extends RuntimeException{
  
  public RetailerRepositoryException(Integer retailerId, Throwable ex){
    super("There was an error while retrieving the retailer " + retailerId, ex);
  }
  
}
