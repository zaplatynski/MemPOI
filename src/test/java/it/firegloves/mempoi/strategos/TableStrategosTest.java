package it.firegloves.mempoi.strategos;

import it.firegloves.mempoi.builder.MempoiSheetBuilder;
import it.firegloves.mempoi.builder.MempoiTableBuilder;
import it.firegloves.mempoi.config.WorkbookConfig;
import it.firegloves.mempoi.domain.MempoiSheet;
import it.firegloves.mempoi.domain.MempoiTable;
import it.firegloves.mempoi.domain.footer.MempoiFooter;
import it.firegloves.mempoi.exception.MempoiException;
import it.firegloves.mempoi.testutil.PrivateAccessHelper;
import it.firegloves.mempoi.testutil.TestHelper;
import it.firegloves.mempoi.util.Errors;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TableStrategosTest {

    private XSSFWorkbook wb;
    private TableStrategos tableStrategos;
    private XSSFSheet sheet;
    private MempoiSheet mempoiSheet;

    @Mock
    private PreparedStatement prepStmt;

    @Before
    public void prepare() {
        MockitoAnnotations.initMocks(this);

        this.wb = new XSSFWorkbook();
        this.tableStrategos = new TableStrategos(new WorkbookConfig().setWorkbook(wb));

        this.sheet = wb.createSheet();
        this.mempoiSheet = new MempoiSheet(prepStmt)
                .setColumnList(TestHelper.getMempoiColumnList(wb))
                .setSheet(sheet);
    }



    /******************************************************************************************************************
     *                          setColumnIds
     *****************************************************************************************************************/


    @Test
    public void tableColsIdsAreSettedSequentially() throws Exception {

        XSSFTable table = sheet.createTable(new AreaReference(TestHelper.AREA_REFERENCE, wb.getSpreadsheetVersion()));

        Method setTableColumnsMethod = PrivateAccessHelper.getAccessibleMethod(tableStrategos, "setTableColumns", XSSFTable.class, MempoiSheet.class);
        setTableColumnsMethod.invoke(tableStrategos, table, this.mempoiSheet);

        this.validateTableColumns(table);
    }


    /******************************************************************************************************************
     *                          addTable
     *****************************************************************************************************************/


    @Test
    public void addsAnExcelTable() throws Exception {



        MempoiTable mempoiTable = TestHelper.getTestMempoiTable(wb);

        Method addTableMethod = PrivateAccessHelper.getAccessibleMethod(tableStrategos, "addTable", MempoiSheet.class, MempoiTable.class);
        addTableMethod.invoke(tableStrategos, mempoiSheet, mempoiTable);

        XSSFTable table = sheet.getTables().get(0);

        assertEquals(1, sheet.getTables().size());
        this.validateTableColumns(table);
        assertTrue(table.getCTTable().isSetAutoFilter());
        assertEquals(TestHelper.TABLE_NAME, table.getCTTable().getName());
        assertEquals(TestHelper.DISPLAY_TABLE_NAME, table.getCTTable().getDisplayName());
    }


    /******************************************************************************************************************
     *                          manageMempoiTable
     *****************************************************************************************************************/

    @Test
    public void manageMempoiTable() {

        XSSFSheet sheet = wb.createSheet();

        MempoiSheet mempoiSheet = MempoiSheetBuilder.aMempoiSheet()
                .withPrepStmt(prepStmt)
                .withMempoiTableBuilder(TestHelper.getTestMempoiTableBuilder(wb))
                .build()
                .setSheet(sheet);
        mempoiSheet.setColumnList(TestHelper.getMempoiColumnList(wb));

        tableStrategos.manageMempoiTable(mempoiSheet, TestHelper.getAreaReference(wb));

        XSSFTable table = sheet.getTables().get(0);

        assertEquals(1, sheet.getTables().size());
        this.validateTableColumns(table);
        assertTrue(table.getCTTable().isSetAutoFilter());
        assertEquals(TestHelper.TABLE_NAME, table.getCTTable().getName());
        assertEquals(TestHelper.DISPLAY_TABLE_NAME, table.getCTTable().getDisplayName());
    }


    @Test
    public void manageMempoiTable_withSheetNotOfTypeXSSFSheet_shouldFail() throws Exception {

        Arrays.asList(SXSSFWorkbook.class, HSSFWorkbook.class)
                .forEach(wbTypeClass -> {

                    Constructor<? extends Workbook> constructor;
                    Workbook workbook;
                    try {
                        constructor = wbTypeClass.getConstructor();
                        workbook = constructor.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }

                    Sheet sheet = workbook.createSheet();
                    MempoiSheet mempoiSheet = MempoiSheetBuilder.aMempoiSheet()
                            .withPrepStmt(prepStmt)
                            .withMempoiTableBuilder(TestHelper.getTestMempoiTableBuilder(wb))
                            .build();

                    try {
                        tableStrategos.manageMempoiTable(mempoiSheet, TestHelper.getAreaReference(wb));
                    } catch (Exception e) {
                        assertTrue(e instanceof MempoiException);
                        assertEquals(Errors.ERR_TABLE_SUPPORTS_ONLY_XSSF, e.getMessage());
                    }
                });
    }


    @Test
    public void manageTableTest_withoutMempoiTable() {

        XSSFSheet sheet = wb.createSheet();

        MempoiSheet mempoiSheet = TestHelper.getMempoiSheetBuilder(wb, prepStmt)
                .withMempoiTableBuilder(null)
                .build()
                .setSheet(sheet);

        this.tableStrategos.manageMempoiTable(mempoiSheet,TestHelper.getAreaReference(wb));

        assertEquals(0, ((XSSFSheet)mempoiSheet.getSheet()).getTables().size());
    }


    /******************************************************************************************************************
     *                          generic validations
     *****************************************************************************************************************/

    private void validateTableColumns(XSSFTable table) {

        List<CTTableColumn> tableColumnList = table.getCTTable().getTableColumns().getTableColumnList();

        assertEquals(TestHelper.MEMPOI_COLUMN_NAMES.length, tableColumnList.size());
        IntStream.range(0, tableColumnList.size()).forEachOrdered(i -> assertEquals(i + 1, tableColumnList.get(i).getId()));
    }
}
