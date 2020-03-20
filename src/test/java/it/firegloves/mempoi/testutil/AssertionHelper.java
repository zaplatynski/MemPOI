/**
 * created by firegloves
 */

package it.firegloves.mempoi.testutil;

import it.firegloves.mempoi.domain.MempoiTable;
import it.firegloves.mempoi.domain.pivottable.MempoiPivotTable;
import it.firegloves.mempoi.styles.MempoiStyler;
import it.firegloves.mempoi.styles.template.StyleTemplate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

public class AssertionHelper {

    /**
     * validate two CellStyle checking all properties managed by MemPOI
     *
     * @param cellStyle         the cell's CellStyle
     * @param expectedCellStyle the expected CellStyle
     */
    public static void validateCellStyle(CellStyle cellStyle, CellStyle expectedCellStyle) {

        if (null != expectedCellStyle) {

            assertEquals(expectedCellStyle.getFillForegroundColor(), cellStyle.getFillForegroundColor());
            assertEquals(expectedCellStyle.getFillPattern(), cellStyle.getFillPattern());

            assertEquals(expectedCellStyle.getBorderBottom(), cellStyle.getBorderBottom());
            assertEquals(expectedCellStyle.getBottomBorderColor(), cellStyle.getBottomBorderColor());
            assertEquals(expectedCellStyle.getBorderLeft(), cellStyle.getBorderLeft());
            assertEquals(expectedCellStyle.getLeftBorderColor(), cellStyle.getLeftBorderColor());
            assertEquals(expectedCellStyle.getBorderRight(), cellStyle.getBorderRight());
            assertEquals(expectedCellStyle.getRightBorderColor(), cellStyle.getRightBorderColor());
            assertEquals(expectedCellStyle.getBorderTop(), cellStyle.getBorderTop());
            assertEquals(expectedCellStyle.getTopBorderColor(), cellStyle.getTopBorderColor());

            if (expectedCellStyle instanceof XSSFCellStyle) {
                assertEquals(((XSSFCellStyle) cellStyle).getFont().getFontHeightInPoints(), ((XSSFCellStyle) expectedCellStyle).getFont().getFontHeightInPoints());
                assertEquals(((XSSFCellStyle) cellStyle).getFont().getColor(), ((XSSFCellStyle) expectedCellStyle).getFont().getColor());
                assertEquals(((XSSFCellStyle) cellStyle).getFont().getBold(), ((XSSFCellStyle) expectedCellStyle).getFont().getBold());
            }
        }
    }


    /**
     * makes some assertion comparing a MempoiStyler (with its cell styles) and a StyleTemplate (with its cell styles)
     *
     * @param mempoiStyler
     * @param template
     * @param wb
     */
    public static void validateTemplateAndStyler(MempoiStyler mempoiStyler, StyleTemplate template, Workbook wb) {

        AssertionHelper.validateCellStyle(template.getCommonDataCellStyle(wb), mempoiStyler.getCommonDataCellStyle());
        AssertionHelper.validateCellStyle(template.getNumberCellStyle(wb), mempoiStyler.getCommonDataCellStyle());
        AssertionHelper.validateCellStyle(template.getDateCellStyle(wb), mempoiStyler.getDateCellStyle());
        AssertionHelper.validateCellStyle(template.getDatetimeCellStyle(wb), mempoiStyler.getDatetimeCellStyle());
        AssertionHelper.validateCellStyle(template.getHeaderCellStyle(wb), mempoiStyler.getHeaderCellStyle());
        AssertionHelper.validateCellStyle(template.getSubfooterCellStyle(wb), mempoiStyler.getSubFooterCellStyle());
    }

    /**
     * validates the XSSFTable contained in the received XSSFSheet (assuming that the table reflects TestHelper data)
     *
     * @param sheet the XSSFSheet containing the table to validate
     */
    public static void validateTable(XSSFSheet sheet) {

        XSSFTable table = sheet.getTables().get(0);

        assertEquals(TestHelper.AREA_REFERENCE, table.getArea().formatAsString());
        assertEquals(TestHelper.TABLE_NAME, table.getName());
        assertEquals(TestHelper.DISPLAY_TABLE_NAME, table.getDisplayName());
        assertEquals(5, table.getColumnCount());
        assertTrue(table.getCTTable().isSetAutoFilter());
        assertEquals(0, table.getStartColIndex());
        assertEquals(4, table.getEndColIndex());
        assertEquals(0, table.getStartRowIndex());
        assertEquals(4, table.getEndRowIndex());
    }


