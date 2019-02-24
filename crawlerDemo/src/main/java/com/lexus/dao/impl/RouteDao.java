package com.lexus.dao.impl;

import com.lexus.dao.IRouteDao;
import com.lexus.domain.Route;
import com.lexus.utils.JDBCUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class RouteDao implements IRouteDao {
    private JdbcTemplate jt = new JdbcTemplate(JDBCUtil.getDataSource());

    @Override
    public Integer addRoute(Route route) {
        String sql = "insert into route values(null,?,?,?,?,?,?,?,?,?,?,?)";
        int add = 0;
        try {
            add = jt.update(sql, route.getRname(), route.getPrice(), route.getRouteIntroduce(), route.getRflag(), route.getRdate(), route.getIsThemeTour(), route.getCount(), route.getCid(), route.getRimage(), route.getSid(), route.getSourceId());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return add;
    }
}
