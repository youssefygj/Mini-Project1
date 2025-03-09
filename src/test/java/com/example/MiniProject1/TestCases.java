package com.example.MiniProject1;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestCases {
    // region
    @Value("${spring.application.userDataPath}")
    private String userDataPath;

    @Value("${spring.application.productDataPath}")
    private String productDataPath;

    @Value("${spring.application.orderDataPath}")
    private String orderDataPath;

    @Value("${spring.application.cartDataPath}")
    private String cartDataPath;

    @Autowired
    private ObjectMapper objectMapper;

    private void overRideAll(){
        try{
            objectMapper.writeValue(new File(userDataPath), new ArrayList<User>());
            objectMapper.writeValue(new File(productDataPath), new ArrayList<Product>());
            objectMapper.writeValue(new File(orderDataPath), new ArrayList<Order>());
            objectMapper.writeValue(new File(cartDataPath), new ArrayList<Cart>());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    private User addUser(User user) {
        try {
            File file = new File(userDataPath);
            ArrayList<User> users;
            if (!file.exists()) {
                users = new ArrayList<>();
            }
            else {
                users = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, User[].class)));
            }
            users.add(user);
            objectMapper.writeValue(file, users);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    private ArrayList<User> getUsers() {
        try {
            File file = new File(userDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<User>(Arrays.asList(objectMapper.readValue(file, User[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    private Product addProduct(Product product) {
        try {
            File file = new File(productDataPath);
            ArrayList<Product> products;
            if (!file.exists()) {
                products = new ArrayList<>();
            }
            else {
                products = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
            }
            products.add(product);
            objectMapper.writeValue(file, products);
            return product;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    private ArrayList<Product> getProducts() {
        try {
            File file = new File(productDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Product>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    private Cart addCart(Cart cart){
        try{
            File file = new File(cartDataPath);
            ArrayList<Cart> carts;
            if (!file.exists()) {
                carts = new ArrayList<>();
            }
            else {
                carts = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
            }
            carts.add(cart);
            objectMapper.writeValue(file, carts);
            return cart;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    private ArrayList<Cart> getCarts() {
        try {
            File file = new File(cartDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Cart>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    private Order addOrder(Order order){
        try{
            File file = new File(orderDataPath);
            ArrayList<Order> orders;
            if (!file.exists()) {
                orders = new ArrayList<>();
            }
            else {
                orders = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
            }
            orders.add(order);
            objectMapper.writeValue(file, orders);
            return order;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    private ArrayList<Order> getOrders() {
        try {
            File file = new File(orderDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Order>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    private enum Model{
        USER,
        PRODUCT,
        CART,
        ORDER,
    }

    private Object find(Model model, Object toFind){
        switch(model){
            case Model.USER:
                ArrayList<User> users = getUsers();
                for(User user: users){
                    if(user.getId().equals(((User)toFind).getId())){
                        return user;
                    }
                }
                break;
            case Model.PRODUCT:
                ArrayList<Product> products = getProducts();
                for(Product product: products){
                    if(product.getId().equals(((Product)toFind).getId())){
                        return product;
                    }
                }
                break;
            case Model.CART:
                ArrayList<Cart> carts = getCarts();
                for(Cart cart: carts){
                    if(cart.getId().equals(((Cart)toFind).getId())){
                        return cart;
                    }
                }
                break;
            case Model.ORDER:
                ArrayList<Order> orders = getOrders();
                for(Order order: orders){
                    if(order.getId().equals(((Order)toFind).getId())){
                        return order;
                    }
                }
                break;
        }
        return null;
    }
    // endregion

    @BeforeEach
    void setUp(){
        overRideAll();
    }

    @AfterEach
    void tearDown(){
        overRideAll();
    }

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    // region User
    // region addUser
    @Test
    void handleAddUser_ShouldReturnValidUser_WhenGivenValidUser(){
        // Arrange
        User expectedUser = new User("name");

        // Act
        User actualUser = userService.addUser(expectedUser);

        // Assert
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertNotNull(find(Model.USER, expectedUser));
    }

    @Test
    void handleAddUser_ShouldThrowException_WhenGivenDuplicateUser(){
        // Arrange
        User user = new User("name");
        addUser(user);

        // Act & Assert
        assertThrows(Exception.class, () -> userService.addUser(new User(user.getId(), "different name")));
    }

    @Test
    void handleAddUser_ShouldThrowException_WhenGivenInValidUser(){
        assertThrows(Exception.class, () -> userService.addUser(new User()));
    }
    // endregion

    // region getUsers
    @Test
    void handleGetUsers_ShouldReturnValidArrayList_WhenJsonFileIsNotEmpty(){
        // Arrange
        User expectedUser = new User("name");
        addUser(expectedUser);

        // Act
        ArrayList<User> actualUsers = userService.getUsers();

        // Assert
        assertNotNull(actualUsers);
        assertFalse(actualUsers.isEmpty());
        assertEquals(expectedUser.getId(), actualUsers.getFirst().getId());
    }

    @Test
    void handleGetUsers_ShouldReturnEmptyArrayList_WhenJsonFileIsEmpty(){
        // Act
        ArrayList<User> actualUsers = userService.getUsers();

        // Assert
        assertNotNull(actualUsers);
        assertTrue(actualUsers.isEmpty());
    }

    @Test
    void handleGetUsers_ShouldReturnValidArrayList_WhenJsonFileIsLarge(){
        // Arrange
        int n = 1000;
        for(int i = 0; i < n; i++){
            addUser(new User("name"));
        }

        // Act
        ArrayList<User> actualUsers = userService.getUsers();

        // Assert
        assertNotNull(actualUsers);
        assertEquals(n, actualUsers.size());
    }
    // endregion

    // region getUserById
    @Test
    void handleGetUserById_ShouldReturnValidUser_WhenGivenValidUserId(){
        // Arrange
        User expectedUser = new User("name");
        addUser(expectedUser);

        // Act
        User actualUser = userService.getUserById(expectedUser.getId());

        // Arrange
        assertNotNull(actualUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
    }

    @Test
    void handleGetUserById_ShouldThrowException_WhenGivenNonExistentUserId(){
        assertThrows(Exception.class, () -> userService.getUserById(UUID.randomUUID()));
    }

    @Test
    void handleGetUserById_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> userService.getUserById(null));
    }
    // endregion

    // region getOrderByUserId
    @Test
    void handleGetOrderByUserId_ShouldReturnValidOrders_WhenGivenValidUserId(){
        // Arrange
        UUID userId = UUID.randomUUID();

        ArrayList<Order> expectedOrders = new ArrayList<Order>();
        expectedOrders.add(new Order(userId));
        addOrder(expectedOrders.getFirst());

        addUser(new User(userId,"name",expectedOrders));

        // Act
        List<Order> actualOrders =  userService.getOrdersByUserId(userId);

        // Assert
        assertNotNull(actualOrders);
        assertFalse(actualOrders.isEmpty());
        assertEquals(expectedOrders.getFirst().getId(), actualOrders.getFirst().getId());
    }

    @Test
    void handleGetOrderByUserId_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> userService.getOrdersByUserId(null));
    }

    @Test
    void handleGetOrderByUserId_ShouldThrowException_WhenGivenNonExistentUserId(){
        assertThrows(Exception.class, () -> userService.getOrdersByUserId(UUID.randomUUID()));
    }
    // endregion

    // region addOrderToUser
    @Test
    void handleAddOrderToUser_ShouldUpdateJsonFiles_WhenGivenValidUserId(){
        // Arrange
        ArrayList<Product> products = new ArrayList<>();
        int n = 2;
        for(int i = 0; i < n; i++){
            products.add(new Product("name",1));
            addProduct(products.get(i));
        }

        User user = new User("name");
        addUser(user);

        Cart cart = new Cart(user.getId(), products);
        addCart(cart);

        // Act
        userService.addOrderToUser(user.getId());

        // Assert
        ArrayList<Order> actualOrders = getOrders();
        assertFalse(actualOrders.isEmpty());
        assertEquals(user.getId(), actualOrders.getFirst().getUserId());
        assertEquals(n, products.getFirst().getPrice());
        for(int i = 0; i < n; i++){
            assertEquals(products.get(i).getId(), actualOrders.getFirst().getProducts().get(i).getId());
        }

        User actualUser = (User) find(Model.USER, user);
        assertFalse(actualUser.getOrders().isEmpty());
        assertEquals(actualUser.getOrders().getFirst().getId(), actualOrders.getFirst().getId());

        Cart actualCart = (Cart) find(Model.CART, cart);
        assertTrue(actualCart.getProducts().isEmpty());
    }

    @Test
    void handleAddOrderToUser_ShouldThrowException_WhenCartDoesNotExist(){
        // Arrange
        User user = new User("name");
        addUser(user);

        // Act & Assert
        assertThrows(Exception.class, () -> userService.addOrderToUser(user.getId()));
    }

    @Test
    void handleAddOrderToUser_ShouldThrowException_WhenCartIsEmpty(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart cart = new Cart(user.getId());
        addCart(cart);

        // Act & Assert
        assertThrows(Exception.class, () -> userService.addOrderToUser(user.getId()));
    }
    // endregion

    // region emptyCart
    @Test
    void handleEmptyCart_ShouldUpdateJsonFile_WhenCartIsNotEmpty(){
        // Arrange
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("name",1));
        addProduct(products.getFirst());

        User user = new User("name");
        addUser(user);

        Cart cart = new Cart(user.getId(), products);
        addCart(cart);

        // Act
        userService.emptyCart(user.getId());

        // Assert
        Cart actualCart = (Cart) find(Model.CART, cart);
        assertNotNull(actualCart);
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    void handleEmptyCart_ShouldNotUpdateJsonFile_WhenCartDoesNotExist(){
        // Arrange
        User user = new User("name");
        addUser(user);

        // Act
        userService.emptyCart(user.getId());

        // Assert
        ArrayList<Cart> carts = getCarts();
        assertTrue(carts.isEmpty());
    }

    @Test
    void handleEmptyCart_ShouldNotUpdateJsonFile_WhenCartIsEmpty(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart expectedCart = new Cart(user.getId());

        // Act
        userService.emptyCart(user.getId());

        // Assert
        Cart actualCart = (Cart) find(Model.CART, expectedCart);
        assertNotNull(actualCart);
        assertTrue(actualCart.getProducts().isEmpty());
    }
    // endregion

    // region removeOrderFromUser
    @Test
    void handleRemoveOrderFromUser_ShouldUpdateJsonFiles_WhenOrderExists(){
        // Arrange
        User user = new User("name");

        ArrayList<Order> orders = new ArrayList<Order>();
        for(int i = 0; i < 2; i++){
            orders.add(new Order(user.getId()));
            addOrder(orders.get(i));
        }

        user.setOrders(orders);
        addUser(user);

        // Act
        userService.removeOrderFromUser(user.getId(), orders.getFirst().getId());

        // Assert
        Order removedOrder = (Order) find(Model.ORDER, orders.getFirst());
        assertNull(removedOrder);

        Order remainingOrder = (Order) find(Model.ORDER, orders.getLast());
        assertNotNull(remainingOrder);

        User actualUser = (User) find(Model.USER, user);
        assertEquals(actualUser.getOrders().size(), 1);
        assertEquals(actualUser.getOrders().getFirst().getId(), remainingOrder.getId());
        assertNotEquals(actualUser.getOrders().getFirst().getId(), orders.getFirst().getId());
    }

    @Test
    void handleRemoveOrderFromUser_ShouldThrowException_WhenOrderDoesNotExist(){
        // Arrange
        User user = new User("name");
        addUser(user);

        // Act
        assertThrows(Exception.class, () -> userService.removeOrderFromUser(user.getId(), UUID.randomUUID()));
    }

    @Test
    void handleRemoveOrderFromUser_ShouldThrowException_WhenOrderDoesNotBelongToUser(){
        // Arrange
        User user = new User("name");
        
        User differentUser = new User("name");

        ArrayList<Order> differentUserOrders = new ArrayList<Order>();
        differentUserOrders.add(new Order(differentUser.getId()));
        addOrder(differentUserOrders.getFirst());


        differentUser.setOrders(differentUserOrders);
        addUser(differentUser);
        
        
        // Act && Assert
        assertThrows(Exception.class, () -> userService.removeOrderFromUser(user.getId(), differentUserOrders.getFirst().getId()));
    }
    // endregion

    // region deleteUserById
    @Test
    void handleDeleteUserById_ShouldUpdateJsonFile_WhenGivenValidUserId(){
        // Arrange
        User user = new User("name");

        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("name",1));
        addProduct(products.getFirst());

        Cart cart = new Cart(user.getId(), products);
        addCart(cart);

        ArrayList<Order> orders = new ArrayList<Order>();
        orders.add(new Order(user.getId()));
        addOrder(orders.getFirst());

        
        user.setOrders(orders);
        addUser(user);
    
        // Act
        userService.deleteUserById(user.getId());
        
        // Assert
        assertTrue(getUsers().isEmpty());
        assertTrue(getCarts().isEmpty());
        assertTrue(getOrders().isEmpty());
    }

    @Test
    void handleDeleteUserById_ShouldNotUpdateCartsJsonFile_WhenUserCartsDoNotExist(){
        // Arrange
        User user = new User("name");
        addUser(user);

        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("name",1));
        addProduct(products.getFirst());

        Cart cart = new Cart(UUID.randomUUID(), products);
        addCart(cart);

        // Act
        userService.deleteUserById(user.getId());

        // Assert
        assertTrue(getUsers().isEmpty());
        assertFalse(getCarts().isEmpty());
    }

    @Test
    void handleDeleteUserById_ShouldNotUpdateOrdersJsonFile_WhenUserOrdersDoNotExist(){
        // Arrange
        User user = new User("name");
        addUser(user);

        addOrder(new Order(UUID.randomUUID()));

        // Act
        userService.deleteUserById(user.getId());

        // Assert
        assertTrue(getUsers().isEmpty());
        assertFalse(getOrders().isEmpty());
    }

    @Test
    void handleDeleteUserById_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> userService.deleteUserById(null));
    }

    @Test
    void handleDeleteUserById_ShouldThrowException_WhenGivenNonExistentUserId(){
        assertThrows(Exception.class, () -> userService.deleteUserById(UUID.randomUUID()));
    }
    // endregion
    // endregion

    // region Product
    // region addProduct
    @Test
    void handleAddProduct_ShouldReturnValidProduct_WhenGivenValidProduct(){
        // Arrange
        Product expectedProduct = new Product("name", 1);

        // Act
        Product actualProduct = productService.addProduct(expectedProduct);

        // Assert
        assertEquals(expectedProduct.getId(), actualProduct.getId());
    }

    @Test
    void handleAddProduct_ShouldAddProductToProductJsonFile_WhenGivenValidProduct(){
        // Arrange
        Product expectedProduct = new Product("name", 1);

        // Act
        productService.addProduct(expectedProduct);

        // Assert
        Product actualProduct = (Product) find(Model.USER, expectedProduct);
        assertNotNull(actualProduct);
    }

    @Test
    void handleAddProduct_ShouldThrowException_WhenGivenDuplicateProduct(){
        // Arrange
        Product product = new Product("name", 1);
        addProduct(product);

        // Act & Assert
        assertThrows(Exception.class, () -> productService.addProduct(new Product(product.getId(), "different name", 2)));
    }
    // endregion

    // region getProducts
    @Test
    void handleGetProducts_ShouldReturnValidArrayList_WhenJsonFileIsNotEmpty(){
        // Arrange
        Product expectedProduct = new Product("name", 1);
        addProduct(expectedProduct);

        // Act
        ArrayList<Product> actualProducts = productService.getProducts();

        // Assert
        assertNotNull(actualProducts);
        assertFalse(actualProducts.isEmpty());
        assertEquals(expectedProduct.getId(), actualProducts.getFirst().getId());
    }

    @Test
    void handleGetProducts_ShouldReturnEmptyArrayList_WhenJsonFileIsEmpty(){
        // Act
        ArrayList<Product> actualProducts = productService.getProducts();

        // Assert
        assertNotNull(actualProducts);
        assertTrue(actualProducts.isEmpty());
    }

    @Test
    void handleGetProducts_ShouldReturnValidArrayList_WhenJsonFileIsLarge(){
        // Arrange
        int n = 1000;
        for(int i = 0; i < n; i++){
            addProduct(new Product("name", 1));
        }

        // Act
        ArrayList<Product> actualProducts = productService.getProducts();

        // Assert
        assertNotNull(actualProducts);
        assertEquals(n, actualProducts.size());
    }
    // endregion

    // region getProductById
    @Test
    void handleGetProductById_ShouldReturnValidProduct_WhenGivenValidProductId(){
        // Arrange
        Product expectedProduct = new Product("ame", 1);
        addProduct(expectedProduct);

        // Act
        Product actualProduct = productService.getProductById(expectedProduct.getId());

        // Assert
        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getId(), actualProduct.getId());
    }

    @Test
    void handleGetProductById_ShouldThrowException_WhenGivenNonExistentProductId(){
        assertThrows(Exception.class, () -> productService.getProductById(UUID.randomUUID()));
    }

    @Test
    void handleGetProductById_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> productService.getProductById(null));
    }
    // endregion

    // region update Product
    @Test
    void handleUpdateProduct_ShouldReturnValidProductAndUpdateJsonFiles_WhenGivenValidArgs(){
        // Arrange
        User user = new User("name");

        Product product = new Product("name", 1);
        addProduct(product);
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        Cart cart = new Cart(user.getId(), products);
        addCart(cart);

        Order order = new Order(user.getId(), products);
        addOrder(order);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);

        user.setOrders(orders);
        addUser(user);

        String newName = "new name";
        double newPrice = 1.2F;
        // Act
        Product actualProduct = productService.updateProduct(product.getId(), newName, newPrice);

        // Assert
        assertEquals(product.getId(), actualProduct.getId());
        assertEquals(newName, actualProduct.getName());
        assertEquals(newPrice, actualProduct.getPrice());

        Cart actualCart = (Cart) find(Model.CART, cart);
        assertEquals(actualProduct.getPrice(), actualCart.getProducts().getFirst().getPrice());

        Order actualOrder = (Order) find(Model.ORDER, order);
        assertEquals(order, actualOrder);
    }

    @Test
    void handleUpdateProduct_ShouldThrowException_WhenGivenNonExistentProductId(){
        assertThrows(Exception.class,() -> productService.updateProduct(UUID.randomUUID(),"name", 1));
    }

    @Test
    void handleUpdateProduct_ShouldThrowException_WhenGivenNullProductId(){
        assertThrows(Exception.class,() -> productService.updateProduct(null,"name", 1));
    }
    // endregion

    // region applyDiscount
    @Test
    void handleApplyDiscount_ShouldUpdateJsonFiles_WhenGivenValidArgs(){
        // Arrange
        User user = new User("name");

        Product product = new Product("name", 1);
        addProduct(product);
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        Cart cart = new Cart(user.getId(), products);
        addCart(cart);

        Order order = new Order(user.getId(), products);
        addOrder(order);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);

        user.setOrders(orders);
        addUser(user);

        double discount = 10;
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(product.getId());

        // Act
        productService.applyDiscount(discount, productIds);

        // Assert
        Product actualProduct = (Product) find(Model.PRODUCT, product);
        assertEquals(product.getId(), actualProduct.getId());
        assertEquals(product.getPrice() * (100 - discount) * 0.01 , actualProduct.getPrice());

        Cart actualCart = (Cart) find(Model.CART, cart);
        assertEquals(actualProduct.getPrice(), actualCart.getProducts().getFirst().getName());

        Order actualOrder = (Order) find(Model.ORDER, order);
        assertEquals(order, actualOrder);
    }

    @Test
    void handleApplyDiscount_ShouldThrowException_WhenGivenInvalidDiscount(){
        // Arrange
        Product product = new Product("name", 1);
        addProduct(product);
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(product.getId());

        // Act & Assert
        assertThrows(Exception.class, () -> productService.applyDiscount(110, productIds));
    }

    @Test
    void handleApplyDiscount_ShouldThrowException_WhenGivenNonExistentProductIds(){
        // Arrange
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(UUID.randomUUID());

        // Act & Assert
        assertThrows(Exception.class, () -> productService.applyDiscount(10, productIds));
    }
    // endregion

    // region deleteProductById
    @Test
    void handleDeleteProductById_ShouldUpdateJsonFile_WhenGivenValidProductId(){
        // Arrange
        User user = new User("name");

        Product product = new Product("name", 1);
        addProduct(product);
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        Cart cart = new Cart(user.getId(), products);
        addCart(cart);

        Order order = new Order(user.getId(), products);
        addOrder(order);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);

        user.setOrders(orders);
        addUser(user);


        // Act
        productService.deleteProductById(product.getId());

        // Assert
        Product actualProduct = (Product) find(Model.PRODUCT, product);
        assertNull(actualProduct);

        Cart actualCart = (Cart) find(Model.CART, cart);
        assertTrue(actualCart.getProducts().isEmpty());

        Order actualOrder = (Order) find(Model.ORDER, order);
        assertEquals(order, actualOrder);
    }

    @Test
    void handleDeleteProductById_ShouldThrowException_WhenGivenNonExistentProductId(){
        assertThrows(Exception.class, () -> productService.deleteProductById(UUID.randomUUID()));
    }

    @Test
    void handleDeleteProductById_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> productService.deleteProductById(null));
    }
    // endregion
    // endregion

    // region Cart
    // region addCart
    @Test
    void handleAddCart_ShouldReturnValidCart_WhenGivenValidCart(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Product product = new Product("name", 1);
        addProduct(product);
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        Cart expectedCart = new Cart(user.getId(), products);

        // Act
        Cart actualCart = cartService.addCart(expectedCart);

        // Assert
        assertEquals(expectedCart, actualCart);
        assertEquals(expectedCart, find(Model.CART, expectedCart));
    }

    @Test
    void handleAddCart_ShouldThrowException_WhenGivenDuplicateCart(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart cart = new Cart(user.getId());
        addCart(cart);

        Cart duplicateCart = new Cart(user.getId());

        // Act & Assert
        assertThrows(Exception.class, () -> cartService.addCart(duplicateCart));
    }

    @Test
    void handlerAddCart_ShouldThrowException_WhenGivenNonExistentUserId(){
        assertThrows(Exception.class, () -> cartService.addCart(new Cart(UUID.randomUUID())));
    }
    // endregion

    // region getCarts
    @Test
    void handleGetCarts_ShouldReturnValidArrayList_WhenJsonFileIsNotEmpty(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart expectedCart = new Cart(user.getId());
        addCart(expectedCart);

        // Act
        ArrayList<Cart> actualCarts = cartService.getCarts();

        // Assert
        assertEquals(expectedCart.getId(), actualCarts.getFirst().getId());
    }

    @Test
    void handleGetCarts_ShouldReturnEmptyArrayList_WhenJsonFileIsEmpty(){
        // Act
        ArrayList<Cart> actualCarts = cartService.getCarts();

        // Assert
        assertNotNull(actualCarts);
        assertTrue(actualCarts.isEmpty());
    }

    @Test
    void handleGetCarts_ShouldReturnValidArrayList_WhenJsonFileIsLarge() {
        // Arrange
        int n = 1000;
        for (int i = 0; i < n; i++) {
            User user = new User("name");
            addUser(user);

            addCart(new Cart(user.getId()));
        }

        // Act
        ArrayList<Cart> actualCarts = cartService.getCarts();

        // Assert
        assertEquals(n, actualCarts.size());
    }
    // endregion

    // region getCartById
    @Test
    void handleGetCartById_ShouldReturnValidCart_WhenGivenValidCartId(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart expectedCart = new Cart(user.getId());
        addCart(expectedCart);

        // Act
        Cart actualCart = cartService.getCartById(expectedCart.getId());

        // Arrange
        assertEquals(expectedCart, actualCart);
    }

    @Test
    void handleGetCartById_ShouldThrowException_WhenGivenNonExistentCartId(){
        assertThrows(Exception.class, () -> cartService.getCartById(UUID.randomUUID()));
    }

    @Test
    void handleGetCartById_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> cartService.getCartById(null));
    }
    // endregion

    // region getCartByUserId
    @Test
    void handleGetCartByUserId_ShouldReturnValidCart_WhenGivenValidUserId(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart expectedCart = new Cart(user.getId());
        addCart(expectedCart);

        // Act
        Cart actualCart = cartService.getCartByUserId(user.getId());

        // Arrange
        assertEquals(expectedCart, actualCart);
    }

    @Test
    void handleGetCartByUserId_ShouldThrowException_WhenGivenNonExistentCartId(){
        assertThrows(Exception.class, () -> cartService.getCartByUserId(UUID.randomUUID()));
    }

    @Test
    void handleGetCartByUserId_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> cartService.getCartByUserId(null));
    }
    // endregion

    // region addProductToCart
    @Test
    void handleAddProductToCart_ShouldUpdateJsonFile_WhenGivenValidArgs(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart cart = new Cart(user.getId());
        addCart(cart);

        Product product = new Product("name", 1);
        addProduct(product);

        // Act
        cartService.addProductToCart(cart.getId(), product);

        // Assert
        Cart actualCart = (Cart) find(Model.CART, cart);
        assertEquals(product, actualCart.getProducts().getFirst());
    }

    @Test
    void handleAddProductToCart_ShouldThrowException_WhenGivenNonExistentCartId(){
        // Arrange
        Product product = new Product("name", 1);
        addProduct(product);

        // Act & Assert
        assertThrows(Exception.class, () -> cartService.addProductToCart(UUID.randomUUID(), product));
    }

    @Test
    void handleAddProductToCart_ShouldThrowException_WhenGivenNonExistentProduct(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart cart = new Cart(user.getId());
        addCart(cart);

        Product product = new Product("name", 1);

        // Act & Assert
        assertThrows(Exception.class, () -> cartService.addProductToCart(cart.getId(), product));
    }
    // endregion

    // region deleteProductFromCart
    @Test
    void handleDeleteProductFromCart_ShouldUpdateJsonFile_WhenGivenValidArgs(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Product product = new Product("name", 1);
        addProduct(product);
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        Cart cart = new Cart(user.getId(), products);
        addCart(cart);

        // Act
        cartService.deleteProductFromCart(cart.getId(), product);

        // Assert
        Cart actualCart = (Cart) find(Model.CART, cart);
        assertTrue(actualCart.getProducts().isEmpty());
    }

    @Test
    void handleDeleteProductFromCart_ShouldThrowException_WhenGivenNonExistentCartId(){
        // Arrange
        Product product = new Product("name", 1);
        addProduct(product);

        // Act & Assert
        assertThrows(Exception.class, () -> cartService.deleteProductFromCart(UUID.randomUUID(), product));
    }

    @Test
    void handleAddProductToCart_ShouldThrowException_WhenGivenProductNotInCart(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart cart = new Cart(user.getId());
        addCart(cart);

        Product product = new Product("name", 1);
        addProduct(product);

        // Act & Assert
        assertThrows(Exception.class, () -> cartService.deleteProductFromCart(cart.getId(), product));
    }
    // endregion

    // region deleteCartById
    @Test
    void handleDeleteCartById_ShouldUpdateJsonFile_WhenGivenValidCartId(){
        // Arrange
        User user = new User("name");
        addUser(user);

        Cart cart = new Cart(user.getId());
        addCart(cart);

        // Act
        cartService.deleteCartById(cart.getId());

        // Arrange
        assertNull(find(Model.CART, cart));
    }

    @Test
    void handleDeleteCartById_ShouldThrowException_WhenGivenNonExistentCartId(){
        assertThrows(Exception.class, () -> cartService.deleteCartById(UUID.randomUUID()));
    }

    @Test
    void handleDeleteCartById_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> cartService.deleteCartById(null));
    }
    // endregion
    // endregion

    // region Order
    // region addOrder
    @Test
    void handleAddOrder_ShouldUpdateJsonFiles_WhenGivenValidOrder(){
        // Arrange
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("name",1));
        addProduct(products.getFirst());

        User user = new User("name");
        addUser(user);

        Order expectedOrder = new Order(user.getId(), products);

        // Act
        orderService.addOrder(expectedOrder);

        // Assert
        Order actualOrder = (Order) find(Model.ORDER, expectedOrder);
        assertNotNull(actualOrder);
        assertEquals(expectedOrder.getId(), actualOrder.getId());

        User actualUser = (User) find(Model.USER, user);
        assertEquals(expectedOrder.getId(), actualUser.getOrders().getFirst().getId());
    }

    @Test
    void handleAddOrder_ShouldThrowException_WhenGivenNonExistentUserId(){
        // Arrange
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("name",1));
        addProduct(products.getFirst());

        // Act & Assert
        assertThrows(Exception.class, () -> orderService.addOrder(new Order(UUID.randomUUID(), products)));
    }

    @Test
    void handleAddOrder_ShouldThrowException_WhenGivenNonExistentProduct(){
        // Arrange
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("name",1));

        User user = new User("name");
        addUser(user);

        // Act & Assert
        assertThrows(Exception.class, () -> orderService.addOrder(new Order(user.getId(), products)));
    }
    // endregion

    // region getOrders
    @Test
    void handleGetOrders_ShouldReturnValidArrayList_WhenJsonFileIsNotEmpty(){
        // Arrange
        Order expectedOrder = new Order(UUID.randomUUID());
        addOrder(expectedOrder);

        // Act
        ArrayList<Order> actualOrders = orderService.getOrders();

        // Assert
        assertNotNull(actualOrders);
        assertFalse(actualOrders.isEmpty());
        assertEquals(expectedOrder.getId(), actualOrders.getFirst().getId());
    }

    @Test
    void handleGetOrders_ShouldReturnEmptyArrayList_WhenJsonFileIsEmpty(){
        // Act
        ArrayList<Order> actualOrders = orderService.getOrders();

        // Assert
        assertNotNull(actualOrders);
        assertTrue(actualOrders.isEmpty());
    }

    @Test
    void handleGetOrders_ShouldReturnValidArrayList_WhenJsonFileIsLarge(){
        // Arrange
        int n = 1000;
        for(int i = 0; i < n; i++){
            addOrder(new Order(UUID.randomUUID()));;
        }

        // Act
        ArrayList<Order> actualOrders = orderService.getOrders();;

        // Assert
        assertNotNull(actualOrders);
        assertEquals(n, actualOrders.size());
    }
    // endregion

    // region getOrderById
    @Test
    void handleGetOrderById_ShouldReturnValidOrder_WhenGivenValidOrderId(){
        // Arrange
        Order expectedOrder = new Order(UUID.randomUUID());
        addOrder(expectedOrder);

        // Act
        Order actualOrder = orderService.getOrderById(expectedOrder.getId());

        // Arrange
        assertNotNull(actualOrder);
        assertEquals(expectedOrder.getId(), actualOrder.getId());
    }

    @Test
    void handleGetOrderById_ShouldThrowException_WhenGivenNonExistentOrderId(){
        assertThrows(Exception.class, () -> orderService.getOrderById(UUID.randomUUID()));
    }

    @Test
    void handleGetOrderById_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> orderService.getOrderById(null));
    }
    // endregion

    // region deleteOrderById
    @Test
    void handleDeleteOrderById_ShouldUpdateJsonFiles_WhenGivenValidOrderId(){
        // Arrange
        User user = new User("name");

        ArrayList<Order> orders = new ArrayList<Order>();
        for(int i = 0; i < 2; i++){
            orders.add(new Order(user.getId()));
            addOrder(orders.get(i));
        }

        user.setOrders(orders);
        addUser(user);

        // Act
        orderService.deleteOrderById(orders.getFirst().getId());

        // Assert
        Order removedOrder = (Order) find(Model.ORDER, orders.getFirst());
        assertNull(removedOrder);

        Order remainingOrder = (Order) find(Model.ORDER, orders.getLast());
        assertNotNull(remainingOrder);

        User actualUser = (User) find(Model.USER, user);
        assertEquals(actualUser.getOrders().size(), 1);
        assertEquals(actualUser.getOrders().getFirst().getId(), remainingOrder.getId());
        assertNotEquals(actualUser.getOrders().getFirst().getId(), orders.getFirst().getId());
    }

    @Test
    void handleDeleteOrderById_ShouldThrowException_WhenGivenNull(){
        assertThrows(Exception.class, () -> orderService.deleteOrderById(null));
    }

    @Test
    void handleDeleteOrderByID_ShouldThrowException_WhenGivenNonExistentOrderID(){
        assertThrows(Exception.class, () -> orderService.deleteOrderById(UUID.randomUUID()));
    }
    // endregion
    // endregion
}