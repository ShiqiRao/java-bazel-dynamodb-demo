package com.example.demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build();
        createTable(client, "test");
        List<String> tables = listTables(client);
        for (String tableName : tables) {
            getTableInfo(client, tableName);
            deleteTable(client, tableName);
        }
    }

    static void createTable(AmazonDynamoDB client, String table_name) {
        CreateTableRequest request = new CreateTableRequest()
                .withAttributeDefinitions(new AttributeDefinition(
                        "Name", ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement("Name", KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput(
                        10L, 10L))
                .withTableName(table_name);


        try {
            CreateTableResult result = client.createTable(request);
            System.out.println(result.getTableDescription().getTableName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    static List<String> listTables(AmazonDynamoDB client) {

        ListTablesRequest request;

        boolean more_tables = true;
        String last_name = null;
        List<String> tables = new ArrayList<>();
        while (more_tables) {
            try {
                if (last_name == null) {
                    request = new ListTablesRequest().withLimit(10);
                } else {
                    request = new ListTablesRequest()
                            .withLimit(10)
                            .withExclusiveStartTableName(last_name);
                }

                ListTablesResult table_list = client.listTables(request);
                List<String> table_names = table_list.getTableNames();

                if (table_names.size() > 0) {
                    for (String cur_name : table_names) {
                        System.out.format("* %s\n", cur_name);
                        tables.add(cur_name);
                    }
                } else {
                    System.out.println("No tables found!");
                    System.exit(0);
                }

                last_name = table_list.getLastEvaluatedTableName();
                if (last_name == null) {
                    more_tables = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tables;
    }

    static void getTableInfo(AmazonDynamoDB ddb, String table_name) {
        try {
            TableDescription table_info =
                    ddb.describeTable(table_name).getTable();

            if (table_info != null) {
                System.out.format("Table name  : %s\n",
                        table_info.getTableName());
                System.out.format("Table ARN   : %s\n",
                        table_info.getTableArn());
                System.out.format("Status      : %s\n",
                        table_info.getTableStatus());
                System.out.format("Item count  : %d\n",
                        table_info.getItemCount().longValue());
                System.out.format("Size (bytes): %d\n",
                        table_info.getTableSizeBytes().longValue());

                ProvisionedThroughputDescription throughput_info =
                        table_info.getProvisionedThroughput();
                System.out.println("Throughput");
                System.out.format("  Read Capacity : %d\n",
                        throughput_info.getReadCapacityUnits().longValue());
                System.out.format("  Write Capacity: %d\n",
                        throughput_info.getWriteCapacityUnits().longValue());

                List<AttributeDefinition> attributes =
                        table_info.getAttributeDefinitions();
                System.out.println("Attributes");
                for (AttributeDefinition a : attributes) {
                    System.out.format("  %s (%s)\n",
                            a.getAttributeName(), a.getAttributeType());
                }
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

    static void deleteTable(AmazonDynamoDB ddb, String table_name) {
        try {
            ddb.deleteTable(table_name);
            System.out.println(table_name + " deleted");
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

}
