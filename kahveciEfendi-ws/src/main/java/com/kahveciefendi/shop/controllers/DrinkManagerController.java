package com.kahveciefendi.shop.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kahveciefendi.shop.datatypes.DiscountedPriceData;
import com.kahveciefendi.shop.datatypes.DrinkAdditionData;
import com.kahveciefendi.shop.datatypes.DrinkData;
import com.kahveciefendi.shop.datatypes.VoidEmptyJson;
import com.kahveciefendi.shop.datatypes.OrderData;
import com.kahveciefendi.shop.services.DrinkService;

/**
 * Handles drink management requests.
 * 
 * @author Ayhan Dardagan
 *
 */
@RestController
public class DrinkManagerController {

  private static final Logger logger = LoggerFactory.getLogger(DrinkManagerController.class);

  @Autowired
  private DrinkService drinkService;

  /**
   * Post request on /saveupdatedrink.
   * 
   * @return {@link DrinkData} as JSON
   */
  @PostMapping(value = "/saveupdatedrink", produces = "application/json")
  public ResponseEntity<VoidEmptyJson> postSaveUpdateDrink(@RequestBody DrinkData drinkData) {

    logger.info("Save/update drink");
    if (!drinkService.saveUpdateDrink(drinkData)) {
      logger.error("Save/update drink unsuccessful");
      return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), NOT_MODIFIED);
    }

    return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), CREATED);
  }

  /**
   * Post request on /saveupdatedrinkaddition.
   * 
   * @return {@link DrinkAdditionData} as JSON
   */
  @PostMapping(value = "/saveupdatedrinkaddition", produces = "application/json")
  public ResponseEntity<VoidEmptyJson> postSaveUpdateDrinkAddition(
      @RequestBody DrinkAdditionData drinkAdditionData) {

    logger.info("Save/update drink addition");
    if (!drinkService.saveUpdateDrinkAddition(drinkAdditionData)) {
      logger.error("Save/update drink addition unsuccessful");
      return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), NOT_MODIFIED);
    }

    return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), CREATED);
  }

  /**
   * Post request on /deletedrink.
   * 
   * @return {@link DrinkData} as JSON
   */
  @PostMapping(value = "/deletedrink", produces = "application/json")
  public ResponseEntity<VoidEmptyJson> postDeleteDrink(@RequestBody DrinkData drinkData) {

    logger.info("Delete drink");
    if (!drinkService.deleteDrink(drinkData)) {
      logger.error("Delete drink unsuccessful");
      return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), NOT_MODIFIED);
    }

    return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), CREATED);
  }

  /**
   * Post request on /deletedrinkaddition.
   * 
   * @return {@link DrinkAdditionData} as JSON
   */
  @PostMapping(value = "/deletedrinkaddition", produces = "application/json")
  public ResponseEntity<VoidEmptyJson> postDeleteDrinkAddition(
      @RequestBody DrinkAdditionData drinkAdditionData) {

    logger.info("Delete drink addition");
    if (!drinkService.deleteDrinkAddition(drinkAdditionData)) {
      logger.error("Delete drink addition unsuccessful");
      return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), NOT_MODIFIED);
    }

    return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), CREATED);
  }

  /**
   * Post request on /drinks.
   * 
   * @return List of {@link DrinkData} as JSON
   */
  @PostMapping(value = "/drinks", produces = "application/json")
  public ResponseEntity<List<DrinkData>> postDrinks() {

    logger.info("Requesting all drinks");
    List<DrinkData> drinks = drinkService.getAllDrinks();

    if (drinks == null) {
      logger.info("No drinks found");
      return new ResponseEntity<List<DrinkData>>(NO_CONTENT);
    }

    return new ResponseEntity<List<DrinkData>>(drinks, OK);
  }

  /**
   * Post request on /drinkadditions.
   * 
   * @return List of {@link DrinkAdditionData} names as JSON
   */
  @PostMapping(value = "/drinkadditions", produces = "application/json")
  public ResponseEntity<List<DrinkAdditionData>> postDrinkAdditions() {

    logger.info("Requesting all drink additions");
    List<DrinkAdditionData> drinkAdditionNames = drinkService.getAllDrinkAdditions();

    if (drinkAdditionNames == null) {
      logger.info("No drink addtions found");
      return new ResponseEntity<List<DrinkAdditionData>>(NO_CONTENT);
    }

    return new ResponseEntity<List<DrinkAdditionData>>(drinkAdditionNames, OK);
  }

  /**
   * Post request on /calculatediscount.
   * 
   * @param orders
   *          Orders in JSON
   * @return {@link DiscountedPriceData} in JSON
   */
  @PostMapping(value = "/calculatediscount", produces = "application/json")
  public ResponseEntity<DiscountedPriceData> postCalculatediscount(
      @RequestBody List<OrderData> orders) {

    if (orders == null || orders.size() == 0) {
      logger.info("User didnt provide parameters");
      return new ResponseEntity<DiscountedPriceData>(PRECONDITION_FAILED);
    }

    logger.info("Calculate discount");
    DiscountedPriceData discount = drinkService.calculateDiscount(orders);

    if (discount == null) {
      logger.error("Calculating discount unsuccessful");
      return new ResponseEntity<DiscountedPriceData>(NO_CONTENT);
    }

    return new ResponseEntity<DiscountedPriceData>(discount, OK);
  }

  /**
   * Post request on /giveorder.
   * 
   * @param orders
   *          Orders in JSON
   * @return HttpStatus.OK if successful
   */
  @PostMapping(value = "/giveorder", produces = "application/json")
  public ResponseEntity<VoidEmptyJson> postGiveOrder(@RequestBody List<OrderData> orders) {

    if (orders == null || orders.size() == 0) {
      logger.info("User didnt provide parameters");
      return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), PRECONDITION_FAILED);
    }

    org.springframework.security.core.userdetails.User authenticatedUser = //
        (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

    logger.info("Give order");
    if (!drinkService.giveOrder(authenticatedUser.getUsername(), orders)) {
      logger.error("Order unsuccessful");
      return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), NOT_MODIFIED);
    }

    return new ResponseEntity<VoidEmptyJson>(new VoidEmptyJson(), CREATED);
  }

}