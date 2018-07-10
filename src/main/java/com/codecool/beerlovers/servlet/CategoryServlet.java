package com.codecool.beerlovers.servlet;

import com.codecool.beerlovers.beerdb.config.AppConfig;
import com.codecool.beerlovers.beerdb.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mysql.cj.util.StringUtils;


import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/categories/*")
public class CategoryServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        EntityManager em = new AppConfig().entityManager();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if(path == null || path.equals("/")) {
            List<Category> categories = em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
            resp.getWriter().write(objectMapper.writeValueAsString(categories));

        }else{
            String[] splits = path.split("/");
            if(splits.length != 2 || !StringUtils.isStrictlyNumeric(splits[1])){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "This is bad URL, try again");
            }else{
                int i = Integer.valueOf(splits[1]);
                Category category = em.createQuery("SELECT c FROM Category c WHERE id = :idFromURI", Category.class)
                        .setParameter("idFromURI", i).getSingleResult();
                String catToJSON  = objectMapper.writeValueAsString(category);
                resp.getWriter().write(catToJSON);
            }
        }
    }

}
