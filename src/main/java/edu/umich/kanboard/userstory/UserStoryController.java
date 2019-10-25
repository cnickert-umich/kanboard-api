package edu.umich.kanboard.userstory;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.sql.DataSource;
import javax.xml.ws.Response;
import java.util.Collections;
import java.util.List;

@RestController
public class UserStoryController {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserStoryRepository userStoryRepository;

    @ApiOperation(value = "Get All User Stories", notes = "Gets all User Stories ", response = UserStoryEntity.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @GetMapping("/us")
    @CrossOrigin
    public ResponseEntity<List<UserStoryEntity>> getAllUserStories() {
        return ResponseEntity.ok(userStoryRepository.findAll());
    }

    @ApiOperation(value = "Get a User Story", notes = "Gets a user story given an id", response = UserStoryEntity.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @GetMapping("/us/{id}")
    @CrossOrigin
    public ResponseEntity<UserStoryEntity> getUserStory(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        UserStoryEntity userStory = userStoryRepository.findByUserStoryId(id);
        if(userStory == null) {
            return new ResponseEntity<>(new UserStoryEntity(), headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(userStory, headers, HttpStatus.OK);

    }

    @ApiOperation(value = "Creates or Updates a User Story", notes = "Will update or save a User Story based on if the User Story already exists", response = UserStoryEntity.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @PostMapping("/us")
    @CrossOrigin
    public ResponseEntity<UserStoryEntity> createOrUpdateAUserStory(
            @ApiParam(name = "User Story", value = "User Story", required = false, format = MediaType.APPLICATION_JSON_VALUE)
            @RequestBody(required = true)
                    UserStoryEntity userStoryEntity) {

        if(userStoryEntity.getStoryStatus() == null) {
            userStoryEntity.setStoryStatus(UserStoryStatus.DEFINED);
        }

        return ResponseEntity.ok(userStoryRepository.save(userStoryEntity));
    }

    @ApiOperation(value = "Delete a User Story", notes = "Deletes a Specific User Story", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @DeleteMapping("/us/{id}")
    @CrossOrigin
    public ResponseEntity<UserStoryEntity> deleteUserStory(@PathVariable Long id) {
        UserStoryEntity userStoryEntity = userStoryRepository.findByUserStoryId(id);
        userStoryRepository.deleteById(id);
        return ResponseEntity.ok(userStoryEntity);
    }
}
