package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Category;
import com.codecool.beerlovers.beerdb.repository.CategoryRepository;
import com.codecool.beerlovers.beerdb.util.JsonUtils;
import com.codecool.beerlovers.beerdb.util.JsonUtilsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
        if(path == null || path.equals("/")) {
            List<Category> categories = categoryRepository.getAll();
            sendAsJson(resp, categories);
        }else{
            if(isNotCorrectPath(path)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                int id = getCategoryIdFromPath(path);
                if(!(categoryRepository.getById(id) == null)) {
                    Category category = categoryRepository.getById(id);
                    sendAsJson(resp, category);
                }else{
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
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
        resp.sendError(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if(path == null || path.equals("/")) {
            categoryRepository.deleteAll();
        }else {
            if (isNotCorrectPath(path)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                int id = getCategoryIdFromPath(path);
                if (!(categoryRepository.getById(id) == null)) {
                    Category category = categoryRepository.getById(id);
                    categoryRepository.delete(category);
                } else {
                    resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                }
            }
        }
        resp.sendRedirect("/categories/");

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if (isNotCorrectPath(path)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            int id = getCategoryIdFromPath(path);
            String json = jsonUtils.getStringFromHttpServletRequest(req);
            Category category = objectMapper.readValue(json, Category.class);
            if (!(categoryRepository.getById(id) == null) && category.getId() == id) {
                categoryRepository.update(category);
                resp.sendRedirect("/categories/" + category.getId());
            } else {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }

    private boolean isNotCorrectPath(String pathInfo) {
        String[] splits = pathInfo.split("/");
        return splits.length != 2 || !org.apache.commons.lang3.StringUtils.isNumeric(splits[1]);
    }

    private int getCategoryIdFromPath(String pathInfo) {
        String[] splits = pathInfo.split("/");
        return Integer.parseInt(splits[1]);
    }

    private void sendAsJson(HttpServletResponse resp, Object object) throws IOException {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(object));
    }


}

