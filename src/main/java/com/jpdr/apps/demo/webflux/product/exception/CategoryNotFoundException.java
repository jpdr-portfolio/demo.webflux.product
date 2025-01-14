package com.jpdr.apps.demo.webflux.product.exception;

public class CategoryNotFoundException extends RuntimeException{
  
  public CategoryNotFoundException(Long categoryId){
    super("The category " + categoryId + " wasn't found.");
  }
  
}
