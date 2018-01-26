import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import { Drink } from '../types/drink';
import { DrinkAddition } from '../types/addition';
import { Calculation } from '../types/calculation';
import { OrderRequest } from '../types/orderrequest';

@Injectable()
export class DrinkManagerService {

  constructor(private http: Http) { }

  private baseUrl = 'http://localhost:8080/kahveciefendi/';

  private addUpdateDrinkUrl = 'saveupdatedrink';
  private addUpdateDrinkAdditionUrl = 'saveupdatedrinkaddition';
  private deleteDrinkUrl = 'deletedrink';
  private deleteDrinkAdditionUrl = 'deletedrinkaddition';

  private drinkUrl = 'drinks';
  private additionUrl = 'drinkadditions';
  private calculationUrl = 'calculatediscount';
  private orderUrl = 'giveorder';

  // Save or update drink and addition assignments
  saveOrUpdateDrink(hashedAuth: string, drink: Drink): Observable<void> {
    let body = JSON.stringify(drink);
    let headers = new Headers({ 'Content-Type': 'application/json' });
    headers.append('Authorization', 'Basic ' + hashedAuth);
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.addUpdateDrinkUrl, body, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Save or update drink addition
  saveOrUpdateDrinkAddition(hashedAuth: string, drinkAddition: DrinkAddition): Observable<void> {
    let body = JSON.stringify(drinkAddition);
    let headers = new Headers({ 'Content-Type': 'application/json' });
    headers.append('Authorization', 'Basic ' + hashedAuth);
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.addUpdateDrinkAdditionUrl, body, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Delete drink
  deleteDrink(hashedAuth: string, drink: Drink): Observable<void> {
    let body = JSON.stringify(drink);
    let headers = new Headers({ 'Content-Type': 'application/json' });
    headers.append('Authorization', 'Basic ' + hashedAuth);
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.deleteDrinkUrl, body, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Delete drink addition
  deleteDrinkAddition(hashedAuth: string, drinkAddition: DrinkAddition): Observable<void> {
    let body = JSON.stringify(drinkAddition);
    let headers = new Headers({ 'Content-Type': 'application/json' });
    headers.append('Authorization', 'Basic ' + hashedAuth);
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.deleteDrinkAdditionUrl, body, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Get all drinks with additions
  getDrinks(): Observable<Drink[]> {

    return this.http.post(this.baseUrl + this.drinkUrl, undefined)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Get all drink additions
  getDrinkAdditions(): Observable<DrinkAddition[]> {

    return this.http.post(this.baseUrl + this.additionUrl, undefined)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Get all drinks with additions
  getDiscountCalculation(requestParams: OrderRequest[]): Observable<Calculation> {
    let body = JSON.stringify(requestParams);
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.calculationUrl, body, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Give order
  giveOrder(hashedAuth: string, orderRequestParams: OrderRequest[]): Observable<void> {
    let body = JSON.stringify(orderRequestParams);
    let headers = new Headers({ 'Content-Type': 'application/json' });
    headers.append('Authorization', 'Basic ' + hashedAuth);
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.orderUrl, body, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    let body = res.json();
    return body || {};
  }

  private handleError(error: Response | any) {

    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}