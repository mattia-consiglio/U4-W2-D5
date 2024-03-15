package mattiaconsiglio.library;

public class Book extends AbstractBook {
    private String author;
    private String genre;

    public Book(int isbn, String title, int publishYear, int pages, String author, String genre) {
        super(isbn, title, publishYear, pages);
        this.author = author;
        this.genre = genre;
    }
}
