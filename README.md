# Unusual-spending

This project is for implementing the [Unusual Spending
Kata](https://github.com/testdouble/contributing-tests/wiki/Unusual-Spending-Kata).
It is preconfigured with JUnit, Hamcrest, and Mockito for testing. It is also
set up to import the third-party dependencies needed by the kata and defined in
[unusual-spending-vendor](./src/main/java).
[kata link](https://kata-log.rocks/unusual-spending-kata)

```
.
├── README.md
├── pom.xml
├── src
│   ├── main
│   │   └── java
│   │       └── spending
│   │           └── TriggersUnusualSpendingEmail.java
```

To get started, write a Collaboration test for
`spending.TriggersUnusualSpendingEmail` that breaks the problem down into two,
three, or four parts.


### Problem statement

You work at a credit card company and as a value-add they want to start providing alerts to users when their spending 
in any particular category is higher than usual.

A `Payment` is a simple value object with a price, description, and category
A `Category` is an enumerable type of a collection of things like “entertainment”, “restaurants”, and “golf”
For a given userId, fetch the payments for the current month and the previous month
Compare the total amount paid for the each month, grouped by category; filter down to the categories for which the 
user spent at least 50% more this month than last month
Compose an e-mail message to the user that lists the categories for which spending was unusually high,
with a subject like “Unusual spending of $1076 detected!” and this body:
Hello card user!

We have detected unusually high spending on your card in these categories:

* You spent $148 on groceries
* You spent $928 on travel

Love,

The Credit Card Company


## Thought process
- we have to compare how much the user spent previous month and the current month
- it will be compared according to the categories
- sending an alert only if the user has spent 50% more than the last month
- Eg:
    - Jan: User spent 100 rupees for Entertainment category
    - February : 160, which is 50% more ie 100+(50% of 100) = 150, so if it is more than 150 then it will send an email

### Creation of classes
- A class which sends email
- A class which has the responsibility to compare the price from previous month
- A Payment class which has price, description, cateogy
- A Category enum class
- Something which is known as a user which has userId