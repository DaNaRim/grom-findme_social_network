package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.NoAccessException;
import com.findme.exception.NotFoundException;
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
    public ResponseEntity<String> addRelationship(@RequestParam String userFromIdStr,
                                                  @RequestParam String userToIdStr,
                                                  HttpSession session) {
        try {
            long userFromId;
            long userToId;
            try {
                userFromId = Long.parseLong(userFromIdStr);
                userToId = Long.parseLong(userToIdStr);

            } catch (NumberFormatException e) {
                throw new BadRequestException("Fields filed incorrect");
            }

            validateAccess(userFromId, session);

            relationshipService.addRelationShip(userFromId, userToId);

            return new ResponseEntity<>("Friend request sent", HttpStatus.OK);
        } catch (UnauthorizedException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NoAccessException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
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
    public ResponseEntity<String> updateRelationship(@RequestParam String userFromIdStr,
                                                     @RequestParam String userToIdStr,
                                                     @RequestParam String status,
                                                     HttpSession session) {
        try {
            long userFromId;
            long userToId;
            RelationshipStatus relationshipStatus;
            try {
                userFromId = Long.parseLong(userFromIdStr);
                userToId = Long.parseLong(userToIdStr);
                relationshipStatus = RelationshipStatus.valueOf(status);

            } catch (NumberFormatException e) {
                throw new BadRequestException("Fields filed incorrect");
            }
            validateAccess(userFromId, session);

            relationshipService.updateRelationShip(userFromId, userToId, relationshipStatus);

            return new ResponseEntity<>("Relationship updated", HttpStatus.OK);
        } catch (UnauthorizedException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NoAccessException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
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
    public String getIncomeRequests(@RequestParam String userIdStr, HttpSession session, Model model) {
        try {
            long userId;
            try {
                userId = Long.parseLong(userIdStr);

            } catch (NumberFormatException e) {
                throw new BadRequestException("Fields filed incorrect");
            }

            validateAccess(userId, session);

            List<Relationship> relationships = relationshipService.getIncomeRequests(userId);

            model.addAttribute("incomeRequests", relationships);
            return "incomeRequests";
        } catch (UnauthorizedException e) {
            model.addAttribute("error", e.getMessage());
            return "401";
        } catch (NoAccessException e) {
            model.addAttribute("error", e.getMessage());
            return "403";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        } catch (BadRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "400";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            model.addAttribute("error", "Something went wrong");
            return "500";
        }
    }

    @GetMapping(path = "/outcomeRequests")
    public String getOutcomeRequests(@RequestParam String userIdStr, HttpSession session, Model model) {
        try {
            long userId;
            try {
                userId = Long.parseLong(userIdStr);

            } catch (NumberFormatException e) {
                throw new BadRequestException("Id filed incorrect");
            }

            validateAccess(userId, session);

            List<Relationship> relationships = relationshipService.getOutcomeRequests(userId);

            model.addAttribute("outcomeRequests", relationships);
            return "outcomeRequests";
        } catch (UnauthorizedException e) {
            model.addAttribute("error", e.getMessage());
            return "401";
        } catch (NoAccessException e) {
            model.addAttribute("error", e.getMessage());
            return "403";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        } catch (BadRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "400";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            model.addAttribute("error", "Something went wrong");
            return "500";
        }
    }

    private void validateAccess(long userId, HttpSession session) throws UnauthorizedException, NoAccessException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        if ((long) session.getAttribute("userId") != userId) {
            throw new NoAccessException("You can`t do that in the name of another user");
        }
    }
}
