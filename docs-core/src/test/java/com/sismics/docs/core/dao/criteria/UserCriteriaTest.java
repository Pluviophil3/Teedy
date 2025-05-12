package com.sismics.docs.core.dao.criteria;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.Test;
// import org.junit.BeforeClass;

public class UserCriteriaTest {

    public UserCriteriaTest() {
        // Constructor for UserCriteriaTest
    }
    
    @Test
    public void testSetUserId() {
        UserCriteria userCriteria;
        userCriteria = new UserCriteria();
        String userId = "user456";
        userCriteria.setUserId(userId);
        Assert.assertEquals(userId, userCriteria.getUserId(), "userId should match the value set");
    }
}
