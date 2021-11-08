package cn.weihu.kol.util;


import cn.weihu.kol.biz.bo.ParamsBo;
import com.csvreader.CsvWriter;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUtils {

    private static final String filePath = "";

    /**
     * CSV文件生成方法
     *
     * @param head
     * @param dataList
     * @param filename
     * @return
     */
    public static String createCSVFile(List<String> head, List<List<Object>> dataList,
                                       String filename) {

        CsvWriter csvWriter = null;
        try {
            String dirPath = filePath + File.separator + "template";
            File   csvFile = new File(dirPath);
            if(csvFile != null && !csvFile.exists()) {
                csvFile.mkdirs();
            }
            // 创建CSV写对象 例如:CsvWriter(文件路径,分隔符,编码格式);
            dirPath += File.separator + filename + ".csv";
            csvWriter = new CsvWriter(dirPath, ',', StandardCharsets.UTF_8);
            // 写表头
            head.set(0, new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}) + head.get(0));
            csvWriter.writeRecord(head.toArray(new String[head.size()]));
            if(dataList != null && !dataList.isEmpty()) {
                for(List<Object> data : dataList) {
                    csvWriter.writeRecord(head.toArray(new String[data.size()]));
                }
            }
            csvWriter.close();
            return dirPath;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(null != csvWriter) {
                csvWriter.close();
            }
        }
        return null;
    }

    public static List<ParamsBo> params2List(String params) {
        ParamsBo[]     array        = new Gson().fromJson(params, ParamsBo[].class);
        List<ParamsBo> paramsBoList = Arrays.asList(array);
        Collections.sort(paramsBoList);
        return paramsBoList;
    }

    public static List<String> params2Head(String params) {
        ParamsBo[]     array        = new Gson().fromJson(params, ParamsBo[].class);
        List<ParamsBo> paramsBoList = Arrays.asList(array);
        List<String> list = paramsBoList.stream().sorted(Comparator.comparingInt(ParamsBo::getIndex))
                .map(ParamsBo::getName).collect(Collectors.toList());
        return list;
    }


    public static String params2Headname(String params) {
        String result;
        if(StringUtils.isBlank(params)) {
            result = "手机号";
        } else {
            ParamsBo bo = new ParamsBo();
            bo.setIndex(0);
            bo.setName("手机号");
            bo.setVariable("phone");
            ParamsBo[]     array        = new Gson().fromJson(params, ParamsBo[].class);
            List<ParamsBo> paramsBoList = Arrays.stream(array).collect(Collectors.toList());
            paramsBoList.add(0, bo);
            Collections.sort(paramsBoList);
            List<Object> list = paramsBoList.stream().sorted(Comparator.comparingInt(ParamsBo::getIndex))
                    .map(ParamsBo::getName).collect(Collectors.toList());
            result = Joiner.on(",").join(list);
        }
        return result;
    }


    public static void main(String[] args) {
        String json = "[{\"index\":1,\"name\":\"参数1\",\"dataType\":\"location\",\"defaultValue\":\"北京市\",\"variable\":\"param1\",\"required\":true},{\"index\":2,\"name\":\"参数2\",\"dataType\":\"enterprise\",\"defaultValue\":\"默认值\",\"variable\":\"param2\",\"required\":true}]";
        CSVUtils.params2Head(json);
        CSVUtils     csvUtils = new CSVUtils();
        List<String> heads    = CSVUtils.params2Head(json);
        String       csvfile  = csvUtils.createCSVFile(heads, null, 1 + "_" + 1);
        System.out.println(csvfile);
//        CSVUtils.writeCSV();
    }
}
