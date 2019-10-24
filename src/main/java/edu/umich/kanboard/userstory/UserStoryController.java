package edu.umich.kanboard.userstory;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserStoryController {

    @Autowired
    UserStoryRepository userStoryRepository;

    @ApiOperation(value = "Get All User Stories", notes = "Gets all User Stories ", response = UserStoryEntity.class, responseContainer = "List", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @GetMapping("/us")
    @CrossOrigin
    public List<UserStoryEntity> getUserStories(
            @ApiParam(name = "name", value = "Optional User Story Name", required = false)
            @RequestBody(required = false)
                    String name) {

        if (name != null && !name.equals("")) {
            return userStoryRepository.findTop8ByNameIgnoreCaseContainingOrderByName(name);
        }
        return userStoryRepository.findAll();
    }

    @PostMapping("/us")
    @CrossOrigin
    public UserStoryEntity saveUserStory(
            @ApiParam(name = "User Story", value = "User Story", required = false, format = "application/json")
            @RequestBody(required = true)
                    UserStoryEntity userStoryEntity) {

        return userStoryRepository.save(userStoryEntity);
    }
}
