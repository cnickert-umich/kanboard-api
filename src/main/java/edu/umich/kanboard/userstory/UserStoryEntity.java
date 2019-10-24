package edu.umich.kanboard.userstory;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_story")
@Data
@NoArgsConstructor
public class UserStoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The User Story ID")
    @Nullable
    private Integer userStoryId;

    @Column
    @ApiModelProperty(notes = "The name of the User Story")
    @NotNull
    private String name;

    @Column
    @ApiModelProperty(notes = "The User Story's description")
    @NotNull
    private String description;

}
