package edu.umich.kanboard;

import edu.umich.kanboard.column.ColumnExceptions;
import edu.umich.kanboard.userstory.UserStoryExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler({
            UserStoryExceptions.UserStoryNotFound.class
    })
    public void notFoundErrorCode(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({
            UserStoryExceptions.UserStoryInvalidNameException.class,
            UserStoryExceptions.UserStoryInvalidDescriptionException.class,
            UserStoryExceptions.UserStoryBadPriorityException.class,
            ColumnExceptions.ColumnTooFewException.class,
            ColumnExceptions.ColumnTooManyException.class,
            ColumnExceptions.ColumnInvalidNameException.class,
            ColumnExceptions.ColumnNameTooLong.class
    })
    public void badRequestErrorCode(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
