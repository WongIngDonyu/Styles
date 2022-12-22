package com.example.styles.Controllers;
import com.example.styles.Service.Model;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import org.tensorflow.SavedModelBundle;

import java.io.File;
import java.io.FileNotFoundException;

@Controller
@RequestMapping("/")
public class MainController {
    Model model1 = new Model ();
    SavedModelBundle model = SavedModelBundle.load("src/main/resources/my_model", "serve");
    String path = "C:\\Photo\\load.jpg";
    public MainController (){

    }
    @GetMapping("/")
    public String MainPage(){
        return "index.html";
    }

    @GetMapping(value = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public String sendErrorPage(){
        System.out.println("error");
        return "index.html";
    }
    @GetMapping(value = "/Baroque", produces = MediaType.TEXT_HTML_VALUE)
    public String sendBaroquePage(){
        return "Baroque/index.html";
    }
    @GetMapping(value = "/Baroquemore", produces = MediaType.TEXT_HTML_VALUE)
    public String sendBaroqueMorePage(){
        return "Baroque/more/index.html";
    }
    @GetMapping(value = "/Bauhaus", produces = MediaType.TEXT_HTML_VALUE)
    public String sendBauhausPage(){
        return "Bauhaus/index.html";
    }
    @GetMapping(value = "/Bauhausmore", produces = MediaType.TEXT_HTML_VALUE)
    public String sendBauhausMorePage(){
        return "Bauhaus/more/index.html";
    }
    @GetMapping(value = "/Edwardian", produces = MediaType.TEXT_HTML_VALUE)
    public String sendEdwardianPage(){
        return "Edwardian/index.html";
    }
    @GetMapping(value = "/Edwardianmore", produces = MediaType.TEXT_HTML_VALUE)
    public String sendEdwardianMorePage(){
        return "Edwardian/more/index.html";
    }
    @GetMapping(value = "/Postmodern", produces = MediaType.TEXT_HTML_VALUE)
    public String sendPostmodernPage(){
        return "Postmodern/index.html";
    }
    @GetMapping(value = "/Postmodernmore", produces = MediaType.TEXT_HTML_VALUE)
    public String sendPostmodernMorePage(){
        return "Postmodern/more/index.html";
    }
    @GetMapping(value = "/Gothic", produces = MediaType.TEXT_HTML_VALUE)
    public String sendGothicPage(){
        return "Gothic/index.html";
    }
    @GetMapping(value = "/Gothicmore", produces = MediaType.TEXT_HTML_VALUE)
    public String sendGothicMorePage(){
        return "Gothic/more/index.html";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public RedirectView Processing(@RequestParam("file") MultipartFile file) throws FileNotFoundException{
        if(!file.isEmpty()){
            try{
                file.transferTo(new File(path));
                String str = model1.getAnswer(path,model);
                System.out.println(str);
                switch (str) {
                    case "Baroque":
                        return new RedirectView("/Baroque");
                    case "Bauhaus":
                        return new RedirectView("/Bauhaus");
                    case "Edwardian":
                        return new RedirectView("/Edwardian");
                    case "Gothic":
                        return new RedirectView("/Gothic");
                    case "Postmodern":
                        return new RedirectView("/Postmodern");
                    default:
                        return new RedirectView("/error");
                }
            }
            catch (Exception e){
             System.out.println(e);
                return new RedirectView("/error");
            }
        }
        else {
            System.out.println("Файл не найден");
            return new RedirectView("/errorNoFile");
        }
    }
}
