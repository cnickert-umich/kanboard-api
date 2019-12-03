package edu.umich.kanboard.column;

import edu.umich.kanboard.userstory.UserStoryEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ColumnController {

    @Autowired
    private ColumnService columnService;

    @ApiOperation(value = "Get All Columns", notes = "Gets all the columns and their corresponding ids ", response = ColumnEntity.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @GetMapping("/column")
    @CrossOrigin
    public ResponseEntity<List<ColumnEntity>> getAllColumns() {
        return ResponseEntity.ok(columnService.getAllColumns());
    }


    @ApiOperation(value = "Creates or Updates a Column", notes = "Will update or save a column based on if the column already exists", response = UserStoryEntity.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/column")
    @CrossOrigin
    public ResponseEntity<ColumnEntity> createOrUpdateAColumn(
            @ApiParam(name = "ColumnEntity", value = "Column", required = true, format = MediaType.APPLICATION_JSON_VALUE)
            @RequestBody
                    ColumnEntity columnEntity) {
        return ResponseEntity.ok(columnService.createOrUpdateColumn(columnEntity));
    }

    @ApiOperation(value = "Delete a Column", notes = "Deletes a column", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @DeleteMapping("/column/{id}")
    @CrossOrigin
    public ResponseEntity<ColumnEntity> deleteColumn(@PathVariable Long id) {
        columnService.deleteColumn(id);
        return ResponseEntity.ok().build();
    }

}