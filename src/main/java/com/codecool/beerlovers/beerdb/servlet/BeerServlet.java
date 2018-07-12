package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Beer;
import com.codecool.beerlovers.beerdb.repository.BeerRepository;
import com.codecool.beerlovers.beerdb.util.JsonUtils;
import com.codecool.beerlovers.beerdb.util.URLQueryDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "BeerServlet", urlPatterns = {"/beers/*", "/api/beers/*"})
@Repository
@Transactional
public class BeerServlet extends AbstractServlet {

    public static final int RETURN_COLLECTION = -1;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private URLQueryDecoder queryDecoder;

    Logger log = Logger.getLogger(getClass().getName());


    private ObjectMapper mapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = "";
        Pair<Integer, Integer> paginationArguments = getPaginationArguments(req.getQueryString());
        int beerID = getIDOfBeer(req.getRequestURI());
        if (beerID > RETURN_COLLECTION) {
            json = mapper.writeValueAsString(beerRepository.getById(beerID));
        } else if (paginationArguments != null) {
            json = mapper.writeValueAsString(beerRepository.getAll(paginationArguments.getKey(), paginationArguments.getValue()));
        } else json = mapper.writeValueAsString(beerRepository.getAll());

        resp.setContentType("application/json");
        resp.getWriter().write(json);

    }

    @Override
    @Transactional
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = jsonUtils.getStringFromHttpServletRequest(req);

        if (!jsonUtils.checkJsonCompatibility(requestBody, Beer.class)) {
            resp.sendError(HttpStatus.CONFLICT.value(), "Your request is invalid or you try to post more than one entity !");
            return;
        }

        Beer newBeer = mapper.readValue(requestBody, Beer.class);
        if (beerRepository.getById(newBeer.getId()) != null)
            resp.sendError(HttpStatus.CONFLICT.value(), "This beer already exists !");
        else {
            beerRepository.create(newBeer);
            log.info(String.valueOf(newBeer.getId()));
            resp.getWriter().write(String.valueOf(newBeer.getId()));
            resp.setStatus(HttpStatus.OK.value());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        int beerID = getIDOfBeer(req.getRequestURI());
        if (beerID > RETURN_COLLECTION) beerRepository.deleteById(beerID);
        else beerRepository.deleteAll();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jsonString = jsonUtils.getStringFromHttpServletRequest(req);
        if (!jsonUtils.checkJsonCompatibility(jsonString, Beer.class)) {
            resp.sendError(HttpStatus.CONFLICT.value(), "Your request is invalid or you try to post more than one entity !");
            return;
        }
        Beer beer = mapper.readValue(jsonString, Beer.class);
        beer.setId(getIDOfBeer(req.getRequestURI()));
        if (beerRepository.getById(beer.getId()) == null) {
            resp.sendError(HttpStatus.CONFLICT.value(), "This beer does not exist !");
        } else beerRepository.update(beer);

    }

    private int getIDOfBeer(String requestURI) {
        List<String> pathVariables = Arrays.asList(requestURI.split("/"));

        try {
            return Integer.parseInt(pathVariables.get(pathVariables.size() - 1));
        } catch (Exception e) {
            return RETURN_COLLECTION;
        }
    }

    private Pair<Integer, Integer> getPaginationArguments(String query) {
        HashMap<String, String> queryKeyValue = queryDecoder.apply(query);
        Pair<Integer, Integer> result = null;
        try {
            int from = Integer.parseInt(queryKeyValue.get("from"));
            int to = Integer.parseInt(queryKeyValue.get("to"));
            result = new Pair<>(from, to);

        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        return result;
    }
}
