# WinBid - Online Bidding Platform (Backend)

WinBid is a modern auction platform backend built with Spring Boot where users can participate in product auctions by placing small bids. Admins can manage products, and winners are automatically selected when bidding completes.

## 🚀 Features

### 🔒 Authentication & Security
- JWT-based authentication (using jjwt)
- Spring Security integration
- Role-based access control (Admin/User)
- Password encryption

### 🛠️ Core Functionality
- RESTful API endpoints
- Data validation with Spring Validation
- Exception handling
- Actuator endpoints for monitoring

### 📦 Product Management (Admin)
- CRUD operations for products
- Image upload to Cloudinary
- Set total required bids per product
- Product status tracking

### 💰 Bidding System
- Place bids API
- Bid validation
- Automatic winner selection
- Email notifications (Spring Mail)

### 📊 Database
- MySQL relational database
- Spring Data JPA repositories
- Entity relationships
- Auditing fields

## 🛠️ Technologies Used

### Backend Stack
- **Spring Boot** 3.x
- **Spring Security** (Authentication/Authorization)
- **Spring Data JPA** (Database access)
- **MySQL** (Database)
- **JJWT** (JSON Web Tokens)
- **Cloudinary** (Image storage)
- **Spring Mail** (Email notifications)
- **Lombok** (Boilerplate reduction)
- **Spring Boot Actuator** (Monitoring)


## 🚀 Getting Started

### Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.6+

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/SinghTanmay024/winbid-backend.git
   cd winbid-backend
