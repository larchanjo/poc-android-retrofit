package br.com.app.domain;

import java.util.Date;
import lombok.Data;

@Data
public class Product {

  private Integer id;
  private Date createdAt;
  private String name;
  private Double price;

}