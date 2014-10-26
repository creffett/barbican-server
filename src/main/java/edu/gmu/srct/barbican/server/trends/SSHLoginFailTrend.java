package edu.gmu.srct.barbican.server.trends;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

/**
 * Created by mgauto on 10/26/14.
 */
public class SSHLoginFailTrend extends Trend {


    @Override
    public void check() {
        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("flamingbarbican.cloudapp.net", 9300));
        SearchResponse response = client.prepareSearch()
                .setQuery(QueryBuilders.matchQuery("file", "/var/log/auth.log")) // Query
                .setQuery(QueryBuilders.matchQuery("message", "failure"))
                        //.setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
                .setFrom(0).setSize(1000).setExplain(true)
                .execute()
                .actionGet();
        java.util.Iterator<SearchHit> hit_it = response.getHits().iterator();
        while(hit_it.hasNext()){
            SearchHit hit = hit_it.next();
            System.out.println(hit.getSourceAsString());
        }
        client.close();
    }
}
