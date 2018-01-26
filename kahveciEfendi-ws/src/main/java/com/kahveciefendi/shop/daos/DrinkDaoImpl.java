package com.kahveciefendi.shop.daos;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.kahveciefendi.shop.datatypes.DiscountedPriceData;
import com.kahveciefendi.shop.datatypes.DrinkAdditionData;
import com.kahveciefendi.shop.datatypes.DrinkData;
import com.kahveciefendi.shop.datatypes.OrderData;
import com.kahveciefendi.shop.models.Drink;
import com.kahveciefendi.shop.models.DrinkAddition;
import com.kahveciefendi.shop.models.Order;
import com.kahveciefendi.shop.models.OrderedDrink;
import com.kahveciefendi.shop.models.OrderedDrinkAddition;
import com.kahveciefendi.shop.models.User;

/**
 * DrinkDao providing access to drinks and drink_additions table.
 * 
 * @author Ayhan Dardagan
 *
 */
@Repository
public class DrinkDaoImpl implements DrinkDao {

  private static final Logger logger = LoggerFactory.getLogger(DrinkDaoImpl.class);

  public static final int DRINK_ID = 0;
  public static final int DRINK_NAME = 1;
  public static final int DRINK_DESC = 2;
  public static final int DRINK_PRICE = 3;
  public static final int DRINK_ADDITION_ID = 4;
  public static final int DRINK_ADDITION_NAME = 5;
  public static final int DRINK_ADDITION_DESC = 6;
  public static final int DRINK_ADDITION_PRICE = 7;

  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private MessageSource messageSource;

  private Locale tr = new Locale("tr");

  protected Session getSession() {
    return this.sessionFactory.getCurrentSession();
  }

  @Override
  public boolean saveUpdateDrink(DrinkData drinkData) {

    Drink drink = new Drink();

    drink.setId(drinkData.getId());
    drink.setName(drinkData.getName());
    drink.setDescription(drinkData.getDescription());
    drink.setPrice(drinkData.getPrice());

    // Add drink additions when any selected
    if (drinkData.getAdditions() != null && drinkData.getAdditions().size() > 0) {
      List<Long> drinkAdditionIds = new ArrayList<Long>();

      for (DrinkAdditionData addition : drinkData.getAdditions()) {
        drinkAdditionIds.add(addition.getId());
      }

      drink.setDrinkAdditions(getDrinkAdditionsByIds(drinkAdditionIds));
    }

    getSession().saveOrUpdate(drink);

    logger.info("Drink saved/updated");

    return true;
  }

  @Override
  public boolean saveUpdateDrinkAddition(DrinkAdditionData drinkAdditionData) {

    DrinkAddition drinkAddition = new DrinkAddition();

    drinkAddition.setId(drinkAdditionData.getId());
    drinkAddition.setName(drinkAdditionData.getName());
    drinkAddition.setDescription(drinkAdditionData.getDescription());
    drinkAddition.setPrice(drinkAdditionData.getPrice());

    getSession().saveOrUpdate(drinkAddition);

    logger.info("Drink addition saved/updated");

    return true;
  }

  @Override
  public boolean deleteDrink(DrinkData drinkData) {

    // Info: Drinks can be deleted, but reports about that drink can never; So, there is no cascade
    // delete at ordered_drinks table. But the drink_id there can be null. We need to set it
    // manually.
    Query updateQuery = getSession()
        .createQuery("UPDATE OrderedDrink SET parentDrink = null WHERE parentDrink = :drinkId");
    updateQuery.setLong("drinkId", drinkData.getId());
    updateQuery.executeUpdate();

    // Delete drink
    Drink drink = new Drink();
    drink.setId(drinkData.getId());
    getSession().delete(drink);

    logger.info("Drink deleted");

    return true;
  }

