package com.jpdr.apps.demo.webflux.product.exception.product;

public class ProductNotFoundException extends RuntimeException{
  
  public ProductNotFoundException(Integer productId){
    super("The product " + productId + " wasn't found.");
  }
  
}
