package com.kahveciefendi.shop.datatypes;

import java.util.List;

/**
 * Order data which consists of drink id, drink additions ids and drink amount.
 * 
 * @author Ayhan Dardagan
 *
 */
public class OrderData {

  private Long id;

  private List<Long> additionIds;

  private Long amount;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<Long> getAdditionIds() {
    return additionIds;
  }

  public void setAdditionIds(List<Long> additionIds) {
    this.additionIds = additionIds;
  }

  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

}
