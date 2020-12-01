package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.exception.ServiceException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RelationshipService {

    Relationship addRelationShip(long userFromId, long userToId) throws ServiceException, InternalServerException;

    Relationship updateRelationShip(long userFromId, long userToId, RelationshipStatus status)
            throws ServiceException, InternalServerException;

    List<Relationship> getIncomeRequests(long userId) throws ServiceException, InternalServerException;

    List<Relationship> getOutcomeRequests(long userId) throws ServiceException, InternalServerException;

}