  @Override
  public boolean deleteDrinkAddition(DrinkAdditionData drinkAdditionData) {

    // Info: Drink additions can be deleted, but reports about that drink can never; So, there is no
    // cascade delete at ordered_drink_additions table. But the drink_addition_id there can be null.
    // We need to set it manually.
    Query updateQuery = getSession().createQuery(
        "UPDATE OrderedDrinkAddition SET parentDrinkAddition = null WHERE parentDrinkAddition = :drinkAdditionId");
    updateQuery.setLong("drinkAdditionId", drinkAdditionData.getId());
    updateQuery.executeUpdate();

    // Delete drink
    DrinkAddition drinkAddition = new DrinkAddition();
    drinkAddition.setId(drinkAdditionData.getId());
    getSession().delete(drinkAddition);

    logger.info("Drink addition deleted");

    return true;
  }

  @Override
  public List<DrinkData> findAllDrinks() {

    List<DrinkData> drinkDatas = new ArrayList<DrinkData>(20);
    String curDrinkName = null;

    // Left join to M-N table to map to additions -> Get all drinks with assigned additions.
    Criteria criteria = getSession().createCriteria(Drink.class);
    criteria.createAlias("drinkAdditions", "da", JoinType.LEFT_OUTER_JOIN);
    criteria.setProjection(Projections.projectionList(). //
        add(Projections.property("id")). //
        add(Projections.property("name")). //
        add(Projections.property("description")). //
        add(Projections.property("price")). //
        add(Projections.property("da.id")). //
        add(Projections.property("da.name")). //
        add(Projections.property("da.description")). //
        add(Projections.property("da.price")));
    criteria.addOrder(org.hibernate.criterion.Order.asc("name"));
    criteria.addOrder(org.hibernate.criterion.Order.asc("da.name"));

    @SuppressWarnings("unchecked")
    List<Object[]> drinks = criteria.list();

    if (drinks.size() == 0) {

      logger.info("No drinks found");
      return null;
    }

    // Convert result set to DrinkData structure
    for (Object[] row : drinks) {

      if (!row[DRINK_NAME].equals(curDrinkName)) {
        curDrinkName = (String) row[DRINK_NAME];
        drinkDatas.add(new DrinkData((Long) row[DRINK_ID], curDrinkName, (String) row[DRINK_DESC],
            (double) row[DRINK_PRICE]));
      }

      // If any additions available, put the DrinkDataAddition into DrinkData
      if (row[DRINK_ADDITION_ID] != null) {
        drinkDatas.get(drinkDatas.size() - 1)
            .addDrinkAddition(new DrinkAdditionData((Long) row[DRINK_ADDITION_ID],
                (String) row[DRINK_ADDITION_NAME], (String) row[DRINK_ADDITION_DESC],
                (double) row[DRINK_ADDITION_PRICE]));
      }
    }

    return drinkDatas;
  }

  @Override
  public List<DrinkAdditionData> findAllDrinkAdditions() {

    List<DrinkAdditionData> drinkAdditionsDatas = new ArrayList<DrinkAdditionData>(20);

    @SuppressWarnings("unchecked")
    List<Object[]> drinkAdditions = getSession()
        .createQuery("SELECT id, name, description, price FROM DrinkAddition").list();

    if (drinkAdditions.size() == 0) {

      logger.info("No drink additions found");
      return null;
    }

    for (Object[] row : drinkAdditions) {
      drinkAdditionsDatas.add(new DrinkAdditionData((Long) row[DRINK_ID], (String) row[DRINK_NAME],
          (String) row[DRINK_DESC], (double) row[DRINK_PRICE]));
    }

    return drinkAdditionsDatas;
  }

