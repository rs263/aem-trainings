package com.trainingsite.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.osgi.service.component.annotations.Component;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/saveFormData"
        })
public class SaveFormDataServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(SaveFormDataServlet.class);

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        // Handle POST request for form submission
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        if (firstName != null && lastName != null && email != null) {
            try {
                // Get the resource resolver and session
                ResourceResolver resourceResolver = request.getResourceResolver();
                Session session = resourceResolver.adaptTo(Session.class);

                if (session != null) {
                    // Check if the parent node exists at /content/trainingsite/form
                    String formPath = "/content/trainingsite/form";
                    org.apache.sling.api.resource.Resource formResource = resourceResolver.getResource(formPath);

                    if (formResource != null) {
                        Node formNode = formResource.adaptTo(Node.class);
                        if (formNode != null) {
                            // Add the email as a node name under the form path
                            Node emailNode = formNode.addNode(email, "nt:unstructured");
                            emailNode.setProperty("firstName", firstName);
                            emailNode.setProperty("lastName", lastName);
                            emailNode.setProperty("email", email);

                            // Save the changes to JCR
                            session.save();

                            response.setStatus(SlingHttpServletResponse.SC_OK);
                            response.getWriter().write("Form data saved successfully.");
                        } else {
                            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            response.getWriter().write("Parent node creation failed.");
                        }
                    } else {
                        response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().write("Parent form node not found.");
                    }
                } else {
                    response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Session is null.");
                }
            } catch (Exception e) {
                LOG.error("Error saving form data", e);
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error saving form data.");
            }
        } else {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing required parameters.");
        }
    }
}
