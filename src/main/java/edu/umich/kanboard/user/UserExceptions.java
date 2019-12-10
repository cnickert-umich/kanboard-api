package edu.umich.kanboard.user;

public class UserExceptions {

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String username) {
            super("The username already exists: " + username);
        }
    }
}
