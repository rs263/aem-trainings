package com.trainingsite.core.services;


import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface PageListService {

    List<String> getAllPageByTitle(ResourceResolver resourceResolver, String title);
}
