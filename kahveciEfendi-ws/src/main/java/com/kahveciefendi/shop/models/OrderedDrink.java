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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Model representing a row in the table ordered_drinks.
 * 
 * @author Ayhan Dardagan
 *
 */
@Entity
@Table(name = "ordered_drinks")
public class OrderedDrink {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ordered_drink_id", unique = true, nullable = false)
  private Long id;

  @Column(name = "date", length = 10, nullable = false)
  private String date;

  @Column(name = "total_price_with_amount_and_additions", nullable = false)
  private double totalPrice;

  @Column(name = "amount", nullable = false)
  private Long amount;

  @Column(name = "drink_name", length = 30, nullable = false)
  private String drinkName;

  @Column(name = "drink_price", nullable = false)
  private double drinkPrice;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id", nullable = false)
  private Order parentOrder;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "drink_id", nullable = true)
  private Drink parentDrink;

  @OneToMany(mappedBy = "parentOrderedDrink", cascade = CascadeType.ALL, orphanRemoval = true, //
      fetch = FetchType.LAZY)
  @Column(nullable = false)
  private List<OrderedDrinkAddition> orderedDrinkAdditions;

  /**
   * Constructor.
   */
  public OrderedDrink() {
    this.orderedDrinkAdditions = new ArrayList<OrderedDrinkAddition>(10);
  }

  /**
   * Constructor.
   * 
   * @param date
   *          Order date in format "YYYY-MM"
   * @param amount
   *          How many drinks were ordered
   * @param drinkName
   *          Drink name as it was in the drink table during the order
   * @param drinkPrice
   *          Drink price as it was in the drink table during the order
   * @param parentOrder
   *          {@code Order} parent
   * @param parentDrink
   *          {@code Drink} parent
   */
  public OrderedDrink(String date, Long amount, String drinkName, double drinkPrice,
      Order parentOrder, Drink parentDrink) {
    this.date = date;
    this.amount = amount;
    this.drinkName = drinkName;
    this.drinkPrice = drinkPrice;
    this.parentOrder = parentOrder;
    this.parentDrink = parentDrink;
    this.orderedDrinkAdditions = new ArrayList<OrderedDrinkAddition>(10);
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

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

  public String getDrinkName() {
    return drinkName;
  }

  public void setDrinkName(String drinkName) {
    this.drinkName = drinkName;
  }

  public double getDrinkPrice() {
    return drinkPrice;
  }

  public void setDrinkPrice(double drinkPrice) {
    this.drinkPrice = drinkPrice;
  }

  public Order getParentOrder() {
    return parentOrder;
  }

  public void setParentOrder(Order parentOrder) {
    this.parentOrder = parentOrder;
  }

  public List<OrderedDrinkAddition> getOrderedDrinkAdditions() {
    return orderedDrinkAdditions;
  }

  public void setOrderedDrinkAdditions(List<OrderedDrinkAddition> orderedDrinkAdditions) {
    this.orderedDrinkAdditions = orderedDrinkAdditions;
  }

}
