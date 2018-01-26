package com.kahveciefendi.shop.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Model representing a row in the table drink_additions.
 * 
 * @author Ayhan Dardagan
 *
 */
@Entity
@Table(name = "drink_additions")
public class DrinkAddition {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "drink_addition_id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", length = 30, unique = true, nullable = false)
  private String name;

  @Column(name = "description", length = 60)
  private String description;

  @Column(name = "price", nullable = false)
  private double price;

  @OneToMany(mappedBy = "parentDrinkAddition", orphanRemoval = true, fetch = FetchType.LAZY)
  private List<OrderedDrinkAddition> orderedDrinkAdditions;

  /**
   * Constructor.
   */
  public DrinkAddition() {
    orderedDrinkAdditions = new ArrayList<OrderedDrinkAddition>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public List<OrderedDrinkAddition> getOrderedDrinkAdditions() {
    return orderedDrinkAdditions;
  }

  public void setOrderedDrinkAdditions(List<OrderedDrinkAddition> orderedDrinkAdditions) {
    this.orderedDrinkAdditions = orderedDrinkAdditions;
  }

}
