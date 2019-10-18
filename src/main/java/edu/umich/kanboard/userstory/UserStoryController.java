package edu.umich.kanboard.userstory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(required = false)
                    String name) {

        if (name != null && !name.equals("")) {
            return userStoryRepository.findTop8ByNameIgnoreCaseContainingOrderByName(name);
        }
        return userStoryRepository.findAll();
    }
}
