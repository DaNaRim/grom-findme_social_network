package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;

import java.util.List;

public interface RelationshipDao {

    Relationship save(Relationship relationship) throws InternalServerException;

    Relationship findById(long id) throws InternalServerException;

    Relationship update(Relationship relationship) throws InternalServerException;

    void delete(Relationship relationship) throws InternalServerException;

    RelationshipStatus getRelationshipStatus(long userFromId, long userToId) throws InternalServerException;

    Relationship findByUsers(long userFromId, long userToId) throws InternalServerException;

    List<Relationship> getIncomeRequests(long userId) throws InternalServerException;

    List<Relationship> getOutcomeRequests(long userId) throws InternalServerException;

}
