package com.kahveciefendi.shop.daos;

import java.util.List;

import com.kahveciefendi.shop.datatypes.DiscountedPriceData;
import com.kahveciefendi.shop.datatypes.DrinkAdditionData;
import com.kahveciefendi.shop.datatypes.DrinkData;
import com.kahveciefendi.shop.datatypes.OrderData;

/**
 * DrinkDao interface.
 * 
 * @author Ayhan Dardagan
 *
 */
public interface DrinkDao {

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
   * Find all drinks with their pre-configured drink additions.
   * 
   * @return List of {@link DrinkData}
   */
  List<DrinkData> findAllDrinks();

  /**
   * Find all drink additions.
   * 
   * @return List of {@link DrinkAdditionData}
   */
  List<DrinkAdditionData> findAllDrinkAdditions();

  /**
   * Calculates discount based on potential order.
   * 
   * @param orderDatas
   *          List of {@code OrderData}
   */
  DiscountedPriceData calculateDiscount(List<OrderData> orderDatas);

  /**
   * Save order which consists of drinks and additions.
   * 
   * @param authenticatedUserName
   *          Name of the authenticated user
   * @param orderDatas
   *          List of {@code OrderData}
   * @return True if successful
   */
  boolean saveOrder(String authenticatedUserName, List<OrderData> orderDatas);

}