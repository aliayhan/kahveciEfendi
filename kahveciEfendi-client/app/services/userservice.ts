import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { User } from '../types/user';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class UserService {

  constructor(private http: Http) { }

  // TODO just for demo purposes: Simplified user login, get login data from JSON 
  // (you dont need to remember username/password)
  getUsers() {
    return this.http.get('../../assets/data/users.json')
      .toPromise()
      .then(res => <User[]>res.json().data)
      .then(data => { return data; });
  }
}