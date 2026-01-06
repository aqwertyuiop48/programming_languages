package com.javatpoint;

import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.github.bonigarcia.wdm.WebDriverManager;
import reactor.core.publisher.Flux;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringBootRestExampleApplicationTests {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Test Example.com page title")
    void testPageTitle() {
        driver.get("https://example.com");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        assertEquals("Example Domain", driver.getTitle());
    }

    @Test
    @DisplayName("Test heading text on Example.com")
    void testHeadingText() {
        driver.get("https://example.com");
        WebElement heading =
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

        assertEquals("Example Domain", heading.getText());
    }

    @Test
    @DisplayName("Spring webflux")
    void springWebFlux(){
        System.out.println(countBalls(1,10));
        System.out.println(countBalls(5,15));
    }
    public int countBalls(int lowLimit, int highLimit) {
        return Flux.fromIterable(() -> IntStream.rangeClosed(lowLimit, highLimit).iterator())
                .flatMap(n -> Flux.just(n).expand(i -> i > 9 ? Flux.just(i / 10) : Flux.empty()).map(i -> i % 10).reduce(0, Integer::sum))
                .groupBy(sum -> sum)
                .flatMap(Flux::count)
                .reduce(0L, Math::max)
                .block()
                .intValue();
    }

    @Test
    @DisplayName("Test page body text content")
    void testPageTextContent() {
        driver.get("https://example.com");
        WebElement body =
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        assertTrue(body.getText().contains("This domain is for use in documentation"));
    }

    @Test
    @DisplayName("Test navigation to IANA website")
    void testNavigation() {
        driver.get("https://example.com");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        assertTrue(driver.getCurrentUrl().contains("example.com"));

        driver.get("https://www.iana.org/domains/reserved");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

        assertTrue(driver.getCurrentUrl().contains("iana.org"));
    }

    @Test
    @DisplayName("Test page has content")
    void testPageHasContent() {
        driver.get("https://example.com");
        WebElement body =
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        assertNotNull(body);
        assertTrue(body.getText().length() > 0);
        assertTrue(body.getText().contains("Example Domain"));
    }

    @Test
    @DisplayName("Test page structure")
    void testPageStructure() {
        driver.get("https://example.com");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        WebElement heading = driver.findElement(By.tagName("h1"));
        WebElement body = driver.findElement(By.tagName("body"));

        assertNotNull(heading);
        assertTrue(body.isDisplayed());
    }

    @Test
    @DisplayName("Code execution")
    void code() throws IOException {
        System.out.println("""
            2 + 33 = %s
            2 + 33 = %d
            """
                .formatted(2 + 33
                        ,2+33
                ));

        Set<Integer> s1 = new LinkedHashSet<>();
        s1.addAll(new ArrayList<>(List.of(1, 2)));
        System.out.println(s1);
        System.out.println(s1.size());

        Set<Integer> s2 = new LinkedHashSet<>();
        s2.addAll(new ArrayList<>(Arrays.asList(1, 2)));
        s2.add(null);
        System.out.println(s2);
        System.out.println(s2.size());

        Files_();
    }

    public void Files_() throws IOException {
        Path path = Paths.get("test.txt");
        Files.writeString(Paths.get("test.txt"), """
                Hello
                This is my first file
                This is from Spring Boot App
                """,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);

        System.out.println(Files.readAllLines(Path.of("test.txt")));
        List<String> list = Files.readAllLines(Path.of("test.txt"));
        list.stream().sorted(Comparator.comparing(String::length)).collect(Collectors.toList());
        Collections.reverse(list);

        Files.writeString(path,"");
        System.out.println(Files.readAllLines(Path.of("test.txt")));
        Files.write(path,list,StandardOpenOption.APPEND);
        System.out.println(Files.readAllLines(Path.of("test.txt")));
        Files.deleteIfExists(Path.of("test.txt"));
    }


    @Test
    @DisplayName("Constructor Chaining and Design patterns")
    void next(){
        new ConstructorChaining().main();
        new DesignPatterns().main();
    }

}

class SuperConstructorChaining{ }
class ConstructorChaining extends SuperConstructorChaining{
    // calling a constructor from another constructor
    // this() and super()
    // constructor overloading is mandatory
    ConstructorChaining(){
        // “Java allows only one explicit constructor call (this or super) and it must be the first statement, because constructor chaining must be linear.”
        this(5);
        System.out.println("This is default constructor!");
    }
    ConstructorChaining(int i){
        this("Interview preparation");
        System.out.println("This is int constructor!");
    }
    ConstructorChaining(String s){
        this(6f);
        System.out.println("This is String constructor!");
    }
    ConstructorChaining(float s){
        super();
        System.out.println("This is float constructor!");
    }

