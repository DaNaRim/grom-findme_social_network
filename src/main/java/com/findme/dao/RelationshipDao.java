package com.findme.dao;

import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RelationshipDao {

    Relationship save(Relationship relationship);

    Relationship update(Relationship relationship);

    Relationship findByUsers(long userFromId, long userToId);

    RelationshipStatus findStatus(long userFromId, long userToId);

    List<Relationship> getIncomeRequests(long userId);

    List<Relationship> getOutcomeRequests(long userId);

    Long findActionUserId(long userFromId, long userToId);

    Date findDateModify(long userFromId, long userToId);

    int countOutcomeRequests(long userId);

    int countFriends(long userId);

}
