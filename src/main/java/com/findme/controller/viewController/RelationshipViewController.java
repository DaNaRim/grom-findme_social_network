package com.findme.controller.viewController;

import com.findme.exception.UnauthorizedException;
import com.findme.model.Relationship;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping(path = "/relationship")
public class RelationshipViewController {

    private final RelationshipService relationshipService;

    @Autowired
    public RelationshipViewController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @GetMapping(path = "/incomeRequests")
    public String getIncomeRequests(@SessionAttribute(required = false) Long userId,
                                    Model model) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        List<Relationship> relationships = relationshipService.getIncomeRequests(userId);

        model.addAttribute("incomeRequests", relationships);
        return "incomeRequests";
    }

    @GetMapping(path = "/outcomeRequests")
    public String getOutcomeRequests(@SessionAttribute(required = false) Long userId,
                                     Model model) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        List<Relationship> relationships = relationshipService.getOutcomeRequests(userId);

        model.addAttribute("outcomeRequests", relationships);
        return "outcomeRequests";
    }
}
