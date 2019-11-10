package edu.umich.kanboard.column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "column_headers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColumnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The column ID")
    @Nullable
    private Long id;

    @Column
    @ApiModelProperty(notes = "The column name")
    @NotNull
    private String name;
}
