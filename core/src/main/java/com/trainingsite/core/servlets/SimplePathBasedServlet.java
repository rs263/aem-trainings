package com.trainingsite.core.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingsite.core.services.PageListService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=/bin/simpleServlet",
        "sling.servlet.methods=GET"
})
public class SimplePathBasedServlet extends SlingAllMethodsServlet {

    @Reference
    PageListService pageListService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        ObjectMapper mapper = new ObjectMapper();
        ResourceResolver resourceResolver = request.getResourceResolver();
        List<String> pagePaths = pageListService.getAllPageByTitle(resourceResolver, title);
        String jsonString = Optional.of(pagePaths)
                .map(pagePathList -> {
                    try {
                        return mapper.writeValueAsString(pagePathList);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).orElse("");
        response.getWriter().write(jsonString);
    }
}
