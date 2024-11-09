SERVLETS
Servlets manage the functionality of an e-commerce application, where users can log in, view products, and make purchases. The three main components are:
1.	CheckoutServlet - handles the purchase process for users.
2.	ProductServlet - displays products to logged-in users.
3.	UserServlet - manages user registration, login, and logout.
Each servlet’s functions and their operations are explained below:
1. CheckoutServlet
The CheckoutServlet processes user purchases, validating wallet balances and updating product and order information in the database.
•	doPost:
o	Opens a Hibernate session for database interaction and retrieves the current HttpSession to access the logged-in user.
o	Gets the productId from the request, checks if the product exists, and verifies that the user’s wallet has enough balance.
o	If the user has sufficient funds:
--Deducts the product’s price from the user’s wallet and updates the user in the database.
--Creates a new Order, saves it to the database, and forwards the request to the orderConfirmation.jsp page.
o	If the wallet balance is insufficient, it redirects to the products page with an error message.
2. ProductServlet
The ProductServlet is responsible for displaying products to users who have logged in and have a valid session token.
•	doGet:
o	Retrieves the current session and checks for a sessionToken in the cookies.
o	If a session and valid session token exist:
--Opens a Hibernate session, fetches products using ProductDao, and forwards the product list to productList.jsp.
o	If no valid session or session token is found, it redirects to login.jsp with an error message to prompt login.
3. UserServlet
The UserServlet manages user registration, login, and logout operations.
•	doPost:
o	Determines the action (either register or login) based on the action parameter and calls the corresponding method.
•	doGet:
o	Calls logoutUser if the action is logout, logging the user out.
registerUser
•	Takes sanitized username and password from the request, checks for an existing username in the database.
•	If no existing user is found, it creates a new User, sets an initial wallet balance, saves it to the database, and commits the transaction.
•	If the registration is successful, it redirects to login.jsp; otherwise, it rolls back the transaction and logs an error.
loginUser
•	Verifies user credentials by comparing the username and password against the database.
•	If the credentials match, it generates a unique sessionToken, stores the user and token in HttpSession, and sends the token in a cookie.
•	If login is successful, it redirects to /products. If credentials are invalid, it redirects to login.jsp with an error.
logoutUser
•	Invalidates the current session and removes the session token cookie, effectively logging out the user.

DAO:
DAO (Data Access Object) classes handle database operations for different entities in the application. They use Hibernate to interact with the database and manage transactions.
OrderDao
OrderDao provides methods to interact with the Order table in the database.
•	Constructor:
o	Initializes OrderDao with a Hibernate Session for database transactions.
•	getOrdersByUserId:
o	Retrieves a list of orders for a specific user by querying the Order table based on the userId.
o	Uses HQL (Hibernate Query Language) to fetch orders where user_id matches the given userId.
•	saveOrder:
o	Saves a new Order to the database.
o	Begins a transaction, saves the Order object, and commits the transaction if successful.
o	Rolls back the transaction in case of an exception to avoid partial or failed updates.
ProductDao
ProductDao handles operations on the Product table.
•	Constructor:
o	Accepts a Hibernate Session to initialize the DAO for database interactions.
•	getAllProducts:
o	Retrieves a list of all products in the database.
o	Begins a transaction, runs an HQL query to fetch all Product entries, commits the transaction, and logs the list size.
o	Rolls back the transaction in case of an error and prints the stack trace for debugging.
•	getProductById:
o	Fetches a specific product by its productId.
o	Begins a transaction, retrieves the product with the specified ID, and commits the transaction if successful.
o	If an error occurs, it rolls back the transaction and logs the exception.
UserDao
UserDao manages operations for the User entity.
•	Constructor:
o	Initializes UserDao with a Hibernate Session.
•	updateUser:
o	Updates an existing user in the database, typically for changes like wallet balance.
o	Starts a transaction, updates the User record, and commits if successful. If an exception occurs, it rolls back the transaction and rethrows the error.
•	getUserById:
o	Retrieves a User object based on its userId.
o	Uses the session’s get method to fetch the User entity with the provided ID.
These DAOs handle data manipulation within transactions, ensuring that each operation can be rolled back if something goes wrong, maintaining data integrity across operations.

