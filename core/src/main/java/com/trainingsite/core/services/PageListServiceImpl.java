package com.trainingsite.core.services;

import com.day.cq.commons.Filter;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component(service = PageListService.class)
public class PageListServiceImpl implements PageListService {

    private static final String PAGE_PATH = "/content/trainingsite/us/en";

    @Override
    public List<String> getAllPageByTitle(ResourceResolver resourceResolver, String title) {

        List<String> pagePaths = new ArrayList<>();
        Resource resource = resourceResolver.getResource(PAGE_PATH);
        Page page = resource.adaptTo(Page.class);
        Iterator<Page> pageIterator = page.listChildren(aPage -> aPage.getTitle().equals(title));
        while (pageIterator.hasNext()) {
            Page next = pageIterator.next();
            pagePaths.add(next.getPath());
        }
        return pagePaths;
    }
}
