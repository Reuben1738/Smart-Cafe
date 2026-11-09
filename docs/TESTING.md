# Testing Evidence

| Test ID | Test Description | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- |
| T-01 | Register with an existing email | Error message: "This email is already registered" | Displayed correctly | Pass |
| T-02 | Login with incorrect password | Error message: "Incorrect password" | Displayed correctly | Pass |
| T-03 | Login with empty fields | Inline error: "Email is required" | Displayed correctly | Pass |
| T-04 | Add item to cart | Toast message: "Item added to cart!" | Displayed correctly | Pass |
| T-05 | Place order with empty cart | Toast message: "Your cart is empty!" | Displayed correctly | Pass |
| T-06 | Place a valid order | Order saved to DB, cart cleared, success Toast | Worked correctly | Pass |
| T-07 | View Order History | Displays past orders with correct total and status | Worked correctly | Pass |
| T-08 | Search for "Latte" | RecyclerView filters to show only Latte | Worked correctly | Pass |