package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Style;
import com.codecool.beerlovers.beerdb.repository.StyleRepository;
import com.codecool.beerlovers.beerdb.util.JsonUtils;
import com.codecool.beerlovers.beerdb.util.JsonUtilsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/styles/*")
public class StyleServlet extends AbstractServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private StyleRepository styleRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getPathInfo();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if(path == null || path.equals("/")) {
            List<Style> styles = styleRepository.getAll();
            resp.getWriter().write(objectMapper.writeValueAsString(styles));

        }else{
            String[] splits = path.split("/");
            if(splits.length != 2 || !StringUtils.isStrictlyNumeric(splits[1])){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = Integer.valueOf(splits[1]);
                if(!(styleRepository.getById(id) == null)) {
                    Style style = styleRepository.getById(id);
                    String catToJSON = objectMapper.writeValueAsString(style);
                    resp.setContentType("application/json");
                    resp.getWriter().write(catToJSON);
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
            String[] splits = path.split("/");
            if(splits.length != 2 || !StringUtils.isStrictlyNumeric(splits[1])){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = Integer.valueOf(splits[1]);
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
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            String[] splits = path.split("/");
            if (splits.length != 2 || !StringUtils.isStrictlyNumeric(splits[1])) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                int id = Integer.valueOf(splits[1]);
                String json = jsonUtils.getStringFromHttpServletRequest(req);
                Style style = objectMapper.readValue(json, Style.class);
                if (!(styleRepository.getById(id) == null) && style.getId() == id ) {
                    styleRepository.update(style);
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }

        resp.sendRedirect("/styles/");
    }

    private boolean isNotCorrectPath(String pathInfo) {
        String[] splits = pathInfo.split("/");
        return splits.length != 2 || !org.apache.commons.lang3.StringUtils.isNumeric(splits[1]);
    }

    private int getStyleIdFromPath(String pathInfo) {
        String[] splits = pathInfo.split("/");
        return Integer.parseInt(splits[1]);
    }

    private void sendAsJson(HttpServletResponse resp, Object object) throws IOException {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(object));
    }

}

