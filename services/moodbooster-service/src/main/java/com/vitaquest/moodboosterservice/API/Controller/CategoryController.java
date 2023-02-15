package com.vitaquest.moodboosterservice.API.Controller;


import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.moodboosterservice.Domain.DTO.AddCategoryDTO;
import com.vitaquest.moodboosterservice.Domain.DTO.UpdateCategoryDTO;
import com.vitaquest.moodboosterservice.Domain.Models.Category;
import com.vitaquest.moodboosterservice.Domain.Service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "Category Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service)
    {
        this.service = service;
    }

    @ApiOperation(value = "Add category")
    @PostMapping(value = "/category")
    public @ResponseBody
    ResponseEntity<Category> addCategory(@RequestBody AddCategoryDTO DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            return new ResponseEntity<>(service.addCategory(DTO), HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Get all categories")
    @GetMapping(value = "/category/all")
    public @ResponseBody
    List<Category> getAllCategories() {
        return service.getAllCategories();
    }

    @ApiOperation(value = "Get category")
    @GetMapping(value = "/category/{id}")
    public @ResponseBody
    Category getCategory(@PathVariable String id) {
        return service.getCategoryById(id);
    }

    @ApiOperation(value = "Update category")
    @PutMapping(value = "/category")
    public @ResponseBody
    ResponseEntity<Category> updateCategory(@RequestBody UpdateCategoryDTO DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            if(DTO.getName().equals("")){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(service.updateCategory(DTO), HttpStatus.valueOf(200));
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Delete category")
    @DeleteMapping(value = "/category/{id}")
    public @ResponseBody
    ResponseEntity<Category> deleteCategory(@PathVariable String id) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            service.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private boolean isAdmin(Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        JSONArray arr = (JSONArray) claims.get("roles");
        if (arr != null) {
            String role = (String) arr.get(0);
            return role.equals("Role.Admins");
        } else {
            return false;
        }
    }
}
