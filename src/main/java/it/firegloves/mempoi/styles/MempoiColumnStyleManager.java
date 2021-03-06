/**
 * this class is responsible to map mempoi columns to the desired cell styles
 */

package it.firegloves.mempoi.styles;

import it.firegloves.mempoi.domain.EExportDataType;
import it.firegloves.mempoi.domain.MempoiColumn;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.*;

public class MempoiColumnStyleManager {

    private static final Set<EExportDataType> dateStylerTypes = EnumSet.of(EExportDataType.DATE);
    private static final Set<EExportDataType> datetimeStylerTypes = EnumSet.of(EExportDataType.TIME, EExportDataType.TIMESTAMP);
    private static final Set<EExportDataType> integerStylerTypes = EnumSet.of(EExportDataType.INT);
    private static final Set<EExportDataType> floatingPointStylerTypes = EnumSet.of(EExportDataType.DOUBLE, EExportDataType.FLOAT);
    private final List<Set<EExportDataType>> stylerTypesSet = Arrays.asList(dateStylerTypes, datetimeStylerTypes, integerStylerTypes, floatingPointStylerTypes);

    private HashMap<Set<EExportDataType>, CellStyle> cellStylerMap;

    private MempoiStyler reportStyler;


    public MempoiColumnStyleManager(MempoiStyler reportStyler) {
        this.reportStyler = reportStyler;
        this.initCellStyleMap();
    }

    /**
     * populates strategy pattern's map to apply a style to the cells
     */
    private void initCellStyleMap() {
        this.cellStylerMap = new HashMap<>();
        this.cellStylerMap.put(dateStylerTypes, this.reportStyler.getDateCellStyle());
        this.cellStylerMap.put(datetimeStylerTypes, this.reportStyler.getDatetimeCellStyle());
        this.cellStylerMap.put(floatingPointStylerTypes, this.reportStyler.getFloatingPointCellStyle());
        this.cellStylerMap.put(integerStylerTypes, this.reportStyler.getIntegerCellStyle());
    }


    /**
     * @param type
     * @return the corresponding EnumSet<EExportDataType>
     */
    private Set<EExportDataType> getEExportDataTypeEnumSet(EExportDataType type) {

        return stylerTypesSet.stream().filter(e -> e.contains(type)).findFirst().orElseGet(Collections::emptySet);
    }


    /**
     * populate the received MempoiColumn list with the corresponding CellStyle
     *
     * @param mempoiColumnList MempoiColumn list to which apply CellStyle
     */
    public void setMempoiColumnListStyler(List<MempoiColumn> mempoiColumnList) {

        mempoiColumnList.forEach(mc -> {

            // if column name == 'id' -> no style
            if (mc.getColumnName().equalsIgnoreCase("id")) {
                mc.setCellStyle(this.reportStyler.getIntegerCellStyle());
            } else {
                // else default style
                mc.setCellStyle(this.cellStylerMap.getOrDefault(this.getEExportDataTypeEnumSet(mc.getType()), this.reportStyler.getCommonDataCellStyle()));
            }
        });
    }


    /**
     * check if the received EExportDataType belongs to the numeric list
     * @param type the EExportDataType to check
     * @return true if the received EExportDataType belongs to the numeric list, false otherwise
     */
    public static boolean isNumericType(EExportDataType type) {
        return floatingPointStylerTypes.contains(type) || integerStylerTypes.contains(type);
    }
}