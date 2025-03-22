package vn.hoidanit.jobhunter.exception;

public class EmailExistedException extends RuntimeException {

    public EmailExistedException(String messsage) {
        super(messsage);
    }
}