  @Override
  public DiscountedPriceData calculateDiscount(List<OrderData> orderDatas) {

    double orderTotalPrice = 0d;
    // Lowest priced drink is free after 3rd drink and more
    double discountFreeDrink = Double.MAX_VALUE;
    Long drinkCount = 0l;
    // 25% if total order price is higher 12TL
    double discount25 = 0d;

    // Determine total order price, drink count and lowest priced drink (with additions)

    for (OrderData orderData : orderDatas) {

      // Get drink
      Drink drink = getDrinkById(orderData.getId());
      if (drink == null) {
        logger.error("Drink not found for order {}", orderData);
        return null;
      }

      // Get ordered drink additions
      List<DrinkAddition> additions = null;
      if (orderData.getAdditionIds().size() > 0) {
        additions = getDrinkAdditionsByIds(orderData.getAdditionIds());
      }
      double orderedAdditionsTotalPrice = 0d;

      if (additions != null) {
        for (DrinkAddition addition : additions) {
          orderedAdditionsTotalPrice += addition.getPrice();
        }
      }

      // Search for lowest value drink (with additions)
      if ((drink.getPrice() + orderedAdditionsTotalPrice) < discountFreeDrink) {
        discountFreeDrink = (drink.getPrice() + orderedAdditionsTotalPrice);
      }

      // Count drinks
      drinkCount += orderData.getAmount();

      // Ordered drink total price consists of: (Drink price + additions price) * drink amount
      double orderedDrinkTotalPrice = (drink.getPrice() + orderedAdditionsTotalPrice)
          * orderData.getAmount();

      // Add to total order price
      orderTotalPrice += orderedDrinkTotalPrice;
    }

    orderTotalPrice = Math.round(orderTotalPrice * 100d) / 100d;

    // Check if and which discount applies

    if (drinkCount < 3) {
      discountFreeDrink = 0;
    }

    if (orderTotalPrice > 12) {
      discount25 = Math.round(orderTotalPrice * 0.25d * 100d) / 100d;
    }

    DiscountedPriceData discount = null;

    if (discountFreeDrink > 0 || discount25 > 0) {
      if (discountFreeDrink >= discount25) {

        double priceToPay = orderTotalPrice - discountFreeDrink;
        String discountReason = messageSource.getMessage("discount.on3rd.lowest", null, tr);

        discount = new DiscountedPriceData(orderTotalPrice, discountFreeDrink, //
            discountReason, priceToPay);
      } else {

        double priceToPay = orderTotalPrice - discount25;
        String discountReason = messageSource.getMessage("discount.on12tl.25", null, tr);

        discount = new DiscountedPriceData(orderTotalPrice, discount25, discountReason, priceToPay);
      }
    } else {
      // No discount
      discount = new DiscountedPriceData(orderTotalPrice, 0d, null, orderTotalPrice);
    }

    return discount;
  }

