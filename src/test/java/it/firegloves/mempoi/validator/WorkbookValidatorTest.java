/**
 * created by firegloves
 */

package it.firegloves.mempoi.validator;

import it.firegloves.mempoi.exception.MempoiException;
import it.firegloves.mempoi.util.Errors;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WorkbookValidatorTest {

    private WorkbookValidator areaReferenceValidator = new WorkbookValidator();
    private String error = "Error";

    @Test
    public void validateWorkbookTypeAndThrow_willSuccess() {

        Workbook[] workbooks = { new XSSFWorkbook(), new HSSFWorkbook(), new SXSSFWorkbook()};
        Arrays.asList(workbooks).forEach(wb -> this.areaReferenceValidator.validateWorkbookTypeAndThrow(wb, wb.getClass(), error));
    }

    @Test
    public void validateWorkbookTypeAndThrow_differentClass_willFail() {

        Map<Workbook, List<Class<? extends Workbook>>> map = new HashMap<>();
        map.put(new XSSFWorkbook(), Arrays.asList(HSSFWorkbook.class, SXSSFWorkbook.class));
        map.put(new HSSFWorkbook(), Arrays.asList(XSSFWorkbook.class, SXSSFWorkbook.class));
        map.put(new SXSSFWorkbook(), Arrays.asList(HSSFWorkbook.class, XSSFWorkbook.class));

        map.keySet().forEach(wb -> {

            map.get(wb).forEach(wbClazz -> {

                try {
                    this.areaReferenceValidator.validateWorkbookTypeAndThrow(wb, wb.getClass(), error);

                } catch (MempoiException e) {

                    assertEquals(String.format("Validating %s against class %s", wb.getClass().getName(), wbClazz.getName()),
                            error,
                            e.getMessage());
                }
            });
        });
    }


    @Test(expected = MempoiException.class)
    public void validateWorkbookTypeAndThrow_withNullWorkbook_willFail() {

        this.areaReferenceValidator.validateWorkbookTypeAndThrow(null, HSSFWorkbook.class, error);
    }

    @Test(expected = MempoiException.class)
    public void validateWorkbookTypeAndThrow_withNullClass_willFail() {

        this.areaReferenceValidator.validateWorkbookTypeAndThrow(new HSSFWorkbook(), null, error);
    }

    @Test(expected = MempoiException.class)
    public void validateWorkbookTypeAndThrow_withNullError_willFail() {

        this.areaReferenceValidator.validateWorkbookTypeAndThrow(new HSSFWorkbook(), XSSFWorkbook.class, null);
    }
}
