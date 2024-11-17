package org.com.project.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllErrors(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Error occurred: " + ex.getMessage());
        return "error";
    }

    @RequestMapping("/error")
    @ResponseBody
    public String handleError() {
        return "An error occurred. Please try again.";
    }
}
