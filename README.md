# Bank Account Simulation System

**Developed by Shivamani**

A Java application that simulates a simple banking system using Object-Oriented Programming (OOP) concepts and a Swing-based Graphical User Interface (GUI).

## Features

- **Object-Oriented Design**: Utilizes classes, inheritance, and runtime polymorphism.
- **Account Types**: 
  - **Savings Account**: Prevents withdrawals exceeding the available balance and allows adding interest (5%).
  - **Current Account**: Allows overdrafts up to a fixed limit ($1000).
- **Interactive GUI**: Built with Java Swing, featuring input validation, real-time balance updates, and interactive dialogs.

## How to Run

1. Make sure you have the Java Development Kit (JDK) installed.
2. Clone this repository or download the source code.
3. Open a terminal/command prompt in the directory containing the files.
4. Compile the application:
   ```bash
   javac *.java
   ```
5. Run the application:
   ```bash
   java BankSimulationApp
   ```

## Usage

1. Select your desired account type from the dropdown.
2. Enter your **Account Number**, **Name**, and your starting **Initial Balance**.
3. Click **Submit** to validate, then **Create Account**.
4. Use the **Operations** buttons to `Deposit`, `Withdraw`, `Check Balance`, or `Add Interest`.
