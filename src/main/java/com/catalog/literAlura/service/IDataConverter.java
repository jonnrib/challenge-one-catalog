package com.catalog.literAlura.service;

public interface IDataConverter {
    <T> T getData(String json, Class<T> type);
}

