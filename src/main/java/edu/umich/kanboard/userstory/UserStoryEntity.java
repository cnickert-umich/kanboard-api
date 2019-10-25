package edu.umich.kanboard.userstory;


import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserStoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The User Story ID")
    @Nullable
    private Long userStoryId;

    @Column
    @ApiModelProperty(notes = "The name of the User Story")
    @NotNull
    private String name;

    @Column
    @ApiModelProperty(notes = "The User Story's description")
    @NotNull
    private String description;

    @Column
    @ApiModelProperty(notes = "The User Story Status")
    @Enumerated(EnumType.STRING)
    private UserStoryStatus storyStatus;

}
