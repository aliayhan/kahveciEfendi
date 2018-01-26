import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { SelectItem, UIChart } from 'primeng/primeng';
import { SessionStorage } from 'ng2-webstorage';
import { Base64 } from 'js-base64';
import { UserService } from '../services/userservice';
import { ReportService } from '../services/reportservice';
import { User } from '../types/user';
import { Report } from '../types/report';

@Component({
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {

  errorMessage: string = undefined;

  // Logged-in user
  @SessionStorage('savedLoggedInUser') loggedInUser: User;

  // DOM elements
  @ViewChild('userChart') userChartElement: UIChart;
  @ViewChild('drinkChart') drinkChartElement: UIChart;
  @ViewChild('additionChart') additionChartElement: UIChart;

  // Service provided
  users: User[];
  orderedDrinks: string[];
  orderedDrinkAdditions: string[];
  userReport: Report;
  drinkReport: Report;
  additionReport: Report;

  // Dropdown data
  selectableUsers: SelectItem[];
  selectedUser: User = undefined;

  selectableOrderedDrinks: SelectItem[];
  selectedOrderedDrink: string = undefined;

  selectableOrderedAdditions: SelectItem[];
  selectedOrderedAddition: string = undefined;

  // Charts data
  userChartData: any;
  drinkChartData: any;
  additionChartData: any;

  // Initializers

  constructor(private userService: UserService, private reportService: ReportService) { }

  ngOnInit() {

    // Prepare admin user authorization hash
    let hashedAuth = this.generateHashAuthKey();
    if (hashedAuth === undefined) {
      return;
    }

    // Get all users
    this.userService.getUsers().then(users => {
      this.users = users;
      this.selectableUsers = [];

      // Filling dropdown data 'users'
      this.selectableUsers.push({ label: 'Lütfen seçiniz', value: undefined });
      for (let i = 0; i < this.users.length; i++) {
        let user: User = this.users[i];
        this.selectableUsers.push({ label: user.fullName, value: user });
      }
    });

    // Get all ordered drink names
    this.reportService.getOrderedDrinks(hashedAuth).subscribe(
      orderedDrinks => {
        this.orderedDrinks = orderedDrinks;
        this.selectableOrderedDrinks = [];

        // Filling dropdown data 'orderedDrinks'
        this.selectableOrderedDrinks.push({ label: 'Lütfen seçiniz', value: undefined });
        for (let i = 0; i < this.orderedDrinks.length; i++) {
          let orderedDrinkName: string = this.orderedDrinks[i];
          this.selectableOrderedDrinks.push({ label: orderedDrinkName, value: orderedDrinkName });
        }
      },
      error => this.errorMessage = <any>error);

    // Get all ordered drink addition names
    this.reportService.getOrderedDrinkAdditions(hashedAuth).subscribe(
      orderedDrinkAdditions => {
        this.orderedDrinkAdditions = orderedDrinkAdditions;
        this.selectableOrderedAdditions = [];

        // Filling dropdown data 'orderedAdditions'
        this.selectableOrderedAdditions.push({ label: 'Lütfen seçiniz', value: undefined });
        for (let i = 0; i < this.orderedDrinkAdditions.length; i++) {
          let orderedAdditionName: string = this.orderedDrinkAdditions[i];
          this.selectableOrderedAdditions.push({ label: orderedAdditionName, value: orderedAdditionName });
        }
      },
      error => this.errorMessage = <any>error);
  }


  // Events

  // User report: Click on update button
  updateUserChart() {
    if (this.userChartElement !== undefined) {
      this.userChartElement.refresh();
    }
  }

  // Drink report: Click on update button
  updateOrderedDrinkChart() {
    if (this.drinkChartElement !== undefined) {
      this.drinkChartElement.refresh();
    }
  }

  // Addition report: Click on update button
  updateOrderedAdditionChart() {
    if (this.additionChartElement !== undefined) {
      this.additionChartElement.refresh();
    }
  }

  // User report: Select user -> Chart data reload
  generateUserReport() {

    if (this.selectedUser === undefined) {
      return;
    }

    // Prepare admin user authorization hash
    let hashedAuth = this.generateHashAuthKey();
    if (hashedAuth === undefined) {
      return;
    }

    // Get reports based on selected user
    this.reportService.getUserReport(hashedAuth, this.selectedUser.loginName).subscribe(
      userReport => {
        this.userReport = userReport;

        // Set user chart data
        this.userChartData = {
          labels: this.userReport.dateAxis,
          datasets: [
            {
              label: this.selectedUser.fullName,
              data: this.userReport.valueAxis,
              fill: false,
              borderColor: '#4bc0c0'
            }
          ]
        };
      },
      error => this.errorMessage = <any>error);
  }

  // Drink report: Select ordered drink -> Chart data reload
  generateOrderedDrinkReport() {

    if (this.selectedOrderedDrink === undefined) {
      return;
    }

    // Prepare admin user authorization hash
    let hashedAuth = this.generateHashAuthKey();
    if (hashedAuth === undefined) {
      return;
    }

    // Get reports based on selected drink
    this.reportService.getDrinkReport(hashedAuth, this.selectedOrderedDrink).subscribe(
      drinkReport => {
        this.drinkReport = drinkReport;

        // Set user chart data
        this.drinkChartData = {
          labels: this.drinkReport.dateAxis,
          datasets: [
            {
              label: this.selectedOrderedDrink,
              data: this.drinkReport.valueAxis,
              fill: true,
              backgroundColor: '#9CCC65',
              borderColor: '#7CB342',
            }
          ]
        };
      },
      error => this.errorMessage = <any>error);
  }

  // Addition report: Select ordered drink addition -> Chart data reload
  generateOrderedAdditionReport() {

    if (this.selectedOrderedAddition === undefined) {
      return;
    }

    // Prepare admin user authorization hash
    let hashedAuth = this.generateHashAuthKey();
    if (hashedAuth === undefined) {
      return;
    }

    // Get reports based on selected drink addition
    this.reportService.getDrinkAdditionReport(hashedAuth, this.selectedOrderedAddition).subscribe(
      additionReport => {
        this.additionReport = additionReport;

        // Set user chart data
        this.additionChartData = {
          labels: this.additionReport.dateAxis,
          datasets: [
            {
              label: this.selectedOrderedAddition,
              data: this.additionReport.valueAxis,
              fill: true,
              backgroundColor: '#42A5F5',
              borderColor: '#1E88E5'
            }
          ]
        };
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

  // Helpers

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
}





