package it.firegloves.mempoi.functional;

import it.firegloves.mempoi.MemPOI;
import it.firegloves.mempoi.builder.MempoiBuilder;
import it.firegloves.mempoi.domain.MempoiSheet;
import it.firegloves.mempoi.domain.footer.NumberSumSubFooter;
import it.firegloves.mempoi.styles.template.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class HueStyleTemplateTest extends FunctionalBaseTest {

    @Test
    public void testWithFileAndStandardTemplate() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_with_file_and_standard_template.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(new MempoiSheet(prepStmt))
                    .setStyleTemplate(new StandardStyleTemplate())
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testWithFileAndSummerTemplate() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_file_and_summer_template.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(new MempoiSheet(prepStmt))
                    .setStyleTemplate(new SummerStyleTemplate())
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWithFileAndAquaTemplate() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_file_and_aqua_template.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(new MempoiSheet(prepStmt))
                    .setStyleTemplate(new AquaStyleTemplate())
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWithFileAndForestTemplate() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_file_and_forest_template.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(new MempoiSheet(prepStmt))
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setStyleTemplate(new ForestStyleTemplate())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWithFileAndForestTemplateOverriden() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_file_and_forest_template_overriden.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(new MempoiSheet(prepStmt))
                    .setStyleTemplate(new ForestStyleTemplate())
                    .setHeaderCellStyle(headerCellStyle)
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testWithFileAndForestTemplateOverridenOnSheetstyler() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_with_file_and_forest_template_overriden_on_sheetstyler.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        numberCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        MempoiSheet sheet = new MempoiSheet(prepStmt);
        sheet.setNumberCellStyle(numberCellStyle);

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(sheet)
                    .setStyleTemplate(new ForestStyleTemplate())
                    .setHeaderCellStyle(headerCellStyle)
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testWithFileAndStoneTemplate() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_with_file_and_stone_template.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(new MempoiSheet(prepStmt))
                    .setStyleTemplate(new StoneStyleTemplate())
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWithFileAndRoseTemplate() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_with_file_and_rose_template.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(new MempoiSheet(prepStmt))
                    .setStyleTemplate(new RoseStyleTemplate())
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testWithFileAndPurpleTemplate() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_with_file_and_purple_template.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(new MempoiSheet(prepStmt))
                    .setStyleTemplate(new PurpleStyleTemplate())
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testWithFileAndPanegiriconTemplate() {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_with_file_and_panegiricon_template.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .addMempoiSheet(new MempoiSheet(prepStmt))
                    .setStyleTemplate(new PanegiriconStyleTemplate())
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWithFileAndMultipleSheetTemplates() throws SQLException {

        File fileDest = new File(this.outReportFolder.getAbsolutePath(), "test_with_file_and_multiple_sheet_templates.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        MempoiSheet dogsSheet = new MempoiSheet(conn.prepareStatement("SELECT id, creation_date, dateTime, timeStamp AS STAMPONE, name, valid, usefulChar, decimalOne, bitTwo, doublone, floattone, interao, mediano, attempato, interuccio FROM mempoi.export_test"), "Dogs");
        dogsSheet.setStyleTemplate(new SummerStyleTemplate());

        CellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        numberCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        MempoiSheet catsheet = new MempoiSheet(prepStmt, "Cats");
        catsheet.setStyleTemplate(new ForestStyleTemplate());
        catsheet.setNumberCellStyle(numberCellStyle);

        List<MempoiSheet> sheetList = Arrays.asList(dogsSheet, catsheet);

        try {

            MemPOI memPOI = new MempoiBuilder()
                    .setDebug(true)
                    .setWorkbook(workbook)
                    .setFile(fileDest)
                    .setAdjustColumnWidth(true)
                    .setMempoiSheetList(sheetList)
//                    .setStyleTemplate(new PanegiriconStyleTemplate())     <----- it has no effects because for each sheet a template is specified
                    .setMempoiSubFooter(new NumberSumSubFooter())
                    .setEvaluateCellFormulas(true)
                    .build();

            CompletableFuture<String> fut = memPOI.prepareMempoiReportToFile();
            assertEquals("file name len === starting fileDest", fileDest.getAbsolutePath(), fut.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
