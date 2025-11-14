package vn.edu.fpt.coffeeshop.Domain;

public class AiRequest {
    private String message;

    public AiRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
