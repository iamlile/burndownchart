package com.iamlile.jira.burndown.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lee on 2018/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JiraServiceTest {

    @Autowired
    private JiraService jiraService;

    @org.junit.Test
    public void testSetJiraSprint() throws Exception {
        jiraService.setJiraSprint(1);

    }
}