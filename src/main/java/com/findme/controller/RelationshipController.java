package com.findme.controller;

import com.findme.exception.NoAccessException;
import com.findme.exception.NotFoundException;
import com.findme.exception.ServiceException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.service.RelationshipService;
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

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<String> addRelationship(@RequestParam String userFromId,
                                                  @RequestParam String userToId,
                                                  HttpSession session) {
        try {
            relationshipService.addRelationShip(userFromId, userToId, session);

            return new ResponseEntity<>("New Relationship created", HttpStatus.CREATED);
        } catch (ServiceException e) {
            if (e.getCause() instanceof UnauthorizedException) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
            if (e.getCause() instanceof NoAccessException) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }
            if (e.getCause() instanceof NotFoundException) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updateRelationship(@RequestParam String userFromId,
                                                     @RequestParam String userToId,
                                                     @RequestParam RelationshipStatus status,
                                                     HttpSession session) {
        try {
            relationshipService.updateRelationShip(userFromId, userToId, status, session);

            return new ResponseEntity<>("Relationship updated", HttpStatus.OK);
        } catch (ServiceException e) {
            if (e.getCause() instanceof UnauthorizedException) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
            if (e.getCause() instanceof NoAccessException) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }
            if (e.getCause() instanceof NotFoundException) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/incomeRequests")
    public String getIncomeRequests(@RequestParam String userId, HttpSession session, Model model) {
        try {
            List<Relationship> relationships = relationshipService.getIncomeRequests(userId, session);

            model.addAttribute("incomeRequests", relationships);
            return "";
        } catch (ServiceException e) {
            if (e.getCause() instanceof UnauthorizedException) {
                model.addAttribute("error", e.getMessage());
                return "401";
            }
            if (e.getCause() instanceof NoAccessException) {
                model.addAttribute("error", e.getMessage());
                return "403";
            }
            if (e.getCause() instanceof NotFoundException) {
                model.addAttribute("error", e.getMessage());
                return "404";
            }
            model.addAttribute("error", e.getMessage());
            return "400";
        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong");
            return "500";
        }
    }

    @GetMapping(path = "/outcomeRequests")
    public ResponseEntity<String> getOutcomeRequests(@RequestParam String userId, HttpSession session) {
        return null;
    }

    @GetMapping(path = "/friends")
    public ResponseEntity<String> getFriends(@RequestParam String userId, HttpSession session) {
        return null;
    }
}
