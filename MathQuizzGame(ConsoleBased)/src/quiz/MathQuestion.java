package quiz;
 
public class MathQuestion <T extends Number>{
	   private T number1;
	   private T number2;
	   private String operator;
	   private double answer;
 
	    public MathQuestion(T number1, T number2, String operator) {
	        this.number1 = number1;
	        this.number2 = number2;
	        this.operator = operator;
	        this.answer = calculateAnswer();
	    }
	    private double calculateAnswer() {
	        return switch (operator) {
	            case "+" -> number1.doubleValue() + number2.doubleValue();
	            case "-" -> number1.doubleValue() - number2.doubleValue();
	            case "*" -> number1.doubleValue() * number2.doubleValue();
	            case "/" -> number1.doubleValue() / number2.doubleValue();
	            default -> throw new IllegalArgumentException("Invalid operator");
	        };
	    }
	    public boolean checkAnswer(double playerAnswer){
	        return Math.abs(playerAnswer-answer)<0.01;
	    }
	    @Override
	    public String toString() {
	        return number1 + " " + operator + " " + number2 +"=?";
	}
	    public double getCorrectAnswer() {
			// TODO Auto-generated method stub
			return answer;
		}
	    
	}
 