MODEL CLASSES:
Model classes define the structure and behavior of Order, Product, and User entities, corresponding to tables in the database.
Order Model
The Order class represents an order placed by a user.
•	Fields:
o	id: Primary key, auto-generated.
o	userId: Refers to the ID of the user who placed the order.
o	productId: Refers to the ID of the product in the order.
o	orderDate: The timestamp when the order was created.
•	Annotations:
o	@Entity and @Table(name = "orders"): Map this class to the "orders" table.
o	@Id and @GeneratedValue(strategy = GenerationType.IDENTITY): Specify id as the primary key with auto-increment.
o	@Column: Map fields to table columns and enforce properties like nullable = false.
•	Methods: Standard getters and setters for each field.
Product Model
The Product class represents a product available for purchase.
•	Fields:
o	id: Primary key, auto-generated.
o	name: The product's name.
o	price: The product's price.
o	description: A description of the product.
•	Annotations:
o	@Entity and @Table(name = "products"): Map this class to the "products" table.
o	@Id and @GeneratedValue(strategy = GenerationType.IDENTITY): Specify id as the primary key with auto-increment.
o	@Column: Map fields to table columns and enforce constraints like nullable = false.
•	Methods: Standard getters and setters for each field.
User Model
The User class represents a user in the system with authentication and wallet features.
•	Fields:
o	id: Primary key, auto-generated.
o	username: Unique identifier for the user.
o	password: Hashed password stored in the database.
o	wallet: Represents the user's wallet balance.
•	Annotations:
o	@Entity and @Table(name = "users"): Map this class to the "users" table.
o	@Id and @GeneratedValue(strategy = GenerationType.IDENTITY): Specify id as the primary key with auto-increment.
o	@Column: Ensure columns are not nullable, and that username is unique.
•	Methods:
o	setPassword: Hashes the password using BCrypt before storing it in the password field.
o	checkPassword: Verifies the given plaintext password against the hashed password in the database.
o	Helper functions hashPassword and standard getters/setters.
Each class is mapped to a database table, with fields as columns, using JPA (Java Persistence API) annotations to define relationships and constraints. Additionally, the User model includes password hashing for secure authentication.


JSP FILES:
1. index.jsp - User Registration Page
This page provides a simple form for users to register.
•	Form: Contains fields for username and password and submits data via POST to UserServlet with an action parameter set to "register".
•	JavaScript Alert: Checks the URL for an error parameter to display an alert if there's an error message, such as from failed registration attempts.
•	Navigation: Includes a link to login.jsp for users who already have an account.
2. login.jsp - Login Page
This page handles user login with a form.
•	Form: Contains fields for username and password and submits to UserServlet with an action parameter set to "login".
•	JavaScript Alert: Similar to index.jsp, it checks for an error parameter in the URL to display login-related errors.
•	Navigation: Provides a link to index.jsp for new users who need to register.
3. orderconfirmation.jsp - Order Confirmation Page
Displays a confirmation message to the user after a successful purchase.
•	Product Details: Uses JSP expressions to display product.name and product.price, passed as attributes from the servlet that processes the checkout.
•	Navigation: Includes a link to return to the product list.
4. productList.jsp - Product List Page
Shows the list of products available for purchase and user-specific details like wallet balance.
•	User Information: Displays the logged-in user’s username and wallet balance.
•	Product Table: Uses <c:forEach> from JSTL to iterate over productList, which is passed as an attribute containing a list of available products. Each product displays in a table row with its ID, name, price, and description.
•	Buy Button: Each product row has a form to submit the selected product's ID to CheckoutServlet, initiating the purchase process.
•	JavaScript Alert: Alerts users of any error parameter in the URL, useful for displaying purchase-related issues.
•	Logout Link: Provides a link to log out via UserServlet with action=logout.


