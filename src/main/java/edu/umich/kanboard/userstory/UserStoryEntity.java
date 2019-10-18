package edu.umich.kanboard.userstory;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_story")
@Data
public class UserStoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userStoryId;

    @Column
    String name;

    @Column
    String description;

}
