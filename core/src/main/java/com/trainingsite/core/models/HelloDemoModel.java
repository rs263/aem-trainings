package com.trainingsite.core.models;

import com.trainingsite.core.services.PageListService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Named;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HelloDemoModel {

    @ValueMapValue
    private String subtitle;

    @OSGiService
    private PageListService pageListService;

    @SlingObject
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    public List<String> getPagePaths() {
        String title = request.getParameter("title");
        return pageListService.getAllPageByTitle(resourceResolver, title);
    }

    public String getSubtitle() {
        return subtitle;
    }
}
