package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path = "/relationship")
public class RelationshipController {

    private final RelationshipService relationshipService;
    private final UserService userService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService, UserService userService) {
        this.relationshipService = relationshipService;
        this.userService = userService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<String> addRelationship(@RequestParam String userToIdStr, Model model, HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            long userToId;
            try {
                userToId = Long.parseLong(userToIdStr);

            } catch (NumberFormatException e) {
                throw new BadRequestException("Fields filed incorrect");
            }
            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            Relationship relationship = relationshipService.addRelationship(actionUserId, userToId);

            try {
                userService.updateDateLastActive(actionUserId);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            model.addAttribute("relationship", relationship);
            return new ResponseEntity<>("Friend request sent", HttpStatus.OK);
        } catch (UnauthorizedException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updateRelationship(@RequestParam String userToIdStr,
                                                     @RequestParam String status,
                                                     Model model,
                                                     HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            long userToId;
            RelationshipStatus relationshipStatus;
            try {
                userToId = Long.parseLong(userToIdStr);
                relationshipStatus = RelationshipStatus.valueOf(status);

            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Fields filed incorrect");
            }
            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            Relationship relationship =
                    relationshipService.updateRelationShip(actionUserId, userToId, relationshipStatus);

            try {
                userService.updateDateLastActive(actionUserId);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            model.addAttribute("relationship", relationship);
            return new ResponseEntity<>("Relationship updated", HttpStatus.OK);
        } catch (UnauthorizedException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/incomeRequests")
    public String getIncomeRequests(HttpSession session, Model model) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            List<Relationship> relationships = relationshipService.getIncomeRequests(actionUserId);

            try {
                userService.updateDateLastActive(actionUserId);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            model.addAttribute("incomeRequests", relationships);
            return "incomeRequests";
        } catch (UnauthorizedException e) {
            model.addAttribute("error", e.getMessage());
            return "401";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            model.addAttribute("error", "Something went wrong");
            return "500";
        }
    }

    @GetMapping(path = "/outcomeRequests")
    public String getOutcomeRequests(HttpSession session, Model model) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            List<Relationship> relationships = relationshipService.getOutcomeRequests(actionUserId);

            try {
                userService.updateDateLastActive(actionUserId);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            model.addAttribute("outcomeRequests", relationships);
            return "outcomeRequests";
        } catch (UnauthorizedException e) {
            model.addAttribute("error", e.getMessage());
            return "401";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            model.addAttribute("error", "Something went wrong");
            return "500";
        }
    }

}
