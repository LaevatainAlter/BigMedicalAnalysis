package com.bjtu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by gimling on 17-6-7.
 */
@Service
public class JsonService {

    private final static Logger logger = LoggerFactory.getLogger(JsonService.class);

    private static ObjectMapper om;

    @Autowired
    public void setObjectMapper(ObjectMapper om){
        this.om = om;
    }

    public static String toJson(Object object){
        if(om==null){
            logger.info("Can't autowired ObjectMapper,Create new ObjectMapper");
            om = new ObjectMapper();
        }
        try {
            String json = om.writeValueAsString(object);
            return json;
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
            logger.error(e.getLocalizedMessage());
        }
        return "";
    }

    public static <T> T fromJson(String json,Class<T> type)  {
        try {
            return om.readValue(json,type);
        } catch (IOException e) {
            logger.info(e.getMessage());
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    public static <T> T fromJson(File json, Class<T> type)  {
        try {
            return om.readValue(json,type);
        } catch (IOException e) {
            logger.info(e.getMessage());
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    public static boolean writeJsonToFile(File file,Map json)  {
        try {
            om.writeValue(file,json);
            return true;
        } catch (IOException e) {
            logger.info(e.getMessage());
            logger.error(e.getLocalizedMessage());
            return false;
        }
    }
}
