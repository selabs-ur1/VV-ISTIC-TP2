public class Book {
    private String id;
    public String title;
    public String edition;

    public Book(String id, String title, String edition) {
        this.id = id;
        this.title = title;
        this.edition = edition;
    }

    public String getTitle() {
        return title;
    }
}