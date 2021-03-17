package com.findme.controller.restController;

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
                                        @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        return relationshipService.addRelationship(userId, Long.parseLong(userToId));
    }

    @PutMapping(path = "/update")
    public Relationship updateRelationship(@RequestParam String userToId,
                                           @RequestParam String status,
                                           @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        return relationshipService.updateRelationShip(userId,
                Long.parseLong(userToId),
                RelationshipStatus.valueOf(status));
    }
}
