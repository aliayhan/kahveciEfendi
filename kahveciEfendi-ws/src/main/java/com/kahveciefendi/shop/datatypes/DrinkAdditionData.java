package com.kahveciefendi.shop.datatypes;

/**
 * Drink addition data type which consists of drink addition name and id.
 * 
 * @author Ayhan Dardagan
 *
 */
public class DrinkAdditionData {

  private Long id;

  private String name;

  private String description;

  private double price;

  /**
   * Constructor.
   */
  public DrinkAdditionData() {
  }

  /**
   * Constructor.
   * 
   * @param id
   *          Drink addition id
   * @param name
   *          Drink addition name
   * @param description
   *          Drink addition description
   * @param price
   *          Drink addition price
   */
  public DrinkAdditionData(Long id, String name, String description, double price) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
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

}
