package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.exception.ServiceException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public interface RelationshipService {

    Relationship addRelationShip(String userFromIdStr, String userToIdStr, HttpSession session)
            throws ServiceException, InternalServerException;

    Relationship updateRelationShip(String userFromIdStr, String userToIdStr,
                                    RelationshipStatus status, HttpSession session)
            throws ServiceException, InternalServerException;

    List<Relationship> getIncomeRequests(String userIdStr, HttpSession session)
            throws ServiceException, InternalServerException;

    List<Relationship> getOutcomeRequests(String userIdStr, HttpSession session)
            throws ServiceException, InternalServerException;

}
