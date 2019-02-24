package com.lexus.test;

import com.lexus.domain.Route;
import com.lexus.service.IRouteService;
import com.lexus.utils.ContextFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Test01 {
    @Test
    public void test01() throws Exception {
        InputStream content = null;
        FileOutputStream fos = null;
        try {
            //爬取金马旅游网页数据:http://www.jinmalvyou.com/search/index/view_type/1/keyword/%E5%9B%BD%E5%86%85
            //将数据加载到内存中,形成一个document对象,然后操作这个对象将数据存储进数据库
            //1.将网络上的资源加载进内存形成一个document对象
            String keyword = "国内";
            keyword = URLEncoder.encode(keyword, "UTF-8");

            for (int i = 1; i <= 10; i++) {
                String url = "http://www.jinmalvyou.com/search/index/view_type/1/keyword/" + keyword;
                if (i >= 2) {
                    //第二页开始url改变,在后面拼接
                    url = url + "/p/" + i + ".html";
                }
                //2.使用jsoup框架将网络资源加载进内存,转换成document对象
                Document doc = Jsoup.connect(url).get();
                //System.out.println(document);
                //3.找到每一条路线li
                Elements elements = doc.select(".rl-b-li");
                //System.out.println(elements.equals(elements1));
                //继承了ArrayList,可用集合方式遍历
                Route route = new Route();
                content = null;
                fos = null;
                for (Element element : elements) {
                    //获取每条路线的rname
                    String rname = element.selectFirst(".pro-title a").text();
                    route.setRname(rname);
                    //获取每条路线的price
                    Double price = Double.valueOf(element.selectFirst(".price strong").text());
                    route.setPrice(price);
                    //获取每条路线的introduction
                    String routeIntroduce = element.selectFirst(".pro-colomn").text();
                    route.setRouteIntroduce(routeIntroduce);
                    //设置rflag
                    route.setRflag("1");
                    //设置rdate
                    route.setRdate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    //设置isThemeTour
                    route.setIsThemeTour("1");
                    //设置收藏数量count
                    route.setCount(0);
                    //设置所属分类cid,就是国内游,5
                    route.setCid(5);
                    //设置sid
                    route.setSid(1);
                    //设置sourceId,在rname内
                    Element rnameElement = element.selectFirst(".pro-title a");
                    //href为a标签内属性
                    String href = rnameElement.attr("href");
                    //截取=号后面字符串
                    String sid = href.substring(href.indexOf("=") + 1);
                    route.setSourceId(sid);
                    //设置rimage
                    Element rimageElement = element.selectFirst(".pro-img img");
                    String src = "http:" + rimageElement.attr("src");
                    //String rimage = src.substring(src.lastIndexOf("/"));
                    //直接用UUID生成一串字符串拼接在image图片名内
                    //String uuid = UuidUtil.getUuid()+ ".jpg";
                    String uuid = UUID.randomUUID().toString() + ".jpg";
                    String rimage = "img/product/small/" + uuid;
                    route.setRimage(rimage);
                    //把route对象存储到数据库中
                    IRouteService service = (IRouteService) ContextFactory.getContextInstance("routeService");
                    Integer add = service.addRoute(route);
                    if (add == 1) {
                        System.out.println("添加数据成功");
                    }

                    //使用HttpClient进行下载,创建一个客户端
                    CloseableHttpClient client = HttpClients.createDefault();
                    //发起get请求
                    HttpGet get = new HttpGet(src);
                    //使用客户端执行请求,得到响应response
                    CloseableHttpResponse response = client.execute(get);
                    //从响应中获取entity响应体
                    HttpEntity entity = response.getEntity();
                    //从响应体内获取内容
                    content = entity.getContent();
                    //获取输出流
                    fos = new FileOutputStream("D:/usr/photos/" + uuid);
                    //把下载的图片写入
                    int copy = IOUtils.copy(content, fos);
                    System.out.println(copy);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            content.close();
            fos.close();
        }
    }
}
