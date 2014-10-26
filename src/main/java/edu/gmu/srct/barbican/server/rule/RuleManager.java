package edu.gmu.srct.barbican.server.rule;

import com.google.gson.Gson;
import edu.gmu.srct.barbican.server.trends.Trend;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;

/**
 * Created by mgauto on 10/26/14.
 */
public class RuleManager {
    private Gson gson = new Gson();
    private Client client;
    private ArrayList<Rule> rules = new ArrayList<Rule>();

    public RuleManager(Client client) {
        this.client = client;
    }

    public Trend[] getAllRules() {
        SearchResponse response = client.prepareSearch()
                .setQuery(QueryBuilders.matchQuery("type", "barbican-rule")) // Query
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
