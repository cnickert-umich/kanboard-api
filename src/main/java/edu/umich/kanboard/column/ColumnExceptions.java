package edu.umich.kanboard.column;

public class ColumnExceptions {
    public static class ColumnInvalidNameException extends RuntimeException {
        ColumnInvalidNameException(String name) {
            super("The column name is invalid or missing: " + name);
        }
    }

    public static class ColumnTooManyException extends RuntimeException {
        ColumnTooManyException() {
            super("You cannot exceed the max number of columns: " + ColumnConstants.MAX_COLUMNS);
        }
    }

    public static class ColumnTooFewException extends RuntimeException {
        ColumnTooFewException() {
            super("You must have a minimum amount of columns: " + ColumnConstants.MIN_COLUMNS);
        }
    }

    public static class ColumnNameTooLong extends RuntimeException {
        ColumnNameTooLong() {
            super("You cannot exceed the max character length of a column: " + ColumnConstants.MAX_COLUMN_STRING_LENGTH);
        }
    }

}
