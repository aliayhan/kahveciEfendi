import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';
import { SelectItem } from 'primeng/primeng';
import { SessionStorage } from 'ng2-webstorage';
import { UserService } from '../services/userservice';
import { User } from '../types/user';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  @Output() toggleLogin = new EventEmitter();

  // Logged-in user
  @SessionStorage('savedLoggedInUser') loggedInUser: User;
  
  users: User[];

  // Dropdown data
  selectableUsers: SelectItem[];
  selectedUser: User = undefined;

  constructor(private router: Router, private userService: UserService) { }

  // Event on init: Getting services datas
  ngOnInit() {
    this.userService.getUsers().then(users => {
      this.users = users;
      this.selectableUsers = [];

      // Filling dropdown data
      this.selectableUsers.push({ label: 'Lütfen seçiniz', value: undefined });
      for (let i = 0; i < this.users.length; i++) {
        let user: User = this.users[i];
        this.selectableUsers.push({ label: user.fullName, value: user });
      }
    });
  }

  // Event on login button clicked: Remeber user
  loginUser() {
    if (this.selectedUser !== undefined) {
      this.loggedInUser = this.selectedUser;
      // Send event to parent
      this.toggleLogin.next();
    }
  }

  // Event on logout button clicked: Forget user
  logoutUser() {
    this.selectedUser = undefined;
    this.loggedInUser = null; // ng2-webstorage works with null
    // Send event to parent
    this.toggleLogin.next();
    // Navigate to shop
    this.router.navigateByUrl('/shop');
  }
}
