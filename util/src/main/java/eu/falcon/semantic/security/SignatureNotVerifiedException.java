package eu.falcon.semantic.security;

public class SignatureNotVerifiedException extends Exception {
    public SignatureNotVerifiedException(String message) {
        super(message);
    }
}
