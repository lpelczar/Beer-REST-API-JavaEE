package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Style;
import com.codecool.beerlovers.beerdb.repository.StyleRepository;
import com.codecool.beerlovers.beerdb.util.JsonUtils;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/styles/*")
public class StyleServlet extends AbstractServlet {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private StyleRepository styleRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getPathInfo();
        if(path == null || path.equals("/")) {
            List<Style> styles = styleRepository.getAll();
            sendAsJson(resp, styles);

        }else{
            if(isNotCorrectPath(path)){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = getIdFromPath(path);
                if(!(styleRepository.getById(id) == null)) {
                    Style style = styleRepository.getById(id);
                    sendAsJson(resp, style);
                }else{
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = jsonUtils.getStringFromHttpServletRequest(req);

        Style style = objectMapper.readValue(json, Style.class);
        if(!(styleRepository.getById(style.getId()) == null)) {
            styleRepository.create(style);
        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.sendRedirect("/styles/");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if(path == null || path.equals("/")) {
            styleRepository.deleteAll();
        }else{
            if(isNotCorrectPath(path)){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = getIdFromPath(path);
                if(!(styleRepository.getById(id) == null)) {
                    Style style = styleRepository.getById(id);
                    styleRepository.delete(style);
                }else{
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
        resp.sendRedirect("/styles/");

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (isNotCorrectPath(path)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            int id = getIdFromPath(path);
            String json = jsonUtils.getStringFromHttpServletRequest(req);
            Style style = objectMapper.readValue(json, Style.class);
            if (!(styleRepository.getById(id) == null) && style.getId() == id ) {
                styleRepository.update(style);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        resp.sendRedirect("/styles/");
    }

}

