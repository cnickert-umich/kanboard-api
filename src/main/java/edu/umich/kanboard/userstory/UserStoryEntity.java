package edu.umich.kanboard.userstory;


import com.fasterxml.jackson.annotation.JsonInclude;
import edu.umich.kanboard.column.ColumnEntity;
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

    @ApiModelProperty(notes = "The column a User Story is associated with")
    @ManyToOne
    private ColumnEntity column;

    @Column
    @ApiModelProperty(notes = "The priority associated with the User Story (lower means higher priority)")
    @NotNull
    private Integer priority;

}
