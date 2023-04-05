package com.vitaquest.imageservice.API.Controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.imageservice.Domain.DTO.AddImageDTO;
import com.vitaquest.imageservice.Domain.Models.Image;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "Image Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
public class ImageController {
    private final ImageService service;

    public ImageController(ImageService service)
    {
        this.service = service;
    }

    @ApiOperation(value = "Add image")
    @PostMapping
    public @ResponseBody ResponseEntity<Image> addImage(@RequestBody AddImageDTO DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            return new ResponseEntity<>(service.addImage(DTO), HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Get image by ID")
    @GetMapping(value = "/{imageId}")
    public @ResponseBody Image getImageByID(@PathVariable String imageId) {
        return service.getImageByID(imageId);
    }

    @ApiOperation(value = "Delete image by ID")
    @DeleteMapping(value = "/{imageId}")
    public ResponseEntity<HttpStatus> deleteImageByID(@PathVariable String imageId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            service.deleteImageByID(imageId);
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
