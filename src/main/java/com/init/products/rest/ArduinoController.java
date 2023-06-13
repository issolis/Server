package com.init.products.rest;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.Serial;
import java.util.Objects;

@RestController
@RequestMapping("/api/messages")

/**
 * Codigo que permite el uso del arduino
 * @author Isaac, Monica y Arturo
 * @return null
 *
 */
public class ArduinoController {

    boolean value = false;
    public static String morse = "";
    public void setValue(boolean value){
        this.value = value;
    }
    //post mapping para manejar login
    @PostMapping("/login")
    public ResponseEntity<String> loginPost(@RequestBody TextRequest  request){

        XML xml_Reader = new XML();
        if (!Objects.equals(request.password, "")) {
            xml_Reader.setPassword(request.password);
        }
        else{
            xml_Reader.setPassword(morse);
        }
        xml_Reader.setUsername(request.username);
        xml_Reader.XML_Reader();


        if (xml_Reader.value){
        	ThreadRead.success = 1;
            morse = "";
            queryController.user = request.username;
            sidebarController.user = request.username;
            return ResponseEntity.ok("Funciono");
        }
        else {
            SerialReader.success = 2;
            morse = "";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

    }
    //Post mapping para registrar nuevo usuario :)
    @PostMapping("/register")
    public ResponseEntity<String> registerPost(@RequestBody TextRequest  request){
        //métodos de escritura de usuario en xml
        String dirXML = "direccion";

        Huffman huf = new Huffman();

        System.out.println(request.password);

        if (!Objects.equals(request.password, "")) {

            huf.translate_new_password(request.password);
        }
        else {

            huf.translate_new_password(morse);
        }
        String newAccount = "<linea>" + request.username + ";" + huf.data_translated + "</linea>";
        XML.writeToXML(dirXML,newAccount);
        morse = "";

        String folder_path = "direccion" + "\\" +request.username;
        File folder = new File(folder_path);
        Boolean created = folder.mkdir();
        if (created){
            System.out.println("Se creo el folder");
        }

        return ResponseEntity.ok("Funciono");
    }

    public static class TextRequest {
        private String username;

        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}