package mattiaconsiglio;

import com.github.javafaker.Faker;
import mattiaconsiglio.library.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

        int currIsbn = 100_000_000; //shortest ISBN possibile

        for (int i = 0; i < quantity; i++) {
            currIsbn += new Random().nextInt(10, 1000);
            library.add(bookLibrarySupplier.get(currIsbn));
        }

        for (int i = 0; i < quantity; i++) {
            currIsbn += new Random().nextInt(10, 1000);
            library.add(magazineLibrarySupplier.get(currIsbn));
        }


        SaveBooksOnDisk(library);


        while (true) {


            switch (mainMenu(scanner)) {
                case 1: {
                    if (chooseElement(scanner, "add") == 1) {
                        Book.add(library, scanner, bookLibrarySupplier);
                    } else {
                        Magazine.add(library, scanner, magazineLibrarySupplier);
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
                    Set<AbstractBook> abstractBooks = library.stream().filter(b -> b.getPublishYear() == year).collect(Collectors.toSet());
                    if (abstractBooks.size() == 0) {
                        System.out.println("No books found!");
                    } else {
                        System.out.println("Books found:");
                        abstractBooks.forEach(System.out::println);
                    }
                    break;
                }

                case 4: {
                    System.out.println("Enter an author:");
                    String author = scanner.nextLine();
                    Set<Book> books = library.stream().filter(abstractBook -> abstractBook instanceof Book).filter(b -> ((Book) b).getAuthor().equals(author)).map(b -> (Book) b).collect(Collectors.toSet());
                    if (books.size() == 0) {
                        System.out.println("No books found!");
                    } else {
                        System.out.println("Books found:");
                        books.forEach(System.out::println);
                    }
                    break;
                }
                case 5: {
                    System.out.println("Saving library...");
                    SaveBooksOnDisk(library);
                    break;
                }
                case 6: {
                    System.out.println("Loading library...");
                    library.clear();
                    library.addAll(LoadBooksFromDisk());
                    if (library.size() > 0) {

                        System.out.println();
                        System.out.println("Do you want to see the library loaded? (y/n)");
                        String answer = scanner.nextLine();
                        if (answer.equals("y")) {
                            library.forEach(System.out::println);
                        }
                    }
                    break;
                }

                case 7: {
                    System.out.println("Library size: " + library.size());
                    library.forEach(System.out::println);
                    break;
                }

                case 8:
                    System.out.println("Exiting");
                    return;
                default:
                    continue;
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
            System.out.println("6. Load the library from file");
            System.out.println("7. Display the library");
            System.out.println("8. Exit");


            String option = scanner.nextLine();

            switch (option) {
                case "1", "2", "3", "4", "5", "6", "7": {
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

    public static void SaveBooksOnDisk(Set<AbstractBook> books) {
        File file = new File("src/library.txt");
        try {
            FileUtils.writeStringToFile(file, "", StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }


        books.forEach(abstractBook -> {
            String className = abstractBook.getClass().getName().replace("mattiaconsiglio.library.", "");
            String stringJoiner = "//";
            try {
                if (abstractBook instanceof Book) {
                    FileUtils.writeStringToFile(file, className + stringJoiner + abstractBook.getIsbn() + stringJoiner + abstractBook.getTitle() + stringJoiner + abstractBook.getPublishYear() + stringJoiner + abstractBook.getPages() + stringJoiner + ((Book) abstractBook).getAuthor() + stringJoiner + ((Book) abstractBook).getGenre() + System.lineSeparator(), StandardCharsets.UTF_8, true);
                }
                if (abstractBook instanceof Magazine) {
                    FileUtils.writeStringToFile(file, className + stringJoiner + abstractBook.getIsbn() + stringJoiner + abstractBook.getTitle() + stringJoiner + abstractBook.getPublishYear() + stringJoiner + abstractBook.getPages() + stringJoiner + ((Magazine) abstractBook).getPeriodicity() + System.lineSeparator(), StandardCharsets.UTF_8, true);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
        System.out.println("Library saved on file");
    }

    public static Set<AbstractBook> LoadBooksFromDisk() {
        File file = new File("src/library.txt");
        Set<AbstractBook> abstractBooks = new HashSet<>();
        if (file.exists()) {
            if (file.canRead()) {
                try {
                    String fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    String[] lines = fileContent.split(System.lineSeparator());
                    for (String line : lines) {
                        String[] bookData = line.split("//");
                        if (bookData[0].equals("Book")) {
                            Book book = new Book(Integer.parseInt(bookData[1]), bookData[2], Integer.parseInt(bookData[3]), Integer.parseInt(bookData[4]), bookData[5], bookData[6]);
                            abstractBooks.add(book);
                        } else if (bookData[0].equals("Magazine")) {
                            Magazine magazine = new Magazine(Integer.parseInt(bookData[1]), bookData[2], Integer.parseInt(bookData[3]), Integer.parseInt(bookData[4]), Periodicity.valueOf(bookData[5]));
                            abstractBooks.add(magazine);
                        }
                    }
                    System.out.println("Library loaded from file");
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.err.println("File corrupted. " + e.getMessage()); // if the file is saved correctly, there should be no exception
                }
            } else {
                System.err.println("File not readable");
            }
        } else {
            System.err.println("File not found");
        }
        return abstractBooks;
    }
}
