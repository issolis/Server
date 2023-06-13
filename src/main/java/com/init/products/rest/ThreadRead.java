package com.init.products.rest;



import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;


/**
 * Implementacion del hilo que permite tener una funcionalidad en arduino
 * @author Isaac Monica Arturo
 * @return void
 *
 */
public class ThreadRead extends Thread{
    public static int success = 0;
    @Override
    public void run() {
        SerialPort port = SerialPort.getCommPort("COM5");
        port.openPort();
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0,0);
        Scanner scanner = new Scanner(port.getInputStream());
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] bytes = line.split(",");
            if (Objects.equals(bytes[0], "1")){
                ArduinoController.morse = ArduinoController.morse + "0";
            }
            else if (Objects.equals(bytes[1], "1")){
                ArduinoController.morse = ArduinoController.morse + "1";
            }
            try {
                port.getOutputStream().write(success);
                success = 0;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(bytes[2]);
            System.out.println(ArduinoController.morse);
        }
    }
}