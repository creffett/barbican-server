package edu.gmu.srct.barbican.server;

import com.google.gson.Gson;
import edu.gmu.srct.barbican.server.trends.Trend;
import edu.gmu.srct.barbican.server.trends.TrendingEngine;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import javax.naming.directory.SearchResult;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static spark.Spark.*;
import static org.elasticsearch.node.NodeBuilder.*;

/**
 * Created by mgauto on 10/25/14.
 */
public class Server {
    private String url="";
    private Gson gson = new Gson();
    private TrendingEngine trendingEngine = new TrendingEngine();

    public static void main(String[] args) {
        //new Server().initAPI();
        testCurl();
    }

    public void initAPI() {
        get("/hello", (req, res) -> "Hello World");
        get("/trends", (req, res) -> processTrends());
        post("/trend", (req, res) -> processPutTrend(req.body()));
    }

    public String processTrends() {
        return gson.toJson(trendingEngine.getTrends());
    }

    public String processPutTrend(String document) {
        try {
            String request = "http://" + url+"/";
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(document.getBytes().length));
            connection.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(document);
            wr.flush();
            wr.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error";
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "Error";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
        trendingEngine.registerTrend(gson.fromJson(document, Trend.class));
        return "OK";
    }

    private static void testCurl() {
        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("flamingbarbican.cloudapp.net", 9300));
        SearchResponse response = client.prepareSearch()
                .setQuery(QueryBuilders.matchQuery("file", "/var/log/auth.log")) // Query
                .setQuery(QueryBuilders.matchQuery("message", "failure"))
                //.setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        java.util.Iterator<SearchHit> hit_it = response.getHits().iterator();
        while(hit_it.hasNext()){
            SearchHit hit = hit_it.next();
            System.out.println(hit.getSourceAsString());
        }
    }
}
