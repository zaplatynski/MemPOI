package it.firegloves.mempoi.styles.template;

import it.firegloves.mempoi.styles.StandardDataFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

@Data
@Accessors(chain = true)
public abstract class HueStyleTemplate implements StyleTemplate {

    private short headerCellBgColorIndex = IndexedColors.CORAL.getIndex();
    private short headerFontColorIndex = IndexedColors.WHITE.getIndex();
    private short headerFontSizeInPoint = 16;
    private short commonCellBgColorIndex = IndexedColors.WHITE.getIndex();
    private short commonFontColorIndex = IndexedColors.BLACK.getIndex();
    private short borderColorIndex = IndexedColors.BLACK.getIndex();
    private short subFooterCellBgColorIndex = IndexedColors.CORAL.getIndex();
    private short subFooterFontColorIndex = IndexedColors.WHITE.getIndex();


    @Override
    public CellStyle getHeaderCellStyle(Workbook workbook) {

        CellStyle cellStyle = this.setGenericCellStyle(workbook, this.headerCellBgColorIndex, this.headerFontColorIndex, true, this.borderColorIndex);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        if (cellStyle instanceof XSSFCellStyle) {
            ((XSSFCellStyle)cellStyle).getFont().setFontHeightInPoints(headerFontSizeInPoint);
        } else {
            ((HSSFCellStyle)cellStyle).getFont(workbook).setFontHeight((short)(headerFontSizeInPoint*20));
        }

        cellStyle.setWrapText(true);
        return cellStyle;
    }

    @Override
    public CellStyle getSubfooterCellStyle(Workbook workbook) {

        return this.setGenericCellStyle(workbook, this.subFooterCellBgColorIndex, this.subFooterFontColorIndex, true, this.borderColorIndex);
    }

    @Override
    public CellStyle getDateCellStyle(Workbook workbook) {

        CellStyle cellStyle = this.setGenericCellStyle(workbook, this.commonCellBgColorIndex, this.commonFontColorIndex, false, this.borderColorIndex);
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(StandardDataFormat.STANDARD_DATE_FORMAT.getFormat()));
        return cellStyle;
    }

    @Override
    public CellStyle getDatetimeCellStyle(Workbook workbook) {

        CellStyle cellStyle = this.setGenericCellStyle(workbook, this.commonCellBgColorIndex, this.commonFontColorIndex, false, this.borderColorIndex);
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(StandardDataFormat.STANDARD_DATETIME_FORMAT.getFormat()));
        return cellStyle;
    }

    @Override
    public CellStyle getIntegerCellStyle(Workbook workbook) {

        CellStyle cellStyle = this.setGenericCellStyle(workbook, this.commonCellBgColorIndex, this.commonFontColorIndex, false, this.borderColorIndex);
//        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(StandardDataFormat.STANDARD_NUMBER_FORMAT.getFormat()));
        return cellStyle;
    }

    @Override
    public CellStyle getFloatingPointCellStyle(Workbook workbook) {

        CellStyle cellStyle = this.setGenericCellStyle(workbook, this.commonCellBgColorIndex, this.commonFontColorIndex, false, this.borderColorIndex);
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(StandardDataFormat.STANDARD_FLOATING_NUMBER_FORMAT.getFormat()));
        return cellStyle;
    }


    @Override
    public CellStyle getCommonDataCellStyle(Workbook workbook) {

        return this.setGenericCellStyle(workbook, this.commonCellBgColorIndex, this.commonFontColorIndex, false, this.borderColorIndex);
    }


    /**
     * create the generic cellstyle
     * @param workbook
     * @param cellBgColorIndex
     * @param fontColorIndex
     * @param bold
     * @param borderColor
     * @return
     */
    private CellStyle setGenericCellStyle(Workbook workbook, short cellBgColorIndex, short fontColorIndex, boolean bold, short borderColor) {

        CellStyle cellStyle = workbook.createCellStyle();

        this.addBgCellColor(cellStyle, cellBgColorIndex);

        this.addFontStyle(workbook, cellStyle, fontColorIndex, bold);

        this.addCellBorders(cellStyle, borderColor);

        return cellStyle;
    }
}