    /**
     * validates the MempoiTable assuming that the table reflects TestHelper data
     *
     * @param wb the Workbook used to create the MempoiTable
     * @param mempoiTable the MempoiTable to validate
     */
    public static void validateMempoiTable(Workbook wb, MempoiTable mempoiTable) {

        assertEquals(TestHelper.TABLE_NAME, mempoiTable.getTableName());
        assertEquals(TestHelper.DISPLAY_TABLE_NAME, mempoiTable.getDisplayTableName());
        assertEquals(TestHelper.AREA_REFERENCE, mempoiTable.getAreaReference());
        assertEquals(wb, mempoiTable.getWorkbook());
    }

    /**
     * validates the MempoiPivotTable assuming that the table reflects TestHelper data
     *
     * @param wb the Workbook used to create the MempoiTable
     * @param mempoiPivotTable the MempoiTable to validate
     */
    public static void validateMempoiPivotTable(Workbook wb, MempoiPivotTable mempoiPivotTable) {

        assertEquals(wb, mempoiPivotTable.getWorkbook());
        assertEquals(TestHelper.POSITION, mempoiPivotTable.getPosition());
        assertNotNull(mempoiPivotTable.getSource().getMempoiSheet());
        assertEquals(TestHelper.AREA_REFERENCE, mempoiPivotTable.getSource().getAreaReference().formatAsString());

        assertArrayEquals(TestHelper.ROW_LABEL_COLUMNS.toArray(), mempoiPivotTable.getRowLabelColumns().toArray());
        assertArrayEquals(TestHelper.REPORT_FILTER_COLUMNS.toArray(), mempoiPivotTable.getReportFilterColumns().toArray());

        validateColumnLabelColumns(TestHelper.SUM_COLS_LABEL_COLUMNS, mempoiPivotTable.getColumnLabelColumns().get(DataConsolidateFunction.SUM));
        validateColumnLabelColumns(TestHelper.AVERAGE_COLS_LABEL_COLUMNS, mempoiPivotTable.getColumnLabelColumns().get(DataConsolidateFunction.AVERAGE));
    }


    /**
     * validates the 2 List
     * @param expected
     * @param actual
     */
    public static void validateColumnLabelColumns(List<String> expected, List<String> actual) {

        IntStream.range(0, expected.size())
                .forEach(i -> assertEquals(expected.get(i), actual.get(i)));
    }


    /**
     * asserts that the received XSSFPivotTable reflects the data contained in the TestHelper.class
     * @param pivotTable
     */
    public static void assertPivotTable(XSSFPivotTable pivotTable) {

        // RowLabel
        List<Integer> rowLabelIndexes = TestHelper.getRowLabelIndexesForAssertion();
        pivotTable.getRowLabelColumns().forEach(i -> assertTrue(rowLabelIndexes.contains(i)));

        // ReportFilter
        List<Integer> rowFilterIndexes = TestHelper.getRowFilterIndexesForAssertion();
        List<CTPageField> pageFieldList = pivotTable.getCTPivotTableDefinition().getPageFields().getPageFieldList();

        assertEquals(rowFilterIndexes.size(), pageFieldList.size());
        pageFieldList.forEach(ctPageField -> assertTrue(rowFilterIndexes.contains(ctPageField.getFld())));

        // ColumnLabel
        EnumMap<DataConsolidateFunction, List<Long>> columnLabelColumns = TestHelper.getColumnLabelColumnsForAssertion();
        List<CTDataField> dataFieldList = pivotTable.getCTPivotTableDefinition().getDataFields().getDataFieldList();

        dataFieldList.forEach(ctDataField -> {
            DataConsolidateFunction key = DataConsolidateFunction.valueOf(ctDataField.getSubtotal().toString().toUpperCase());
            assertTrue(columnLabelColumns.get(key).contains(ctDataField.getFld()));
        });
    }
}
