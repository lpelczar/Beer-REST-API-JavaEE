package com.codecool.beerlovers.beerdb.servlet;


import com.codecool.beerlovers.beerdb.model.Brewery;
import com.codecool.beerlovers.beerdb.util.HttpRequestToJsonString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/breweries/*")
public class BreweryServlet extends HttpServlet {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private HttpRequestToJsonString requestToJsonString;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Brewery> breweries = entityManager.createQuery("SELECT b FROM Brewery b", Brewery.class).getResultList();
            sendAsJson(resp, breweries);
        } else {

            if (!isCorrectPath(pathInfo)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int breweryId = getBreweryIdFromPath(pathInfo);
            if (getBreweryById(breweryId) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            sendAsJson(resp, getBreweryById(breweryId));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {

            String requestBody = requestToJsonString.apply(req);

            ObjectMapper mapper = new ObjectMapper();
            Brewery brewery;
            try {
                brewery = mapper.readValue(requestBody, Brewery.class);
            } catch (IOException e) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Invalid JSON format");
                return;
            }

            if (brewery.getId() != 0) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Remove ID from your request");
                return;
            }

            entityManager.getTransaction().begin();
            entityManager.persist(brewery);
            entityManager.getTransaction().commit();
            resp.sendError(HttpServletResponse.SC_CREATED);
        } else {
            resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Invalid path");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NO_CONTENT);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length != 2 || !StringUtils.isNumeric(splits[1])) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            String breweryId = splits[1];

            entityManager.getTransaction().begin();
            Brewery brewery = entityManager.find(Brewery.class, Integer.parseInt(breweryId));

            if (brewery == null) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            String requestBody = requestToJsonString.apply(req);

            ObjectMapper mapper = new ObjectMapper();
            Brewery mappedBrewery;
            try {
                mappedBrewery = mapper.readValue(requestBody, Brewery.class);
            } catch (IOException e) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Invalid JSON format");
                return;
            }

            if (mappedBrewery.getId() != brewery.getId()) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            entityManager.merge(mappedBrewery);
            entityManager.getTransaction().commit();
            resp.sendError(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            entityManager.getTransaction().begin();
            Query q1 = entityManager.createQuery("DELETE FROM Beer");
            Query q2 = entityManager.createQuery("DELETE FROM Brewery");
            q1.executeUpdate();
            q2.executeUpdate();
            entityManager.getTransaction().commit();
            resp.sendRedirect("/breweries/");
        } else {

            String[] splits = pathInfo.split("/");

            if (splits.length != 2 || !StringUtils.isNumeric(splits[1])) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            String breweryId = splits[1];
            Query query = entityManager.createQuery("SELECT b FROM Brewery b WHERE b.id = :id", Brewery.class);
            query.setParameter("id", Integer.parseInt(breweryId));

            if (query.getResultList().size() == 0) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            entityManager.getTransaction().begin();
            Brewery brewery = entityManager.find(Brewery.class, Integer.parseInt(breweryId));
            entityManager.remove(brewery);
            entityManager.getTransaction().commit();
            resp.sendError(HttpServletResponse.SC_ACCEPTED);
        }
    }

    private void sendAsJson(HttpServletResponse response, Object toJson) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(out, toJson);
        response.getWriter().write(new String(out.toByteArray()));
    }
    private boolean isCorrectPath(String pathInfo) {
        String[] splits = pathInfo.split("/");
        return splits.length == 2 && StringUtils.isNumeric(splits[1]);
    }

    private int getBreweryIdFromPath(String pathInfo) {
        String[] splits = pathInfo.split("/");
        return Integer.parseInt(splits[1]);
    }

    @Transactional
    public Brewery getBreweryById(int breweryId) {
        return entityManager.find(Brewery.class, breweryId);
    }
}
