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

import javax.xml.ws.Response;
import java.util.Collections;
import java.util.List;

@RestController
public class UserStoryController {

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
    public ResponseEntity<UserStoryEntity> getUserStory(@PathVariable Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        UserStoryEntity userStory = userStoryRepository.findByUserStoryId(id);
        if(userStory == null) {
            return new ResponseEntity<>(new UserStoryEntity(), headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(userStory, headers, HttpStatus.OK);

    }

    @ApiOperation(value = "Save a User Story", notes = "Saves a given User Story", response = UserStoryEntity.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @PostMapping("/us")
    @CrossOrigin
    public ResponseEntity<UserStoryEntity> saveUserStory(
            @ApiParam(name = "User Story", value = "User Story", required = false, format = MediaType.APPLICATION_JSON_VALUE)
            @RequestBody(required = true)
                    UserStoryEntity userStoryEntity) {
        return ResponseEntity.ok(userStoryRepository.save(userStoryEntity));
    }
}
