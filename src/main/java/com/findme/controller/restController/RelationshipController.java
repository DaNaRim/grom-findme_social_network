package com.findme.controller.restController;

import com.findme.exception.BadRequestException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping(path = "/relationship")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @PostMapping(path = "/add")
    public Relationship addRelationship(@RequestParam String userToId,
                                        @SessionAttribute Long userId) throws Exception {

        long userToId1;
        try {
            userToId1 = Long.parseLong(userToId);

        } catch (NumberFormatException e) {
            throw new BadRequestException("Fields filed incorrect");
        }
        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        return relationshipService.addRelationship(userId, userToId1);
    }

    @PutMapping(path = "/update")
    public Relationship updateRelationship(@RequestParam String userToId,
                                           @RequestParam String status,
                                           @SessionAttribute Long userId) throws Exception {

        long userToId1;
        RelationshipStatus relationshipStatus;
        try {
            userToId1 = Long.parseLong(userToId);
            relationshipStatus = RelationshipStatus.valueOf(status);

        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Fields filed incorrect");
        }
        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        return relationshipService.updateRelationShip(userId, userToId1, relationshipStatus);
    }
}
