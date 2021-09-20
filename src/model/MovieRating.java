package model;

public class MovieRating {
	
	public MovieRating(User user, Movie movie, int rating) {
		super();
		this.user = user;
		this.movie = movie;
		this.rating = rating;
	}
	private User user;
	private Movie movie;
	private int rating;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	

}
