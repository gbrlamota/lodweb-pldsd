package model;

public class Movie {
	
	public Movie(int movieid) {
		super();
		this.movieid = movieid;
	}
	
	public Movie(String movieuri) {
		super();
		this.uri = movieuri;
	}
	
	public Movie(int movieid, String title, String uri) {
		super();
		this.movieid = movieid;
		this.title = title;
		this.uri = uri;
	}
	
	private int movieid;
	private String title;
	private String uri;
	
	public int getMovieId() {
		return movieid;
	}
	public void setId(int movieid) {
		this.movieid = movieid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	

}
