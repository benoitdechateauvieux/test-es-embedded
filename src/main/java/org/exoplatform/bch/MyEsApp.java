package org.exoplatform.bch;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

/**
 * Created by bdechateauvieux on 3/20/15.
 */
public class MyEsApp {

    public static void main(String[] args) {

        //Start Node
        ImmutableSettings.Builder settings =
                ImmutableSettings.settingsBuilder();
        settings.put("node.name", "orange11-node");
        settings.put("path.data", "target/data/index");
        settings.put("http.enabled", false);
        Node node = NodeBuilder.nodeBuilder()
                .settings(settings)
                .clusterName("orange11-cluster")
                .data(true).local(true).node();

        Client client = node.client();
        System.out.println("STARTED");


        //Create Index
        client.admin().indices().create(Requests.createIndexRequest("orange11")).actionGet();
        System.out.println("INDEX CREATED");


        byte[] jsonDocument = "{\"title\": \"title\", \"Content\": \"content\"}".getBytes();

        IndexRequest indexRequest =
                Requests.indexRequest("orange11")
                        .type("blog")
                        .id("1")
                        .source(jsonDocument);
        IndexResponse indexResponse =
                client.index(indexRequest).actionGet();
        System.out.println("DOC INDEXED");


        SearchRequestBuilder requestBuilder =
                client.prepareSearch("orange11")
                        .setTypes("blog")
                        .setQuery(QueryBuilders.termQuery("multi", "test"));
        SearchResponse searchResponse = requestBuilder.execute().actionGet();
        System.out.println(searchResponse);
        System.out.println("QUERY EXECUTED");

    }
}
