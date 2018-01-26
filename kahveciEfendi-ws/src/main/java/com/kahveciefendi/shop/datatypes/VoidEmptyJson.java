package com.kahveciefendi.shop.datatypes;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * HttpStatus.NO_CONTENT does not fullfill its job, because client lib sometime dont react on that.
 * So always returning this when response is void.
 * 
 * @author Ayhan Dardagan
 *
 */
@JsonSerialize
public class VoidEmptyJson {
}