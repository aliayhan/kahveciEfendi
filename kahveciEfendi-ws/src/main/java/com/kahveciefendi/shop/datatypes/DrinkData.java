package com.kahveciefendi.shop.datatypes;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Drink data type which consists of a drink and its possible drink additions.
 * 
 * @author Ayhan Dardagan
 *
 */
@JsonIgnoreProperties(value = {"amounts", "totalPrice"})
public class DrinkData {

  Long id;

  String name;

  String description;

  double price;

  List<DrinkAdditionData> additions;

  /**
   * Constructor.
   */
  public DrinkData() {
    this.additions = new ArrayList<DrinkAdditionData>(20);
  }

  /**
   * Constructor.
   * 
   * @param name
   *          Drink name
   * @param description
   *          Drink description
   * @param price
   *          Drink price
   */
  public DrinkData(Long id, String name, String description, double price) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.additions = new ArrayList<DrinkAdditionData>(20);
  }

  /**
   * Add drink addition.
   * 
   * @param drinkAddition
   *          Drink addition of type {@link DrinkAdditionData}
   */
  public void addDrinkAddition(DrinkAdditionData drinkAddition) {
    additions.add(drinkAddition);
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

  public List<DrinkAdditionData> getAdditions() {
    return additions;
  }

  public void setAdditions(List<DrinkAdditionData> additions) {
    this.additions = additions;
  }

}
