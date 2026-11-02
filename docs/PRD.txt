Author: Reuben Uwaifo P2826038
Date: 28 October 2026
Individual — Customer role

Purpose
This document will capture the structural requirements for the Smart Café android application.

Stakeholders
• Café customer which is the Primary user - Browse menu, place and track orders, track, leave feedback and ratings.
• Lab tutor (client) – Requirements, documentation, professional delivery, time constraint.
• Developer - Implement, Design, build, test and document the entire process.

Functional Requirements

Authentication & Account
• The system should allow a customer to register with name, email, and password - Needed to associate orders with a user.
• The system should validate the registration form (valid email format, password length, empty fields).
• The system should reject registration with an email that already exists - Prevents duplicate accounts.
• The system should allow a registered customer to log in with email and password.
• The system should keep the customer logged in across app restarts until they log out.
• The system should provide a logout action.

Menu
• The system should display the menu grouped by category.
• The system should show item name, brief description, price, and availability on the menu list.
• The system should allow viewing an item with full description and price.
• The system should allow searching menu items by name.
• The system should allow filtering by category.
• The system should allow filtering by availability status.
• The system should show unavailable items and prevent adding them to the cart.

Cart
• The system should allow adding an available item to the cart.
• The system should allow increasing or decreasing quantity of a cart item.
• The system should allow removing an item from the cart.
• The system should display the cart total.
• The system should clear the cart after a successfully placing an order.

Orders
• The system should allow placing an order from the cart with a single confirmation action.
• The system should not accept order placement if the cart is empty.
• The system should show a confirmation screen after order placement with the order ID and total.
• The system should list the customer's previous orders, newest first, with status and total.
• The system should allow opening an order to see its items and status.
• The system should allow the customer to view the status of each order (Pending, Preparing, Ready for Collection, Collected).
• The system should automatically advance order status to simulate the admin counterpart.
• The system should notify the customer when an order's status changes.

Feedback & Ratings
• The system should allow submitting a 1–5-star rating for a collected order.
• The system should allow an optional written feedback comment (max 500 chars).

Non-Functional Requirements
• The app will be implemented in Kotlin using Android Studio and XML layouts.
• All form inputs shall be validated with error messages - Invalid inputs cannot be submitted.
• The app should run smoothly on the emulator.
• The app should manage network failure and never crash.
• Passwords should be hashed and not stored in plaintext.