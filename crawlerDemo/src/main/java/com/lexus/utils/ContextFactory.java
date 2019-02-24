package com.lexus.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

public class ContextFactory {
    public static Object getContextInstance(String id) {
        //1.解析application.xml文件,根据id获取对应的标签
        //1.1将配置文件转换成字节输入流
        InputStream is = ContextFactory.class.getClassLoader().getResourceAsStream("applicationContext.xml");
        //1.2创建一个解析器
        SAXReader reader = new SAXReader();
        try {
            //1.3读取配置文件,转换成document对象
            Document document = reader.read(is);
            //1.4根据id查找context标签(XML解析方式)
            Element element = (Element) document.selectSingleNode("//context[@id='" + id + "']");
            //1.5获取标签里面的文本,并去掉前后的空格
            String className = element.getTextTrim();
            //1.6根据反射创建对象
            Class<?> clazz = Class.forName(className);
            Object o = clazz.newInstance();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
