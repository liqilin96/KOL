package cn.weihu.kol.util;

import cn.weihu.kol.biz.bo.WorkOrderDataBo;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lql
 * @date 2021/11/16 9:55
 * Description：
 */
@Slf4j
public class EasyExcelUtil {

    public static List<Object> readExcelOnlySheet1(InputStream excelInputStream) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader   excelReader   = getReader(excelInputStream, excelListener);
        if(excelReader == null) {
            return new ArrayList<>();
        }
        List<ReadSheet> readSheetList = excelReader.excelExecutor().sheetList();
        for(ReadSheet readSheet : readSheetList) {
            //只读sheet 1
            if(readSheet.getSheetNo() != null && readSheet.getSheetNo() == 1) {
                excelReader.read(readSheet);
            }
        }
        excelReader.finish();
        return excelListener.getDataList();
    }


    public static List<Object> readExcel(InputStream excelInputStream) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader   excelReader   = getReader(excelInputStream, excelListener);
        if(excelReader == null) {
            return new ArrayList<>();
        }
        List<ReadSheet> readSheetList = excelReader.excelExecutor().sheetList();
        for(ReadSheet readSheet : readSheetList) {
            excelReader.read(readSheet);
            break;
        }
        excelReader.finish();
        return excelListener.getDataList();
    }

    /**
     * 导出Excel(一个sheet)
     *
     * @param response HttpServletResponse
     * @param list     数据list
     * @param fileName 导出的文件名
     *                 //     * @param sheetName 导入文件的sheet名
     *                 //     * @param clazz     实体类
     */
//    public static <T> void writeExcel(HttpServletResponse response, List<T> list, String fileName, String sheetName, Class<T> clazz) {
//
//        OutputStream outputStream = getOutputStream(response, fileName);
//        ExcelWriter  excelWriter  = com.alibaba.excel.EasyExcel.write(outputStream, clazz).build();
//        WriteSheet   writeSheet   = com.alibaba.excel.EasyExcel.writerSheet(sheetName).build();
//        excelWriter.write(list, writeSheet);
//        excelWriter.finish();
//    }
    public static <T> void writeExcel(HttpServletResponse response, List<T> list, String fileName) {
        ExcelWriter excelWriter = null;
        try {
            //设置ConetentType CharacterEncoding Header,需要在excelWriter.write()之前设置
            response.setContentType("mutipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("数据详情").build();
            excelWriter.write(list, writeSheet);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            excelWriter.finish();
        }
    }

    public static void writeExcelSheet(HttpServletResponse response, List<WorkOrderDataBo> excelList, String fileName, String templateType) {
        ExcelWriter excelWriter = null;
        try {
            //设置ConetentType CharacterEncoding Header,需要在excelWriter.write()之前设置
            response.setContentType("mutipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            InputStream is = null;
            if("1".equals(templateType)) {
                is = new ClassPathResource("【KOL】抖音、快手询价单订单导出模板.xlsx").getInputStream();
            } else {
                is = new ClassPathResource("【KOL】非抖音、快手询价单订单导出模板.xlsx").getInputStream();

            }

            excelWriter = EasyExcel.write(response.getOutputStream())
//                    .registerWriteHandler(new CustomCellWriteHandler())
                    .withTemplate(is)
                    .build();

            WriteSheet writeSheet = EasyExcel.writerSheet("需求单").build();

            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            fillConfig.setAutoStyle(Boolean.FALSE);

            int total = 0;

            Map<String, String> fillData = new HashMap<>();

            for(WorkOrderDataBo wb : excelList) {
                total += Double.parseDouble(wb.getPrice() == null ? "0" : wb.getPrice());
            }

            fillData.put("total", Integer.toString(total));
            excelWriter.fill(excelList, fillConfig, writeSheet);
            excelWriter.fill(fillData, fillConfig, writeSheet);
            excelWriter.finish();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    public static void writeExcelSheet(HttpServletResponse response, List<?> list, String fileName, List<String> sheetNames) {
        ExcelWriter excelWriter = null;
        try {
            //设置ConetentType CharacterEncoding Header,需要在excelWriter.write()之前设置
            response.setContentType("mutipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx");

            excelWriter = EasyExcel.write(response.getOutputStream()).build();

            for(int i = 0; i < list.size(); i++) {
                List data = (List) list.get(i);
                if(data.size() == 1)
                    continue;
                WriteSheet writeSheet = EasyExcel.writerSheet(sheetNames.get(i)).build();
                excelWriter.write(data, writeSheet);
            }
//            WriteSheet writeSheet = EasyExcel.writerSheet("库外数据").build();
//            excelWriter.write((List)list.get(0), writeSheet);
//            writeSheet = EasyExcel.writerSheet("新意").build();
//            excelWriter.write((List)list.get(1), writeSheet);
//            writeSheet = EasyExcel.writerSheet("维格").build();
//            excelWriter.write((List)list.get(2), writeSheet);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            excelWriter.finish();
        }
    }


    /**
     * 导出时生成OutputStream
     */
    private static OutputStream getOutputStream(HttpServletResponse response, String fileName) {
        //创建本地文件
        String filePath = fileName + ".xlsx";
        File   file     = new File(filePath);
        try {
            if(!file.exists() || file.isDirectory()) {
                file.createNewFile();
            }
            fileName = new String(filePath.getBytes(), "ISO-8859-1");
            response.addHeader("Content-Disposition", "filename=" + fileName);
            return response.getOutputStream();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> ExcelReader getReader(InputStream inputStream, ExcelListener excelListener) {
        try {

            ExcelReader excelReader = com.alibaba.excel.EasyExcel.read(inputStream, excelListener).build();
            inputStream.close();
            return excelReader;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    static class ExcelListener extends AnalysisEventListener {
        /**
         * 可以通过实例获取该值
         */
        private List<Object> dataList = new ArrayList<>();

        @Override
        public void invoke(Object object, AnalysisContext context) {
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            dataList.add(object);
            handleBusinessLogic();
          /*
        如数据过大，可以进行定量分批处理
        if(dataList.size()>=200){
            handleBusinessLogic();
            dataList.clear();
        }
         */
        }

        @Override
        public void invokeHead(Map headMap, AnalysisContext context) {
            dataList.add(headMap);
        }

//        @Override
//        public void invokeHeadMap(Map headMap, AnalysisContext context) {
//
//        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            //非必要语句，查看导入的数据
            System.out.println("导入的数据条数为: " + dataList.size());
        }

        //根据业务自行实现该方法，例如将解析好的dataList存储到数据库中
        private void handleBusinessLogic() {

        }

        public List<Object> getDataList() {
            return dataList;
        }

        public void setDataList(List<Object> dataList) {
            this.dataList = dataList;
        }
    }

//    /**
//     * 下载本地文件
//     *
//     * @param
//     * @throws
//     */
//    public static void downloadLocal(HttpServletResponse response) throws IOException {
//        // 下载本地文件
//        // 文件的默认保存名
//        String fileName = "【KOL】询价单模版.xlsx";
//        // 读到流中
//        // 文件的存放路径
//        try(InputStream inStream = new FileInputStream("src/main/resources/【KOL】询价单模版.xlsx")) {
//            // 设置输出的格式
//            response.reset();
//            response.setContentType("MSEXCEL");
//            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
//            // 循环取出流中的数据
//            byte[] b = new byte[100];
//            int    len;
//            try {
//                while((len = inStream.read(b)) > 0) {
//                    response.getOutputStream().write(b, 0, len);
//                }
//            } catch(IOException e) {
//                log.error("本地文件下载失败 ：{}", e);
//            }
//        }
//
//    }
}
