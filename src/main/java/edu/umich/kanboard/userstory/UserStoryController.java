package edu.umich.kanboard.userstory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserStoryController {

    @Autowired
    UserStoryRepository userStoryRepository;

    @GetMapping("/us")
    public List<UserStoryEntity> getGuests(@RequestParam(required = false) String name) {
        if (name != null && !name.equals("")) {
            return userStoryRepository.findTop8ByNameIgnoreCaseContainingOrderByName(name);
        }
        return userStoryRepository.findAll();
    }


}
