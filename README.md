<div align="center">

# NTS Management System

### High-Performance Administration and Candidate Management Portal

[![Java](https://img.shields.io/badge/Java-22-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-22.0.2-FF6F00?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.9.15-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Architecture](https://img.shields.io/badge/Architecture-OOP%20%2F%20Clean%20Code-4CAF50?style=for-the-badge)](https://github.com/argentium0/NTS_Management_System)
[![License](https://img.shields.io/badge/License-MIT-9E9E9E?style=for-the-badge)](LICENSE)

</div>

---

## 📖 Overview

The **NTS Management System** is a modern, enterprise-grade desktop application built with JavaFX and Java 22. It provides an all-in-one portal for the National Testing Service (NTS), enabling candidate registrations, exam schedule management, test centre allocation, and staff duty assignments (Superintendents & Invigilators) nationwide.

---

## 🚀 Features

* **🔑 Authentic Candidate Login & Registration**: CNIC-based authentication portal for candidates to view exam statuses and register for upcoming testing sessions (NAT, GAT, TOEIC).
* **📊 Admin Operations Matrix**: Comprehensive dashboard rendering real-time staff allocations, test centre deployments, and nationwide metrics.
* **🏫 Test Centre & Venue Allocation**: Interactive management of testing centers, room capacities, building types, and allocation dates.
* **👥 Staff Duty Management**: Seamless assignment of Superintendents and Invigilators with automated allowance calculation and duty tracking.
* **🔔 Custom Modal Dialog System**: Sharp, flat, custom JavaFX modal dialogs for notifications, confirmations, and validation alerts.

---

## 🛠️ Tech Stack

* **Language**: Java 22 / JDK 22
* **UI Framework**: JavaFX 22.0.2 (Controls, FXML, Graphics)
* **Build System**: Apache Maven 3.9.15
* **Architecture**: Object-Oriented Programming (OOP) & Clean Component Design
* **Styling**: Modern Vanilla CSS (`styles.css`) featuring custom design tokens and flat geometry

---

## 🎨 Design System

The application adheres strictly to the official NTS high-contrast visual identity:
* **Primary Brand Colors**: NTS Action Orange (`#F28221`) and NTS Label Navy (`#2A4D7C`).
* **Secondary Colors**: Charcoal Slate (`#2C3238`), Coral Red (`#ED6B6B`), and Emerald Green (`#34B878`).
* **Typography**: Clean, sans-serif typography (`Inter`, `Segoe UI`, `Open Sans`).
* **Geometry**: Flat, 4px corner radii with zero heavy drop-shadows or 3D clutter.

---

## ⚙️ Installation & Setup

### Prerequisites
* **Java Development Kit (JDK 22)** installed and configured.
* **Apache Maven** installed (or use the included `mvn.cmd` wrapper).

### Running the Application

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/argentium0/NTS_Management_System.git
   cd NTS_Management_System
   ```

2. **Compile and Run using Maven**:
   ```bash
   mvn clean javafx:run
   ```
   *Alternatively, if running in PowerShell:*
   ```powershell
   .\mvn javafx:run
   ```

---

## 📄 License

This project is licensed under the MIT License — see the LICENSE file for details.
