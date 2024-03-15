package mattiaconsiglio.library;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Magazine extends AbstractBook<Magazine> {
    private Periodicity periodicity;


    public Magazine(int isbn, String title, int publishYear, int pages, Periodicity periodicity) {
        super(isbn, title, publishYear, pages);
        this.periodicity = periodicity;
    }


    public static void add(Set<AbstractBook> library, Scanner scanner) {
        int isbn = askAndVerifyInt("Insert book ISBN (8 digits)", scanner, 10_000_000, 99_999_999);

        while (true) {

            int finalIsbn = isbn;
            if (library.stream().anyMatch(book -> book.getIsbn() == finalIsbn)) {
                System.err.println("Error: ISBN already present in the library");
                isbn = askAndVerifyInt("Insert book ISBN", scanner, 10_000_000, 99_999_999);
            } else {
                break;
            }
        }

        System.out.println("Insert book title:");
        String tile = scanner.nextLine();

        int year = askAndVerifyInt("Insert book publish year", scanner, 1900, LocalDate.now().getYear() + 1);

        int pages = askAndVerifyInt("Insert book pages", scanner, 1, 10000);
        Periodicity periodicity;


        System.out.println("Choose magazine periodicity:");
        for (int i = 0; i < Periodicity.values().length; i++) {
            System.out.println(i + 1 + ". " + Periodicity.values()[i]);
        }
        int n = askAndVerifyInt("Insert periodicity number", scanner, 1, Periodicity.values().length);
        periodicity = Periodicity.values()[n - 1];

        Magazine magazine = new Magazine(isbn, tile, year, pages, periodicity);
        library.add(magazine);

        System.out.println("Magazine added!");
        System.out.println(magazine);
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Magazine magazine)) return false;
        return isbn == magazine.isbn && Objects.equals(title, magazine.title) && publishYear == magazine.publishYear && pages == magazine.pages && periodicity == magazine.periodicity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodicity);
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "periodicity=" + periodicity +
                ", isbn=" + isbn +
                ", title='" + title + '\'' +
                ", publishYear=" + publishYear +
                ", pages=" + pages +
                '}';
    }
}
