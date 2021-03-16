package com.findme.controller.restController;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.service.RelationshipService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/relationship")
public class RelationshipController {

    private final RelationshipService relationshipService;

    private static final Logger logger = LogManager.getLogger(RelationshipController.class);

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Object> addRelationship(@RequestParam String userToId, HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            long userToId1;
            try {
                userToId1 = Long.parseLong(userToId);

            } catch (NumberFormatException e) {
                throw new BadRequestException("Fields filed incorrect");
            }
            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            Relationship relationship = relationshipService.addRelationship(actionUserId, userToId1);

            return new ResponseEntity<>(relationship, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<Object> updateRelationship(@RequestParam String userToId,
                                                     @RequestParam String status,
                                                     HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            long userToId1;
            RelationshipStatus relationshipStatus;
            try {
                userToId1 = Long.parseLong(userToId);
                relationshipStatus = RelationshipStatus.valueOf(status);

            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Fields filed incorrect");
            }
            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            Relationship relationship =
                    relationshipService.updateRelationShip(actionUserId, userToId1, relationshipStatus);

            return new ResponseEntity<>(relationship, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
