package com.mansi.travellerblogapplication.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mansi.travellerblogapplication.Entity.TravellerEntity;
import com.mansi.travellerblogapplication.Service.TravellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class TravellerController {

    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private TravellerService travellerService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = {"/", "/home"})
    public String addProductPage() {
        return "index";
    }

    @PostMapping("/image/saveImageDetails")
    public @ResponseBody ResponseEntity<?> createProduct(@RequestParam("name") String name,
                                                         @RequestParam("date") double date, @RequestParam("description") String description, Model model, HttpServletRequest request
            ,final @RequestParam("image") MultipartFile file) {
        try {
            //String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            log.info("uploadDirectory:: " + uploadDirectory);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();
            log.info("FileName: " + file.getOriginalFilename());
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            String[] names = name.split(",");
            String[] descriptions = description.split(",");
            Date createDate = new Date();
            log.info("Name: " + names[0]+" "+filePath);
            log.info("description: " + descriptions[0]);
            log.info("date: " + date);
            try {
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    log.info("Folder Created");
                    dir.mkdirs();
                }
                // Save the file locally
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception e) {
                log.info("in catch");
                e.printStackTrace();
            }
            byte[] imageData = file.getBytes();
            TravellerEntity travellerEntity = new TravellerEntity();
            travellerEntity.setName(names[0]);
            travellerEntity.setImage(imageData);
            travellerEntity.setDate(date);
            travellerEntity.setDescription(descriptions[0]);
            travellerEntity.setCreateDate(createDate);
            travellerService.saveImage(travellerEntity);
            log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
            return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/image/display/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<TravellerEntity> travellerEntity)
            throws ServletException, IOException {
        log.info("Id :: " + id);
        travellerEntity = travellerService.getImageById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(travellerEntity.get().getImage());
        response.getOutputStream().close();
    }

    @GetMapping("/image/imageDetails")
    String showProductDetails(@RequestParam("id") Long id, Optional<TravellerEntity> travellerEntity, Model model) {
        try {
            log.info("Id :: " + id);
            if (id != 0) {
                travellerEntity = travellerService.getImageById(id);

                log.info("products :: " + travellerEntity);
                if (travellerEntity.isPresent()) {
                    model.addAttribute("id", travellerEntity.get().getId());
                    model.addAttribute("description", travellerEntity.get().getDescription());
                    model.addAttribute("name", travellerEntity.get().getName());
                    model.addAttribute("date", travellerEntity.get().getDate());
                    return "imagedetails";
                }
                return "redirect:/home";
            }
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home";
        }
    }

    @GetMapping("/image/show")
    String show(Model map) {
        List<TravellerEntity> images = travellerService.getAllActiveImages();
        map.addAttribute("images", images);
        return "images";
    }
}
