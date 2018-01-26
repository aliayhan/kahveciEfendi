package com.kahveciefendi.shop.datatypes;

/**
 * General error class.
 * 
 * @author Ayhan Dardagan
 *
 */
public class ErrorData {

  private int errorCode;

  private String message;

  /**
   * Constructor.
   * 
   * @param errorCode
   *          Error code
   * @param message
   *          Error message
   */
  public ErrorData(int errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
