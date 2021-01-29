package com.findme.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Relationship", schema = "public")
@NamedNativeQueries({
        @NamedNativeQuery(name = Relationship.QUERY_FIND_BY_USERS,
                query = "SELECT * FROM Relationship"
                        + " WHERE user_from = :" + Relationship.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_FROM_ID,
                resultClass = Relationship.class),

        @NamedNativeQuery(name = Relationship.QUERY_FIND_STATUS_BY_USERS,
                query = "SELECT status FROM Relationship"
                        + " WHERE user_from = :" + Relationship.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_FROM_ID),

        @NamedNativeQuery(name = Relationship.QUERY_GET_INCOME_REQUESTS_BY_ACTION_USER_ID,
                query = "SELECT * FROM Relationship"
                        + " WHERE status = 'REQUESTED'"
                        + " AND action_user_id != :" + Relationship.ATTRIBUTE_ACTION_USER_ID
                        + " ORDER BY date_modify",
                resultClass = Relationship.class),

        @NamedNativeQuery(name = Relationship.QUERY_GET_OUTCOME_REQUESTS_BY_ACTION_USER_ID,
                query = "SELECT * FROM Relationship"
                        + " WHERE status = 'REQUESTED'"
                        + " AND action_user_id = :" + Relationship.ATTRIBUTE_ACTION_USER_ID
                        + " ORDER BY date_modify",
                resultClass = Relationship.class),


        @NamedNativeQuery(name = Relationship.QUERY_FIND_ACTION_USER_ID_BY_USERS,
                query = "SELECT action_user_id FROM Relationship"
                        + " WHERE user_from = :" + Relationship.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_FROM_ID),

        @NamedNativeQuery(name = Relationship.QUERY_FIND_DATE_MODIFY_BY_USERS,
                query = "SELECT date_modify FROM Relationship"
                        + " WHERE user_from = :" + Relationship.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_FROM_ID),

        @NamedNativeQuery(name = Relationship.QUERY_COUNT_OUTCOME_REQUESTS_BY_ACTION_USER_ID,
                query = "SELECT COUNT(*) FROM Relationship"
                        + " WHERE status = 'REQUESTED'"
                        + " AND action_user_id = :" + Relationship.ATTRIBUTE_ACTION_USER_ID),

        @NamedNativeQuery(name = Relationship.QUERY_COUNT_FRIENDS_BY_USER_ID,
                query = "SELECT COUNT(*) FROM Relationship"
                        + " WHERE status = 'FRIENDS'"
                        + " AND (user_from = :" + Relationship.ATTRIBUTE_USER_ID
                        + " OR user_to = :" + Relationship.ATTRIBUTE_USER_ID + ")"),


        @NamedNativeQuery(name = Relationship.QUERY_FIND_ID_BY_USERS,
                query = "SELECT id FROM Relationship"
                        + " WHERE user_from = :" + Relationship.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + Relationship.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + Relationship.ATTRIBUTE_USER_FROM_ID)
})
public class Relationship {

    public static final String QUERY_FIND_BY_USERS = "Relationship.findByUsers";
    public static final String QUERY_FIND_STATUS_BY_USERS = "Relationship.findStatusByUsers";
    public static final String QUERY_GET_INCOME_REQUESTS_BY_ACTION_USER_ID = "Relationship.getIncomeRequestsByActionUserId";
    public static final String QUERY_GET_OUTCOME_REQUESTS_BY_ACTION_USER_ID = "Relationship.getOutcomeRequestsByActionUserId";

    public static final String QUERY_FIND_ACTION_USER_ID_BY_USERS = "Relationship.findActionUserIdByUsers";
    public static final String QUERY_FIND_DATE_MODIFY_BY_USERS = "Relationship.findDateModifyByUsers";
    public static final String QUERY_COUNT_OUTCOME_REQUESTS_BY_ACTION_USER_ID = "Relationship.countOutcomeRequestsByActionUserId";
    public static final String QUERY_COUNT_FRIENDS_BY_USER_ID = "Relationship.countFriendsByUserId";

    public static final String QUERY_FIND_ID_BY_USERS = "Relationship.findIdByUsers";


    public static final String ATTRIBUTE_USER_ID = "userId"; //userFrom and userTo
    public static final String ATTRIBUTE_USER_FROM_ID = "userFromId";
    public static final String ATTRIBUTE_USER_TO_ID = "UserToId";
    public static final String ATTRIBUTE_ACTION_USER_ID = "ActionUserId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_from", nullable = false, updatable = false)
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "user_to", nullable = false, updatable = false)
    private User userTo;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;

    //system fields

    @Column(name = "action_user_id", nullable = false)
    private Long actionUserId;

    @Column(name = "date_modify", insertable = false, nullable = false)
    private Date dateModify;

    public Relationship() {
    }

    public Relationship(User userFrom, User userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
    }

    public Relationship(User userFrom, User userTo, RelationshipStatus status) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public RelationshipStatus getStatus() {
        return status;
    }

    public void setStatus(RelationshipStatus status) {
        this.status = status;
    }


    public void setActionUserId(Long actionUserId) {
        this.actionUserId = actionUserId;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = new Date(dateModify.getTime());
    }
}
