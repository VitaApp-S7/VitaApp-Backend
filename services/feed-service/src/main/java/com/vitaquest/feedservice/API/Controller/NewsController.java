package com.vitaquest.feedservice.API.Controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.feedservice.Domain.DTO.AddNewsDTO;
import com.vitaquest.feedservice.Domain.Models.News;
import com.vitaquest.feedservice.Domain.Service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "News Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyAuthority('SCOPE_User.All')")
@Slf4j
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    // News
    @ApiOperation(value = "Add news")
    @PostMapping
    public @ResponseBody ResponseEntity<News> addNews(@RequestBody AddNewsDTO DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
              return new ResponseEntity<>(service.addNews(DTO), HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Get news by Id")
    @GetMapping(value = "/{newsId}")
    public @ResponseBody News getNewsByID(@PathVariable String newsId){
        return service.getNewsByID(newsId);
    }

    @ApiOperation(value = "Get all news")
    @GetMapping(value = "/all")
    public @ResponseBody List<News> getAllNews() {
        return service.getAllNews();
    }

    @ApiOperation(value = "Delete news by ID")
    @DeleteMapping(value = "/{newsId}")
    public ResponseEntity<HttpStatus> deleteNewsByID(@PathVariable String newsId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            service.deleteNewsByID(newsId);
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
