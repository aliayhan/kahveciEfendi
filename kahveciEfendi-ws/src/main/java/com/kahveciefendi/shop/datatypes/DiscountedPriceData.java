package com.kahveciefendi.shop.datatypes;

/**
 * This data type consists of the information of the total price, discount price, discount reason
 * and priceToPay.
 * 
 * @author Ayhan Dardagan
 *
 */
public class DiscountedPriceData {

  private double totalPrice;

  private double discount;

  private String discountReason;

  private double priceToPay;

  /**
   * Constructor.
   * 
   * @param totalPrice
   *          Total price
   * @param discount
   *          Discount
   * @param discountReason
   *          Discount reason
   * @param priceToPay
   *          Price to pay
   */
  public DiscountedPriceData(double totalPrice, double discount, String discountReason,
      double priceToPay) {
    this.totalPrice = totalPrice;
    this.discount = discount;
    this.discountReason = discountReason;
    this.priceToPay = priceToPay;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
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

  public double getPriceToPay() {
    return priceToPay;
  }

  public void setPriceToPay(double priceToPay) {
    this.priceToPay = priceToPay;
  }

}
