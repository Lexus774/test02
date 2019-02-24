package com.lexus.service.impl;

import com.lexus.dao.IRouteDao;
import com.lexus.domain.Route;
import com.lexus.service.IRouteService;
import com.lexus.utils.ContextFactory;

public class RouteService implements IRouteService {
   private IRouteDao dao= (IRouteDao) ContextFactory.getContextInstance("routeDao");
    @Override
    public Integer addRoute(Route route) throws Exception {
       Integer add= dao.addRoute(route);
        return add;
    }
}
