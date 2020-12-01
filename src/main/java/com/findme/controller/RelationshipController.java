package com.findme.controller;

import com.findme.exception.*;
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

            } catch (ArithmeticException e) {
                throw new BadRequestException("Id`s filed incorrect");
            }

            validateAccess(userFromId, session);

            relationshipService.addRelationShip(userFromId, userToId);

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
    public ResponseEntity<String> updateRelationship(@RequestParam String userFromIdStr,
                                                     @RequestParam String userToIdStr,
                                                     @RequestParam RelationshipStatus status,
                                                     HttpSession session) {
        try {
            long userFromId;
            long userToId;
            try {
                userFromId = Long.parseLong(userFromIdStr);
                userToId = Long.parseLong(userToIdStr);

            } catch (ArithmeticException e) {
                throw new BadRequestException("Id`s filed incorrect");
            }
            validateAccess(userFromId, session);

            relationshipService.updateRelationShip(userFromId, userToId, status);

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
    public String getIncomeRequests(@RequestParam String userIdStr, HttpSession session, Model model) {
        try {
            long userId;
            try {
                userId = Long.parseLong(userIdStr);

            } catch (ArithmeticException e) {
                throw new BadRequestException("Id filed incorrect");
            }

            validateAccess(userId, session);

            List<Relationship> relationships = relationshipService.getIncomeRequests(userId);

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
    public ResponseEntity<String> getOutcomeRequests(@RequestParam String userIdStr, HttpSession session) {
        try {
            long userId;
            try {
                userId = Long.parseLong(userIdStr);

            } catch (ArithmeticException e) {
                throw new BadRequestException("Id filed incorrect");
            }

            validateAccess(userId, session);

        } catch (Exception e) {

        }

        return null;
    }

    @GetMapping(path = "/friends")
    public ResponseEntity<String> getFriends(@RequestParam String userId, HttpSession session) {
        return null;
    }

    private void validateAccess(long userId, HttpSession session) throws UnauthorizedException, NoAccessException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        if (session.getAttribute("userId") != String.valueOf(userId)) {
            throw new NoAccessException("You can`t do that in the name of another user");
        }
    }
}
