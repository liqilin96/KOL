package cn.weihu.kol.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author lql
 * @date 2021/8/9 14:40
 * Description： ExcelExport
 */
public class ExcelExport {


    /**
     * description: 导出数据excel
     *
     * @param sheetName
     * @param headers
     * @param dataList
     * @param destFile
     * @return void
     */
    public static void export(String sheetName, String[] headers, List<List<Object>> dataList, File destFile) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        createSheet(sheetName, headers, dataList, workbook);
        workbook.write(new FileOutputStream(destFile));
    }


    /**
     * description: 导出excel --- 支持web
     *
     * @param sheetName sheet表名字
     * @param headers   表头
     * @param dataList  表数据
     * @param fileName  导出文件名
     * @param response
     * @return void
     */
    public static void export(String sheetName, String[] headers, List<List<Object>> dataList, String fileName
            , HttpServletResponse response) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        createSheet(sheetName, headers, dataList, workbook);
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
        // 删除临时文件
        workbook.dispose();
    }

    /**
     * description: 创建sheet表格
     *
     * @param sheetName 表sheet 名字
     * @param headers   表头
     * @param dataList  表数据
     * @param wb
     * @return void
     */
    public static void createSheet(String sheetName, String[] headers, List<List<Object>> dataList, SXSSFWorkbook wb) {
        Sheet sheet = wb.createSheet(sheetName);
        // 设置表头和单元格格式
        CellStyle headStyle = setHeaderStyle(wb);
        CellStyle bodyStyle = setBodyStyle(wb);
        // 创建表头和单元格数据
        createHeader(headers, sheet, headStyle);
        createBody(dataList, sheet, bodyStyle);
    }

    /**
     * description: 创建表头
     *
     * @param headers
     * @param sheet
     * @param headStyle
     * @return void
     */
    private static void createHeader(String[] headers, Sheet sheet, CellStyle headStyle) {
        Row row = sheet.createRow(0);

        row.setHeightInPoints(16F);
        for (int i = 0; i < headers.length; i++) {
            // 创建单元格
            Cell cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
//            sheet.trackAllColumnsForAutoSizing();
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * description: 表格中填充数据
     *
     * @param dataList
     * @param sheet
     * @param bodyStyle
     * @return void
     */
    private static void createBody(List<List<Object>> dataList, Sheet sheet, CellStyle bodyStyle) {
        for (int i = 0; i < dataList.size(); i++) {
            // 从第二行开始，第一行做表头
            Row row = sheet.createRow(i + 1);
            List<Object> rowList = dataList.get(i);
            for (int j = 0; j < rowList.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(bodyStyle);
                XSSFRichTextString text = new XSSFRichTextString(rowList.get(j) == null ? "null" : rowList.get(j).toString());
                cell.setCellValue(text);
//                sheet.trackAllColumnsForAutoSizing();
                sheet.autoSizeColumn(i);
            }
        }
    }

    /**
     * description: 设置单元格内容样式
     *
     * @param wb
     * @return HSSFCellStyle
     * @version v1.0
     */
    private static CellStyle setBodyStyle(SXSSFWorkbook wb) {
        // 设置表格单元格格式
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        // 设置字体格式
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setFont(font);
        return style;
    }

    /**
     * description: 设置表头样式
     *
     * @param wb
     * @return
     * @return HSSFCellStyle
     */
    private static CellStyle setHeaderStyle(SXSSFWorkbook wb) {
        // 设置表格单元格格式
        CellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        // 设置字体格式
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        return style;
    }


    /**
     * @param file
     * @param englishHeader 英文头
     * @return 读取数据封装List
     */
    public static List<Map<String, Object>> ExcelReadMap(MultipartFile file, List<String> englishHeader) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
//            取得表头
            Row rr = sheet.getRow(0);
            String[] arrHead = englishHeader.toArray(new String[englishHeader.size()]);
//            保存表头
//            String[] arrHead = new String[rr.getPhysicalNumberOfCells()];
//            int j = 0;
//            for (Cell ce : rr) {
//                int roC = rr.getPhysicalNumberOfCells();
//                int ceC = ce.getColumnIndex();
//                arrHead[j] = ce.getStringCellValue();
//                j++;
//            }

//            构建list对象集合
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                Map<String, Object> map = new HashMap<String, Object>();
                for (int k = 0; k < arrHead.length; k++) {
//                    获取行数据
                    Cell cell = row.getCell(k);
                    if (cell == null) {
                        String nu = "";
                        map.put(arrHead[k], nu);
                    } else {
                        //获取值并自己格式化
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:// 字符串型
                                map.put(arrHead[k], cell.getStringCellValue());
//                            System.out.println(cell.getRichStringCellValue().getString());
                                break;
                            case Cell.CELL_TYPE_NUMERIC:// 数值型
                                if (DateUtil.isCellDateFormatted(cell)) { // 如果是date类型则 ，获取该cell的date值
                                    Date date = cell.getDateCellValue();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    String da = format.format(date);
                                    map.put(arrHead[k], da);
//                                System.out.println(cell.getDateCellValue());
                                } else {// 纯数字
                                    if (("" + cell.getNumericCellValue()).indexOf("E") != -1 || ("" + cell.getNumericCellValue()).indexOf("e") != -1 || ("" + cell.getNumericCellValue()).indexOf("+") != -1) {
                                        BigDecimal bd = new BigDecimal("" + cell.getNumericCellValue());
                                        map.put(arrHead[k], bd.toString());
                                    } else {
                                        int number = (int) cell.getNumericCellValue();
                                        map.put(arrHead[k], number);
                                    }

//                                System.out.println(cell.getNumericCellValue());
                                }
                                break;
                            case Cell.CELL_TYPE_BLANK:// 空值
                                String nu = "";
                                map.put(arrHead[k], nu);
                                break;
                        }
                    }

                }
                list.add(map);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