  @Override
  public boolean saveOrder(String authenticatedUserName, List<OrderData> orderDatas) {

    double orderTotalPrice = 0d;
    String orderDate = getCurrentDateString();

    // Lowest priced drink is free after 3rd drink and more
    double discountFreeDrink = Double.MAX_VALUE;
    Long drinkCount = 0l;
    // 25% if total order price is higher 12TL
    double discount25 = 0d;

    // Get user
    User user = getAuthenticatedUser(authenticatedUserName);
    if (user == null) {
      logger.error("Authenticated user not found, canceling orders");
      return false;
    }

    // Create order
    Order order = new Order(orderDate, user);
    user.getOrders().add(order);

    for (OrderData orderData : orderDatas) {

      // Get drink
      Drink drink = getDrinkById(orderData.getId());
      if (drink == null) {
        logger.error("Drink not found for order {}, canceling orders", orderData);
        return false;
      }

      // Get ordered drink additions
      List<DrinkAddition> additions = null;
      if (orderData.getAdditionIds().size() > 0) {
        additions = getDrinkAdditionsByIds(orderData.getAdditionIds());
      }
      double orderedAdditionsTotalPrice = 0d;

      // Create orderedDrink
      OrderedDrink orderedDrink = new OrderedDrink(orderDate, orderData.getAmount(),
          drink.getName(), drink.getPrice(), order, drink);
      order.getOrderedDrinks().add(orderedDrink);

      // Create OrderedDrinkAdditions and calculate addition's total price
      if (additions != null) {
        for (DrinkAddition addition : additions) {
          orderedDrink.getOrderedDrinkAdditions().add(new OrderedDrinkAddition(orderDate,
              addition.getName(), addition.getPrice(), orderedDrink, addition));

          orderedAdditionsTotalPrice += addition.getPrice();
        }
      }

      // Search for lowest value drink (with additions)
      if ((drink.getPrice() + orderedAdditionsTotalPrice) < discountFreeDrink) {
        discountFreeDrink = (drink.getPrice() + orderedAdditionsTotalPrice);
      }

      // Count drinks
      drinkCount += orderData.getAmount();

      // Ordered drink total price consists of: (Drink price + additions price) * drink amount
      double orderedDrinkTotalPrice = (drink.getPrice() + orderedAdditionsTotalPrice)
          * orderData.getAmount();
      orderedDrink.setTotalPrice(orderedDrinkTotalPrice);

      // Add to total order price
      orderTotalPrice += orderedDrinkTotalPrice;
    }

    orderTotalPrice = Math.round(orderTotalPrice * 100d) / 100d;

    // Order total price consists of all total prices of ordered drinks
    order.setTotalPrice(orderTotalPrice);

    // Check if and which discount applies

    if (drinkCount < 3) {
      discountFreeDrink = 0;
    }

    if (orderTotalPrice > 12) {
      discount25 = Math.round(orderTotalPrice * 0.25d * 100d) / 100d;
    }

    if (discountFreeDrink > 0 || discount25 > 0) {
      if (discountFreeDrink >= discount25) {
        order.setDiscount(discountFreeDrink);
        order.setDiscountReason("3rd drink of lowest value free");
        order.setTotalPriceWithDiscount(orderTotalPrice - discountFreeDrink);
      } else {
        order.setDiscount(discount25);
        order.setDiscountReason("25% when order value higher 12TL");
        order.setTotalPriceWithDiscount(orderTotalPrice - discount25);
      }
    } else {
      // No discount
      order.setDiscount(0d);
      order.setTotalPriceWithDiscount(orderTotalPrice);
    }

    // Save order on user
    getSession().persist(user);

    return true;
  }

  /**
   * Authenticated user as user object from users table.
   * 
   * @return User
   */
  @SuppressWarnings("unchecked")
  private User getAuthenticatedUser(String userName) {

    Query userQuery = getSession().createQuery("FROM User WHERE loginName = :name");
    userQuery.setString("name", userName);
    List<User> userQueryResult = userQuery.list();

    if (userQueryResult.size() == 0) {
      logger.error("Authenticated user {} not found in users table", userName);
      return null;
    }
    return userQueryResult.get(0);
  }

  /**
   * Drink by it's id.
   * 
   * @param drinkId
   *          Drink id
   * @return Drink
   */
  @SuppressWarnings("unchecked")
  private Drink getDrinkById(Long drinkId) {

    Query drinkQuery = getSession().createQuery("FROM Drink WHERE id IN (:drinkId)");
    drinkQuery.setLong("drinkId", drinkId);
    List<Drink> drinkQueryResult = drinkQuery.list();
    if (drinkQueryResult.size() == 0) {
      logger.error("Drink not found with id {}", drinkId);
      return null;
    }

    return drinkQueryResult.get(0);
  }

  /**
   * Drink additiions by their ids.
   * 
   * @param drinkAdditionIds
   *          Drink addition id list
   * @return List of {@code DrinkAddition}
   */
  @SuppressWarnings("unchecked")
  private List<DrinkAddition> getDrinkAdditionsByIds(List<Long> drinkAdditionIds) {

    Query additionQuery = getSession().createQuery("FROM DrinkAddition WHERE id IN (:additionIds)");
    additionQuery.setParameterList("additionIds", drinkAdditionIds);

    return additionQuery.list();
  }

  /**
   * Current date of turkey.
   * 
   * @return Date string in format YYYY-MM
   */
  private String getCurrentDateString() {

    ZonedDateTime zt = ZonedDateTime.now(ZoneId.of("Turkey"));
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");
    return dtf.format(zt);
  }
}
