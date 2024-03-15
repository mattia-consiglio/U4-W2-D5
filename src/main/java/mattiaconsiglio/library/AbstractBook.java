package mattiaconsiglio.library;

import mattiaconsiglio.exceptions.NumberOutOfLimitException;

import java.util.Scanner;

public abstract class AbstractBook<T> {
    protected int isbn;
    protected String title;
    protected int publishYear;
    protected int pages;

    public AbstractBook(int isbn, String title, int publishYear, int pages) {
        this.isbn = isbn;
        this.title = title;
        this.publishYear = publishYear;
        this.pages = pages;
    }

    public int getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public int getPages() {
        return pages;
    }

    public static int askAndVerifyInt(String message, Scanner scanner, int min, int max) {
        int n;
        while (true) {
            System.out.println(message);
            try {
                String s = scanner.nextLine();
                n = Integer.parseInt(s);

                if (n > max || n < min) throw new NumberOutOfLimitException(min, max);

                break;
            } catch (NumberFormatException | NumberOutOfLimitException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        return n;
    }


    @Override
    public String toString() {
        return "AbstractBook{" +
                "isbn=" + isbn +
                ", title='" + title + '\'' +
                ", publishYear=" + publishYear +
                ", pages=" + pages +
                '}';
    }
}
