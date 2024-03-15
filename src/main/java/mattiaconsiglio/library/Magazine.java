package mattiaconsiglio.library;

public class Magazine extends AbstractBook {
    private Periodicity periodicity;

    public Magazine(int isbn, String title, int publishYear, int pages, Periodicity periodicity) {
        super(isbn, title, publishYear, pages);
        this.periodicity = periodicity;
    }
}
