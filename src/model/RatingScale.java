package model;

public enum RatingScale {
	
    ONE   (-3),
    TWO   (-2),
    THREE (1),
    FOUR  (2),
    FIVE  (3);
	
    private final int scale;

    RatingScale(int scale) {
        this.scale = scale;

    }
    private int scale() { return scale; }
    
    public static int returnScale(int userRating) {
    	int userScale = 0;
    	switch (userRating) {
	        case 1:
	        	userScale = ONE.scale();
	            break;
	                
	        case 2:
	        	userScale = TWO.scale();
	            break;
	                     
	        case 3:
	        	userScale = THREE.scale();
	            break;
	                    
	        case 4:
	        	userScale = FOUR.scale();
	            break;
	        case 5:
	        	userScale = FIVE.scale();
	            break;
    	}
    	return userScale;
    }

    public static void main(String[] args) {
    	
    	//testing
    	int userRating = 1;
    	
    	System.out.println(returnScale(userRating));
    	
    	
    }

}
