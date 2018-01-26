package com.kahveciefendi.shop.daos;

import java.util.List;

import com.kahveciefendi.shop.datatypes.ReportData;

/**
 * ReportDao interface.
 * 
 * @author Ayhan Dardagan
 *
 */
public interface ReportDao {

  /**
   * Find orders for given user login name.
   * 
   * @param loginName
   *          Login name
   * @return Report of orders
   */
  ReportData findOrdersByLoginName(String loginName);

  /**
   * Finds all ordered drink names.
   * 
   * @return List of ordered drink names
   */
  List<String> findAllOrderedDrinkNames();

  /**
   * Find drink orders for given drink name.
   * 
   * @return Report of ordered drinks
   */
  ReportData findOrderedDrinksByDrinkName(String drinkName);

  /**
   * Finds all ordered drink addition names.
   * 
   * @return List of ordered drink addition names
   */
  List<String> findAllOrderedDrinkAdditionNames();

  /**
   * Find drink addition orders for given drink addition name.
   * 
   * @return Report of ordered drink additions
   */
  ReportData findOrderedDrinkAdditionsByDrinkAdditionName(String drinkAdditionName);
  
}
