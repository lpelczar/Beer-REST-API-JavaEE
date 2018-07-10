package com.codecool.beerlovers.servlet;

import com.codecool.beerlovers.beerdb.config.AppConfig;
import com.codecool.beerlovers.beerdb.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mysql.cj.util.StringUtils;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;


@WebServlet("/categories/*")
public class CategoryServlet extends HttpServlet {

    private EntityManager em = new AppConfig().entityManager();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if(path == null || path.equals("/")) {
            List<Category> categories = em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
            resp.getWriter().write(objectMapper.writeValueAsString(categories));

        }else{
            String[] splits = path.split("/");
            if(splits.length != 2 || !StringUtils.isStrictlyNumeric(splits[1])){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = Integer.valueOf(splits[1]);
                if(isCategoryInDatabse(id)) {
                    Category category = em.createQuery("SELECT c FROM Category c WHERE id = :idFromURI", Category.class)
                            .setParameter("idFromURI", id).getSingleResult();
                    String catToJSON = objectMapper.writeValueAsString(category);
                    resp.getWriter().write(catToJSON);
                }else{
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        Category category = objectMapper.readValue(jb.toString(), Category.class);
        if(!isCategoryInDatabse(category.getId())) {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.sendRedirect("/categories/" + category.getId());
    }



    private boolean isCategoryInDatabse(int id){
        Query query = em.createQuery("SELECT c FROM Category c WHERE id = :idFromURI", Category.class)
                .setParameter("idFromURI", id);
        return (query.getResultList().size() == 1);
    }
}

