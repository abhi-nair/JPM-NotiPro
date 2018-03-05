package com.uk.jpm.notipro.app;

import com.uk.jpm.notipro.controller.NotiProController;
import com.uk.jpm.notipro.log.NotiProLogUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Abhishek Nair (5th March 2018)
 * This class contains the main method to start the application
 * Emulating messages received from a 3rd party organization as reading each lines from a demo file ->
 * Could be considered as a rest call from the 3rd party organization which is received by the controller in an MVC based application service
 */
public class NotiProApplication {
    
    public static void main(String[] args) throws IOException {
        NotiProLogUtil.debug("Entered main()", NotiProApplication.class.getName());
        
        //Initiates the Controller that receives messages
        NotiProController notiProController = new NotiProController();
        
        //Read the demo-file line by line process each notification
        Files.lines(Paths.get("demoInput/demo.txt")).forEach(notiProController::recordSale);
        
        NotiProLogUtil.debug("Exiting main()", NotiProApplication.class.getName());
    }
}