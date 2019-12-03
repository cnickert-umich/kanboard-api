package edu.umich.kanboard.userstory;

public class UserStoryExceptions {
    public static class UserStoryInvalidNameException extends RuntimeException {
        UserStoryInvalidNameException(String name) {
            super("The user story name is invalid or missing: " + name);
        }
    }

    public static class UserStoryInvalidDescriptionException extends RuntimeException {
        UserStoryInvalidDescriptionException(String description) {
            super("The user story description is invalid or missing: " + description);
        }
    }

    public static class UserStoryBadPriorityException extends RuntimeException {
        UserStoryBadPriorityException(int priority) {
            super("The user story priority is too high or low: " + priority);
        }
    }

    public static class UserStoryNotFound extends RuntimeException {
        UserStoryNotFound() {
            super("The user story was not found");
        }
    }
}
