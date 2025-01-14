package com.jpdr.apps.demo.webflux.product.exception;

public class ProductNotFoundException extends RuntimeException{
  
  public ProductNotFoundException(Long productId){
    super("The product " + productId + " wasn't found.");
  }
  
}
