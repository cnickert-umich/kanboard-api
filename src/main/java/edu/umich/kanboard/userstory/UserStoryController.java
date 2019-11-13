package edu.umich.kanboard.userstory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserStoryController {

    @Autowired
    private UserStoryService userStoryService;

    @ApiOperation(value = "Get All User Stories", notes = "Gets all User Stories ", response = UserStoryEntity.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @GetMapping("/us")
    @CrossOrigin
    public ResponseEntity<List<UserStoryEntity>> getAllUserStories() {
        return ResponseEntity.ok(userStoryService.getAllUserStories());
    }

    @ApiOperation(value = "Get a User Story", notes = "Gets a user story given an id", response = UserStoryEntity.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @GetMapping("/us/{id}")
    @CrossOrigin
    public ResponseEntity<UserStoryEntity> getUserStory(@PathVariable Long id) {
        return ResponseEntity.ok(userStoryService.getUserStoryById(id));

    }

    @ApiOperation(value = "Creates or Updates a User Story", notes = "Will update or save a User Story based on if the User Story already exists", response = UserStoryEntity.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @PostMapping("/us")
    @CrossOrigin
    public ResponseEntity<UserStoryEntity> createOrUpdateAUserStory(
            @ApiParam(name = "User Story", value = "User Story", format = MediaType.APPLICATION_JSON_VALUE)
            @RequestBody
                    UserStoryEntity userStory) {

        return ResponseEntity.ok(userStoryService.saveUserStory(userStory));
    }

    @ApiOperation(value = "Delete a User Story", notes = "Deletes a Specific User Story", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @DeleteMapping("/us/{id}")
    @CrossOrigin
    public ResponseEntity<?> deleteUserStory(@PathVariable Long id) {
        userStoryService.deleteUserStory(id);
        return ResponseEntity.noContent().build();
    }
}
