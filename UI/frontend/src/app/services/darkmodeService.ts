import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DarkModeService {
  url = 'http://localhost:8084/api/dark-mode/';
  private darkModeStatus = new BehaviorSubject<boolean>(false);
  private intervalId: any;

  constructor(private http: HttpClient) {
    this.checkDarkModeStatus();
    this.startCheckingDarkModeStatus();
  }

  async checkDarkModeStatus() {
    const data = await fetch(this.url);
    const isDarkMode = await data.json();
    if( isDarkMode !== this.darkModeStatus.value) {
      this.darkModeStatus.next(isDarkMode);
    }
  }

  private startCheckingDarkModeStatus() {
    // Call the function immediately, and then set the interval
    this.checkDarkModeStatus();
    this.intervalId = setInterval(() => {
      this.checkDarkModeStatus();
    }, 200); // Calls checkDarkModeStatus every 5 seconds
  }

  getDarkModeStatus(): Observable<boolean> {
    return this.darkModeStatus.asObservable();
  }

  ngOnDestroy() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }
}
