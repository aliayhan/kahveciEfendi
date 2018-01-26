package com.kahveciefendi.shop.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kahveciefendi.shop.daos.ReportDao;
import com.kahveciefendi.shop.datatypes.ReportData;

/**
 * User service which encapsulates dao.
 * 
 * @author Ayhan Dardagan
 *
 */
@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

  private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

  @Autowired
  ReportDao reportDao;

  @Override
  public ReportData getOrdersByLoginName(String loginName) {
    logger.info("Getting orders for user {}", loginName);
    return reportDao.findOrdersByLoginName(loginName);
  }

  @Override
  public List<String> getAllOrderedDrinkNames() {
    logger.info("Getting all ordered drink names");
    return reportDao.findAllOrderedDrinkNames();
  }

  @Override
  public ReportData getOrderedDrinksByDrinkName(String drinkName) {
    logger.info("Getting all ordered drinks");
    return reportDao.findOrderedDrinksByDrinkName(drinkName);
  }

  @Override
  public List<String> getAllOrderedDrinkAdditionNames() {
    logger.info("Getting all ordered drink addition names");
    return reportDao.findAllOrderedDrinkAdditionNames();
  }

  @Override
  public ReportData getOrderedDrinksByDrinkAdditionName(String drinkAdditionName) {
    logger.info("Getting all ordered drink additions");
    return reportDao.findOrderedDrinkAdditionsByDrinkAdditionName(drinkAdditionName);
  }

}