package cn.weihu.kol.util;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;

public class CustomCellWriteHandler implements CellWriteHandler {

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, WriteCellData<?> cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        cellStyle.setFillBackgroundColor(IndexedColors.WHITE.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(cellStyle);
    }
}
