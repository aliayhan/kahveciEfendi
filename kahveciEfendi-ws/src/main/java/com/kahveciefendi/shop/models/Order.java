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
 * Model representing a row in the table orders.
 * 
 * @author Ayhan Dardagan
 *
 */
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "order_id", unique = true, nullable = false)
  private Long id;

  @Column(name = "date", length = 10, nullable = false)
  private String date;

  @Column(name = "total_price", nullable = false)
  private double totalPrice;

  @Column(name = "discount", nullable = false)
  private double discount;

  @Column(name = "discount_reason")
  private String discountReason;

  @Column(name = "total_price_with_discount", nullable = false)
  private double totalPriceWithDiscount;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private User parentUser;

  @OneToMany(mappedBy = "parentOrder", cascade = CascadeType.ALL, orphanRemoval = true, //
      fetch = FetchType.LAZY)
  @Column(nullable = false)
  private List<OrderedDrink> orderedDrinks;

  /**
   * Constructor.
   */
  public Order() {
    this.orderedDrinks = new ArrayList<OrderedDrink>(10);
  }

  /**
   * Constructor.
   * 
   * @param date
   *          Order date in format "YYYY-MM"
   * @param parentUser
   *          {@code User} parent
   */
  public Order(String date, User parentUser) {
    this.date = date;
    this.parentUser = parentUser;
    this.orderedDrinks = new ArrayList<OrderedDrink>(10);
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

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public String getDiscountReason() {
    return discountReason;
  }

  public void setDiscountReason(String discountReason) {
    this.discountReason = discountReason;
  }

  public double getTotalPriceWithDiscount() {
    return totalPriceWithDiscount;
  }

  public void setTotalPriceWithDiscount(double totalPriceWithDiscount) {
    this.totalPriceWithDiscount = totalPriceWithDiscount;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public User getParentUser() {
    return parentUser;
  }

  public void setParentUser(User parentUser) {
    this.parentUser = parentUser;
  }

  public List<OrderedDrink> getOrderedDrinks() {
    return orderedDrinks;
  }

  public void setOrderedDrinks(List<OrderedDrink> orderedDrinks) {
    this.orderedDrinks = orderedDrinks;
  }

}
