package com.catalog.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(@JsonAlias("title") String bookName,
                       @JsonAlias("download_count") Integer downloadCount,
                       @JsonAlias("languages") List<String> languages) {
}
