package com.kahveciefendi.shop.services;

import java.util.List;

import com.kahveciefendi.shop.datatypes.DiscountedPriceData;
import com.kahveciefendi.shop.datatypes.DrinkAdditionData;
import com.kahveciefendi.shop.datatypes.DrinkData;
import com.kahveciefendi.shop.datatypes.OrderData;

/**
 * DrinkService interface.
 * 
 * @author Ayhan Dardagan
 *
 */
public interface DrinkService {

  /**
   * Save update drink
   * 
   * @param drink
   *          Drink
   * @return True if successful
   */
  boolean saveUpdateDrink(DrinkData drinkData);

  /**
   * Save update drink addition
   * 
   * @param drinkAddition
   *          Drink addition
   * @return True if successful
   */
  boolean saveUpdateDrinkAddition(DrinkAdditionData drinkAdditionData);

  /**
   * Delete drink
   * 
   * @param drink
   *          Drink
   * @return True if successful
   */
  boolean deleteDrink(DrinkData drinkData);

  /**
   * Delete drink addition
   * 
   * @param drinkAddition
   *          Drink addition
   * @return True if successful
   */
  boolean deleteDrinkAddition(DrinkAdditionData drinkAdditionData);
  
  /**
   * Getting all drinks with their pre-configured additions.
   * 
   * @return List of {@link DrinkData}
   */
  List<DrinkData> getAllDrinks();

  /**
   * Getting all drink additions.
   * 
   * @return List of {@link DrinkAdditionData}
   */
  List<DrinkAdditionData> getAllDrinkAdditions();

  /**
   * Calculates discount based on potential order.
   * 
   * @param orderDatas
   *          List of {@code OrderData}
   */
  DiscountedPriceData calculateDiscount(List<OrderData> orderDatas);
  
  /**
   * Give order which consists of drinks and additions.
   * 
   * @param authenticatedUserName
   *          Name of the authenticated user
   * @param orderDatas
   *          List of {@code OrderData}
   * @return True if successful
   */
  boolean giveOrder(String authenticatedUserName, List<OrderData> orderDatas);

}