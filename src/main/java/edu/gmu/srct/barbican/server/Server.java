package edu.gmu.srct.barbican.server;

import com.google.gson.Gson;
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

import static spark.Spark.*;
import static org.elasticsearch.node.NodeBuilder.*;

/**
 * Created by mgauto on 10/25/14.
 */
public class Server {
    private Gson gson = new Gson();
    private TrendingEngine trendingEngine = new TrendingEngine();

    public static void main(String[] args) {
        //new Server().initAPI();
        testCurl();
    }

    public void initAPI() {
        get("/hello", (req, res) -> "Hello World");
        get("/trends", (req, res) -> processTrends());
    }

    public String processTrends() {
        return gson.toJson(trendingEngine.getTrends());
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
