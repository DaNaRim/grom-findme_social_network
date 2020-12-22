package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RelationshipDao {

    Relationship save(Relationship relationship) throws InternalServerException;

    Relationship update(Relationship relationship) throws InternalServerException;

    Relationship findByUsers(long userFromId, long userToId) throws InternalServerException;

    RelationshipStatus findStatusByUsers(long userFromId, long userToId) throws InternalServerException;

    Date getDateModify(long userId) throws InternalServerException;

    List<Relationship> getIncomeRequests(long userId) throws InternalServerException;

    List<Relationship> getOutcomeRequests(long userId) throws InternalServerException;

    int countOutcomeRequests(long userId) throws InternalServerException;

    int countFriends(long userId) throws InternalServerException;
}
