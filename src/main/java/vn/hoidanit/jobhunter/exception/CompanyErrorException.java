package vn.hoidanit.jobhunter.exception;

public class CompanyErrorException extends RuntimeException {
    public CompanyErrorException(String message) {
        super(message);
    }
}