    public void main(){
        ConstructorChaining cc = new ConstructorChaining();
        //cc.main();
    }
}

class DesignPatterns{
    public void main () {
        System.out.println("=======================Creational patterns===================");

        System.out.println("=======================Singleton pattern===================");
        new Singleton().main();

        System.out.println("=======================Builder pattern===================");
        User user = User.builder()
                .firstName("Sreedhar")
                .lastName("K")
                .age(30)
                .country("India")
                .profession("Java Developer")
                .build();
        System.out.println(user);

        System.out.println("===================== Factory pattern  =====================");
        user = User.developer("Sreedhar_", "K.")
                .toBuilder()
                .age(26)
                .build();
        System.out.println(user);

        User manager = User.manager("Los", "Angeles")
                .toBuilder()
                .age(46)
                .build();
        System.out.println(manager);

        System.out.println("===================== Prototype pattern  =====================");

        // Prototype pattern - 1
        User proto = User.developer("Proto", "Type");
        User u1 = proto.copy().toBuilder().age(36).build();
        User u2 = proto.copy().toBuilder().age(40).build();
        System.out.println(u1);
        System.out.println(u2);

        // Prototype pattern - 2
        User base = User.builder()
                .firstName("Sreedhar")
                .lastName("K")
                .country("India")
                .profession("Java Developer")
                .build();

        User copy = base.clone().toBuilder()
                .age(30)
                .build();
        System.out.println(copy);

        System.out.println("=======================Structural patterns===================");

        // Adapter pattern
        System.out.println("=======================Adapter pattern===================");
        List<LegacyUser> legacyUsers = List.of(
                new LegacyUser("John Doe", 45),
                new LegacyUser("Jane Smith", 30)
        );
        List<User> users = new LegacyUserAdapter(null).toUsers(legacyUsers);
        users.forEach(System.out::println);

        System.out.println("=======================Decorator pattern===================");
        UserView view =
                new PremiumUserDecorator(new BasicUserView(user));
        System.out.println(view.display());

        System.out.println("=======================Facade pattern===================");
        new UserRegistrationFacade(
                new UserValidator(),
                new UserRepository(),
                new EmailService()
        ).register(user);

        System.out.println("=======================Behavioral patterns===================");

        System.out.println("=======================Strategy pattern===================");
        BillingService billing =
                new BillingService(new PremiumUserDiscount());
        System.out.println(billing.bill(user, 1000));

        System.out.println("=======================Observer pattern===================");
        UserEventPublisher publisher = new UserEventPublisher();
        publisher.subscribe(new EmailObserver("a@b.com"));
        publisher.publish(user, "USER_REGISTERED");

        System.out.println("=======================Command pattern===================");
        new UserCommandExecutor(new ActivateUserCommand()).run(user);

        System.out.println("=======================State pattern===================");
        UserState state = new ActiveState();
        state.handle(user);
        state = new BlockedState();
        state.handle(user);
        state = new ActiveState();
        state.handle(user);

        System.out.println("=======================Iterator pattern===================");
        User user1 = User.builder() .firstName("Sreedhar") .lastName("test") .age(20) .country("India") .profession("Java Developer") .build();
        User user2 = User.builder() .firstName("Sreedhar2") .lastName("test2") .age(30) .country("India") .profession("Java Developer") .build();

        UserCollection collection =
                new UserCollection(List.of(user1, user2));

        for (User u : collection) {
            System.out.println(u);
        }

        System.out.println("=======================Memento pattern===================");
        UserCaretaker caretaker = new UserCaretaker();
        caretaker.save(user);
        user = user.toBuilder().age(50).build();
        user = caretaker.restore(user);
        System.out.println(user);

        System.out.println("=======================Chain of Responsibility pattern===================");
        UserHandler chain =
                new AgeValidationHandler(
                        new CountryValidationHandler(null)
                );
        chain.handle(user);

        System.out.println("=======================Template Method pattern===================");
        new EmailUserRegistration().register(user);

        System.out.println("=======================Mediator pattern===================");
        UserChatMediator mediator = new UserChatMediator();
        mediator.addUser(user1);
        mediator.addUser(user2);
        mediator.sendMessage(user1, "Hello!");

        System.out.println("=======================Visitor pattern===================");
        VisitableUser vu = new VisitableUser(user);
        vu.accept(new UserPrintVisitor());

        System.out.println("=======================Interpreter pattern===================");
        Expression rule =
                new AndExpression(
                        new AgeExpression(18),
                        new CountryExpression("India")
                );

        System.out.println(rule.interpret(user));


    }
}

