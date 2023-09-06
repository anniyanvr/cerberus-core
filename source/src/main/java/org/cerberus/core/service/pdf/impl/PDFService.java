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
package org.cerberus.core.service.pdf.impl;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.cerberus.core.crud.entity.Parameter;
import org.cerberus.core.crud.entity.Tag;
import org.cerberus.core.crud.entity.Test;
import org.cerberus.core.crud.entity.TestCaseExecution;
import org.cerberus.core.crud.entity.TestCaseExecutionFile;
import org.cerberus.core.crud.entity.TestCaseStepActionControlExecution;
import org.cerberus.core.crud.entity.TestCaseStepActionExecution;
import org.cerberus.core.crud.entity.TestCaseStepExecution;
import org.cerberus.core.crud.service.IParameterService;
import org.cerberus.core.crud.service.ITestCaseExecutionService;
import org.cerberus.core.exception.CerberusException;
import org.springframework.stereotype.Service;

import org.cerberus.core.service.pdf.IPDFService;
import org.cerberus.core.util.DateUtil;
import org.cerberus.core.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author bcivel
 */
@Service
public class PDFService implements IPDFService {

    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger(PDFService.class);

    @Autowired
    private ITestCaseExecutionService testCaseExecutionService;
    @Autowired
    private IParameterService parameterService;

