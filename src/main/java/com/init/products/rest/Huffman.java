package com.init.products.rest;
import java.util.*;


public class Huffman {
    Map<String, Character> huffmanTable = new HashMap<>();
    public String decoded_str;
    public int lenght;
    public boolean value;
    String data_translated;
    private String username;
    private String password;
    //public LoginController nose= new LoginController();

    XML xml = new XML();
    List<String> lista_users = new ArrayList<>();
    List<String> lista_passwords = new ArrayList<>();
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public  void decode(String compressedString, Map<String, Character> huffmanTable) {
        StringBuilder sb = new StringBuilder();
        String currentCode = "";
        for (int i = 0; i < compressedString.length(); i++) {
            currentCode += compressedString.charAt(i);
            if (huffmanTable.containsKey(currentCode)) {
                char decodedChar = huffmanTable.get(currentCode);
                sb.append(decodedChar);
                currentCode = "";
            }
        }
        decoded_str = sb.toString();
    }
    public void check(){
        //seteo de codificacion huffman
        huffmanTable.put("00", '0');
        huffmanTable.put("01", '1');
        huffmanTable.put("10", '2');
        huffmanTable.put("110", '3');
        huffmanTable.put("1110", '4');
        huffmanTable.put("11110", '5');
        huffmanTable.put("111110", '6');
        huffmanTable.put("1111110", '7');
        huffmanTable.put("11111110", '8');
        huffmanTable.put("111111110", '9');
        for(int i = 0; i < lenght ; i++ ){
            decode(lista_passwords.get(i), huffmanTable);
            if (Objects.equals(username, lista_users.get(i)) && Objects.equals(password, decoded_str)){
                value = true;


                break;
            }
            else{
                value = false;
            }
        }
        System.out.println(value);

    }

    public void translate_new_password(String data){

        String[] letras = data.split("");

        // Imprimir cada letra en una línea separada
        for (String letra : letras) {
            if(Objects.equals(letra, "0")){
                data_translated = data_translated + "00";
            }
            if(Objects.equals(letra, "1")){
                data_translated = data_translated + "01";
            }
            if(Objects.equals(letra, "2")){
                data_translated = data_translated + "10";
            }
            if(Objects.equals(letra, "3")){
                data_translated = data_translated + "110";
            }
            if(Objects.equals(letra, "4")){
                data_translated = data_translated + "1110";
            }
            if(Objects.equals(letra, "5")){
                data_translated = data_translated + "11110";
            }
            if(Objects.equals(letra, "6")){
                data_translated = data_translated + "111110";
            }
            if(Objects.equals(letra, "7")){
                data_translated = data_translated + "1111110";
            }
            if(Objects.equals(letra, "8")){
                data_translated = data_translated + "11111110";
            }
            if(Objects.equals(letra, "9")){
                data_translated = data_translated + "111111110";
            }
        }
        data_translated= data_translated.substring(4);
        System.out.println(data_translated);

    }
}