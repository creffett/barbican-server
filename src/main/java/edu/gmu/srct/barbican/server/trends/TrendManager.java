package edu.gmu.srct.barbican.server.trends;


import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

/**
 * Created by mgauto on 10/25/14.
 */
public class TrendManager {
    private Client client;

    public TrendManager(Client client) {
        this.client = client;
    }

    public Trend[] getAllTrends() {
        SearchResponse response = client.prepareSearch()
                .setQuery(QueryBuilders.matchQuery("type", "barbican-trend")) // Query
                        //.setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        java.util.Iterator<SearchHit> hit_it = response.getHits().iterator();
        Trend[] trends = new Trend[];
        while(hit_it.hasNext()){
            SearchHit hit = hit_it.next();
            System.out.println(hit.getSourceAsString());
        }
    }
}
