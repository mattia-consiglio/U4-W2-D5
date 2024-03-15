package mattiaconsiglio;

import com.github.javafaker.Faker;
import mattiaconsiglio.library.Book;
import mattiaconsiglio.library.LibrarySupplier;
import mattiaconsiglio.library.Magazine;
import mattiaconsiglio.library.Periodicity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Application {

    public static void main(String[] args) {
        int quantity = 100;
        LibrarySupplier<Book> bookLibrarySupplier = (int isbn) -> {
            Faker faker = new Faker();
            int year = new Random().nextInt(1900, 2025);
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

        List<Book> books = new ArrayList<Book>();
        List<Magazine> magazines = new ArrayList<Magazine>();

        int currIsbn = 1_000_000_000;

        for (int i = 0; i < quantity; i++) {
            books.add(bookLibrarySupplier.get(currIsbn));
            currIsbn++;
        }

        for (int i = 0; i < quantity; i++) {
            magazines.add(magazineLibrarySupplier.get(currIsbn));
            currIsbn++;
        }


    }
}
