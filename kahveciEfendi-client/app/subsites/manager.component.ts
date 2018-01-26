import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { SessionStorage } from 'ng2-webstorage';
import { Base64 } from 'js-base64';
import { DrinkManagerService } from '../services/drinkmanagerservice';
import { User } from '../types/user';
import { Drink } from '../types/drink';
import { DrinkAddition } from '../types/addition';

@Component({
  templateUrl: './manager.component.html',
  styleUrls: ['./manager.component.css']
})
export class ManagerComponent implements OnInit {

  errorMessage: string = undefined;

  // Logged-in user
  @SessionStorage('savedLoggedInUser') loggedInUser: User;

  // Drink data
  drinks: Drink[] = [];
  drinkAdditions: DrinkAddition[] = [];

  // Drink dialog data
  currentDrink: Drink;
  selectedAdditions: string[];
  showDrinkDialog = false;

  // Drink dialog data
  currentDrinkAddition: Drink;
  showDrinkAdditionDialog = false;

  // Initializers

  constructor(private drinkManager: DrinkManagerService) { }

  ngOnInit() {
    this.updateDrinks();
    this.updateDrinkAdditions();
  }


  // Events

  // Drinks table: New drink button click
  newDrink() {
    this.currentDrink = new Drink();
    this.selectedAdditions = [];

    this.showDrinkDialog = true;
  }

  // Drinks table: Edit drink button click
  editDrink(drink: Drink) {

    this.currentDrink = this.cloneDrink(drink);

    // Pre-select already selected additions
    this.selectedAdditions = [];
    for (let i = 0; i < this.currentDrink.additions.length; i++) {
      this.selectedAdditions.push(("" + this.currentDrink.additions[i].id));
    }

    this.showDrinkDialog = true;
  }

  // Drink dialog: Save/edit drink
  saveEditDrink() {

    if (this.currentDrink === undefined) {
      return;
    }

    // Prepare admin user authorization hash
    let hashedAuth = this.generateHashAuthKey();
    if (hashedAuth === undefined) {
      return;
    }

    // Prepare selected additions
    let currentDrinkAdditions: DrinkAddition[] = [];
    for (let i = 0; i < this.selectedAdditions.length; i++) {
      let additionId: number = 0;
      additionId = +this.selectedAdditions[i]; // Convert string to number 
      currentDrinkAdditions.push(new DrinkAddition(additionId));
    }
    this.currentDrink.additions = currentDrinkAdditions;

    // Save or update drink
    this.drinkManager.saveOrUpdateDrink(hashedAuth, this.currentDrink).subscribe(
      nodata => this.updateDrinks(),
      error => this.errorMessage = <any>error);

    this.showDrinkDialog = false;
  }

  // Drinks table: Delete drink button click
  deleteDrink(drink: Drink) {

    // Prepare admin user authorization hash
    let hashedAuth = this.generateHashAuthKey();
    if (hashedAuth === undefined) {
      return;
    }

    // Delete drink
    this.drinkManager.deleteDrink(hashedAuth, drink).subscribe(
      nodata => this.updateDrinks(),
      error => this.errorMessage = <any>error);
  }

  // Drinks table: Formatted output of additions
  formatAdditions(additions: DrinkAddition[]): string {
    let output = '';
    for (let i = 0; i < additions.length; i++) {
      output += additions[i].name;
      output += (i < (additions.length - 1)) ? ', ' : '';
    }
    return output;
  }

  // DrinkAdditions table: New drinkAddition button click
  newDrinkAddition() {

    this.currentDrinkAddition = new DrinkAddition();
    this.showDrinkAdditionDialog = true;
  }

  // DrinkAdditions table: Edit drinkAddition button click
  editDrinkAddition(drinkAddition: DrinkAddition) {

    this.currentDrinkAddition = this.cloneDrinkAddition(drinkAddition);
    this.showDrinkAdditionDialog = true;
  }

  // DrinkAddition dialog: Save/edit drinkAddition
  saveEditDrinkAddition() {

    if (this.currentDrinkAddition === undefined) {
      return;
    }

    // Prepare admin user authorization hash
    let hashedAuth = this.generateHashAuthKey();
    if (hashedAuth === undefined) {
      return;
    }

    // Save or update drink addtion
    this.drinkManager.saveOrUpdateDrinkAddition(hashedAuth, this.currentDrinkAddition).subscribe(
      nodata => {
        this.updateDrinkAdditions();
        this.updateDrinks(); // Update additions selections of drinks too
      },
      error => this.errorMessage = <any>error);

    this.showDrinkAdditionDialog = false;
  }

  // DrinkAdditions table: Delete drinkAddition button click
  deleteDrinkAddition(drinkAddition: DrinkAddition) {

    // Prepare admin user authorization hash
    let hashedAuth = this.generateHashAuthKey();
    if (hashedAuth === undefined) {
      return;
    }

    // Delete drink addition
    this.drinkManager.deleteDrinkAddition(hashedAuth, drinkAddition).subscribe(
      nodata => {
        this.updateDrinkAdditions();
        this.updateDrinks(); // Update additions selections of drinks too
      },
      error => this.errorMessage = <any>error);
  }

  // Event, ngIf of admin menu
  showAdminMenu(): boolean {

    if (this.loggedInUser !== undefined && this.loggedInUser !== null) {
      return this.loggedInUser.role === 'ADMIN';
    }

    return false;
  }


  // Helper

  private updateDrinks() {

    // Update drinks
    this.drinkManager.getDrinks().subscribe(
      drinks => this.drinks = drinks,
      error => this.errorMessage = <any>error);
  }

  private updateDrinkAdditions() {

    // Update drink additions
    this.drinkManager.getDrinkAdditions().subscribe(
      drinkAdditions => this.drinkAdditions = drinkAdditions,
      error => this.errorMessage = <any>error);
  }

  private generateHashAuthKey(): string {

    // Prepare admin user authorization hash
    if (this.loggedInUser === undefined || this.loggedInUser === null) {
      return undefined;
    }
    if (this.loggedInUser.role !== 'ADMIN') {
      return undefined;
    }
    return Base64.encode(this.loggedInUser.loginName + ':' + this.loggedInUser.loginName);
  }

  private cloneDrink(c: Drink): Drink {
    let drink = new Drink();
    for (let prop in c) {
      drink[prop] = c[prop];
    }
    return drink;
  }

  private cloneDrinkAddition(c: DrinkAddition): DrinkAddition {
    let addition = new DrinkAddition();
    for (let prop in c) {
      addition[prop] = c[prop];
    }
    return addition;
  }

}






