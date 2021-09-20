package model;

public class PropertyWeighting {
	
	public PropertyWeighting(User user, String property, Double weight) {
		super();
		this.user = user;
		this.property = property;
		this.weight = weight;
	}
	
	private User user;
	private String property;
	private Double weight;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	
	

}
