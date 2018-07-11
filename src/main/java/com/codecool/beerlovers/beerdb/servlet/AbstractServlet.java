package com.codecool.beerlovers.beerdb.servlet;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@Configurable
public abstract class AbstractServlet extends HttpServlet {


    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
}
