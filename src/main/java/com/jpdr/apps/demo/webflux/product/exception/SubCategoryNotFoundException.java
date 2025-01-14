package com.jpdr.apps.demo.webflux.product.exception;

public class SubCategoryNotFoundException extends RuntimeException{
  
  public SubCategoryNotFoundException(Long subCategoryId){
    super("The subcategory " + subCategoryId + " wasn't found.");
  }
  
}
