package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.UserDemo;
import org.academy.OnlineStoreDemo.service.DemoService;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Base64;

@Controller
@RequestMapping("/demo")
public class DemoController {

    private final ProductService productService;

    private final ProductCategoryService productCategoryService;

    private final UserService userService;

    private final DemoService demoService;


    @Autowired
    public DemoController(ProductService productService, ProductCategoryService productCategoryService, UserService userService, DemoService demoService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.userService = userService;
        this.demoService = demoService;
    }

    @GetMapping()
    public String getDemoPage(Model model, Principal principal){
        UserDto userDto = userService.findByLogin(principal.getName());
        UserDemo userDemo =demoService.findById(6);
        System.out.println(userDemo.getId());
        System.out.println(userDemo.getImage());
        System.out.println(userDemo.getPhotosImagePath());
        model.addAttribute("user", userDto);
        model.addAttribute("products", productService.findAll() );
        model.addAttribute("productCategories" , productCategoryService.findAll());
        model.addAttribute("userDemo",userDemo);

        return "demo";
    }

    @PostMapping("/load")
    public String loadImage(@RequestParam ("file")MultipartFile multipartFile, Model model){

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        demoService.load(fileName);

        String uploadDir = "./user-photos/" + demoService.findById(5).getId();

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                InputStream inputStream = multipartFile.getInputStream();
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(filePath.toFile().getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("products", productService.findAll() );
        model.addAttribute("productCategories" , productCategoryService.findAll());

        return "demo";
    }
}
