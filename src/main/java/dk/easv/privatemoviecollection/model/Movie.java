package dk.easv.privatemoviecollection.model;

public class
Movie {
    private int id;
    private String title;
    private double imdbRating;
    private double myRating;
    private String fileLink;

    public Movie (int id, String title, double imdbRating, double myRating, String fileLink) {
        this.id = id;
        this.title = title;
        this.imdbRating = imdbRating;
        this.myRating = myRating;
        this.fileLink = fileLink;
    }

    public Movie (String title, double imdbRating, double myRating, String fileLink) {
        this.title = title;
        this.imdbRating = imdbRating;
        this.myRating = myRating;
        this.fileLink = fileLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public double getImdbRating() { return imdbRating; }

    public void setImdbRating(double imdbRating) { this.imdbRating = imdbRating; }

    public double getMyRating() { return myRating; }

    public void setMyRating(double myRating) { this.myRating = myRating; }

    public String getFileLink() { return fileLink; }

    public int getId() { return id; }

    public void setId(int id) {this.id = id;}

    public void setFileLink(String fileLink) {this.fileLink = fileLink;}
}
