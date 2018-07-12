package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Style;
import com.codecool.beerlovers.beerdb.util.JsonUtils;
import com.codecool.beerlovers.beerdb.util.JsonUtilsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/styles/*")
public class StyleServlet extends AbstractServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JsonUtils jsonUtils;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getPathInfo();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if(path == null || path.equals("/")) {
            List<Style> styles = entityManager.createQuery("SELECT s FROM Style s", Style.class).getResultList();
            resp.getWriter().write(objectMapper.writeValueAsString(styles));

        }else{
            String[] splits = path.split("/");
            if(splits.length != 2 || !StringUtils.isStrictlyNumeric(splits[1])){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = Integer.valueOf(splits[1]);
                if(isStyleInDatabase(id)) {
                    Style style = entityManager.find(Style.class, id);
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
        if(!isStyleInDatabase(style.getId())) {
            entityManager.getTransaction().begin();
            entityManager.merge(style);
            entityManager.getTransaction().commit();
        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.sendRedirect("/styles/");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        entityManager.getTransaction().begin();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if(path == null || path.equals("/")) {
            Query query = entityManager.createQuery("DELETE FROM Style");
            query.executeUpdate();
            entityManager.getTransaction().commit();

        }else{
            String[] splits = path.split("/");
            if(splits.length != 2 || !StringUtils.isStrictlyNumeric(splits[1])){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = Integer.valueOf(splits[1]);
                if(isStyleInDatabase(id)) {
                    Style style = entityManager.find(Style.class, id);
                    entityManager.remove(style);
                    entityManager.getTransaction().commit();
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
                if (!isStyleInDatabase(style.getId()) && style.getId() == id ) {
                    entityManager.getTransaction().begin();
                    entityManager.merge(style);
                    entityManager.getTransaction().commit();
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }

        resp.sendRedirect("/styles/");
    }

    private boolean isStyleInDatabase(int id){
        Query query = entityManager.createQuery("SELECT s FROM Style s WHERE s.id = :idFromURI", Style.class)
                .setParameter("idFromURI", id);
        return (query.getResultList().size() == 1);
    }
}

