package com.docapis.core;

import com.alibaba.fastjson.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.docapis.core.constant.CoreConstants.JAR_PATH;
import static com.docapis.core.constant.CoreConstants.USER_DIR;

/**
 * abstract doc generator
 * <p>
 * licence Apache 2.0,AGPL-3.0, from japidoc and doc-apis originated
 **/
public class Utils {
    /**
     * 删除文件最大尝试次数
     */
    private static final int DEL_MAX_RETRY_ATTEMPTS = 3;

    public static String toPrettyJson(Object map) {
        return JSONObject.toJSONString(map, true);
    }

    public static String toJson(Object map) {
        return JSONObject.toJSONString(map);
    }

    public static <T> T jsonToObject(String json, Class<T> type) {
        return JSONObject.parseObject(json, type);
    }

    public static void writeToDisk(File f, String content) throws IOException {
        mkdirsForFile(f);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
        writer.write(content);
        writer.close();
    }

    public static void closeSilently(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String streamToString(InputStream in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(in, "utf-8");
        char[] buffer = new char[4096];
        int bytesRead = -1;
        while ((bytesRead = reader.read(buffer)) != -1) {
            stringBuilder.append(buffer, 0, bytesRead);
        }
        reader.close();
        return stringBuilder.toString();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static String removeQuotations(String rawUrl) {
        return rawUrl.replace("\"", "").trim();
    }

    public static String cleanCommentContent(String content) {
        return content.replace("*", "").replace("\n", "").trim();
    }

    public static String getActionUrl(String baseUrl, String relativeUrl) {

        if (relativeUrl == null) {
            return "";
        }

        if (baseUrl == null) {
            return relativeUrl;
        }
        //当注解没写url时会默认取方法名，这里改掉。
        if ("".equals(relativeUrl)) {
            return baseUrl;
        }

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        if (!relativeUrl.startsWith("/")) {
            relativeUrl = "/" + relativeUrl;
        }

        return baseUrl + relativeUrl;
    }

    public static String decapitalize(String name) {
        if (name != null && name.length() != 0) {
            if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
                return name;
            } else {
                char[] chars = name.toCharArray();
                chars[0] = Character.toLowerCase(chars[0]);
                return new String(chars);
            }
        } else {
            return name;
        }
    }

    public static String capitalize(String name) {
        if (name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }

    public static String joinArrayString(String[] array, String separator) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0, len = array.length; i != len; i++) {
            builder.append(array[i]);
            if (i != len - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String getJavaFileName(File javaFile) {
        String fileName = javaFile.getName();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static void wideSearchFile(File rootPath, FilenameFilter filter, List<File> result, boolean stopAtFirstResult) {
        File[] fileList = rootPath.listFiles();
        List<File> dirPaths = new ArrayList<>();
        for (File f : fileList) {
            if (f.isFile() && filter.accept(f, f.getName())) {
                result.add(f);
                if (stopAtFirstResult) {
                    return;
                }
            } else if (f.isDirectory()) {
                dirPaths.add(f);
            }
        }

        for (File dir : dirPaths) {
            if (stopAtFirstResult && !result.isEmpty()) {
                return;
            }
            wideSearchFile(dir, filter, result, stopAtFirstResult);
        }
    }

    public static boolean hasDirInFile(File f, File stopPath, String dirName) {
        File p = f.getParentFile();
        while ((stopPath == null && p != null) || (stopPath != null && !p.getAbsolutePath().equals(stopPath.getAbsolutePath()))) {
            if (dirName.equals(p.getName())) {
                return true;
            }
            p = p.getParentFile();
        }
        return false;
    }

    public static boolean isPlayFramework(File projectDir) {
        File ymlFile = new File(projectDir, "conf/dependencies.yml");
        if (!ymlFile.exists()) {
            return false;
        }
        File routesFile = new File(projectDir, "conf/routes");
        if (!routesFile.exists()) {
            return false;
        }
        return true;
    }

    public static boolean isSpringFramework(File javaSrcDir) {
        List<File> result = new ArrayList<>();
        Utils.wideSearchFile(javaSrcDir, new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".java") && ParseUtils.compilationUnit(f).getImports().stream().anyMatch(im -> im.getNameAsString().contains("org.springframework.web"));
            }
        }, result, true);
        return result.size() > 0;
    }

    public static boolean isJFinalFramework(File javaSrcDir) {
        List<File> result = new ArrayList<>();
        Utils.wideSearchFile(javaSrcDir, new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".java") && ParseUtils.compilationUnit(f).getImports().stream().anyMatch(im -> im.getNameAsString().contains("com.jfinal.core"));
            }
        }, result, true);
        return result.size() > 0;
    }

    public static boolean isValueType(Object value) {
        return value instanceof Number
                || value instanceof String
                || value instanceof java.util.Date;
    }

    public static String getClassName(String packageClass) {
        String[] parts = packageClass.split("\\.");
        return parts[parts.length - 1];
    }

    private static BuildToolTypeEnum getProjectBuildTool(File projectDir) {
        if (new File(projectDir, "settings.gradle").exists()) {
            return BuildToolTypeEnum.GRADLE;
        }

        if (new File(projectDir, "pom.xml").exists()) {
            return BuildToolTypeEnum.MAVEN;
        }

        return BuildToolTypeEnum.UNKNOWN;
    }

    public static List<String> getModuleNames(File projectDir) {
        BuildToolTypeEnum buildToolTypeEnum = getProjectBuildTool(projectDir);

        List<String> moduleNames = new ArrayList<>();

        //gradle
        if (buildToolTypeEnum == BuildToolTypeEnum.GRADLE) {
            try {
                BufferedReader settingReader = new BufferedReader(new InputStreamReader(new
                        FileInputStream(new File(projectDir, "settings.gradle"))));
                String lineText;
                String keyword = "include ";
                while ((lineText = settingReader.readLine()) != null) {
                    int inIndex = lineText.indexOf(keyword);
                    if (inIndex != -1) {
                        moduleNames.add(lineText.substring(inIndex + keyword.length()).replace("'", "").replace("\"", ""));
                    }
                }
                Utils.closeSilently(settingReader);
            } catch (IOException ex) {
                LogUtils.error("read setting.gradle error", ex);
            }
        }

        // maven
        if (buildToolTypeEnum == BuildToolTypeEnum.MAVEN) {
            try {
                SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
                saxParser.parse(new File(projectDir, "pom.xml"), new DefaultHandler() {

                    String moduleName;
                    boolean isModuleTag;

                    @Override
                    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                        if ("module".equalsIgnoreCase(qName)) {
                            isModuleTag = true;
                        }
                    }

                    @Override
                    public void endElement(String uri, String localName, String qName) throws SAXException {
                        if ("module".equalsIgnoreCase(qName)) {
                            moduleNames.add(moduleName);
                            isModuleTag = false;
                        }
                    }

                    @Override
                    public void characters(char[] ch, int start, int length) throws SAXException {
                        if (isModuleTag) {
                            moduleName = new String(ch, start, length);
                        }
                    }
                });
            } catch (Exception ex) {
                LogUtils.error("read pom.xml error", ex);
            }
        }

        if (!moduleNames.isEmpty()) {
            LogUtils.info("find multi modules in this project: %s", Arrays.toString(moduleNames.toArray()));
        }

        return moduleNames;
    }

    public static void mkdirsForFile(File file) {
        if (file.isFile() && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    /**
     * 删除指定文件,失败重试
     *
     * @param filePath 文件路径
     */

    public static void deleteFileWithRetry(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            int retryCount = 0;
            while (retryCount < DEL_MAX_RETRY_ATTEMPTS) {
                try {
                    boolean isDeleted = file.delete();
                    if (isDeleted) {
                        // 删除成功,结束
                        return;
                    }
                } catch (Throwable e) {
                    LogUtils.warn("第:%d 次删除文件:%s 失败", retryCount, filePath);
                }

                // 延后重试
                retryCount++;
                if (retryCount < DEL_MAX_RETRY_ATTEMPTS) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        return;
                    }
                }
            }
            LogUtils.warn("文件:%s 删除失败,可终止项目后手动删除", filePath);
        }
    }


    /**
     * 获取pageInfo/EsPageInfo/js等被打包进jar的文件(快照,因为pageHelper,EsPageInfo是第三方插件,若要反编译出注释则需要用户下载源码后才能使用,显然不合适,所以采用快照,不影响使用体感)
     *
     * @param fileName 文件名
     * @return 对应文件
     */
    public static File getThirdPartJarFile(String fileName) {
        return getThirdPartJarFile(fileName, null);
    }


    /**
     * 获取pageInfo/EsPageInfo/js等被打包进jar的文件(快照,因为pageHelper,EsPageInfo是第三方插件,若要反编译出注释则需要用户下载源码后才能使用,显然不合适,所以采用快照,不影响使用体感)
     *
     * @param fileName 文件名
     * @param filePath 路径
     * @return 对应文件
     */
    public static File getThirdPartJarFile(String fileName, String filePath) {
        String jarPath = getJarPath() + File.separator + JAR_PATH;
        String targetPath;
        if (filePath != null) {
            fileName = filePath + File.separator + fileName;
            targetPath = DocContext.getDocPath() + File.separator + fileName;
        } else {
            targetPath = System.getProperty(USER_DIR) + File.separator + fileName;
        }
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (equalsIgnoreChars(entry.getName(), fileName)) {
                    try (InputStream is = jarFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(targetPath)) {
                        int read;
                        byte[] buffer = new byte[4096];
                        while ((read = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                        }
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // At this point, the file has been written to targetPath if it was found in the JAR
        return new File(targetPath);
    }


    /**
     * 获取当前配置的mvn本地仓库路径
     *
     * @return 仓库路径
     */
    public static String getJarPath() {
        String jarPath = "~/.m2/repository";
        try {
            String mavenHomeDir = System.getenv().get("M2_HOME");
            if (mavenHomeDir == null) {
                throw new RuntimeException("M2_HOME environment variable is not set");
            }

            String settingsXmlPath = mavenHomeDir + "/conf/settings.xml";
            File file = new File(settingsXmlPath);
            if (!file.exists()) {
                throw new RuntimeException("Maven settings.xml doesn't exist");
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("localRepository");

            for (int i = 0; i < nodeList.getLength(); i++) {
                org.w3c.dom.Node node = nodeList.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    jarPath = node.getTextContent();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jarPath;
    }

    /**
     * 判断两个字符串在忽略大小写及指定字符（默认忽略 '/' 和 '\'）的情况下是否相等。
     *
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 如果两个字符串在忽略指定字符后相等则返回true，否则返回false
     */
    public static boolean equalsIgnoreChars(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }

        // 同时替换掉 '/' 和 '\' 字符，并转换为小写比较
        String cleanedStr1 = str1.replaceAll("[/\\\\]", "").toLowerCase();
        String cleanedStr2 = str2.replaceAll("[/\\\\]", "").toLowerCase();

        return cleanedStr1.equals(cleanedStr2);
    }

}
