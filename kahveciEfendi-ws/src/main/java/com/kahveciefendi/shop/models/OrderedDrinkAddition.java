package com.kahveciefendi.shop.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Model representing a row in the table ordered_drink_additions.
 * 
 * @author Ayhan Dardagan
 *
 */
@Entity
@Table(name = "ordered_drink_additions")
public class OrderedDrinkAddition {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ordered_drink_addition_id", unique = true, nullable = false)
  private Long id;

  @Column(name = "date", length = 10, nullable = false)
  private String date;

  @Column(name = "drink_addition_name", length = 30, nullable = false)
  private String drinkAdditionName;

  @Column(name = "drink_addition_price", nullable = false)
  private double drinkAdditionPrice;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ordered_drink_id", nullable = false)
  private OrderedDrink parentOrderedDrink;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "drink_addition_id", nullable = true)
  private DrinkAddition parentDrinkAddition;

  /**
   * Constructor.
   */
  public OrderedDrinkAddition() {
  }

  /**
   * Constructor.
   * 
   * @param date
   *          Order date in format "YYYY-MM"
   * @param drinkAdditionName
   *          Drink addition name as it was in the drink table during the order
   * @param drinkAdditionPrice
   *          Drink addition price as it was in the drink table during the order
   * @param parentOrderedDrink
   *          {@code OrderedDrink} parent
   * @param parentDrinkAddition
   *          {@code DrinkAddition} parent
   */
  public OrderedDrinkAddition(String date, String drinkAdditionName, double drinkAdditionPrice,
      OrderedDrink parentOrderedDrink, DrinkAddition parentDrinkAddition) {
    this.date = date;
    this.drinkAdditionName = drinkAdditionName;
    this.drinkAdditionPrice = drinkAdditionPrice;
    this.parentOrderedDrink = parentOrderedDrink;
    this.parentDrinkAddition = parentDrinkAddition;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDrinkAdditionName() {
    return drinkAdditionName;
  }

  public void setDrinkAdditionName(String drinkAdditionName) {
    this.drinkAdditionName = drinkAdditionName;
  }

  public double getDrinkAdditionPrice() {
    return drinkAdditionPrice;
  }

  public void setDrinkAdditionPrice(double drinkAdditionPrice) {
    this.drinkAdditionPrice = drinkAdditionPrice;
  }

  public OrderedDrink getParentOrderedDrink() {
    return parentOrderedDrink;
  }

  public void setParentOrderedDrink(OrderedDrink parentOrderedDrink) {
    this.parentOrderedDrink = parentOrderedDrink;
  }

}
