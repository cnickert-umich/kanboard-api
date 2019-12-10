package edu.umich.kanboard.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.Nullable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The id of the user")
    @Nullable
    private Long userId;

    @Column
    @ApiModelProperty(notes = "The user's username")
    @NotNull
    private String username;

    @Column
    @ApiModelProperty(notes = "The user's password encoded")
    @NotNull
    private String password;
}