    private Table getTitleTable(String desc1, String desc2) {
        String logoURL = parameterService.getParameterStringByKey(Parameter.VALUE_cerberus_instancelogo_url, "", "https://vm.cerberus-testing.org/img/logo.png");
        // Tittle
        Table tableTitle = new Table(new float[]{100, 500});
        try {
            ImageData imageDataLogo;
            if (StringUtil.isNotEmptyOrNullValue(logoURL)) {
                imageDataLogo = ImageDataFactory.create(logoURL);
            } else {
                imageDataLogo = ImageDataFactory.create("https://vm.cerberus-testing.org/img/logo.png");
            }
            Image image = new Image(imageDataLogo).scaleToFit(100, 70);
            tableTitle.addCell(new Cell().add(image.setBorder(Border.NO_BORDER).setHorizontalAlignment(HorizontalAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        } catch (Exception ex) {
            LOG.error("Error when trying to load content from : " + logoURL);
            LOG.error(ex, ex);
            tableTitle.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        }
        Cell descCell = new Cell();
        descCell.add(new Paragraph(desc1).setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
        if (StringUtil.isNotEmptyOrNullValue(desc2)) {
            descCell.add(new Paragraph(desc2).setBold().setItalic().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
        }
        descCell.setMarginBottom(30).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE);
        tableTitle.addCell(descCell);
        return tableTitle;
    }

    @Override
    public String generatePdf(Tag tag, Date today, String folder) throws FileNotFoundException {

        UUID fileUUID = UUID.randomUUID();
        DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_REPORT);

        // Creating a PdfWriter
        String dest = folder + File.separatorChar + "Campaign Execution Report tmp.pdf";
        LOG.info("Starting to generate PDF Report on :" + dest);
        PdfWriter writer = new PdfWriter(dest);

        // Creating a PdfDocument       
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Load parameters
        String mediaPath = parameterService.getParameterStringByKey(Parameter.VALUE_cerberus_exeautomedia_path, "", "");
        mediaPath = StringUtil.addSuffixIfNotAlready(mediaPath, File.separator);

        boolean displayCountryColumn = parameterService.getParameterBooleanByKey(Parameter.VALUE_cerberus_pdfcampaignreportdisplaycountry_boolean, "", true);

        // Tittle
        Table tableTitle = getTitleTable("Campaign Execution Report", tag.getTag());

        try ( // Creating a Document
                Document document = new Document(pdfDoc)) {

            AreaBreak aB = new AreaBreak();

            // Tittle
            document.add(tableTitle.setMarginLeft(0));

            document.add(new Paragraph().add(getTextFromString("", 10, false)));
            document.add(new Paragraph().add(getTextFromString("", 10, false)));
            document.add(new Paragraph().add(getTextFromString("", 10, false)));

            if (!StringUtil.isEmptyOrNullValue(tag.getDescription())) {
                List<IElement> eleList = HtmlConverter.convertToElements(tag.getDescription());
                Table tableDesc = new Table(new float[]{1000});

                Cell myCell = new Cell();
                for (IElement element : eleList) {
                    myCell.add((IBlockElement) element);
                }
                tableDesc.addCell(myCell.setBorder(Border.NO_BORDER));
                document.add(tableDesc);
            }

            document.add(new Paragraph("Main technical details").setMarginTop(30).setMarginBottom(10).setBold().setFontSize(14));

            long tagDur = (tag.getDateEndQueue().getTime() - tag.getDateCreated().getTime()) / 60000;
            document.add(new Paragraph()
                    .add(getTextFromString("Report generated at ", 10, false))
                    .add(getTextFromString(String.valueOf(df.format(today)), 12, true))
            );
            document.add(new Paragraph()
                    .add(getTextFromString("Campaign started at ", 10, false))
                    .add(getTextFromString(String.valueOf(tag.getDateCreated()), 12, true))
                    .add(getTextFromString(" and ended at ", 10, false))
                    .add(getTextFromString(String.valueOf(tag.getDateEndQueue()), 12, true))
                    .add(getTextFromString(" (duration of ", 10, false))
                    .add(getTextFromString(String.valueOf(tagDur), 12, true))
                    .add(getTextFromString(" min)", 10, false))
            );
            if (displayCountryColumn) {
                document.add(new Paragraph()
                        .add(getTextFromString("Executed on Country(ies): ", 10, false))
                        .add(getTextFromString(StringUtil.convertToString(new JSONArray(tag.getCountryList()), ","), 12, true))
                        .add(getTextFromString(", Environment(s): ", 10, false))
                        .add(getTextFromString(StringUtil.convertToString(new JSONArray(tag.getEnvironmentList()), ","), 12, true))
                        .add(getTextFromString(" and Robot(s): ", 10, false))
                        .add(getTextFromString(StringUtil.convertToString(new JSONArray(tag.getRobotDecliList()), ","), 12, true)));
            } else {
                document.add(new Paragraph()
                        .add(getTextFromString("Executed on Environment(s): ", 10, false))
                        .add(getTextFromString(tag.getEnvironmentList() == null ? "" : StringUtil.convertToString(new JSONArray(tag.getEnvironmentList()), ","), 12, true))
                        .add(getTextFromString(" and Robot(s): ", 10, false))
                        .add(getTextFromString(tag.getRobotDecliList() == null ? "" : StringUtil.convertToString(new JSONArray(tag.getRobotDecliList()), ","), 12, true)));
            }

            /**
             * Result information per status
             */
            document.add(new Paragraph("Global status").setMarginTop(30).setMarginBottom(10).setBold().setFontSize(14));
            // Creating a table
            Table tableGlobalStatus = new Table(new float[]{50, 50, 40})
                    .addHeaderCell(getHeaderCell("Status"))
                    .addHeaderCell(getHeaderCell("Number"))
                    .addHeaderCell(getHeaderCell("%"));

            // Map that will contain the color of every status.
            Map<String, String> statColorMap = new HashMap<>();
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_OK, TestCaseExecution.CONTROLSTATUS_OK_COL_EXT);
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_KO, TestCaseExecution.CONTROLSTATUS_KO_COL_EXT);
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_FA, TestCaseExecution.CONTROLSTATUS_FA_COL_EXT);
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_NA, TestCaseExecution.CONTROLSTATUS_NA_COL_EXT);
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_NE, TestCaseExecution.CONTROLSTATUS_NE_COL_EXT);
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_WE, TestCaseExecution.CONTROLSTATUS_WE_COL_EXT);
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_PE, TestCaseExecution.CONTROLSTATUS_PE_COL_EXT);
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_QU, TestCaseExecution.CONTROLSTATUS_QU_COL_EXT);
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_QE, TestCaseExecution.CONTROLSTATUS_QE_COL_EXT);
            statColorMap.put(TestCaseExecution.CONTROLSTATUS_CA, TestCaseExecution.CONTROLSTATUS_CA_COL_EXT);

            // Map that will contain the nb of execution for global status.
            List<TestCaseExecution> listOfExecutions = testCaseExecutionService.readLastExecutionAndExecutionInQueueByTag(tag.getTag());
            Collections.sort(listOfExecutions, new SortExecution());

            Map<String, Integer> statNbMap = new HashMap<>();
            for (TestCaseExecution execution : listOfExecutions) {
                LOG.debug("1 " + execution.getControlStatus() + " - " + statNbMap.get(execution.getControlStatus()));
                if (statNbMap.get(execution.getControlStatus()) == null) {
                    statNbMap.put(execution.getControlStatus(), 0);
                }
                statNbMap.put(execution.getControlStatus(), statNbMap.get(execution.getControlStatus()) + 1);
                LOG.debug("2 " + execution.getControlStatus() + " - " + statNbMap.get(execution.getControlStatus()));
            }

            // Status list in the correct order.
            float per = 0;
            List<String> statList = new ArrayList<>(Arrays.asList("OK", "KO", "FA", "NA", "NE", "WE", "PE", "QU", "QE", "CA"));
            for (String string : statList) {
                if ((statNbMap.get(string) != null) && (statNbMap.get(string) > 0)) {
                    per = statNbMap.get(string) / (float) tag.getNbExeUsefull();
                    per *= 100;
                    tableGlobalStatus
                            .addCell(getStatusCell(string, 1, 1))
                            .addCell(String.valueOf(statNbMap.get(string))).setTextAlignment(TextAlignment.RIGHT)
                            .addCell(String.format("%.2f", per));

                }
            }
            document.add(tableGlobalStatus);

            document.add(aB);

            /**
             * Summary result per execution
             */
            document.add(getTitleTable("Execution list summary", "").setMarginLeft(0));

            // Creating a table
            Table tableExe;
            if (displayCountryColumn) {
                tableExe = new Table(new float[]{40, 140, 80, 20, 80, 20, 20, 50, 50, 50, 30});

            } else {
                tableExe = new Table(new float[]{40, 140, 80, 20, 80, 20, 50, 50, 50, 30});
            }

            tableExe.addHeaderCell(getHeaderCell("Exe ID"))
                    .addHeaderCell(getHeaderCell("Test Folder"))
                    .addHeaderCell(getHeaderCell("Test ID"))
                    .addHeaderCell(getHeaderCell("Prio"))
                    .addHeaderCell(getHeaderCell("Application"));
            if (displayCountryColumn) {
                tableExe.addHeaderCell(getHeaderCell("Country"));
            }
            tableExe
                    .addHeaderCell(getHeaderCell("Environment"))
                    .addHeaderCell(getHeaderCell("Robot"))
                    .addHeaderCell(getHeaderCell("Started"))
                    .addHeaderCell(getHeaderCell("Ended"))
                    .addHeaderCell(getHeaderCell("Result"));

            DateFormat dfEnd = new SimpleDateFormat(DateUtil.DATE_FORMAT_REPORT_TIME);
            Calendar calStart = Calendar.getInstance();
            Calendar calEnd = Calendar.getInstance();

            int nbColSpan = 8;
            if (displayCountryColumn) {
                nbColSpan = 9;
            }

            for (TestCaseExecution execution : listOfExecutions) {
                Cell cellID = new Cell(2, 1).add(new Paragraph(String.valueOf(execution.getId()))).setFontSize(6).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER);
                Cell cellRes = getStatusCell(execution.getControlStatus(), 2, 1);
                Cell cellTCDesc = new Cell(1, nbColSpan).add(new Paragraph(execution.getDescription())).setFontSize(7);
                calStart.setTimeInMillis(execution.getStart());
                calEnd.setTimeInMillis(execution.getEnd());

                tableExe
                        .addCell(cellID.setAction(PdfAction.createGoTo(String.valueOf(execution.getId()))))
                        .addCell(new Cell().add(new Paragraph(execution.getTest())).setFontSize(7))
                        .addCell(new Cell().add(new Paragraph(execution.getTestCase())).setFontSize(7))
                        .addCell(new Cell().add(new Paragraph(String.valueOf(execution.getTestCasePriority()))).setFontSize(7))
                        .addCell(new Cell().add(new Paragraph(execution.getApplication())).setFontSize(7));
                if (displayCountryColumn) {
                    tableExe
                            .addCell(new Cell().add(new Paragraph(execution.getCountry())).setFontSize(7));
                }
                tableExe
                        .addCell(new Cell().add(new Paragraph(execution.getEnvironment())).setFontSize(7))
                        .addCell(new Cell().add(new Paragraph(execution.getRobot())).setFontSize(7))
                        .addCell(new Cell().add(new Paragraph(df.format(calStart.getTime()))).setFontSize(7))
                        .addCell(new Cell().add(new Paragraph(df.format(calEnd.getTime()))).setFontSize(7))
                        .addCell(cellRes);
                tableExe
                        .addCell(cellTCDesc);
            }
            document.add(tableExe);
            document.add(aB);

            document.add(getTitleTable("Other technical details", "").setMarginLeft(0));

            if (StringUtil.isEmptyOrNullValue(tag.getCampaign())) {
                if (!StringUtil.isEmptyOrNullValue(tag.getUsrCreated())) {
                    document.add(new Paragraph()
                            .add(getTextFromString("Triggered by ", 10, false))
                            .add(getTextFromString(tag.getUsrCreated(), 12, true))
                    );
                }
            } else {
                if (StringUtil.isEmptyOrNullValue(tag.getUsrCreated())) {
                    document.add(new Paragraph()
                            .add(getTextFromString("Triggered from campaign: ", 10, false))
                            .add(getTextFromString(tag.getCampaign(), 12, true))
                    );
                } else {
                    document.add(new Paragraph()
                            .add(getTextFromString("Triggered from campaign: ", 10, false))
                            .add(getTextFromString(tag.getCampaign(), 12, true))
                            .add(getTextFromString(" by ", 10, false))
                            .add(getTextFromString(tag.getUsrCreated(), 12, true))
                    );
                }
            }

            document.add(new Paragraph()
                    .add(getTextFromString("Global result for campaign is ", 10, false))
                    .add(getTextFromString(tag.getCiResult(), 12, true))
                    .add(getTextFromString(" (with a Score of ", 10, false))
                    .add(getTextFromString(String.valueOf(tag.getCiScore()), 12, true))
                    .add(getTextFromString(" vs ", 10, false))
                    .add(getTextFromString(String.valueOf(tag.getCiScoreThreshold()), 12, true))
                    .add(getTextFromString(")", 10, false)));

            document.add(new Paragraph()
                    .add(getTextFromString(String.valueOf(tag.getNbExeUsefull()), 12, true))
                    .add(getTextFromString(" useful executions were performed (Over ", 10, false))
                    .add(getTextFromString(String.valueOf(tag.getNbExe()), 12, true))
                    .add(getTextFromString(" in total including retries)", 10, false)));

            /**
             * Legend
             */
            document.add(new Paragraph("Execution status legend").setMarginTop(30).setMarginBottom(10).setBold().setFontSize(14));
            // Creating a table
            Table tableLegendGlobalStatus = new Table(new float[]{50, 500})
                    .addHeaderCell(getHeaderCell("Status"))
                    .addHeaderCell(getHeaderCell("Meaning"));
            tableLegendGlobalStatus
                    .addCell(getStatusCell(TestCaseExecution.CONTROLSTATUS_OK, 1, 1))
                    .addCell("The execution was performed correctly and all controls were OK.").setTextAlignment(TextAlignment.LEFT)
                    .addCell(getStatusCell(TestCaseExecution.CONTROLSTATUS_KO, 1, 1))
                    .addCell("The execution was performed correcly and at least one control failed resulting a global KO. That means that a bug needs to be reported to development teams.").setTextAlignment(TextAlignment.LEFT)
                    .addCell(getStatusCell(TestCaseExecution.CONTROLSTATUS_FA, 1, 1))
                    .addCell("The execution did not performed correctly and needs a correction from the team that is in charge of managing the testcases. It could be a failed SQL or action during the test.").setTextAlignment(TextAlignment.LEFT)
                    .addCell(getStatusCell(TestCaseExecution.CONTROLSTATUS_NA, 1, 1))
                    .addCell("Test could not be executed as a data could not be retreived. That probably means that the test is not possible in the current environment/status.").setTextAlignment(TextAlignment.LEFT);

            // Adding Table to document
            document.add(tableLegendGlobalStatus);

            document.add(new Paragraph("Test cases legend").setMarginTop(30).setMarginBottom(10).setBold().setFontSize(14));

            Table tableTmp;

            tableTmp = new Table(new float[]{500, 20})
                    .addCell(new Cell().add(new Paragraph().add(getTextFromString("Step", 12, true).setTextAlignment(TextAlignment.LEFT)))
                            .setBorder(Border.NO_BORDER).setBorderLeft(new SolidBorder(ColorConstants.CYAN, 3)).setBorderRight(new SolidBorder(1)).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)))
                    .addCell(getStatusCell("OK", 1, 1).setTextAlignment(TextAlignment.RIGHT));
            document.add(tableTmp.setMarginLeft(0));

            tableTmp = new Table(new float[]{500, 20})
                    .addCell(new Cell().add(new Paragraph().add(getTextFromString("Action", 12, true).setTextAlignment(TextAlignment.LEFT)))
                            .setBorder(Border.NO_BORDER).setBorderLeft(new SolidBorder(ColorConstants.BLUE, 3)).setBorderRight(new SolidBorder(1)).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)))
                    .addCell(getStatusCell("OK", 1, 1).setTextAlignment(TextAlignment.RIGHT));
            document.add(tableTmp.setMarginLeft(20));

            tableTmp = new Table(new float[]{500, 20})
                    .addCell(new Cell().add(new Paragraph().add(getTextFromString("Control", 12, true).setTextAlignment(TextAlignment.LEFT)))
                            .setBorder(Border.NO_BORDER).setBorderLeft(new SolidBorder(ColorConstants.GREEN, 3)).setBorderRight(new SolidBorder(1)).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)))
                    .addCell(getStatusCell("OK", 1, 1).setTextAlignment(TextAlignment.RIGHT));
            document.add(tableTmp.setMarginLeft(40));

            // Closing the document
            LOG.info("Ending to generate PDF Report on :" + dest);
            return dest;
        } catch (ParseException | CerberusException | JSONException ex) {
            LOG.error(ex, ex);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
        return null;
    }

    @Override
    public String generatePdfAppendix(Tag tag, Date today, String folder) throws FileNotFoundException {

        UUID fileUUID = UUID.randomUUID();

        // Creating a PdfWriter
        String dest = folder + File.separatorChar + "Campaign Execution Report Appendix tmp.pdf";
        LOG.info("Starting to generate PDF Report on :" + dest);
        PdfWriter writer = new PdfWriter(dest);

        // Creating a PdfDocument       
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Load parameters
        String mediaPath = parameterService.getParameterStringByKey(Parameter.VALUE_cerberus_exeautomedia_path, "", "");
        mediaPath = StringUtil.addSuffixIfNotAlready(mediaPath, File.separator);

        boolean displayCountryColumn = parameterService.getParameterBooleanByKey(Parameter.VALUE_cerberus_pdfcampaignreportdisplaycountry_boolean, "", true);

        try ( // Creating a Document
                Document document = new Document(pdfDoc)) {

            AreaBreak aB = new AreaBreak();

            // Tittle
            document.add(getTitleTable("", ""));

            Table tableExe, tableTmp;

            tableTmp = new Table(new float[]{600})
                    .addCell(new Cell().add(new Paragraph().add(getTextFromString("APPENDIX", 20, true).setTextAlignment(TextAlignment.CENTER)).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
            document.add(tableTmp.setMarginTop(200));

            tableTmp = new Table(new float[]{600})
                    .addCell(new Cell().add(new Paragraph().add(getTextFromString("Details of Execution Campaign", 20, true).setTextAlignment(TextAlignment.CENTER)).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
            document.add(tableTmp.setMarginTop(40));

            document.add(aB);

            /**
             * Summary result per execution
             */
            List<TestCaseExecution> listOfExecutions = testCaseExecutionService.readLastExecutionAndExecutionInQueueByTag(tag.getTag());
            Collections.sort(listOfExecutions, new SortExecution());

            /**
             * Detail information per execution
             */
            int i = 1;
            for (TestCaseExecution execution : listOfExecutions) {
                document.add(new Paragraph("Execution: " + execution.getId()).setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER).setDestination(String.valueOf(execution.getId())));
                // Adding exeution details
                String coloHex = getColor(execution.getControlStatus());
                document.add(new Paragraph(execution.getControlStatus() + " - " + execution.getDescription())
                        .setBackgroundColor(new DeviceRgb(decodeColor(coloHex, "R"), decodeColor(coloHex, "G"), decodeColor(coloHex, "B"))));
                document.add(new Paragraph()
                        .add(getTextFromString(String.valueOf(execution.getControlMessage()), 12, true)));

                if (displayCountryColumn) {
                    tableExe = new Table(new float[]{200, 90, 20, 70, 80, 70, 70});
                } else {
                    tableExe = new Table(new float[]{200, 90, 20, 70, 70, 70});
                }
                tableExe
                        .addHeaderCell(getHeaderCell("Test Folder"))
                        .addHeaderCell(getHeaderCell("Test ID"))
                        .addHeaderCell(getHeaderCell("Prio"))
                        .addHeaderCell(getHeaderCell("Application"));
                if (displayCountryColumn) {
                    tableExe
                            .addHeaderCell(getHeaderCell("Country"));
                }
                tableExe
                        .addHeaderCell(getHeaderCell("Environment"))
                        .addHeaderCell(getHeaderCell("Robot"));
                tableExe
                        .addCell(execution.getTest())
                        .addCell(execution.getTestCase())
                        .addCell(String.valueOf(execution.getTestCasePriority()))
                        .addCell(execution.getApplication());
                if (displayCountryColumn) {
                    tableExe
                            .addCell(execution.getCountry());

                }
                tableExe
                        .addCell(execution.getEnvironment())
                        .addCell(execution.getRobot());
                document.add(tableExe.setMarginTop(10).setMarginBottom(10));

                @SuppressWarnings("unchecked")
                TestCaseExecution exec = testCaseExecutionService.convert(testCaseExecutionService.readByKeyWithDependency(execution.getId()));
                String desc = "";

                for (TestCaseStepExecution step : exec.getTestCaseStepExecutionList()) {
                    if (!TestCaseExecution.CONTROLSTATUS_NE.equals(step.getReturnCode())) {

                        // Creating a table
                        tableTmp = new Table(new float[]{500, 20})
                                .addCell(new Cell().add(new Paragraph().add(getTextFromString(getElementDescription(step.getDescription(), step.getSort(), step.getIndex(), step.getTest()), 12, true).setTextAlignment(TextAlignment.LEFT)))
                                        .setBorder(Border.NO_BORDER).setBorderLeft(new SolidBorder(ColorConstants.CYAN, 3)).setBorderRight(new SolidBorder(1)).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)))
                                .addCell(getStatusCell(step.getReturnCode(), 1, 1).setTextAlignment(TextAlignment.RIGHT));
                        document.add(tableTmp.setMarginLeft(0).setMarginTop(20));

                        document.add(new Paragraph()
                                .add(getTextFromString(String.valueOf(step.getReturnMessage()), 10, false))
                                .setMarginLeft(0)
                        );

                        // Add images is exist
                        tableTmp = getImageTable(step.getFileList(), mediaPath);
                        if (tableTmp != null) {
                            document.add(tableTmp.setMarginLeft(0));
                        }

                    }

                    for (TestCaseStepActionExecution action : step.getTestCaseStepActionExecutionList()) {
                        if (!TestCaseExecution.CONTROLSTATUS_NE.equals(action.getReturnCode())) {
                            tableTmp = new Table(new float[]{500, 20})
                                    .addCell(new Cell().add(new Paragraph().add(getTextFromString(getElementDescription(action.getDescription(), action.getSort(), 0, action.getTest()), 12, true).setTextAlignment(TextAlignment.LEFT)))
                                            .setBorder(Border.NO_BORDER).setBorderLeft(new SolidBorder(ColorConstants.BLUE, 3)).setBorderRight(new SolidBorder(1)).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)))
                                    .addCell(getStatusCell(action.getReturnCode(), 1, 1).setTextAlignment(TextAlignment.RIGHT));
                            document.add(tableTmp.setMarginLeft(20));

                            document.add(new Paragraph()
                                    .add(getTextFromString(String.valueOf(action.getReturnMessage()), 10, false))
                                    .setMarginLeft(20)
                            );

                            // Add images is exist
                            tableTmp = getImageTable(action.getFileList(), mediaPath);
                            if (tableTmp != null) {
                                document.add(tableTmp.setMarginLeft(20));
                            }

                        }

                        for (TestCaseStepActionControlExecution control : action.getTestCaseStepActionControlExecutionList()) {

                            if (!TestCaseExecution.CONTROLSTATUS_NE.equals(control.getReturnCode())) {
                                tableTmp = new Table(new float[]{500, 20})
                                        .addCell(new Cell().add(new Paragraph().add(getTextFromString(getElementDescription(control.getDescription(), control.getSort(), 0, control.getTest()), 12, true).setTextAlignment(TextAlignment.LEFT)))
                                                .setBorder(Border.NO_BORDER).setBorderLeft(new SolidBorder(ColorConstants.GREEN, 3)).setBorderRight(new SolidBorder(1)).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)))
                                        .addCell(getStatusCell(control.getReturnCode(), 1, 1).setTextAlignment(TextAlignment.RIGHT));
                                document.add(tableTmp.setMarginLeft(40));

                                document.add(new Paragraph()
                                        .add(getTextFromString(String.valueOf(control.getReturnMessage()), 10, false))
                                        .setMarginLeft(40)
                                );

                                // Add images is exist
                                tableTmp = getImageTable(control.getFileList(), mediaPath);
                                if (tableTmp != null) {
                                    document.add(tableTmp.setMarginLeft(40));
                                }
                            }

                        }

                    }

                }

                // Adding area break to the PDF
                if (i++ < listOfExecutions.size()) {
                    document.add(aB);
                }
            }

            // Closing the document
            LOG.info("Ending to generate PDF Report on :" + dest);
            return dest;
        } catch (ParseException | CerberusException ex) {
            LOG.error(ex, ex);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
        return null;
    }

    private String getElementDescription(String desc, int sort, int seq, String test) {
        if (Test.TEST_PRETESTING.equals(test)) {
            return "[PRE] " + desc;
        } else if (Test.TEST_POSTTESTING.equals(test)) {
            return "[POST] " + desc;
        } else if (seq > 0) {
            return "[" + sort + "." + seq + "] " + desc;
        } else {
            return "[" + sort + "] " + desc;
        }
    }

    @Override
    public String addHeaderAndFooter(String pdfFilePathSrc, String destinationFile, Tag tag, Date today) throws FileNotFoundException {
        try {
            LOG.info("Starting to add Headers on PDF Report :" + pdfFilePathSrc + " To : " + destinationFile);

            DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_REPORT);

            // Adding Headers and Footers
            Paragraph header = new Paragraph("Campaign Execution Report - " + tag.getTag())
                    .setFontSize(7).setItalic();
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(pdfFilePathSrc), new PdfWriter(destinationFile));
            Document doc = new Document(pdfDoc);
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {

                Rectangle pageSize = pdfDoc.getPage(i).getPageSize();
                float x = 20;
                float y = pageSize.getTop() - 20;

                // Header insert
                if (i != 1) {
                    doc.showTextAligned(header, x, y, i, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
                }

                // Footer insert
                Paragraph footer = new Paragraph("Page " + i + " / " + pdfDoc.getNumberOfPages())
                        .setFontSize(7).setItalic();
                Paragraph footerLeft = new Paragraph("(C) Cerberus Testing - " + df.format(today))
                        .setFontSize(7).setItalic();

                x = pageSize.getRight() - 60;
//                x = 20;
                y = pageSize.getBottom() + 20;
                doc.showTextAligned(footer, x, y, i, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
                doc.showTextAligned(footerLeft, 20, y, i, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);

            }
            doc.close();
            LOG.info("Ended to add Headers on PDF Report :" + pdfFilePathSrc + " To : " + destinationFile);

        } catch (IOException ex) {
            LOG.error(ex, ex);
        }
        return destinationFile;

    }

    private Text getTextFromString(String text, int fontSize, boolean isBold) {
        if (isBold) {
            return new Text(text).setBold().setFontSize(fontSize);

        } else {
            return new Text(text).setFontSize(fontSize);

        }
    }

    private Table getImageTable(List<TestCaseExecutionFile> fileList, String mediaPath) {
        Table tableTmp = null;
        boolean imageInserted = false;
        // We count the nb of images in the file list.
        int nbImages = 0;
        for (TestCaseExecutionFile exeFile : fileList) {
            if (exeFile.isImage() && !exeFile.getFileDesc().contains("Picture")) {
                nbImages++;
            }
        }

        // If there is at least 1 image in the list
        if (nbImages > 0) {
            tableTmp = new Table(new float[]{150, 500});

            imageInserted = false;
            for (TestCaseExecutionFile controlFile : fileList) {

                if (controlFile.isImage() && !imageInserted) {
                    // Load screenshots to pdf.
                    ImageData imageData;
                    try {
                        File f = new File(mediaPath + controlFile.getFileName());
                        imageInserted = true;
                        if (f.exists()) {
                            imageData = ImageDataFactory.create(mediaPath + controlFile.getFileName());
                            Image image = new Image(imageData).scaleToFit(500, 200);
                            tableTmp.addCell(new Cell().add(new Paragraph().add(getTextFromString(controlFile.getFileDesc(), 7, false)).setTextAlignment(TextAlignment.LEFT))
                                    .setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE))
                                    .addCell(new Cell().add(image.setBorder(Border.NO_BORDER).setHorizontalAlignment(HorizontalAlignment.RIGHT)).setBorder(Border.NO_BORDER));

                        } else {
                            tableTmp.addCell(new Cell().add(new Paragraph().add(getTextFromString(controlFile.getFileDesc(), 7, false)).setTextAlignment(TextAlignment.LEFT))
                                    .setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE))
                                    .addCell(new Cell().add(new Paragraph().add(getTextFromString("File no longuer exist !!!", 7, false)).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                        }
                    } catch (MalformedURLException ex) {
                        LOG.error(ex, ex);
                    } catch (Exception ex) {
                        LOG.error(ex, ex);
                    }
                }

            }

        }

        return tableTmp;
    }

    class SortExecution implements Comparator<TestCaseExecution> {
        // Used for sorting in ascending order of 
        // Label name. 

        @Override
        public int compare(TestCaseExecution a, TestCaseExecution b) {
            if (a != null && b != null) {
//                int aPrio = a.getTestCasePriority();
//                if (a.getTestCasePriority() < 1 || a.getTestCasePriority() > 5) {
//                    aPrio = 999 + a.getTestCasePriority();
//                }
//                int bPrio = b.getTestCasePriority();
//                if (b.getTestCasePriority() < 1 || b.getTestCasePriority() > 5) {
//                    bPrio = 999 + b.getTestCasePriority();
//                }

//                if (aPrio == bPrio) {
                if (a.getTest().equals(b.getTest())) {
                    if (a.getTestCase().equals(b.getTestCase())) {
                        if (a.getEnvironment().equals(b.getEnvironment())) {
                            if (a.getCountry().equals(b.getCountry())) {
                                return a.getRobotDecli().compareToIgnoreCase(b.getRobotDecli());
                            } else {
                                return a.getCountry().compareToIgnoreCase(b.getCountry());
                            }
                        } else {
                            return a.getEnvironment().compareToIgnoreCase(b.getEnvironment());
                        }
                    } else {
                        return a.getTestCase().compareToIgnoreCase(b.getTestCase());
                    }
                } else {
                    return a.getTest().compareToIgnoreCase(b.getTest());
                }
//                } else {
//                    return aPrio - bPrio;
//                }
            } else {
                return 1;
            }
        }
    }

    private Cell getHeaderCell(String text) {
        return new Cell().add(new Paragraph(text)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(8).setTextAlignment(TextAlignment.CENTER);
    }

    private Cell getStatusCell(String status, int rowspan, int colspan) {
        String coloHex = getColor(status);

        Cell cellRes = new Cell(rowspan, colspan)
                .add(new Paragraph(status))
                .setBackgroundColor(new DeviceRgb(decodeColor(coloHex, "R"), decodeColor(coloHex, "G"), decodeColor(coloHex, "B")))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER);

        return cellRes;
    }

    private Cell getContentCell(String text) {
        Cell cellRes = new Cell(1, 1)
                .add(new Paragraph(text));
        return cellRes;
    }

    private int decodeColor(String hexColor, String indexRGB) {
        String hexValue = hexColor.replace("#", "0x");

        int i = Integer.decode(hexValue).intValue();
        switch (indexRGB) {
            case "R":
                return (i >> 16) & 0xFF;
            case "G":
                return (i >> 8) & 0xFF;
            case "B":
                return i & 0xFF;
        }

        return 0;
    }

    private String getColor(String controlStatus) {
        String color = null;

        if ("OK".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_OK_COL_EXT;
        } else if ("KO".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_KO_COL_EXT;
        } else if ("FA".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_FA_COL_EXT;
        } else if ("CA".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_CA_COL_EXT;
        } else if ("NA".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_NA_COL_EXT;
        } else if ("NE".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_NE_COL_EXT;
        } else if ("WE".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_WE_COL_EXT;
        } else if ("PE".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_PE_COL_EXT;
        } else if ("QU".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_QU_COL_EXT;
        } else if ("QE".equals(controlStatus)) {
            color = TestCaseExecution.CONTROLSTATUS_QE_COL_EXT;
        } else {
            color = "#000000";
        }
        return color;
    }

}
