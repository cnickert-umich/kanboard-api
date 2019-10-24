package edu.umich.kanboard.userstory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/us/search")
    @CrossOrigin
    public List<UserStoryEntity> getUserStories(
            @ApiParam(name = "name", value = "Optional User Story Name", required = false)
            @RequestBody(required = false)
                    UserStoryEntity searchEntity) {

        Integer id = searchEntity.getUserStoryId();
        String name = searchEntity.getName();

        if (id != null && id > 0) {
            return Collections.singletonList(userStoryRepository.findByUserStoryId(id));
        } else if (name != null && name.length() > 0) {
            return userStoryRepository.findTop8ByNameIgnoreCaseContainingOrderByName(name);
        }

        return userStoryRepository.findAll();
    }

    @PostMapping("/us/save")
    @CrossOrigin
    public UserStoryEntity saveUserStory(
            @ApiParam(name = "User Story", value = "User Story", required = false, format = "application/json")
            @RequestBody(required = true)
                    UserStoryEntity userStoryEntity) {

        return userStoryRepository.save(userStoryEntity);
    }

}
