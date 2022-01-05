package cn.weihu.kol.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 读取文件内容并解析参数
 *
 * @author qinsf
 */
@Slf4j
public class FileUtil {

    public static String saveFilePath = null;

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {

        File targetFile = new File(saveFilePath + filePath);
        if(!targetFile.exists()) {
            targetFile.mkdirs();
            try {
                Runtime.getRuntime().exec("chmod 777 -R " + targetFile);
            } catch(Exception ignored) {
            }
        }
        FileOutputStream out = new FileOutputStream(saveFilePath + filePath + fileName);
        log.info("保存文件:{}", saveFilePath + filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static void downloadPDF(HttpServletResponse response, String path, Boolean isDel) {

        path = saveFilePath + path;

        File file = new File(path.replace("/", File.separator));
        log.info("download filepath:{}", file.getPath());
        InputStream inputStream = null;
        try {
            if(file.exists()) {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "inline;filename=" + new String(file.getName().getBytes(), "ISO8859-1"));
                response.addHeader("Content-Length", "" + file.length());
                inputStream = new BufferedInputStream(new FileInputStream(file));
                IOUtils.copy(inputStream, response.getOutputStream());
                response.getOutputStream().close();
            } else {
                response.setStatus(500);
                response.addHeader("code", "-1");
                response.addHeader("error", "file not exist");
            }
        } catch(Exception e) {
            log.error("PDF文件合成异常", e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(isDel) {
                FileUtils.deleteQuietly(file);
            }
        }
    }
    public static void download(HttpServletResponse response, String path, Boolean isDel) {

        path = saveFilePath + path;

        File file = new File(path.replace("/", File.separator));
        log.info("download filepath:{}", file.getPath());
        InputStream inputStream = null;
        try {
            if(file.exists()) {
                response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes(), "ISO8859-1"));
                response.addHeader("Content-Length", "" + file.length());
                inputStream = new BufferedInputStream(new FileInputStream(file));
                IOUtils.copy(inputStream, response.getOutputStream());
                response.getOutputStream().close();
            } else {
                response.setStatus(500);
                response.addHeader("code", "-1");
                response.addHeader("error", "file not exist");
            }
        } catch(Exception e) {
            log.error("文件合成异常", e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(isDel) {
                FileUtils.deleteQuietly(file);
            }
        }
    }
//
//    public static void downloadTemplate(String pararms, String name, HttpServletResponse response) {
//        List<String> heads = CSVUtils.params2Head(pararms);
//        try {
//            String filename = name + ".xlsx";
//            filename = URLEncoder.encode(filename, "utf-8");
//            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
//            ExcelUtil.expExcel(name, heads, response.getOutputStream());
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static List<String> loadFile(String fileName) throws IOException {
//        fileName = FileCacheContainerImpl.saveFilePath + fileName;
//        String type = StringUtils.substringAfterLast(fileName, ".");
//        switch(type) {
//            case "txt":
//                return loadFromCSV(new FileInputStream(fileName), StandardCharsets.UTF_8);
//            case "csv":
//                FileInputStream is = new FileInputStream(fileName);
//                Charset charset = checkFileCharset(new FileInputStream(fileName));
////                is.close();
//                return loadFromCSV(new FileInputStream(fileName), charset);
//            case "xls":
//            case "xlsx":
//                return loadFromExcel(fileName, new FileInputStream(fileName));
//            default:
//                return Collections.emptyList();
//        }
//    }
//
//    private static List<String> loadFromCSV(InputStream is, Charset charset) throws IOException {
//        log.info("charset:{}", charset.toString());
//        return IOUtils.readLines(is, charset);
//    }
//
//    private static List<String> loadFromExcel(String fileName, InputStream is) throws IOException {
//        return ExcelUtil.readLines(fileName, is);
//    }
//
//    public static Charset checkFileCharset(InputStream is) {
//        try {
//            byte[] b = new byte[3];
//            is.read(b);
//            if(b[0] == -17 && b[1] == -69 && b[2] == -65) {
//                //utf-8编码前3个字节是固定的
//                return Charsets.UTF_8;
//            } else if(b[0] == -27 && b[1] == -113 && b[2] == -126) {
//                //utf-8无bom,模板文件第一行是固定的,用前3个字节来判断是不是utf-8无bom
//                return Charsets.UTF_8;
//            } else if(b[0] == -26 && b[1] == -119 && b[2] == -117) {
//                return Charsets.UTF_8;
//            } else if(b[0] == -26 && b[1] == -119 && b[2] == -117) {
//                return Charsets.UTF_8;
//            } else {//其他情况默认是GBK
//                return Charset.forName("GBK");
//            }
//        } catch(Exception e) {
//            log.error("字符转换异常", e);
//            return Charset.forName("GBK");
//        }
//    }
//
//    /**
//     * 判断文件的编码格式
//     *
//     * @param fileName :file
//     * @return 文件编码格式
//     * @throws Exception
//     */
//    public static String codeString(File fileName) throws Exception {
//        BufferedInputStream bin = new BufferedInputStream(
//                new FileInputStream(fileName));
//        int    p    = (bin.read() << 8) + bin.read();
//        String code = null;
//        System.out.println("p=======" + p);
//        switch(p) {
//            case 0xefbb:
//            case 59017:
//                code = "UTF-8";
//                break;
//            case 0xfffe:
//                code = "Unicode";
//                break;
//            case 0xfeff:
//                code = "UTF-16BE";
//                break;
//            default:
//                code = "GBK";
//        }
//        IOUtils.closeQuietly(bin);
//        return code;
//    }
//
//    public static void main(String[] args) {
//        String fileName = "d:\\1_1.csv";
//        try {
//            System.out.println(codeString(new File(fileName)));
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            FileInputStream is      = new FileInputStream(fileName);
//            Charset         charset = checkFileCharset(new FileInputStream(fileName));
////                is.close();
//            List<String> list  = loadFromCSV(new FileInputStream(fileName), charset);
//            String       heads = list.get(0);
//            System.out.println(heads);
//            System.out.println(heads.startsWith("\uFEFF"));
//            System.out.println(GsonUtils.gson.toJson(heads.split("\t")));
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }

}
