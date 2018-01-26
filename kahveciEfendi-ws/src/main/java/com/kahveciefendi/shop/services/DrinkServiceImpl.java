package com.kahveciefendi.shop.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kahveciefendi.shop.daos.DrinkDao;
import com.kahveciefendi.shop.datatypes.DiscountedPriceData;
import com.kahveciefendi.shop.datatypes.DrinkAdditionData;
import com.kahveciefendi.shop.datatypes.DrinkData;
import com.kahveciefendi.shop.datatypes.OrderData;

/**
 * Drink service which encapsulates dao.
 * 
 * @author Ayhan Dardagan
 *
 */
@Service
@Transactional
public class DrinkServiceImpl implements DrinkService {

  private static final Logger logger = LoggerFactory.getLogger(DrinkServiceImpl.class);

  @Autowired
  DrinkDao drinkDao;

  @Override
  public boolean saveUpdateDrink(DrinkData drinkData) {
    logger.info("Saving/updating drink");
    return drinkDao.saveUpdateDrink(drinkData);
  }

  @Override
  public boolean saveUpdateDrinkAddition(DrinkAdditionData drinkAdditionData) {
    logger.info("Saving/updating drink addition");
    return drinkDao.saveUpdateDrinkAddition(drinkAdditionData);
  }

  @Override
  public boolean deleteDrink(DrinkData drinkData) {
    logger.info("Deleting drink");
    return drinkDao.deleteDrink(drinkData);
  }

  @Override
  public boolean deleteDrinkAddition(DrinkAdditionData drinkAdditionData) {
    logger.info("Deleting drink addition");
    return drinkDao.deleteDrinkAddition(drinkAdditionData);
  }
  
  @Override
  public List<DrinkData> getAllDrinks() {
    logger.info("Getting all drinks");
    return drinkDao.findAllDrinks();
  }

  @Override
  public List<DrinkAdditionData> getAllDrinkAdditions() {
    logger.info("Getting all drink additions");
    return drinkDao.findAllDrinkAdditions();
  }

  @Override
  public DiscountedPriceData calculateDiscount(List<OrderData> orderDatas) {
    return drinkDao.calculateDiscount(orderDatas);
  }

  @Override
  public boolean giveOrder(String authenticatedUserName, List<OrderData> orderDatas) {
    logger.info("Give order");
    return drinkDao.saveOrder(authenticatedUserName, orderDatas);
  }

}