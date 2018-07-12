package com.codecool.beerlovers.beerdb.servlet;


import com.codecool.beerlovers.beerdb.model.Brewery;
import com.codecool.beerlovers.beerdb.repository.BreweryRepository;
import com.codecool.beerlovers.beerdb.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/breweries/*")
public class BreweryServlet extends AbstractServlet {

    @Autowired
    private BreweryRepository breweryRepository;

    @Autowired
    private JsonUtils jsonUtils;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            handleGettingAllBreweries(resp);
        } else {
            handleGettingOneBrewery(path, resp);
        }
    }

    private void handleGettingAllBreweries(HttpServletResponse resp) throws IOException {
        List<Brewery> breweries = breweryRepository.getAll();
        sendAsJson(resp, breweries);
    }

    private void handleGettingOneBrewery(String path, HttpServletResponse resp) throws IOException {
        if (isNotCorrectPath(path)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int breweryId = getBreweryIdFromPath(path);
        if (breweryRepository.getById(breweryId) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        sendAsJson(resp, breweryRepository.getById(breweryId));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            String requestBody = jsonUtils.getStringFromHttpServletRequest(req);
            Brewery brewery = getBreweryFromRequestBody(requestBody);
            if (brewery == null || brewery.getId() != 0) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            breweryRepository.create(brewery);
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
            return;
        }

        if (isNotCorrectPath(pathInfo)) {
            resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        int breweryId = getBreweryIdFromPath(pathInfo);

        if (breweryRepository.getById(breweryId) == null) {
            resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        String requestBody = jsonUtils.getStringFromHttpServletRequest(req);
        Brewery brewery = breweryRepository.getById(breweryId);
        Brewery newBrewery = getBreweryFromRequestBody(requestBody);

        if (newBrewery == null || newBrewery.getId() != brewery.getId()) {
            resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Invalid JSON format");
            return;
        }
        breweryRepository.update(newBrewery);
        resp.sendError(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            breweryRepository.deleteAll();
            resp.sendError(HttpServletResponse.SC_ACCEPTED);
        } else {

            if (isNotCorrectPath(pathInfo)) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            int breweryId = getBreweryIdFromPath(pathInfo);
            if (breweryRepository.getById(breweryId) == null) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            Brewery brewery = breweryRepository.getById(breweryId);
            breweryRepository.delete(brewery);
            resp.sendError(HttpServletResponse.SC_ACCEPTED);
        }
    }

    private void sendAsJson(HttpServletResponse resp, Object object) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(object));
    }

    private boolean isNotCorrectPath(String pathInfo) {
        String[] splits = pathInfo.split("/");
        return splits.length != 2 || !StringUtils.isNumeric(splits[1]);
    }

    private int getBreweryIdFromPath(String pathInfo) {
        String[] splits = pathInfo.split("/");
        return Integer.parseInt(splits[1]);
    }

    private Brewery getBreweryFromRequestBody(String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        Brewery brewery;
        try {
            brewery = mapper.readValue(requestBody, Brewery.class);
        } catch (IOException e) {
            return null;
        }
        return brewery;
    }
}
