# Testing Evidence

**Author:** Reuben Uwaifo P2826038
**Date:** 12 November 2026

| Test ID | Test Description | Expected Result                                            | Actual Result | Status |
| :--- | :--- |:-----------------------------------------------------------| :--- | :--- |
| T-01 | Register with an existing email | Error message: "This email is already registered"          | Displayed correctly | Pass |
| T-02 | Login with incorrect password | Error message: "Incorrect password"                        | Displayed correctly | Pass |
| T-03 | Login with empty fields | Error: "Email is required"                                 | Displayed correctly | Pass |
| T-04 | Add item to cart | Message: "Item added to cart!"                             | Displayed correctly | Pass |
| T-05 | Place order with empty cart | Message: "Your cart is empty!"                             | Displayed correctly | Pass |
| T-06 | Place a valid order | Order saved to DB, cart cleared, success                   | Worked correctly | Pass |
| T-07 | View Order History | Displays past orders with correct total and status         | Worked correctly | Pass |
| T-08 | Search for "Latte" | RecyclerView filters to show only Latte                    | Worked correctly | Pass |
| T-09 | Display Menu Images | Each menu item shows an image                              | Images loaded correctly | Pass |
| T-10 | Simulate Admin Status Update | Clicking button changes status to "Preparing" then "Ready" | Status updated correctly in DB | Pass |
| T-11 | Leave Feedback | Clicking "Leave Feedback" opens 5-star dialog              | Dialog opened correctly | Pass |
| T-12 | Submit Feedback | Selecting 5 stars and submitting shows "Thank you"         | Feedback saved to Room DB | Pass |
| T-13 | Security Check (Cart) | Trying to access Cart without logging in                   | Redirected to Login Screen | Pass |
| T-14 | Security Check (Orders) | Trying to access Orders without logging in                 | Redirected to Login Screen | Pass |