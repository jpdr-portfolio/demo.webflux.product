package com.jpdr.apps.demo.webflux.product.service.dto.retailer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.NonNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RetailerDto {
  
  @JsonInclude(Include.NON_NULL)
  Integer id;
  @JsonInclude(Include.NON_NULL)
  String name;
  @JsonInclude(Include.NON_NULL)
  Integer sectorId;
  @JsonInclude(Include.NON_NULL)
  String sectorName;
  @JsonInclude(Include.NON_NULL)
  String email;
  @JsonInclude(Include.NON_NULL)
  String address;
  @JsonInclude(Include.NON_NULL)
  Boolean isActive;
  @JsonInclude(Include.NON_EMPTY)
  String creationDate;
  @JsonInclude(Include.NON_EMPTY)
  String deletionDate;
  
}
