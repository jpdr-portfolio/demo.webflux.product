package com.jpdr.apps.demo.webflux.product.exception.product;

public class CategoryNotFoundException extends RuntimeException{
  
  public CategoryNotFoundException(Integer categoryId){
    super("The category " + categoryId + " wasn't found.");
  }
  
}
