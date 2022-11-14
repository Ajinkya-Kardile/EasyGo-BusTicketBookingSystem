# EasyGo - Bus Ticket Booking Application
This is an android application to book bus tickets.

It is the user side application that contain features such as, 
- Search buses
- View Bus details
- Book multiple tickets at a single time
- Digital payment gateway
- View Booked Tickets
- And many more...



Note- You have to add your own google-services.json file from your Firebase console. Otherwise the project don't run properly. 
firebase account configration required
- Enable  Email/Password signIn method in firebase account. 
- create Realtime Database and modify rules like
```
  {
  "rules": {
    ".read": true,
    ".write": true
  }
}
```
note-This rule preferred for only testing perpose.


The admin side application source code is also provided. Please check my profile
