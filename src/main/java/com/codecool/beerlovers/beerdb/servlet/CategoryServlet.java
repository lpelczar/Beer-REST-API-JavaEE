package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Category;
import com.codecool.beerlovers.beerdb.repository.CategoryRepository;
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


@WebServlet("/categories/*")
public class CategoryServlet extends AbstractServlet {


    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CategoryRepository categoryRepository;

    private JsonUtils jsonUtils = new JsonUtilsImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getPathInfo();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if(path == null || path.equals("/")) {
            List<Category> categories = categoryRepository.getAll();
            resp.getWriter().write(objectMapper.writeValueAsString(categories));
        }else{
            String[] splits = path.split("/");
            if(splits.length != 2 || !StringUtils.isStrictlyNumeric(splits[1])){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = Integer.valueOf(splits[1]);
                if(!(categoryRepository.getById(id) == null)) {
                    Category category = categoryRepository.getById(id);
                    String catToJSON = objectMapper.writeValueAsString(category);
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

        Category category = objectMapper.readValue(json, Category.class);
        if(categoryRepository.getById(category.getId()) == null) {
            categoryRepository.create(category);
        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.sendRedirect("/categories/" + category.getId());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if(path == null || path.equals("/")) {
            categoryRepository.deleteAll();
        }else{
            String[] splits = path.split("/");
            if(splits.length != 2 || !StringUtils.isStrictlyNumeric(splits[1])){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = Integer.valueOf(splits[1]);
                if(!(categoryRepository.getById(id) == null)) {
                    Category category = categoryRepository.getById(id);
                    categoryRepository.delete(category);
                }else{
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
        resp.sendRedirect("/categories/");


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
                String json =  jsonUtils.getStringFromHttpServletRequest(req);
                Category category = objectMapper.readValue(json, Category.class);
                if (!(categoryRepository.getById(id) == null) && category.getId() == id ) {
                    categoryRepository.update(category);
                    resp.sendRedirect("/categories/" + category.getId());
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
    }

}

