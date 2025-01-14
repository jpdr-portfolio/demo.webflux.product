package com.jpdr.apps.demo.webflux.product.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RetailerDto implements Serializable {
  
  @JsonInclude(Include.NON_NULL)
  Long id;
  @JsonInclude(Include.NON_NULL)
  String retailerName;
  @JsonInclude(Include.NON_NULL)
  String retailerEmail;
  @JsonInclude(Include.NON_NULL)
  String retailerAddress;
  @JsonInclude(Include.NON_NULL)
  String retailerCity;
  @JsonInclude(Include.NON_NULL)
  String retailerCountry;
  @JsonInclude(Include.NON_NULL)
  Boolean isActive;
  @JsonInclude(Include.NON_EMPTY)
  String creationDate;
  @JsonInclude(Include.NON_EMPTY)
  String deletionDate;
  
}
