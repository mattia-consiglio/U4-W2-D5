package mattiaconsiglio.library;

public abstract class AbstractBook {
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
}