class Singleton{
    public static void main() {
        System.out.println("Singleton");
        SingletonPattern obj1 = SingletonPattern.getInstance();
        System.out.println(obj1);
        SingletonPattern obj2 = SingletonPattern.getInstance();
        System.out.println(obj2);
    }
}

class SingletonPattern {

    // 1. create a private static instance of this class itself
    private static SingletonPattern obj = new SingletonPattern();

    // 2. private constructor to avoid initialization
    private SingletonPattern() {
    }

    // 3. create a public static method
    public static SingletonPattern getInstance(){
        if(obj == null) { // one without locking so that multiple threads can access it
            // double checked locking pattern
            synchronized (SingletonPattern.class){ // synchronizing only the critical section of the code
                if(obj == null) // one with locking so that only one instance is created from SingletonPattern
                    obj = new SingletonPattern();
            }
        }
        return obj;
    }
}


@Builder(toBuilder = true) // this is a prototype creator as well
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
class User implements Cloneable {

    // required
    private String firstName;
    private String lastName;

    // optional
    private int age;
    private String country;
    private String profession;

    // FACTORY METHODS (Lombok-friendly)
    public static User developer(String firstName, String lastName) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .profession("Java Developer")
                .country("India")
                .build();
    }

    // factory method
    public static User manager(String firstName, String lastName) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .profession("Manager")
                .country("USA")
                .build();

    }

    // PROTOTYPE METHOD - 1
    public User copy() {
        return this.toBuilder().build();
    }

    // prototype method - 2
    @Override
    public User clone() {
        try {
            return (User) super.clone(); // shallow copy
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e); // should never happen
        }
    }
}


// Adapter pattern
@Value
class LegacyUser {
    String fullName;
    int age;
}
@RequiredArgsConstructor
class LegacyUserAdapter {

    private final LegacyUser legacyUser;

    public List<User> toUsers(List<LegacyUser> legacyUsers) {
        return legacyUsers.stream()
                .map(lu -> {
                    String[] names = lu.getFullName().split(" ");
                    return User.builder()
                            .firstName(names[0])
                            .lastName(names[1])
                            .age(lu.getAge())
                            .build();
                })
                .toList();
    }

}


// Decorator pattern
interface UserView {
    String display();
}

@RequiredArgsConstructor
class BasicUserView implements UserView {
    private final User user;

    public String display() {
        return user.toString();
    }
}

@RequiredArgsConstructor
class PremiumUserDecorator implements UserView {
    private final UserView view;

    public String display() {
        return view.display() + " [PREMIUM]";
    }
}

// Facade pattern
class UserValidator {
    boolean validate(User user) { return user.getAge() >= 18; }
}

class UserRepository {
    void save(User user) { System.out.println("Saved: " + user); }
}

class EmailService {
    void sendWelcome(User user) {
        System.out.println("Welcome email sent to " + user.getFirstName());
    }
}

@RequiredArgsConstructor
class UserRegistrationFacade {

    private final UserValidator validator;
    private final UserRepository repo;
    private final EmailService email;

    public void register(User user) {
        System.out.println("Starting user registration: " + user);

        if (!validator.validate(user)) {
            throw new RuntimeException("Invalid user");
        }

        repo.save(user);
        email.sendWelcome(user);

        System.out.println("User registration completed for: "
                + user.getFirstName() + " " + user.getLastName());
    }
}

// Behavioral patterns

// Strategy
interface DiscountStrategy {
    double apply(User user, double price);
}

class RegularUserDiscount implements DiscountStrategy {
    public double apply(User user, double price) {
        return price;
    }
}

class PremiumUserDiscount implements DiscountStrategy {
    public double apply(User user, double price) {
        return price * 0.8;
    }
}

@RequiredArgsConstructor
class BillingService {
    private final DiscountStrategy strategy;

    public double bill(User user, double price) {
        return strategy.apply(user, price);
    }
}

// Observer

interface UserObserver {
    void notify(User user, String event);
}

@RequiredArgsConstructor
class EmailObserver implements UserObserver {
    private final String email;
    public void notify(User user, String event) {
        System.out.println("Email to " + email + ": " + event);
    }
}

class UserEventPublisher {
    private final List<UserObserver> observers = new ArrayList<>();

    void subscribe(UserObserver observer) {
        observers.add(observer);
    }

    void publish(User user, String event) {
        observers.forEach(o -> o.notify(user, event));
    }
}

// Command

