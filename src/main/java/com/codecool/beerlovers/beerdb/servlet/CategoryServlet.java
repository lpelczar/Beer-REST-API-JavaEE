package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Category;
import com.codecool.beerlovers.beerdb.repository.CategoryRepository;
import com.codecool.beerlovers.beerdb.util.JsonUtils;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/categories/*")
public class CategoryServlet extends AbstractServlet {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JsonUtils jsonUtils;


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
                int id = getIdFromPath(path);
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
            resp.sendError(HttpServletResponse.SC_NO_CONTENT);
        }
        resp.sendError(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if(path == null || path.equals("/")) {
            categoryRepository.deleteAll();
        }else {
            if (isNotCorrectPath(path)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                int id = getIdFromPath(path);
                if (!(categoryRepository.getById(id) == null)) {
                    Category category = categoryRepository.getById(id);
                    categoryRepository.delete(category);
                    resp.sendError(HttpServletResponse.SC_ACCEPTED);
                } else {
                    resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                }
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (isNotCorrectPath(path)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            int id = getIdFromPath(path);
            String json = jsonUtils.getStringFromHttpServletRequest(req);
            Category category = objectMapper.readValue(json, Category.class);
            if (!(categoryRepository.getById(id) == null) && category.getId() == id) {
                categoryRepository.update(category);
                resp.sendError(HttpServletResponse.SC_CREATED);
            } else {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }


}

