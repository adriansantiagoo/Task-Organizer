package tasks;

public class NotReschedulableException extends RuntimeException{
    public NotReschedulableException(String message){
        super(message);
    }
}