interface UserCommand {
    void execute(User user);
}

class ActivateUserCommand implements UserCommand {
    public void execute(User user) {
        System.out.println("Activated " + user.getFirstName());
    }
}

@RequiredArgsConstructor
class UserCommandExecutor {
    private final UserCommand command;

    void run(User user) {
        command.execute(user);
    }
}


// State

interface UserState {
    void handle(User user);
}

class ActiveState implements UserState {
    public void handle(User user) {
        System.out.println("User is active");
    }
}

class BlockedState implements UserState {
    public void handle(User user) {
        System.out.println("User is blocked");
    }
}

// Iterator

@RequiredArgsConstructor
class UserCollection implements Iterable<User> {

    private final List<User> users;

    @Override
    public Iterator<User> iterator() {
        return users.iterator();
    }
}

// Memento

@Value
class UserMemento {
    String firstName;
    String lastName;
    int age;
    String country;
    String profession;
}

class UserCaretaker {
    private final Deque<UserMemento> history = new ArrayDeque<>();

    void save(User user) {
        history.push(new UserMemento(
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getCountry(),
                user.getProfession()
        ));
    }

    User restore(User user) {
        UserMemento m = history.pop();
        return user.toBuilder()
                .firstName(m.getFirstName())
                .lastName(m.getLastName())
                .age(m.getAge())
                .country(m.getCountry())
                .profession(m.getProfession())
                .build();
    }
}

// Chain of Responsibility

@RequiredArgsConstructor
abstract class UserHandler {
    protected final UserHandler next;

    public final void handle(User user) {
        process(user);
        if (next != null) {
            next.handle(user);
        }
    }

    protected abstract void process(User user);
}


class AgeValidationHandler extends UserHandler {

    public AgeValidationHandler(UserHandler next) {
        super(next);
    }

    protected void process(User user) {
        System.out.println("AgeValidationHandler → checking age");
        if (user.getAge() < 18) {
            throw new RuntimeException("User underage");
        }
    }
}

class CountryValidationHandler extends UserHandler {

    public CountryValidationHandler(UserHandler next) {
        super(next);
    }

    protected void process(User user) {
        System.out.println("CountryValidationHandler → checking country");
        if (user.getCountry() == null) {
            throw new RuntimeException("Country missing");
        }
    }
}

// Template method

abstract class UserRegistrationTemplate {

    public final void register(User user) {
        validate(user);
        save(user);
        notifyUser(user);
    }

    protected abstract void validate(User user);

    protected void save(User user) {
        System.out.println("User saved: " + user);
    }

    protected abstract void notifyUser(User user);
}

class EmailUserRegistration extends UserRegistrationTemplate {

    protected void validate(User user) {
        System.out.println("Validating user...");
    }

    protected void notifyUser(User user) {
        System.out.println("Email sent to " + user.getFirstName());
    }
}

// Mediator
interface ChatMediator {
    void sendMessage(User sender, String message);
}

class UserChatMediator implements ChatMediator {

    private final List<User> users = new ArrayList<>();

    void addUser(User user) {
        users.add(user);
    }

    public void sendMessage(User sender, String message) {
        users.stream()
                .filter(u -> !u.equals(sender))
                .forEach(u ->
                        System.out.println(sender.getFirstName()
                                + " to " + u.getFirstName() + ": " + message)
                );
    }
}

// Visitor

interface UserVisitor {
    void visit(User user);
}

class UserPrintVisitor implements UserVisitor {
    public void visit(User user) {
        System.out.println("Visited: " + user);
    }
}

class UserDiscountVisitor implements UserVisitor {
    public void visit(User user) {
        System.out.println("Discount applied to " + user.getFirstName());
    }
}

class VisitableUser extends User {

    public VisitableUser(User u) {
        super(u.getFirstName(), u.getLastName(),
                u.getAge(), u.getCountry(), u.getProfession());
    }

    void accept(UserVisitor visitor) {
        visitor.visit(this);
    }
}


// Interpreter

interface Expression {
    boolean interpret(User user);
}

@RequiredArgsConstructor
class AgeExpression implements Expression {
    private final int minAge;

    public boolean interpret(User user) {
        return user.getAge() >= minAge;
    }
}

@RequiredArgsConstructor
class CountryExpression implements Expression {
    private final String country;

    public boolean interpret(User user) {
        return country.equals(user.getCountry());
    }
}

@RequiredArgsConstructor
class AndExpression implements Expression {
    private final Expression left;
    private final Expression right;

    public boolean interpret(User user) {
        return left.interpret(user) && right.interpret(user);
    }
}

