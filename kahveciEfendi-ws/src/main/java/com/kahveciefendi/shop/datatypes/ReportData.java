package com.kahveciefendi.shop.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Report type which consists of date axus and a list of {@link ReportData}.
 * 
 * @author Ayhan Dardagan
 *
 */
public class ReportData {
  
  private String label;

  private List<String> dateAxis;
  
  private List<Double> valueAxis;

  /**
   * Constructor.
   */
  public ReportData(String label) {
    this.label = label;
    this.dateAxis = new ArrayList<String>(20);
    this.valueAxis = new ArrayList<Double>(20);
  }

  /**
   * Add new date.
   * 
   * @param date
   *          Date
   */
  public void addDate(String date) {
    dateAxis.add(date);
  }
  
  /**
   * Adds new value.
   * 
   * @param value
   *          Value
   */
  public void addValue(Double value) {
    valueAxis.add(value);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public List<String> getDateAxis() {
    return dateAxis;
  }

  public void setDateAxis(List<String> dateAxis) {
    this.dateAxis = dateAxis;
  }

  public List<Double> getValueAxis() {
    return valueAxis;
  }

  public void setValueAxis(List<Double> valueAxis) {
    this.valueAxis = valueAxis;
  }

}
