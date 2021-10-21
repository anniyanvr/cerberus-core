/**
 * Cerberus Copyright (C) 2013 - 2017 cerberustesting
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
package org.cerberus.api.controller.v001;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.cerberus.crud.service.ITestCaseStepService;
import org.cerberus.api.dto.v001.TestcaseStepDTOV001;
import org.cerberus.api.mapper.v001.TestcaseStepMapperV001;
import org.cerberus.api.service.TestcaseStepApiService;
import org.cerberus.crud.entity.TestCaseStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mlombard
 */
@Api(tags = "TestcaseStep endpoint")
@RestController
@RequestMapping(path = "/public/testcasesteps")
public class TestcaseStepControllerV001 {

    private static final String API_VERSION_1 = "X-API-VERSION=1";
    private static final String API_KEY = "apikey";
    private final ITestCaseStepService testCaseStepService;
    private final TestcaseStepApiService testcaseStepApiService;
    private final TestcaseStepMapperV001 stepMapper;

    @Autowired
    public TestcaseStepControllerV001(ITestCaseStepService testCaseStepService, TestcaseStepApiService testcaseStepApiService, TestcaseStepMapperV001 stepMapper) {
        this.testCaseStepService = testCaseStepService;
        this.testcaseStepApiService = testcaseStepApiService;
        this.stepMapper = stepMapper;
    }

    @ApiOperation("Get all TestcaseSteps")
    @ApiResponse(code = 200, message = "ok", response = TestcaseStepDTOV001.class, responseContainer = "List")
    @GetMapping(headers = {API_VERSION_1, API_KEY}, produces = "application/json")
    public List<TestcaseStepDTOV001> findAllTestcaseSteps(@RequestParam("islibrarystep") boolean isLibraryStep) {
        List<TestCaseStep> steps
                = isLibraryStep
                        ? this.testcaseStepApiService.findAllLibrarySteps()
                        : this.testcaseStepApiService.findAllTestcaseSteps();

        return steps
                .stream()
                .map(this.stepMapper::toDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation("Get all testcase steps from a test folder")
    @ApiResponse(code = 200, message = "ok", response = TestcaseStepDTOV001.class, responseContainer = "List")
    @GetMapping(path = "/{testFolderId}", headers = {API_VERSION_1, API_KEY}, produces = "application/json")
    public List<TestcaseStepDTOV001> findTestcaseStepsByTestFolderId(@PathVariable("testFolderId") String testFolderId) {
        return this.testcaseStepApiService.findTestcaseStepsByTestFolderId(testFolderId)
                .stream()
                .map(this.stepMapper::toDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation("Get all steps of a testcase")
    @ApiResponse(code = 200, message = "ok", response = TestcaseStepDTOV001.class, responseContainer = "List")
    @GetMapping(path = "/{testFolderId}/{testcaseId}", headers = {API_VERSION_1, API_KEY}, produces = "application/json")
    public List<TestcaseStepDTOV001> findTestcaseStepsByTestFolderIdTestcaseId(
            @PathVariable("testFolderId") String testFolderId,
            @PathVariable("testcaseId") String testcaseId) {
        return this.testCaseStepService.readByTestTestCase(testFolderId, testcaseId)
                .getDataList()
                .stream()
                .map(this.stepMapper::toDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation("Get a TestcaseStep filtered by testFolderId and testcaseId")
    @ApiResponse(code = 200, message = "ok", response = TestcaseStepDTOV001.class)
    @GetMapping(path = "/{testFolderId}/{testcaseId}/{stepId}", headers = {API_VERSION_1, API_KEY}, produces = "application/json")
    public TestcaseStepDTOV001 findTestcaseStepByTestFolderIdAndTestcaseIdAndStepId(
            @PathVariable("testFolderId") String testFolderId,
            @PathVariable("testcaseId") String testcaseId,
            @PathVariable("stepId") int stepId,
            @RequestParam("islibrarystep") boolean isLibraryStep) {

        return this.stepMapper.toDTO(
                this.testCaseStepService.readTestcaseStepWithDependencies(
                        testFolderId,
                        testcaseId,
                        stepId
                )
        );
    }

}