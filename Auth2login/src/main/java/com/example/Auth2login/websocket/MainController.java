package com.example.Auth2login.websocket;

import com.example.Auth2login.websocket.Notification;
import com.example.Auth2login.websocket.NotificationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping(value="/api")
public class MainController {

  @Autowired
  private NotificationService notificationService;
    
  /**
   * GET  /  -> show the index page.
   */
  @RequestMapping("/web")
  public String index() {
    return "index";
  }

 

  /**
   * POST  /some-action  -> do an action.
   * 
   * After the action is performed will be notified UserA.
   */
  @RequestMapping(value = "/some-action", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<?> someAction() 
  {
	  System.out.println("some_action");
    notificationService.notify(
      new Notification("hello"), // notification object
      "ankitsharma25795@gmail.com"                   // username
    );
	 /* }*/
    // Return an http 200 status code
    return new ResponseEntity<>(HttpStatus.OK);
  }  
} // class MainController