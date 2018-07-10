package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Beer;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(name = "BeerServlet", urlPatterns = {"/beers", "/api/beers/*"})
public class BeerServlet extends HttpServlet {

    public static final int RETURN_COLLECTION = -1;
    private final EntityManager entityManager;

    Logger log = Logger.getLogger(getClass().getName());


    private ObjectMapper mapper = new ObjectMapper();

    public BeerServlet() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("beersJPA");
        this.entityManager = emf.createEntityManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String query = "SELECT b FROM Beer b WHERE b.category.id != -1 AND b.style.id != -1";
        int beerID = getIDOfBeer(req.getRequestURI());
        log.info(String.valueOf(beerID));
        if (beerID > RETURN_COLLECTION) query = query + "AND b.id = " + beerID;

        List<Beer> beers = entityManager
                .createQuery(query, Beer.class).getResultList();
        resp.setContentType("application/json");

        String json = mapper.writeValueAsString(beers);


        resp.getWriter().write(json);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String requestBody = req
                .getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        Beer newBeer = mapper.readValue(requestBody, Beer.class);


        entityManager.getTransaction().begin();

        entityManager.merge(newBeer);

        entityManager.getTransaction().commit();

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
