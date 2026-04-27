RESTAURANT SYSTEM (JavaFX + MySQL)
=================================

Project Overview
----------------
Restaurant System is a desktop application built using Java and JavaFX.
It simulates a restaurant ordering and management system with two user roles:

1. Customer User
    - Register / Login
    - Browse menu by category
    - Add items to cart
    - Select item options / customization
    - Remove items from cart
    - Payment page (Cash / Card)
    - Place orders

2. Admin User
    - Login with admin account
    - Manage menu items (Create / Read / Update / Delete)
    - Activate / Deactivate products
    - Manage item options (radio / checkbox selections)
    - View dashboard reports
    - Export PDF business reports


Main Features
-------------
- User Login / Register
- Role-based access (User / Admin)
- JavaFX graphical interface
- MySQL database storage
- Shopping cart system
- Payment system
- Stock management
- Dashboard analytics
- PDF report export
- Order history data saved in database


Software Used
-----------------
- Java 17+ (recommended)
- JavaFX SDK
- MySQL Server
- JDBC Connector (MySQL)
- Apache PDFBox (for PDF export)
- IntelliJ IDEA / NetBeans / Eclipse


Minimum Requirements
--------------------
Software:
- Windows / Mac / Linux
- Java JDK 17 or above
- JavaFX installed
- MySQL installed and running

Hardware:
- 4GB RAM minimum
- 200MB free storage


Database Setup (if DatabaseInitializer.java fails)
--------------
1. Open MySQL / phpMyAdmin
2. Create database:

   CREATE DATABASE restaurant_db;

3. Update DBConnection.java if needed:

  - URL: jdbc:mysql://localhost:3306/restaurant_db
  - Username: root
  - Password: yourpassword


How to Run
----------
1. Open project in IDE
2. Add JavaFX library
3. Add MySQL JDBC connector
4. Add PDFBox library
5. Run Launcher.java


Default Admin Account
---------------------
(If manually inserted)

- Username: admin
- Password: admin123

(If no admin exists, insert one in database manually.)


Important Notes
---------------
- Internet connection not required.
- Ensure MySQL service is running before starting program.
- Exported reports are saved as PDF files.
- Admin users cannot place customer orders.


Known Limitations
-----------------
- Payment system is simulation only.
- No online payment gateway integration.
- Designed for coursework / educational use.


Project Structure
-----------------
com.example.restaurantsystem

Controllers:
- LoginController.java
- RegisterController.java
- MenuController.java
- OptionController.java
- AdminController.java
- OptionAdminController.java
- DashboardController.java
- PaymentController.java

Services:
- AuthService
- MenuService
- OrderService
- DashboardService
- ReportService
- OptionService
- DatabaseInitializer

Models:
- User
- MenuItem
- Order
- OrderItem
- ItemOption