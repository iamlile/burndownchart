package com.iamlile.jira.burndown.util;

import com.iamlile.jira.burndown.mapper.JiraMapper;
import com.iamlile.jira.burndown.mapper.JiraSprintMapper;
import com.iamlile.jira.burndown.model.JiraKey;
import com.iamlile.jira.burndown.model.JiraWithBLOBs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lee on 2018/2/17.
 * @author lee
 */
public class JiraHttpClientUtil {
    final static Logger logger = LoggerFactory.getLogger(JiraHttpClientUtil.class);
    public static String PM_HOST= "http://pm.igeeker.org/";
    public static String JIRA_BOARDS_URL = "rest/agile/1.0/board/?type=scrum";
    //"http://pm.igeeker.org/rest/agile/1.0/board/?type=scrum"

    public static String JIRA_SPRINTS_URL = "rest/agile/1.0/board/board_id/sprint";

    public static String JIRA_ISSUE_URL = "rest/agile/1.0/board/board_id/sprint/sprint_id/issue";

    // 创建CookieStore实例
    private static CookieStore cookieStore = null;


    public static String getJiraBoardsFromRemote(String url, String username, String pwd) {
        CloseableHttpClient httpclient = null;
        try {
            //认证信息对象，用于包含访问翻译服务的用户名和密码
            //1.创建客户端访问服务器的httpclient对象   打开浏览器
            //httpclient = new DefaultHttpClient();
             httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            //2.以请求的连接地址创建get请求对象     浏览器中输入网址
            HttpGet httpget = new HttpGet(url);
            if(username!=null && pwd!=null) {
                String userpwd = username + ":" + pwd;
                //username:password--->访问的用户名，密码,并使用base64进行加密，将加密的字节信息转化为string类型，encoding--->token
                String encoding = DatatypeConverter.printBase64Binary(userpwd.getBytes("UTF-8"));

                httpget.setHeader("Authorization", "Basic " + encoding);
            }
            //3.向服务器端发送请求 并且获取响应对象  浏览器中输入网址点击回车
            HttpResponse response = httpclient.execute(httpget);
            //4.设置cookie
            if (cookieStore == null || "".equals(cookieStore)) {
                String host = httpget.getURI().getHost();
                setCookieStore(response, host);
            }
            //5.获取响应对象中的响应码
            StatusLine statusLine = response.getStatusLine();//获取请求对象中的响应行对象
            int responseCode = statusLine.getStatusCode();//从状态行中获取状态码
            if (responseCode == 200) {
                //6.  可以接收和发送消息
                HttpEntity entity = response.getEntity();
                //7.从消息载体对象中获取操作的读取流对象
                InputStream input = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String str1 = br.readLine();
                logger.debug("编码前:" + str1);
                String result = str1;//new String(str1.getBytes("gbk"), "utf-8");
//                String obstr = new Gson().toJson(result);
//                JsonElement je = new JsonParser().parse(result);
//                String values = je.getAsJsonObject().get("values").toString();
//                logger.debug("服务器的响应是:" + values);
                br.close();
                input.close();
                return result;
            } else {
                logger.debug("响应失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpclient) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void setCookieStore(HttpResponse httpResponse,String host) {
        cookieStore = new BasicCookieStore();
        // JSESSIONID
        if (null == httpResponse.getFirstHeader("Set-Cookie")) {
            cookieStore = null;
        }else {
            String setCookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
            String JSESSIONID = setCookie.substring("JSESSIONID=".length(),setCookie.indexOf(";"));
            // 新建一个Cookie
            BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",JSESSIONID);
            cookie.setVersion(0);
            cookie.setDomain(host);
            cookie.setPath("/");
            cookieStore.addCookie(cookie);
        }
    }

    public static void main(String[] args) {
//        String path = "http://pm.igeeker.org/" + JIRA_BOARDS_URL;
//        //String path = "http://pm.igeeker.org/" + JIRA_SPRINTS_URL;
//        path = path.replaceAll("board_id","5");
//        System.out.println(path);
//        String result = JiraHttpClientUtil.getJiraBoardsFromRemote(path, "atc", "AsdQwe123");
//        System.out.println(result);

        String str = "[\"5\",\"13\",\"15\",\"19\"]";
        //System.out.println(str.replaceAll("\\[|\\]","").replaceAll("\\\"",""));
        //System.out.println(str.replaceAll("(\\[|\\]|\\\")",""));
        List<String> boardList = Arrays.asList(str.replaceAll("\\[|\\]|\\\"","").split(","));
        for(String boardId : boardList){
            System.out.println(boardId);
        }


        //List<String> boardList = Arrays.asList(str.replaceAll("\\[|\\]","").split(","));

    }

}
