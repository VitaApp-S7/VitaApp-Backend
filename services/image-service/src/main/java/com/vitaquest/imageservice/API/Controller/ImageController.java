package com.vitaquest.imageservice.API.Controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.imageservice.Domain.DTO.AddImageDTO;
import com.vitaquest.imageservice.Domain.Models.Image;
import com.vitaquest.imageservice.Domain.Service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<Image> addImage(@RequestParam("image") MultipartFile image) throws Exception {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            System.out.println("add image:" + image);
            AddImageDTO dto = new AddImageDTO();
            dto.setData(Base64.getEncoder().encodeToString(image.getBytes()).getBytes());
            dto.setName(image.getOriginalFilename());
            dto.setContentType(image.getContentType());
            dto.setSize(image.getSize());
            //dto.setContentType(contentType);
            System.out.println("ImageDTO Name: " + dto.getName());
            System.out.println("ImageDTO getContentType: " + dto.getContentType());
            System.out.println("ImageDTO getSize: " + dto.getSize());
            //System.out.println("ImageDTO getData: " + Arrays.toString(dto.getData()));
            System.out.println("getOriginalFilename name: " + image.getOriginalFilename());
            return new ResponseEntity<>(service.addImage(dto), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @ApiOperation(value = "Get image by ID")
    @GetMapping(value = "/{imageId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getImageByID(@PathVariable String imageId) throws Exception {
        System.out.println("get image by id: " + imageId);
        BufferedImage image = service.getImageByID(imageId);

        System.out.println("image: " + image);
        if (image == null) {
            System.out.println("image is null");
            return ResponseEntity.notFound().build();
        }

        MediaType contentType;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (image.getType() == BufferedImage.TYPE_3BYTE_BGR || image.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
            contentType = MediaType.IMAGE_PNG;
            ImageIO.write(image, "png", outputStream);
        } else {
            contentType = MediaType.IMAGE_JPEG;
            ImageIO.write(image, "jpeg", outputStream);
        }
        byte[] imageBytes = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        headers.setContentLength(imageBytes.length);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }


    @ApiOperation(value = "Delete image by ID")
    @DeleteMapping(value = "/{imageId}")
    public ResponseEntity<HttpStatus> deleteImageByID(@PathVariable String imageId) throws Exception {
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
