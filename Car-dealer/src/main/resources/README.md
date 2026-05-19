# 🚗 Car Dealership Management System

## 📌 Overview

This is a Java-based Car Dealership Management System that allows users to:

View and filter vehicle inventory
Add and remove vehicles
Search vehicles by price, make/model, year, color, mileage, and type
Create Sales Contracts and Lease Contracts
Save and load dealership and contract data using CSV files

The application is built using Object-Oriented Programming (OOP) principles such as:

Inheritance
Polymorphism
Abstraction
Encapsulation

## 🏗️ Project Structure


## 🚗 Features

🔍 Vehicle Management
View all vehicles
Search by:
Price range
Make & Model
Year range
Color
Mileage range
Vehicle type
Add new vehicles
Remove vehicles by VIN

### 📄 Contract System

The system supports two types of contracts:

### 🧾 Sales Contract

Calculates:
Sales tax (5%)
Recording fee
Processing fee (based on price)
Optional financing with monthly payment calculation

### 📑 Lease Contract

Calculates:
Lease fee (7%)
Expected ending value (50% of price)
Monthly payment calculation based on lease structure

### 💾 File Storage

All data is stored using CSV files:

dealership.csv → Vehicle inventory
contract.csv → Sales and lease contracts

File handling is managed using:

BufferedReader
BufferedWriter
Files.newBufferedReader
Files.newBufferedWriter

### ▶️ How to Run

Open project in IntelliJ IDEA (or any Java IDE)
Ensure JDK 17+ is installed
Run the Main class
Follow the console menu