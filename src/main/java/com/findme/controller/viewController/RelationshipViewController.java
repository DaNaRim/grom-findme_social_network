package com.findme.controller.viewController;

import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.Relationship;
import com.findme.service.RelationshipService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path = "/relationship")
public class RelationshipViewController {

    private final RelationshipService relationshipService;

    private static final Logger logger = LogManager.getLogger(com.findme.controller.restController.RelationshipController.class);

    @Autowired
    public RelationshipViewController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }


    @GetMapping(path = "/incomeRequests")
    public String getIncomeRequests(HttpSession session, Model model) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            List<Relationship> relationships = relationshipService.getIncomeRequests(actionUserId);

            model.addAttribute("incomeRequests", relationships);
            return "incomeRequests";
        } catch (UnauthorizedException e) {
            model.addAttribute("error", e.getMessage());
            return "401";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
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

            model.addAttribute("outcomeRequests", relationships);
            return "outcomeRequests";
        } catch (UnauthorizedException e) {
            model.addAttribute("error", e.getMessage());
            return "401";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            model.addAttribute("error", "Something went wrong");
            return "500";
        }
    }
}
