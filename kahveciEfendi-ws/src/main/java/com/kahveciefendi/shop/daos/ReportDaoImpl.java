package com.kahveciefendi.shop.daos;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kahveciefendi.shop.datatypes.ReportData;
import com.kahveciefendi.shop.models.OrderedDrink;
import com.kahveciefendi.shop.models.OrderedDrinkAddition;
import com.kahveciefendi.shop.models.User;

/**
 * ReportDao providing access to users, orders, ordered_drinks and ordered_drink_additions table.
 * 
 * @author Ayhan Dardagan
 *
 */
@Repository
public class ReportDaoImpl implements ReportDao {

  private static final Logger logger = LoggerFactory.getLogger(ReportDaoImpl.class);

  private static final int REPORT_NAME = 0;
  private static final int REPORT_DATE = 1;
  private static final int REPORT_VALUE = 2;

  @Autowired
  private SessionFactory sessionFactory;

  protected Session getSession() {
    return this.sessionFactory.getCurrentSession();
  }

  @Override
  public ReportData findOrdersByLoginName(String loginName) {

    ReportData report = null;

    // Query: SELECT date, SUM(total_price) FROM users LEFT OUTER JOIN orders ON users.user_id =
    // orders.user_id WHERE users.login_name = :loginName GROUP BY orders.date;

    Criteria criteria = getSession().createCriteria(User.class);
    criteria.createAlias("orders", "o");
    criteria.setProjection(Projections.projectionList(). //
        add(Projections.property("fullName")). //
        add(Projections.groupProperty("o.date")). //
        add(Projections.sum("o.totalPriceWithDiscount")));
    criteria.add(eq("loginName", loginName));
    criteria.addOrder(Order.asc("o.date"));

    @SuppressWarnings("unchecked")
    List<Object[]> orders = criteria.list();

    if (orders.size() == 0) {

      logger.info("Orders for user {} not found in table", loginName);
      return null;
    }

    // Set query result to report

    for (Object[] row : orders) {

      if (report == null) {
        report = new ReportData((String) row[REPORT_NAME]);
      }

      report.addDate((String) row[REPORT_DATE]);
      report.addValue((Double) row[REPORT_VALUE]);
    }

    return report;
  }

  /**
   * REMARK BY AUTHOR: Drinks can be renamed or deleted, prices can be changed, etc. Ordered drinks
   * should not be influenced by this so reports are not changed. Therefore we work with ordered
   * drink names here instead if using the foreign key link drink_id from drinks-table. Same for
   * drink additions.
   */

  @Override
  public List<String> findAllOrderedDrinkNames() {

    String hql = "SELECT drinkName FROM OrderedDrink GROUP BY drinkName ORDER BY drinkName";

    @SuppressWarnings("unchecked")
    List<String> orderedDrinkNames = getSession().createQuery(hql).list();

    if (orderedDrinkNames.size() == 0) {

      logger.info("No ordered drinks found in table");
      return null;
    }

    return orderedDrinkNames;
  }

  @Override
  public ReportData findOrderedDrinksByDrinkName(String drinkName) {

    ReportData report = null;

    // Query: SELECT drink_name, date, SUM(total_price_with_amount_and_additions) FROM
    // ordered_drinks WHERE drink_name = :drink_name GROUP BY date ORDER BY date;

    Criteria criteria = getSession().createCriteria(OrderedDrink.class);
    criteria.setProjection(Projections.projectionList(). //
        add(Projections.property("drinkName")). //
        add(Projections.groupProperty("date")). //
        add(Projections.sum("totalPrice")));
    criteria.add(eq("drinkName", drinkName));
    criteria.addOrder(Order.asc("date"));

    @SuppressWarnings("unchecked")
    List<Object[]> orderedDrink = criteria.list();

    if (orderedDrink.size() == 0) {

      logger.info("Orders for drink {} not found in table", drinkName);
      return null;
    }

    // Set query result to report

    for (Object[] row : orderedDrink) {

      if (report == null) {
        report = new ReportData((String) row[REPORT_NAME]);
      }

      report.addDate((String) row[REPORT_DATE]);
      report.addValue((Double) row[REPORT_VALUE]);
    }

    return report;
  }

  @Override
  public List<String> findAllOrderedDrinkAdditionNames() {

    String hql = "SELECT drinkAdditionName FROM OrderedDrinkAddition GROUP BY drinkAdditionName "
        + "ORDER BY drinkAdditionName";

    @SuppressWarnings("unchecked")
    List<String> orderedDrinkAdditionNames = getSession().createQuery(hql).list();

    if (orderedDrinkAdditionNames.size() == 0) {

      logger.info("No ordered drink additions found in table");
      return null;
    }

    return orderedDrinkAdditionNames;
  }

  @Override
  public ReportData findOrderedDrinkAdditionsByDrinkAdditionName(String drinkAdditionName) {

    ReportData report = null;

    // Query: SELECT drink_addition_name, date, SUM(drink_addition_price) FROM
    // ordered_drink_additions WHERE drink_addition_name = :drinkAdditionName GROUP BY date ORDER BY
    // date

    Criteria criteria = getSession().createCriteria(OrderedDrinkAddition.class);
    criteria.setProjection(Projections.projectionList(). //
        add(Projections.property("drinkAdditionName")). //
        add(Projections.groupProperty("date")). //
        add(Projections.sum("drinkAdditionPrice")));
    criteria.add(eq("drinkAdditionName", drinkAdditionName));
    criteria.addOrder(Order.asc("date"));

    @SuppressWarnings("unchecked")
    List<Object[]> orderedDrinkAdditions = criteria.list();

    if (orderedDrinkAdditions.size() == 0) {

      logger.info("Order additions for drink {} not found in table", drinkAdditionName);
      return null;
    }

    // Set query result to report

    for (Object[] row : orderedDrinkAdditions) {

      if (report == null) {
        report = new ReportData((String) row[REPORT_NAME]);
      }

      report.addDate((String) row[REPORT_DATE]);
      report.addValue((Double) row[REPORT_VALUE]);
    }

    return report;
  }

}