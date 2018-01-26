package com.kahveciefendi.shop.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Model representing a row in the table users.
 * 
 * @author Ayhan Dardagan
 *
 */
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_id", unique = true, nullable = false)
  private Long id;

  @Column(name = "login_name", length = 30, unique = true, nullable = false)
  private String loginName;

  @Column(name = "full_name", length = 60, nullable = false)
  private String fullName;

  @Column(name = "email", length = 60, unique = true, nullable = false)
  private String email;

  @Column(name = "password", length = 60, nullable = false)
  private String password;

  @OneToMany(mappedBy = "parentUser", cascade = CascadeType.ALL, orphanRemoval = true, //
      fetch = FetchType.LAZY)
  @Column(nullable = false)
  private List<Order> orders;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

}
