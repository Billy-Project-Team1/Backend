package com.sparta.billy.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Setter
@Getter
@Document(indexName = "billy")
public class PostDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String detailLocation;

    @Field(type = FieldType.Text)
    private String titleAndDetailLocation;

    public PostDocument(Long id, String title, String detailLocation, String titleAndDetailLocation) {
        this.id = id;
        this.title = title;
        this.detailLocation = detailLocation;
        this.titleAndDetailLocation = titleAndDetailLocation;
    }
}
