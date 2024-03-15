package mattiaconsiglio;

import com.github.javafaker.Faker;
import mattiaconsiglio.library.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import static mattiaconsiglio.library.AbstractBook.askAndVerifyInt;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int quantity = 100;
        LibrarySupplier<Book> bookLibrarySupplier = (int isbn) -> {
            Faker faker = new Faker();
            int year = new Random().nextInt(1900, LocalDate.now().getYear() + 1);
            int pages = new Random().nextInt(10, 5000);
            return new Book(isbn, faker.book().title(), year, pages, faker.book().author(), faker.book().genre());
        };

        LibrarySupplier<Magazine> magazineLibrarySupplier = (int isbn) -> {
            Faker faker = new Faker();
            int year = new Random().nextInt(1900, 2025);
            int pages = new Random().nextInt(10, 30);
            Periodicity periodicity = Periodicity.values()[new Random().nextInt(Periodicity.values().length)];
            return new Magazine(isbn, faker.book().title(), year, pages, periodicity);
        };

        Set<AbstractBook> library = new HashSet<>();

        int currIsbn = 1_000_000_000;
        currIsbn = 0; //for debug

        for (int i = 0; i < quantity; i++) {
            currIsbn++;
            library.add(bookLibrarySupplier.get(currIsbn));
        }

        for (int i = 0; i < quantity; i++) {
            currIsbn++;
            library.add(magazineLibrarySupplier.get(currIsbn));
        }

        library.forEach(System.out::println);


        while (true) {


            switch (mainMenu(scanner)) {
                case 1: {
                    if (chooseElement(scanner, "add") == 1) {
                        Book.add(library, scanner);
                    } else {
                        Magazine.add(library, scanner);
                    }
                    break;
                }
                case 2: {
                    System.out.println("Insert element ISBN to remove:");
                    int isbn = askAndVerifyInt("Insert book ISBN", scanner, 1000_000_000, 999_999_999);
                    AbstractBook book = library.stream().filter(b -> b.getIsbn() == isbn).findFirst().orElse(null);
                    if (book != null) {
                        library.remove(book);
                        System.out.println("Book removed!");
                    } else {
                        System.err.println("Error: ISBN not present in the library");
                    }
                    break;
                }

                case 3: {

                    int year = askAndVerifyInt("Insert year to search:", scanner, 1900, 2024);
                    Set<AbstractBook> books = library.stream().filter(b -> b.getPublishYear() == year).collect(Collectors.toSet());
                    if (books.size() == 0) {
                        System.out.println("No books found!");
                    } else {
                        System.out.println("Books found:");
                        books.forEach(System.out::println);
                    }
                    break;
                }


                case 7:
                    return;
            }
            System.out.println();
        }
    }

    public static int mainMenu(Scanner scanner) {

        while (true) {
            System.out.println("--------- Main menu ---------");
            System.out.println();
            System.out.println("Choose an option number");
            System.out.println("1. Add element");
            System.out.println("2. Remove element by ISBN");
            System.out.println("3. Search by publish year");
            System.out.println("4. Search by author");
            System.out.println("5. Save the library on file");
            System.out.println("6. Save the library on file");
            System.out.println("7. Exit");


            String option = scanner.nextLine();

            switch (option) {
                case "1", "2", "3", "4", "5", "6": {
                    return Integer.parseInt(option);
                }
                default:
                    System.err.println("Option not valid. Select a valid option number");
            }
        }
    }

    public static int chooseElement(Scanner scanner, String action) {
        while (true) {
            System.out.println();
            System.out.println("What element you want to" + action + "?");
            System.out.println("1. Book");
            System.out.println("2. Magazine");
            String option = scanner.nextLine();
            if (option.equals("1") || option.equals("2")) {
                return Integer.parseInt(option);
            } else {
                System.err.println("Option not valid. Select a valid option number");
            }
        }
    }
}
