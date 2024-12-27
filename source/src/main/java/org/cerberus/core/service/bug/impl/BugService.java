/**
 * Cerberus Copyright (C) 2013 - 2025 cerberustesting
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of Cerberus.
 *
 * Cerberus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cerberus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cerberus.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.cerberus.core.service.bug.impl;

import org.cerberus.core.crud.entity.Application;
import org.cerberus.core.crud.entity.Parameter;
import org.cerberus.core.crud.entity.TestCase;
import org.cerberus.core.crud.entity.TestCaseExecution;
import org.cerberus.core.crud.service.IApplicationService;
import org.cerberus.core.crud.service.IParameterService;
import org.cerberus.core.crud.service.ITestCaseExecutionService;
import org.cerberus.core.crud.service.ITestCaseService;
import org.cerberus.core.engine.entity.ExecutionLog;
import org.cerberus.core.exception.CerberusException;
import org.cerberus.core.service.bug.IBugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.cerberus.core.service.bug.github.IGithubService;
import org.cerberus.core.service.bug.jira.IJiraService;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author vertigo17
 */
@Service
public class BugService implements IBugService {

    @Autowired
    private IParameterService parameterService;
    @Autowired
    private IApplicationService applicationService;
    @Autowired
    private ITestCaseService testCaseService;
    @Autowired
    private ITestCaseExecutionService testCaseExecutionService;
    @Autowired
    private IGithubService githubService;
    @Autowired
    private IJiraService jiraService;

    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger(BugService.class);

    @Override
    @Async
    public void createBugAsync(TestCaseExecution execution, boolean forceCreation) {
        createBug(execution, forceCreation);
    }

    @Override
    public JSONObject createBug(TestCaseExecution execution, boolean forceCreation) {
        JSONObject newBugCreated = new JSONObject();
        try {

            if (!parameterService.getParameterBooleanByKey(Parameter.VALUE_cerberus_autobugcreation_enable, execution.getSystem(), false)) {
                LOG.debug("Not creating bug due to parameter.");
                newBugCreated.put("message", "Not creating bug due to parameter : " + Parameter.VALUE_cerberus_autobugcreation_enable);
                return newBugCreated;
            }
            LOG.debug("Trying to create bug.");
            execution.addExecutionLog(ExecutionLog.STATUS_INFO, "Trying To create the bug.");
            // Testcase should have a priority defined and in WORKING status
            if (((execution.getTestCasePriority() >= 1) && !"OK".equalsIgnoreCase(execution.getControlStatus())) || forceCreation) {
                LOG.debug("Execution is not OK, with prio > 0.");
                execution.addExecutionLog(ExecutionLog.STATUS_INFO, "Bug creation - Execution is not OK, with prio > 0.");
                TestCase tc = null;
                try {
                    tc = testCaseService.findTestCaseByKey(execution.getTest(), execution.getTestCase());

                    // There should not be any already existing bug.
                    if ((!testCaseService.isBugAlreadyOpen(tc)) || forceCreation) {
                        execution.addExecutionLog(ExecutionLog.STATUS_INFO, "Bug creation - There is no existing open bug reported.");

                        // All is fine to open a new bug
                        Application currentAppli = new Application();
                        try {
                            currentAppli = applicationService.convert(applicationService.readByKey(execution.getApplication()));
                        } catch (CerberusException ex) {
                            LOG.warn(ex, ex);
                            newBugCreated.put("message", ex.toString());
                        }

                        if (currentAppli != null) {
                            switch (currentAppli.getBugTrackerConnector()) {
                                case Application.BUGTRACKER_JIRA:
                                    newBugCreated.put("bug", jiraService.createJiraIssue(tc, execution, currentAppli.getBugTrackerParam1(), currentAppli.getBugTrackerParam2()));

                                    break;
                                case Application.BUGTRACKER_GITHUB:
                                    newBugCreated.put("bug", githubService.createGithubIssue(tc, execution, currentAppli.getBugTrackerParam1(), currentAppli.getBugTrackerParam2()));

                                    break;
                                default:
                                    throw new AssertionError();
                            }
                        }
                    } else {
                        LOG.debug("Not opening Issue because issue is already open");
                        newBugCreated.put("message", "Not opening Issue because issue is already open");
                        execution.addExecutionLog(ExecutionLog.STATUS_INFO, "Bug creation - There is already an open bug reported.");
                    }
                } catch (CerberusException ex) {
                    newBugCreated.put("message", ex.toString());
                    LOG.warn(ex, ex);
                }
            }
        } catch (JSONException ex) {
            LOG.error(ex, ex);
        }
        return newBugCreated;
    }

    @Override
    public JSONObject createBugFromID(long executionId, String user) {
        JSONObject newBugCreated = new JSONObject();

        LOG.debug("Trying to create bug from execution id {}.", executionId);
        TestCaseExecution execution = null;
        try {
            execution = testCaseExecutionService.findTCExecutionByKey(executionId);

            return this.createBug(execution, true);

        } catch (CerberusException ex) {
            LOG.warn(ex, ex);
        }
        return newBugCreated;
    }

}
