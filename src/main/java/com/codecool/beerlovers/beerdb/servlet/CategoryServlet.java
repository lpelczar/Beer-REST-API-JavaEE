package com.codecool.beerlovers.beerdb.servlet;

import com.codecool.beerlovers.beerdb.model.Category;
import com.codecool.beerlovers.beerdb.repository.CategoryRepository;
import com.codecool.beerlovers.beerdb.util.JsonUtils;
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
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            String requestBody = jsonUtils.getStringFromHttpServletRequest(req);
            Category category = getCategoryFromRequestBody(requestBody);
                if (category == null || category.getId() != 0) {
                    resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
                categoryRepository.create(category);
                resp.sendError(HttpServletResponse.SC_CREATED);
        } else {
            resp.sendError(HttpServletResponse.SC_NO_CONTENT);
        }
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

            if(categoryRepository.getById(id) == null){
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            String requestBody = jsonUtils.getStringFromHttpServletRequest(req);
            Category oldCategory = categoryRepository.getById(id);
            Category newCategory = getCategoryFromRequestBody(requestBody);

            if (newCategory == null || oldCategory.getId() != newCategory.getId()) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            }else{
                categoryRepository.update(newCategory);
                resp.sendError(HttpServletResponse.SC_CREATED);
            }
        }
    }

    private Category getCategoryFromRequestBody(String requestBody) throws IOException {
        if(jsonUtils.checkJsonCompatibility(requestBody, Category.class)) {
            return objectMapper.readValue(requestBody, Category.class);
        } else {
            return null;
        }
    }


}

