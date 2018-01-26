package com.kahveciefendi.shop.services;

import java.util.List;

import com.kahveciefendi.shop.datatypes.ReportData;

/**
 * ReportService interface.
 * 
 * @author Ayhan Dardagan
 *
 */
public interface ReportService {

  /**
   * Getting orders for given user login name.
   * 
   * @param loginName
   *          Login name
   * @return Report of orders
   */
  ReportData getOrdersByLoginName(String loginName);

  /**
   * Getting all ordered drink names.
   * 
   * @return List of ordered drink names
   */
  List<String> getAllOrderedDrinkNames();
  
  /**
   * Getting drink orders for given drink name.
   * 
   * @return Report of ordered drinks
   */
  ReportData getOrderedDrinksByDrinkName(String drinkName);
  
  /**
   * Getting all ordered drink addition names.
   * 
   * @return List of ordered drink addition names
   */
  List<String> getAllOrderedDrinkAdditionNames();

  /**
   * Getting drink addition orders for given drink addition name.
   * 
   * @return Report of ordered drink additions
   */
  ReportData getOrderedDrinksByDrinkAdditionName(String drinkAdditionName);

}