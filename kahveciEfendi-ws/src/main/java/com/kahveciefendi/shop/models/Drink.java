package com.kahveciefendi.shop.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Model representing a row in the table drinks.
 * 
 * @author Ayhan Dardagan
 *
 */
@Entity
@Table(name = "drinks")
public class Drink {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "drink_id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", length = 30, unique = true, nullable = false)
  private String name;

  @Column(name = "description", length = 60)
  private String description;

  @Column(name = "price", nullable = false)
  private double price;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "drink_drink_additions", //
      joinColumns = { @JoinColumn(name = "drink_id") }, //
      inverseJoinColumns = { @JoinColumn(name = "drink_addition_id") })
  private List<DrinkAddition> drinkAdditions;

  @OneToMany(mappedBy = "parentDrink", orphanRemoval = true, fetch = FetchType.LAZY)
  private List<OrderedDrink> orderedDrinks;

  /**
   * Constructor.
   */
  public Drink() {
    drinkAdditions = new ArrayList<DrinkAddition>();
    orderedDrinks = new ArrayList<OrderedDrink>();
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

  public List<DrinkAddition> getDrinkAdditions() {
    return drinkAdditions;
  }

  public void setDrinkAdditions(List<DrinkAddition> drinkAdditions) {
    this.drinkAdditions = drinkAdditions;
  }

  public List<OrderedDrink> getOrderedDrinks() {
    return orderedDrinks;
  }

  public void setOrderedDrinks(List<OrderedDrink> orderedDrinks) {
    this.orderedDrinks = orderedDrinks;
  }

}