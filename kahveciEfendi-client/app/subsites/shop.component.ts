import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { SessionStorage } from 'ng2-webstorage';
import { Base64 } from 'js-base64';
import { DrinkManagerService } from '../services/drinkmanagerservice';
import { Drink } from '../types/drink';
import { DrinkAddition } from '../types/addition';
import { Calculation } from '../types/calculation';
import { OrderRequest } from '../types/orderrequest';
import { User } from '../types/user';

@Component({
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css'],
})
export class ShopComponent implements OnInit {

  errorMessage: string = undefined;

  // Logged-in user
  @SessionStorage('savedLoggedInUser') loggedInUser: User;

  // Drink data
  drinks: Drink[] = [];

  // Order dialog data
  currentOrder: Drink;
  selectedAdditions: string[];
  showAddOrderDialog = false;

  // Basket data
  @SessionStorage('savedOrders') orders: Drink[];
  @SessionStorage('savedCalculations') totalCalculation: Calculation;


  // Initializations

  constructor(private drinkManager: DrinkManagerService) { }

  ngOnInit() {
    this.drinkManager.getDrinks().subscribe(
      drinks => this.drinks = drinks,
      error => this.errorMessage = <any>error);
  }


  // Events

  // Drinks: Select a drink -> Open order dialog
  openOrderDialog(drink: Drink) {
    this.currentOrder = this.cloneDrink(drink);
    this.currentOrder.amount = 1;
    this.selectedAdditions = [];

    this.showAddOrderDialog = true;
  }

  // Drink order dialog: Put drink order to basket
  putDrinkOrderToBasket() {

    // Find DrinkAddition object and put to orderedAdditions list
    let orders = (this.orders !== undefined && this.orders !== null) ? this.orders : [];
    let orderedAdditions: DrinkAddition[] = [];
    let totalAdditionPrice: number = 0;

    for (let i = 0; i < this.selectedAdditions.length; i++) {
      for (let j = 0; j < this.currentOrder.additions.length; j++) {

        // selectedAdditions contains addition ids. Get DrinkAddition object for it and save
        let additionId: number = 0;
        additionId = +this.selectedAdditions[i]; // Convert string to number 
        if (additionId === this.currentOrder.additions[j].id) {

          orderedAdditions.push(this.cloneDrinkAddition(this.currentOrder.additions[j])); // Save addition to list
          totalAdditionPrice += this.currentOrder.additions[j].price; // Calculate total addition price
          break;
        }
      }
    }

    let totalPrice = (this.currentOrder.price + totalAdditionPrice) * this.currentOrder.amount;
    let orderedDrink: Drink = new Drink(this.currentOrder.id, this.currentOrder.name, this.currentOrder.description, this.currentOrder.price,
      orderedAdditions, this.currentOrder.amount, totalPrice);

    orders.push(orderedDrink);
    // TODO: Workaround. Direct assertions can be detected by ng2-webstorage, a push to it not.
    this.orders = orders;

    // Calculate discounts and price to pay
    this.updateOrderCalculations();

    this.showAddOrderDialog = false;
  }

  // Basket: Order basket content
  orderBasket() {

    let requestParams = this.convertBasketToOrderRequest();

    if (this.loggedInUser === undefined || this.loggedInUser === null) {
      this.errorMessage = 'Siparişini onaylamak için lütfen giriş yap';
      return;
    }

    if (requestParams.length > 0) {

      let hashedAuth = Base64.encode(this.loggedInUser.loginName + ':' + this.loggedInUser.loginName);

      // Updates discount calculation based on order basket
      this.drinkManager.giveOrder(hashedAuth, requestParams).subscribe(
        nodata => {},
        error => this.errorMessage = <any>error);
    }

    this.orders = [];
    this.totalCalculation = undefined;
    this.errorMessage = 'Siparişin başarıyla alındı';
  }

  // Basket: Click on delete item
  deleteBasketItem(index) {

    let orders = (this.orders !== undefined && this.orders !== null) ? this.orders : [];
    orders.splice(index, 1);

    // TODO: Workaround. Direct assertions can be detected by ng2-webstorage, a push to it not.
    this.orders = orders;

    // Calculate discounts and price to pay
    this.updateOrderCalculations();
  }

  // Basket: Formatted output of additions
  formatAdditions(additions: DrinkAddition[]): string {
    let output = '';
    for (let i = 0; i < additions.length; i++) {
      output += additions[i].name;
      output += (i < (additions.length - 1)) ? ', ' : '';
    }
    return output;
  }

  // Event, event emitter of login component
  toggleLogin() {
    // Disable errorMessage (e.g. please login to order)
    this.errorMessage = undefined;
  }

  // Event, ngIf of admin menu
  showAdminMenu(): boolean {

    if (this.loggedInUser !== undefined && this.loggedInUser !== null) {
      return this.loggedInUser.role === 'ADMIN';
    }

    return false;
  }


  // Helpers

  private updateOrderCalculations() {

    let requestParams = this.convertBasketToOrderRequest();

    if (requestParams.length > 0) {

      // Updates discount calculation based on order basket
      this.drinkManager.getDiscountCalculation(requestParams).subscribe(
        totalCalculation => this.totalCalculation = totalCalculation,
        error => this.errorMessage = <any>error);
    }
  }

  private convertBasketToOrderRequest(): OrderRequest[] {

    let requestParams: OrderRequest[] = [];

    if (this.orders !== undefined && this.orders !== null && this.orders.length > 0) {

      // Collect requestParams on base of basket order
      for (let i = 0; i < this.orders.length; i++) {

        // Collect addition ids
        let orderAdditionIds: number[] = [];
        for (let j = 0; j < this.orders[i].additions.length; j++) {
          orderAdditionIds.push(this.orders[i].additions[j].id);
        }

        requestParams.push({ id: this.orders[i].id, additionIds: orderAdditionIds, amount: this.orders[i].amount });
      }
    }

    return requestParams;
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




