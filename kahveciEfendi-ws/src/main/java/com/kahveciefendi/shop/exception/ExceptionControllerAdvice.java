package com.kahveciefendi.shop.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.PRECONDITION_REQUIRED;

import java.sql.SQLException;

import org.hibernate.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.kahveciefendi.shop.datatypes.ErrorData;

/**
 * Global exception handler.
 * 
 * @author Ayhan Dardagan
 *
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

  private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

  /**
   * Unrecognized property exception, e.g. when giving JSON in a wrong format to a rest service
   * 
   * @param ex
   *          Unrecognized property exception {@link UnrecognizedPropertyException}
   * @return Error with usage information
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorData> databaseError(HttpMessageNotReadableException ex) {
    logger.error("Property error: ", ex.getRootCause());
    ErrorData error = new ErrorData(PRECONDITION_REQUIRED.value(), ex.getRootCause().getMessage());
    return new ResponseEntity<ErrorData>(error, PRECONDITION_REQUIRED);
  }

  /**
   * Database exception mapping.
   * 
   * @param ex
   *          Database exceptions {@link QueryException}, {@link SQLException} and
   *          {@link DataAccessException}
   * @return General error
   */
  @ExceptionHandler({ QueryException.class, SQLException.class, DataAccessException.class })
  public ResponseEntity<ErrorData> databaseError(Exception ex) {
    logger.error("Database error: ", ex);
    ErrorData error = new ErrorData(INTERNAL_SERVER_ERROR.value(),
        "Database conflict. If this error persists please contact hakan@kahveciefendi.com");
    return new ResponseEntity<ErrorData>(error, INTERNAL_SERVER_ERROR);
  }

  /**
   * General exception mapping.
   * 
   * @param ex
   *          Exception
   * @return General error
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorData> generalError(Exception ex) {
    logger.error("Error: ", ex);
    ErrorData error = new ErrorData(INTERNAL_SERVER_ERROR.value(),
        "Conflict. Please contact hakan@kahveciefendi.com");
    return new ResponseEntity<ErrorData>(error, INTERNAL_SERVER_ERROR);
  }
}