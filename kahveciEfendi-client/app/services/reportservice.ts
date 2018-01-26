import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import { Report } from '../types/report';

@Injectable()
export class ReportService {

  constructor(private http: Http) { }

  private baseUrl = 'http://localhost:8080/kahveciefendi/';

  private orderedDrinkNamesUrl = 'ordereddrinknames';
  private orderedAdditionNamesUrl = 'ordereddrinkadditionnames';
  private userReportUrl = 'userreport';
  private drinkReportUrl = 'drinkreport';
  private additionReportUrl = 'drinkadditionreport';

  // Get names only of ordered drinks
  getOrderedDrinks(hashedAuth: string): Observable<string[]> {

    let headers = new Headers({ 'Authorization': 'Basic ' + hashedAuth });
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.orderedDrinkNamesUrl, undefined, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Get names only of ordered drink additions
  getOrderedDrinkAdditions(hashedAuth: string): Observable<string[]> {

    let headers = new Headers({ 'Authorization': 'Basic ' + hashedAuth });
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.orderedAdditionNamesUrl, undefined, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Get user report
  getUserReport(hashedAuth: string, userLogin: string): Observable<Report> {
    let body = 'username=' + userLogin;
    let headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded' });
    headers.append('Authorization', 'Basic ' + hashedAuth);
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.userReportUrl, body, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Get drink report
  getDrinkReport(hashedAuth: string, orderedDrinkName: string): Observable<Report> {
    let body = 'orderedDrinkName=' + orderedDrinkName;
    let headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded' });
    headers.append('Authorization', 'Basic ' + hashedAuth);
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.drinkReportUrl, body, options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  // Get user report
  getDrinkAdditionReport(hashedAuth: string, orderedAdditionName: string): Observable<Report> {
    let body = 'orderedDrinkAdditionName=' + orderedAdditionName;
    let headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded' });
    headers.append('Authorization', 'Basic ' + hashedAuth);
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.baseUrl + this.additionReportUrl, body, options)
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