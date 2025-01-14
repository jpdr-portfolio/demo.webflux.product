package com.jpdr.apps.demo.webflux.product.exception;

public class RetailerNotFoundException extends RuntimeException{
  
  public RetailerNotFoundException(Long retailerId){
    super("The retailer " + retailerId + " wasn't found.");
  }
  
}
