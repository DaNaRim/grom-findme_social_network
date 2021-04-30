package com.findme.controller.restController;

import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.service.RelationshipService;
import com.findme.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relationship")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @PostMapping("/add")
    public Relationship addRelationship(@RequestParam String userToId) throws Exception {

        return relationshipService.addRelationship(SecurityUtil.getAuthorizedUserId(), Long.parseLong(userToId));
    }

    @PutMapping("/update")
    public Relationship updateRelationship(@RequestParam String userToId,
                                           @RequestParam String status) throws Exception {

        return relationshipService.updateRelationShip(SecurityUtil.getAuthorizedUserId(),
                Long.parseLong(userToId),
                RelationshipStatus.valueOf(status));
    }

    @GetMapping("/incomeRequests")
    public List<Relationship> getIncomeRequests() throws Exception {

        return relationshipService.getIncomeRequests(SecurityUtil.getAuthorizedUserId());
    }

    @GetMapping("/outcomeRequests")
    public List<Relationship> getOutcomeRequests() throws Exception {

        return relationshipService.getOutcomeRequests(SecurityUtil.getAuthorizedUserId());
    }

}
