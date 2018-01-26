import { DrinkAddition } from './addition';

export class Drink {

  constructor(public id?: number, public name?: string, public description?: string, public price?: number, public additions?: DrinkAddition[], public amount?: number, public totalPrice?: number) { }

}