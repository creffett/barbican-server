package edu.gmu.srct.barbican.server.trends;


import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;

/**
 * Created by mgauto on 10/25/14.
 */
public class TrendManager {
    private Gson gson = new Gson();
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
        ArrayList<Trend> trends = new ArrayList<Trend>();
        while(hit_it.hasNext()) {
            trends.add(gson.fromJson(hit_it.next().getSourceAsString(), Trend.class));
        }
        return trends.toArray(new Trend[trends.size()]);
    }
}
