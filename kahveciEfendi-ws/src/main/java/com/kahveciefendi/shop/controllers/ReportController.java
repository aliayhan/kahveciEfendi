package com.kahveciefendi.shop.controllers;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kahveciefendi.shop.datatypes.ReportData;
import com.kahveciefendi.shop.services.ReportService;

/**
 * Handles getting reports requests.
 * 
 * @author Ayhan Dardagan
 *
 */
@RestController
public class ReportController {

  private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

  @Autowired
  private ReportService reportService;

  /**
   * Post request on /userreport.
   * 
   * @return Report of orders as JSON
   */
  @PostMapping(value = "/userreport", produces = "application/json")
  public ResponseEntity<ReportData> postUserreport(@RequestParam("username") String userName) {

    logger.info("Requesting orders for user {}", userName);
    ReportData report = reportService.getOrdersByLoginName(userName);

    if (report == null) {
      logger.info("No orders found for user {}", userName);
      return new ResponseEntity<ReportData>(NO_CONTENT);
    }

    return new ResponseEntity<ReportData>(report, OK);
  }

  /**
   * Post request on /ordereddrinknames.
   * 
   * @return List of ordered drink names as JSON
   */
  @PostMapping(value = "/ordereddrinknames", produces = "application/json")
  public ResponseEntity<List<String>> postOrdereddrinknames() {

    logger.info("Requesting ordered drink names");
    List<String> orderedDrinkNames = reportService.getAllOrderedDrinkNames();

    if (orderedDrinkNames == null) {
      logger.info("No ordered drink names found");
      return new ResponseEntity<List<String>>(NO_CONTENT);
    }

    return new ResponseEntity<List<String>>(orderedDrinkNames, OK);
  }

  /**
   * Post request on /drinkreport.
   * 
   * @return Report of ordered drinks as JSON
   */
  @PostMapping(value = "/drinkreport", produces = "application/json")
  public ResponseEntity<ReportData> postDrinkreport(
      @RequestParam("orderedDrinkName") String orderedDrinkName) {

    logger.info("Requesting ordered drinks");
    ReportData report = reportService.getOrderedDrinksByDrinkName(orderedDrinkName);

    if (report == null) {
      logger.info("No ordered drinks found for drink {}", orderedDrinkName);
      return new ResponseEntity<ReportData>(NO_CONTENT);
    }

    return new ResponseEntity<ReportData>(report, OK);
  }

  /**
   * Post request on /ordereddrinkadditionnames.
   * 
   * @return List of ordered drink addition names as JSON
   */
  @PostMapping(value = "/ordereddrinkadditionnames", produces = "application/json")
  public ResponseEntity<List<String>> postOrdereddrinkadditionnames() {

    logger.info("Requesting ordered drink addition names");
    List<String> orderedDrinkAdditionNames = reportService.getAllOrderedDrinkAdditionNames();

    if (orderedDrinkAdditionNames == null) {
      logger.info("No ordered drink addition names found");
      return new ResponseEntity<List<String>>(NO_CONTENT);
    }

    return new ResponseEntity<List<String>>(orderedDrinkAdditionNames, OK);
  }

  /**
   * Post request on /drinkadditionreport.
   * 
   * @return Report of ordered drink additions as JSON
   */
  @PostMapping(value = "/drinkadditionreport", produces = "application/json")
  public ResponseEntity<ReportData> postDrinkadditionreport(
      @RequestParam("orderedDrinkAdditionName") String orderedDrinkAdditionName) {

    logger.info("Requesting ordered drink additions");
    ReportData report = reportService.getOrderedDrinksByDrinkAdditionName(orderedDrinkAdditionName);

    if (report == null) {
      logger.info("No ordered drink additions found for drink addition {}",
          orderedDrinkAdditionName);
      return new ResponseEntity<ReportData>(NO_CONTENT);
    }

    return new ResponseEntity<ReportData>(report, OK);
  }

}