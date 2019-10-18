package edu.umich.kanboard.userstory;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_story")
@Data
public class UserStoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The User Story ID")
    Integer userStoryId;

    @Column
    @ApiModelProperty(notes = "The name of the User Story")
    String name;

    @Column
    @ApiModelProperty(notes = "The User Story's description")
    String description;

}
