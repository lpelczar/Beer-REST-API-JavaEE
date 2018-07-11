package com.codecool.beerlovers.beerdb.servlet;


import com.codecool.beerlovers.beerdb.model.Beer;
import com.codecool.beerlovers.beerdb.model.Brewery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/breweries/*")
public class BreweryServlet extends HttpServlet {

    private EntityManager entityManager = Persistence.createEntityManagerFactory("beersJPA").createEntityManager();

    // GET /breweries/ - list all breweries
    // GET /breweries/id - retrieve one brewery by id
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        System.out.println(pathInfo);

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Brewery> breweries = entityManager.createQuery("SELECT b FROM Brewery b", Brewery.class).getResultList();
            sendAsJson(resp, breweries);
        } else {
            String[] splits = pathInfo.split("/");

            if (splits.length != 2 || !StringUtils.isNumeric(splits[1])) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String breweryId = splits[1];
            Query query = entityManager.createQuery("SELECT b FROM Brewery b WHERE b.id = :id", Brewery.class);
            query.setParameter("id", Integer.parseInt(breweryId));

            if (query.getResultList().size() == 0) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            sendAsJson(resp, query.getSingleResult());
        }
    }

    // POST /breweries/ - create a new entry in collection
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            String requestBody = req
                    .getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            ObjectMapper mapper = new ObjectMapper();
            Brewery brewery;
            try {
                brewery = mapper.readValue(requestBody, Brewery.class);
            } catch (IOException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
                return;
            }

            if (brewery.getId() != 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Remove ID from your request");
                return;
            }

            entityManager.getTransaction().begin();
            entityManager.persist(brewery);
            entityManager.getTransaction().commit();
            resp.sendRedirect("/breweries/" + brewery.getId());

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
        }
    }

    // DELETE /breweries/ - delete entire collection
    // DELETE /breweries/id - delete one brewery
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        //Delete entire collection
        if (pathInfo == null || pathInfo.equals("/")) {

            entityManager.getTransaction().begin();
            Query q1 = entityManager.createQuery("DELETE FROM Beer");
            Query q2 = entityManager.createQuery("DELETE FROM Brewery");
            q1.executeUpdate();
            q2.executeUpdate();
            entityManager.getTransaction().commit();
            resp.sendRedirect("/breweries/");

        // Delete one brewery
        } else {

            String[] splits = pathInfo.split("/");

            if (splits.length != 2 || !StringUtils.isNumeric(splits[1])) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String breweryId = splits[1];
            Query query = entityManager.createQuery("SELECT b FROM Brewery b WHERE b.id = :id", Brewery.class);
            query.setParameter("id", Integer.parseInt(breweryId));

            if (query.getResultList().size() == 0) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            entityManager.getTransaction().begin();
            // Delete brewery
            Brewery brewery = entityManager.find(Brewery.class, Integer.parseInt(breweryId));

            for (Beer beer : brewery.getBeers()) {
                System.out.println(beer);
            }

//            entityManager.remove(brewery);

            entityManager.getTransaction().commit();
        }

    }

    private void sendAsJson(HttpServletResponse response, Object toJson) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(out, toJson);
        response.getWriter().write(new String(out.toByteArray()));
    }
}
