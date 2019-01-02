/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: CommonUtils.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.commons.utils;

import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hiylo.components.commons.encrypttools.Md5;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:34
 */
public class CommonUtils {
    private final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();
    private final static String JPG = "jpg";
    private final static String BMP = "bmp";
    private final static String PNG = "png";
    private final static String GIF = "gif";
    private final static String COMMA = ",";

    static {
        getAllFileType(); // 初始化文件类型信息
    }

    public static Map<String, String> getFileTypeMap() {
        return FILE_TYPE_MAP;
    }

    public static String createToken() {
        return UUID.randomUUID().toString().replaceAll("-", "_");
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static boolean isImage(byte[] is) {
        String type = CommonUtils.getFileTypeByStream(is);
        if (JPG.equals(type) || BMP.equals(type) || PNG.equals(type) || GIF.equals(type)) {
            return true;
        }
        return false;
    }

    /**
     * Created on 2010-7-1
     * <p>
     * Discription:[getAllFileType,常见文件头信息]
     * </p>
     *
     * @author:[shixing_11@sina.com]
     */
    private static void getAllFileType() {
        // JPEG (jpg)
        FILE_TYPE_MAP.put("jpg", "FFD8FF");
        // PNG (png)
        FILE_TYPE_MAP.put("png", "89504E47");
        // GIF (gif)
        FILE_TYPE_MAP.put("gif", "47494638");
        // TIFF (tif)
        FILE_TYPE_MAP.put("tif", "49492A00");
        // Windows Bitmap (bmp)
        FILE_TYPE_MAP.put("bmp", "424D");
        // CAD (dwg)
        FILE_TYPE_MAP.put("dwg", "41433130");
        // HTML (html)
        FILE_TYPE_MAP.put("html", "68746D6C3E");
        // Rich Text Format (rtf)
        FILE_TYPE_MAP.put("rtf", "7B5C727466");
        FILE_TYPE_MAP.put("xml", "3C3F786D6C");
        FILE_TYPE_MAP.put("zip", "504B0304");
        FILE_TYPE_MAP.put("rar", "52617221");
        // Photoshop (psd)
        FILE_TYPE_MAP.put("psd", "38425053");
        // Email
        FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");
        // [thorough
        // only]
        // (eml)
        // Outlook Express (dbx)
        FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");
        // Outlook (pst)
        FILE_TYPE_MAP.put("pst", "2142444E");
        // MS Word
        FILE_TYPE_MAP.put("xls", "D0CF11E0");
        // MS Excel 注意：word 和 excel的文件头一样
        FILE_TYPE_MAP.put("doc", "D0CF11E0");
        // MS Access (mdb)
        FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");
        // WordPerfect (wpd)
        FILE_TYPE_MAP.put("wpd", "FF575043");
        FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
        // Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("pdf", "255044462D312E");
        // Quicken (qdf)
        FILE_TYPE_MAP.put("qdf", "AC9EBD8F");
        // Windows Password (pwl)
        FILE_TYPE_MAP.put("pwl", "E3828596");
        // Wave (wav)
        FILE_TYPE_MAP.put("wav", "57415645");
        FILE_TYPE_MAP.put("avi", "41564920");
        // Real Audio (ram)
        FILE_TYPE_MAP.put("ram", "2E7261FD");
        // Real Media (rm)
        FILE_TYPE_MAP.put("rm", "2E524D46");
        //
        FILE_TYPE_MAP.put("mpg", "000001BA");
        // Quicktime (mov)
        FILE_TYPE_MAP.put("mov", "6D6F6F76");
        // Windows Media (asf)
        FILE_TYPE_MAP.put("asf", "3026B2758E66CF11");
        // MIDI (mid)
        FILE_TYPE_MAP.put("mid", "4D546864");
    }

    /**
     * Created on 2010-7-1
     * <p>
     * Discription:[getImageFileType,获取图片文件实际类型,若不是图片则返回null]
     * </p>
     *
     * @param f
     * @return fileType
     * @author:[shixing_11@sina.com]
     */
    public final static String getImageFileType(File f) {
        if (isImage(f)) {
            try {
                ImageInputStream iis = ImageIO.createImageInputStream(f);
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
                if (!iter.hasNext()) {
                    return null;
                }
                ImageReader reader = iter.next();
                iis.close();
                return reader.getFormatName();
            } catch (IOException e) {
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Created on 2010-7-1
     * <p>
     * Discription:[getFileByFile,获取文件类型,包括图片,若格式不是已配置的,则返回null]
     * </p>
     *
     * @param file
     * @return fileType
     * @author:[shixing_11@sina.com]
     */
    public final static String getFileByFile(File file) {
        String filetype = null;
        byte[] b = new byte[50];
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int i = is.read(b);
            if (i != -1) {
                filetype = getFileTypeByStream(b);
                is.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filetype;
    }

    /**
     * Created on 2010-7-1
     * <p>
     * Discription:[getFileTypeByStream]
     * </p>
     *
     * @param b
     * @return fileType
     * @author:[shixing_11@sina.com]
     */
    public final static String getFileTypeByStream(byte[] b) {
        String filetypeHex = String.valueOf(getFileHexString(b));
        Iterator<Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
        while (entryiterator.hasNext()) {
            Entry<String, String> entry = entryiterator.next();
            String fileTypeHexValue = entry.getValue();
            if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Created on 2010-7-2
     * <p>
     * Discription:[isImage,判断文件是否为图片]
     * </p>
     *
     * @param file
     * @return true 是 | false 否
     * @author:[shixing_11@sina.com]
     */
    public static final boolean isImage(File file) {
        boolean flag = false;
        try {
            BufferedImage bufreader = ImageIO.read(file);
            int width = bufreader.getWidth();
            int height = bufreader.getHeight();
            if (width == 0 || height == 0) {
                flag = false;
            } else {
                flag = true;
            }
        } catch (IOException e) {
            flag = false;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * Created on 2010-7-1
     * <p>
     * Discription:[getFileHexString]
     * </p>
     *
     * @param b
     * @return fileTypeHex
     * @author:[shixing_11@sina.com]
     */
    public final static String getFileHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static final String firstChatToUpcase(String word) {
        String result = word.substring(0, 1).toUpperCase();
        result += word.substring(1);
        return result;
    }

    /**
     * 将数组的字符串格式转换回为数组
     *
     * @param str 格式为"[]"
     * @return
     */
    public static List<Integer> convertStringToList(String str) {
        // 去除空格
        str = str.replace(" ", "").replace("[", "").replace("]", "");
        List<Integer> list = new LinkedList();
        // 判断是否为空
        if (str != null && !"".equals(str) && str.length() > 0) {
            if (str.contains(COMMA)) {
                String[] strArr = str.split(",");
                for (String val : strArr) {
                    list.add(Integer.parseInt(val));
                }
            } else {
                list.add(Integer.parseInt(str));
            }
        }
        return list;
    }

    /**
     * 将Json对象转化为XML格式字符串
     *
     * @param jsonObject json对象
     * @return 被转化后的xml字符串
     */
    public static String jsonToXML(JSONObject jsonObject) {
        StringBuffer xmlStr = new StringBuffer();
        for (Object key : jsonObject.keySet()) {
            try {
                JSONObject sonJson = JSONObject.fromObject(jsonObject.get(key));
                xmlStr.append("<" + key + ">" + CommonUtils.jsonToXML(sonJson) + "</" + key + ">");
            } catch (Exception e) {
                xmlStr.append("<" + key + ">" + jsonObject.get(key) + "</" + key + ">");
            }
        }
        return xmlStr.toString();
    }

    /**
     * map转xml字符串(包含<![CDATA[DATA]]>)
     *
     * @param map 被转化对象
     * @return 被转化后的xml字符串
     */
    public static String mapToXML(Map map) {
        StringBuffer xmlBuffer = new StringBuffer();
        mapToXMLCycle(map, xmlBuffer);
        return xmlBuffer.toString();
    }

    /**
     * map转xml字符串内部方法
     *
     * @param map
     * @param xmlBuffer
     */
    private static void mapToXMLCycle(Map map, StringBuffer xmlBuffer) {
        Iterator<Entry> entryKeyIterator = map.entrySet()
                .iterator();
        while (entryKeyIterator.hasNext()) {
            Entry e = entryKeyIterator.next();
            String key = e.getKey().toString();
            Object value = e.getValue();
            if (null == value) {
                value = "";
            }
            if (value != null && value.getClass().getName() != null && "java.util.ArrayList".equals(value.getClass().getName())) {
                ArrayList list = (ArrayList) map.get(key);
                xmlBuffer.append("<" + key + ">");
                for (int i = 0; i < list.size(); i++) {
                    mapToXMLCycle((Map) list.get(i), xmlBuffer);
                }
                xmlBuffer.append("</" + key + ">");

            } else {
                if (value instanceof HashMap) {
                    xmlBuffer.append("<" + key + ">");
                    mapToXMLCycle((HashMap) value, xmlBuffer);
                    xmlBuffer.append("</" + key + ">");
                } else if (value instanceof TreeMap) {
                    xmlBuffer.append("<" + key + ">");
                    mapToXMLCycle((TreeMap) value, xmlBuffer);
                    xmlBuffer.append("</" + key + ">");
                } else {
                    xmlBuffer.append("<" + key + "><![CDATA[" + value + "]]></" + key + ">");
                }
            }
        }
    }

    /**
     * xml转化为map
     *
     * @param xmlStr       xml字符串
     * @param baseNodeName 根节点
     * @return 被转化成map格式的对象
     * @throws Exception
     */
    public static Map<String, Object> xml2Map(String xmlStr, String baseNodeName) throws Exception {
        Map<String, Object> nodeMap = new HashMap(0, 0.75F);
        Document document = DocumentHelper.parseText(xmlStr);
        Element rootElement = document.getRootElement();
        if (baseNodeName.equals(rootElement.getName())) {
            nodeMap.put(baseNodeName, CommonUtils.getChildElements(rootElement.elements()));
            return nodeMap;
        } else {
            throw new Exception("wrong baseNodeName");
        }
    }

    /**
     * 将元素集合转化为map格式
     *
     * @param childElements 元素集合
     * @return 被转化后的map对象
     */
    public static Map<String, Object> getChildElements(List<Element> childElements) {
        Map<String, Object> nodeMap = new HashMap(0, 0.75F);
        for (Element element : childElements) {
            if (element.elements().size() == 0) {
                if (element.attribute("name") != null) {
                    nodeMap.put(element.attribute("name").getStringValue(), element.getStringValue());
                } else {
                    nodeMap.put(element.getName(), element.getStringValue());
                }
            } else {
                nodeMap.put(element.getName(), CommonUtils.getChildElements(element.elements()));
            }
        }
        return nodeMap;
    }

    /**
     * map转Json 方法
     *
     * @param map map对象
     * @return 转化后的Json对象
     */
    public static JSONObject map2Json(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        Iterator<Entry<String, Object>> entryKeyIterator = map.entrySet()
                .iterator();
        while (entryKeyIterator.hasNext()) {
            Entry e = entryKeyIterator.next();
            String key = e.getKey().toString();
            Object value = e.getValue();
            if (null == value) {
                value = "";
            }
            if (value != null && value.getClass().getName() != null && "java.util.ArrayList".equals(value.getClass().getName())) {
                ArrayList list = (ArrayList) map.get(key);
                ArrayList<JSONObject> jsonArray = new ArrayList<JSONObject>(20);
                for (int i = 0; i < list.size(); i++) {
                    jsonArray.add(map2Json((Map) list.get(i)));
                }
                jsonObject.put(key, jsonArray);
            } else {
                if (value instanceof HashMap) {
                    jsonObject.put(key, CommonUtils.map2Json((HashMap<String, Object>) value));
                } else if (value instanceof TreeMap) {
                    jsonObject.put(key, CommonUtils.map2Json((TreeMap<String, Object>) value));
                } else {
                    jsonObject.put(key, value);
                }
            }
        }
        return jsonObject;
    }


    /**
     * 对map参数按照首字母排序进行MD5加密
     *
     * @param paramMap  请求参数Map
     * @param md5Key    加密密钥
     * @param upperCase 结果时候转大写
     * @return 加密字符串结果
     */
    public static String signWithMd5(Map<String, Object> paramMap, String md5Key, boolean upperCase) {
        if (upperCase) {
            return Md5.sign(CommonUtils.createSignParam(paramMap, md5Key), "", "utf-8").toUpperCase();
        } else {
            return Md5.sign(CommonUtils.createSignParam(paramMap, md5Key), "", "utf-8").toLowerCase();
        }
    }

    /**
     * 对代签名参数进行排序并组装(空值字段不参与排序)
     *
     * @param paramMap 签名参数map格式
     * @param md5Key   md5密钥,没有可不填
     * @return 组装好的签名字符串
     */
    public static String createSignParam(Map<String, Object> paramMap, String md5Key) {
        StringBuffer signStr = new StringBuffer();
        Map<String, Object> paramTreeMap = new TreeMap<String, Object>();
        paramTreeMap.putAll(paramMap);
        for (String key : paramMap.keySet()) {
            if (StringUtils.hasText(paramTreeMap.get(key) + "")) {
                signStr.append(key + "=" + paramTreeMap.get(key) + "&");
            }
        }
        if (StringUtils.hasText(md5Key)) {
            return signStr.append("key=" + md5Key).toString();
        } else {
            return signStr.toString().substring(0, signStr.length() - 1);
        }
    }


    public static void main(String[] args) {
        Map<String, Object> sonMap = new TreeMap<>();
        sonMap.put("1233", "456");

        System.out.println(CommonUtils.createSignParam(sonMap, ""));

    }

}
