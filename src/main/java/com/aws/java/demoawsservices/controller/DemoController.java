package com.aws.java.demoawsservices.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.aws.java.demoawsservices.controller.dto.request.DemoRequest;
import com.aws.java.demoawsservices.domain.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/demos")
public class DemoController {

    @Autowired
    AmazonDynamoDB dynamodb;

    final String TABLE_NAME = "registros";

    @PostMapping
    ResponseEntity<?> write(@RequestBody DemoRequest request) {
        return ResponseEntity.ok(dynamodb.putItem(new PutItemRequest(TABLE_NAME, Map.of(
                "id", new AttributeValue().withS(UUID.randomUUID().toString()),
                "type", new AttributeValue().withS(UUID.randomUUID().toString()),
                "name", new AttributeValue().withS(request.getName()),
                "description", new AttributeValue().withS(request.getDescription())))));
    }

    @GetMapping
    ResponseEntity<?> readAll() {
        return ResponseEntity.ok(dynamodb.scan(new ScanRequest(TABLE_NAME))
                .getItems()
                .stream()
                .map(map -> new Demo(map.get("id").getS(), map.get("type").getS(), map.get("name").getS(), map.get("description").getS()))
                .toList());
    }

    @GetMapping("/item")
    ResponseEntity<?> readOne(@RequestParam String id, @RequestParam String type) {
        var item = dynamodb.getItem(new GetItemRequest(TABLE_NAME, Map.of("id", new AttributeValue().withS(id), "type", new AttributeValue().withS(type)))).getItem();
        return ResponseEntity.ok(new Demo(item.get("id").getS(), item.get("type").getS(), item.get("name").getS(), item.get("description").getS()));
    }

    @PutMapping("/item")
    ResponseEntity<?> updateOne(@RequestParam String id, @RequestParam String type, @RequestBody DemoRequest request) {
        return ResponseEntity.ok(dynamodb.updateItem(new UpdateItemRequest(TABLE_NAME,
                Map.of("id", new AttributeValue().withS(id),
                       "type", new AttributeValue().withS(type)),
                Map.of("name",new AttributeValueUpdate().withValue(new AttributeValue().withS(request.getName())),
                       "description",new AttributeValueUpdate().withValue(new AttributeValue().withS(request.getName()))))
        ));
    }

    @DeleteMapping
    ResponseEntity<?> delete(@RequestParam String id, @RequestParam String type) {
        return ResponseEntity.ok(dynamodb.deleteItem(
                new DeleteItemRequest(TABLE_NAME, Map.of("id", new AttributeValue().withS(id), "type", new AttributeValue().withS(type)))));
    }
}
