package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Beer;
import com.codecool.beerlovers.beerdb.util.HttpRequestToJsonString;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "BeerServlet", urlPatterns = {"/beers", "/api/beers/*"})
public class BeerServlet extends AbstractServlet {

    public static final int RETURN_COLLECTION = -1;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private HttpRequestToJsonString requestToJsonString;

    Logger log = Logger.getLogger(getClass().getName());


    private ObjectMapper mapper = new ObjectMapper();


    @Override
    @Transactional
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String query = "SELECT b FROM Beer b WHERE b.category.id != -1 AND b.style.id != -1";
        int beerID = getIDOfBeer(req.getRequestURI());
        if (beerID > RETURN_COLLECTION) query = query + "AND b.id = " + beerID;

        List<Beer> beers = entityManager
                .createQuery(query, Beer.class).getResultList();
        resp.setContentType("application/json");

        String json = mapper.writeValueAsString(beers);


        resp.getWriter().write(json);

    }

    @Override
    @Transactional
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String requestBody = requestToJsonString.apply(req);

        Beer newBeer = mapper.readValue(requestBody, Beer.class);

        if (entityManager.find(Beer.class, newBeer.getId()) != null) resp.sendError(409);

        entityManager.merge(newBeer);


    }

    @Override
    @Transactional
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String query = "DELETE FROM Beer";
        int beerID = getIDOfBeer(req.getRequestURI());
        if (beerID > RETURN_COLLECTION) query = query + " b WHERE b.id = " + beerID;

        entityManager.createQuery(query).executeUpdate();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {


    }

    private int getIDOfBeer(String requestURI) {
        List<String> pathVariables = Arrays.asList(requestURI.split("/"));

        try {
            return Integer.parseInt(pathVariables.get(pathVariables.size() - 1));
        } catch (Exception e) {
            return RETURN_COLLECTION;
        }
    }
}
