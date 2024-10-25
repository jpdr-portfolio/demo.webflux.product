package com.jpdr.apps.demo.webflux.product.exception.retailer;

public class RetailerNotFoundException extends RuntimeException{
  
  public RetailerNotFoundException(Integer retailerId, Throwable ex){
    super("The retailer " + retailerId + " wasn't found.", ex);
  }
  
}
