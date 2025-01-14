package com.jpdr.apps.demo.webflux.product.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Data
@Table("retailer")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Retailer {
  
  @Id
  @Column("id")
  Long id;
  @Column("retailer_name")
  String retailerName;
  @Column("retailer_email")
  String retailerEmail;
  @Column("retailer_address")
  String retailerAddress;
  @Column("retailer_city")
  String retailerCity;
  @Column("retailer_country")
  String retailerCountry;
  @Column("is_active")
  Boolean isActive;
  @Column("creation_date")
  OffsetDateTime creationDate;
  @Column("deletion_date")
  OffsetDateTime deletionDate;
  
}
