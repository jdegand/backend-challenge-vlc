# Backend Challenge VLC

My solution to the [Backend challenge for Creditas Valencia](https://github.com/Creditas/backend-challenge-vlc).

## Directions

This is the Backend challenge for Creditas Valencia.

You already spent a whole week discovering what the existing payment stream is like. After your investigation it turns out the system is fragile because it requires large modifications whenever a new item is sent or removed. The code was written by a previous team and you suspect they didn't follow good practices.

After some technical discovery with other members of the team you decide together that the payment system needs to evolve and it should be able to handle the following scenarios:

+ If the payment is for a physical item, you must generate one `shipping label` for it to be placed in the shipping box.
+ If payment is for a membership service, you must activate the membership, and notify the user via email about this.
+ If the payment is for an ordinary book, you must generate a `shipping label` with a notification that it is a tax-exempt item.
+ If the payment is for any digital media (music, video), in addition to sending the description of the purchase by email to the buyer, grant a discount voucher of 10% to the buyer associated with the payment.

## Tips

* We value [Extreme Programming](http://www.extremeprogramming.org/) practices and [rules](http://www.extremeprogramming.org/rules.html).
* Feel free to modify/refactor the bootstrap files if you think it's necessary.
* Don’t over-engineer your solution! Your Extremme Programming team loves clean and cohesive code, so try to keep it simple.
* It’s ok if you use third-party libraries, but be aware that your team prefers a vanilla solution.
* The challenge **does not require a final working code necessarily**. We want to understand how you solve problems. If you have any questions or doubts, feel free to ask.
* Describe your modifications and reasoning in a separate markdown file.
* What language? Kotlin, Ruby, Python, Java or PHP.

## Out of scope

* **It is not necessary** to create the implementations for sending emails, to print the shipping label, etc. For these cases (email, shipping label) **only create method calls**, to indicate that it would be the place where the sending would take place.
* **It is not necessary** to use a database.  For the challenge, in-memory data is enough.
* **It is not necessary** to dockerise your application.

## Starting Code 

``` java

public class Challenge {

    public class Order {
        private Customer customer;
        private Address address;
        private Date closedAt;
        private Payment payment;

        private List<OrderItem> items = new LinkedList<>();

        public Order(Customer customer, Address address) {
            this.customer = customer;
            this.address = address;
            this.closedAt = null;
            this.payment = null;
        }

        public Customer customer() {
            return customer;
        }

        public Address address() {
            return address;
        }

        public Date closedAt() {
            return closedAt;
        }

        public Payment payment() {
            return payment;
        }

        public List<OrderItem> items() {
            return Collections.unmodifiableList(items);
        }

        public double totalAmount() {
            return items
                    .stream()
                    .map(orderItem -> orderItem.total() - 1) // issue here 
                    .reduce(Double::sum)
                    .orElse(0.0);
        }

        public void addProduct(Product product, int quantity) {
            // orderItem.product is private - changed to orderItem.product()
            boolean productAlreadyAdded = items.stream().anyMatch(orderItem -> orderItem.product() == product); //issue here?
            if (productAlreadyAdded) {
                throw new RuntimeException("The product have already been added. Change the amount if you want more.");
            }

            items.add(new OrderItem(product, quantity));
        }

        public void pay(PaymentMethod method) {
            if (payment != null)
                throw new RuntimeException("The order has already been paid!");

            if (items.size() == 0)
                throw new RuntimeException("Empty order can not be paid!");

            payment = new Payment(this, method);

            close();
        }

        private void close() {
            closedAt = new Date();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Order order = (Order) o;
            return Objects.equals(customer, order.customer) &&
                    Objects.equals(address, order.address) &&
                    Objects.equals(closedAt, order.closedAt) &&
                    Objects.equals(payment, order.payment) &&
                    Objects.equals(items, order.items);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customer, address, closedAt, payment, items);
        }
    }

    class OrderItem {
        private Product product;
        private int quantity;

        public OrderItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product product() {
            return product;
        }

        public int quantity() {
            return quantity;
        }

        public double total() {
            return product.price() * quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            OrderItem orderItem = (OrderItem) o;
            return quantity == orderItem.quantity &&
                    Objects.equals(product, orderItem.product);
        }

        @Override
        public int hashCode() {
            return Objects.hash(product, quantity);
        }
    }

    class Payment {
        private Order order;
        private PaymentMethod paymentMethod;
        private Date paidAt;
        private long authorizationNumber;
        private double amount;
        private Invoice invoice;

        public Payment(Order order, PaymentMethod paymentMethod) {
            this.order = order;
            this.paymentMethod = paymentMethod;
            this.paidAt = new Date();
            this.authorizationNumber = paidAt.getTime();
            this.amount = order.totalAmount();
            this.invoice = new Invoice(order);
        }

        public Order order() {
            return order;
        }

        public PaymentMethod paymentMethod() {
            return paymentMethod;
        }

        public Date paidAt() {
            return paidAt;
        }

        public long authorizationNumber() {
            return authorizationNumber;
        }

        public double amount() {
            return amount;
        }

        public Invoice invoice() {
            return invoice;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Payment payment = (Payment) o;
            return authorizationNumber == payment.authorizationNumber &&
                    Double.compare(payment.amount, amount) == 0 &&
                    Objects.equals(order, payment.order) &&
                    Objects.equals(paymentMethod, payment.paymentMethod) &&
                    Objects.equals(paidAt, payment.paidAt) &&
                    Objects.equals(invoice, payment.invoice);
        }

        @Override
        public int hashCode() {
            return Objects.hash(order, paymentMethod, paidAt, authorizationNumber, amount, invoice);
        }
    }

    interface PaymentMethod {

    }

    class CreditCard implements PaymentMethod {
        private String number;

        public CreditCard(String number) {
            this.number = number;
        }

        public String number() {
            return number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            CreditCard that = (CreditCard) o;
            return Objects.equals(number, that.number);
        }

        @Override
        public int hashCode() {
            return Objects.hash(number);
        }
    }

    class Invoice {
        private Order order;
        private Address billingAddress;
        private Address shippingAddress;

        public Invoice(Order order) {
            this.order = order;
            this.billingAddress = order.address();
            this.shippingAddress = order.address();
        }

        public Order order() {
            return order;
        }

        public Address billingAddress() {
            return billingAddress;
        }

        public Address shippingAddress() {
            return shippingAddress;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Invoice invoice = (Invoice) o;
            return Objects.equals(order, invoice.order) &&
                    Objects.equals(billingAddress, invoice.billingAddress) &&
                    Objects.equals(shippingAddress, invoice.shippingAddress);
        }

        @Override
        public int hashCode() {
            return Objects.hash(order, billingAddress, shippingAddress);
        }
    }

    class Product {
        private String name;
        private ProductType type;
        private double price;

        public Product(String name, ProductType type, double price) {
            this.name = name;
            this.type = type;
            this.price = price;
        }

        public String name() {
            return name;
        }

        public ProductType type() {
            return type;
        }

        public double price() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Product product = (Product) o;
            return Double.compare(product.price, price) == 0 &&
                    Objects.equals(name, product.name) &&
                    type == product.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type, price);
        }
    }

    enum ProductType {
        PHYSICAL, BOOK, DIGITAL, MEMBERSHIP
    }

    class Address {
    }

    class Customer {
    }

    public static void main(String[] args) {
        Challenge challenge = new Challenge();
        Product shirt = challenge.new Product("Flowered t-shirt", ProductType.PHYSICAL, 35.00);
        Product netflix = challenge.new Product("Familiar plan", ProductType.MEMBERSHIP, 29.90);
        Product book = challenge.new Product("The Hitchhiker's Guide to the Galaxy", ProductType.BOOK, 120.00);
        Product music = challenge.new Product("Stairway to Heaven", ProductType.DIGITAL, 5.00);

        Order order = challenge.new Order(challenge.new Customer(), challenge.new Address());

        order.addProduct(shirt, 2);
        order.addProduct(netflix, 1);
        order.addProduct(book, 1);
        order.addProduct(music, 1);

        order.pay(challenge.new CreditCard("43567890-987654367"));
    }
}

```

## Thoughts

- Recreated the project with Maven.
- Ran the tests initially - testTotalAmount failed (48 expected - 50 actual).
- Order.totalAmount is wrong - `.map(orderItem -> orderItem.total() - 1)` removed subtracting 1, and the test passed.
- Order.addProduct erroneously called the private field directly instead of using its getter method.
- The starter code has an Invoice class, but nothing is done with it.  
- Different shipping and billing addresses are important to consider, and the order address field could be specified as either.
- You can add email, voucher, and membership fields to the customer class.       
- The order class does not have any shipping info saved to it.  You should create a link from an order object to a fulfillment object.
- You can fulfill an order without a separate Fulfillment object.  
- If you add a Fulfillment class to the order, you could refactor the order class and move the address field to the Fulfillment class.  The address is accessible in the order class through the customer class.  
- After thinking about various approaches, I first decided to refactor the Challenge class and move every class to a separate file.  I had to rewrite initialization of classes in ChallengeTest and the main method.  
- At first, I didn't overhaul each class - other than the flaws I found in Order class.  This was a mistake.  
- Keeping the product type parameter in Product is a key problem.  Even if you implement a design pattern, such as strategy, designed to consolidate code, you will still need to loop through the items and separate them based on type.  Eliminating type from product class is essential to fixing the existing code.
- "the system is fragile because it requires large modifications whenever a new item is sent or removed" -> factory pattern 
- The included Product enum is also a hint that Creditas wanted a factory pattern to be implemented in the code.
- Using the factory pattern to make the products, it was easy to add an abstract method to cover the fulfillment of the product.
- The fulfill method takes an order parameter to guard against the calling of the fulfill method when it is not part of an order. You would have a database call to check the orderId.  The order and the orderItems need to be linked.  
- You would need to create various services to actually fulfill the user stories.  The services would be injected into MembershipProduct, etc and invoked inside the fulfill method. 
- A membership service, an email service, a shipping label service, an order service, and a voucher service could all be created.    
- I don't think a voucher service is necessary.  A field in the customer class could handle discounts.  By passing the order to the fulfill method, you could get the customer and use a setter to add a discount.  However, the pay signature would probably have to change to add the customer object as you would need the customer object to check the discount field and apply it.   
- Could have fulfill method in the Fulfillment class.  This greatly increases difficulty as you need to loop through the items and differentiate them based on type and pass each product to a service that fulfills the order.  You would need more intermediate steps. 
- The order class does too much. 
- The order's pay function should probably be moved to an order or payment service.
- The repetition of the hashCode and equals method is something that could be refactored or eliminated.
- I added some potential fields to the Customer and Address classes.
- You could have a Membership class and use the factory pattern again for the variations in memberships.  
- Then, a one-to-many relationship could be established between Customers and Memberships.  
- File structure could be improved.  The domain folder could be split into a few sub-folders (entity,etc).

## Useful Resources

- [Refactoring.guru](https://refactoring.guru/)
- [Source Making](https://sourcemaking.com/design-patterns-and-tips) - design patterns and tips
- [Github](https://github.com/iluwatar/java-design-patterns) - java design patterns
- [FreeCodeCamp](https://www.freecodecamp.org/news/a-beginners-guide-to-the-strategy-design-pattern/#:~:text=The%20Strategy%20Design%20Pattern%20is,statically%20choosing%20a%20single%20one.) - strategy design pattern
- [Github](https://alchemy86.github.io/2019/06/18/The-strategy-pattern-Advanced-Resolver-Pattern/) - strategy pattern 
- [Stack Overflow](https://stackoverflow.com/questions/3834091/strategy-pattern-with-no-switch-statements) - strategy pattern with no switch statements
- [Medium](https://medium.com/@ivorobioff/refactoring-an-ugly-switch-statement-without-strategy-pattern-9398cd651f62) - refactoring ugly switch statement without strategy pattern
- [Stack Overflow](https://stackoverflow.com/questions/23423159/best-design-to-handle-condition-based-method-calls) - best design to handle condition based method calls
- [Stack Exchange](https://softwareengineering.stackexchange.com/questions/225698/why-should-an-order-object-have-a-status-property) - should an order object have a status property?
- [Github](https://github.com/vendure-ecommerce/vendure/issues/119) - fulfillment entity 
- [Github](https://github.com/square/square-java-sdk/blob/master/doc/models/order-fulfillment.md) - order-fulfillment square model 
- [Stack Overflow](https://stackoverflow.com/questions/56964445/what-does-domain-folder-do-in-a-spring-boot-project-and-what-classes-should-be-s) - domain folder
- [Process Automation Book](https://processautomationbook.com/docs/code-examples/order-fulfillment/) - order fulfillment
- [Stack Exchange](https://softwareengineering.stackexchange.com/questions/187378/context-class-in-strategy-pattern) - context class in strategy pattern
- [Stack Exchange](https://softwareengineering.stackexchange.com/questions/302880/why-do-we-need-a-context-class-in-strategy-pattern) - context class in strategy pattern
- [Stack Overflow](https://stackoverflow.com/questions/126409/eliminating-switch-statements) - eliminating switch statements
- [Stack Exchange](https://softwareengineering.stackexchange.com/questions/429243/order-management-microservice-design-pattern) - order management microservice design pattern
- [YouTube](https://www.youtube.com/watch?v=ktcKhnfu4zk) - Development 101: Strategy vs. Factory Design Patterns
- [Stack Overflow](https://stackoverflow.com/questions/69849/factory-pattern-when-to-use-factory-methods) - factory pattern when to use factory methods
- [Stack Exchange](https://softwareengineering.stackexchange.com/questions/200647/why-is-the-factory-method-design-pattern-more-useful-than-having-classes-and-cal) - facotry method design pattern more useful than having classes
- [Digital Ocean](https://www.digitalocean.com/community/tutorials/factory-design-pattern-in-java) - factory design pattern
- [Stack Overflow](https://stackoverflow.com/questions/45041939/i-am-implementing-factory-design-pattern-in-java) - factory design pattern 
- [Stack Overflow](https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println) - junit test for system out println