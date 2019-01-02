/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: DatabaseTools.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.db;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author hiylo
 * @ClassName: DatabaseUtil
 * @Description: 数据库工具 ,封装数据库操作模版
 * @date 2012年12月30日 上午9:32:07
 */
@Repository("database")
public class DatabaseTools {
    private final String STRING_CLASS_NAME = String.class.getClass().getName();
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean flag = false;
    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SessionFactory hibernateFactory;

    {

    }

    @Autowired
    public DatabaseTools(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }

    @Bean
    public HibernateTemplate hibernateTemplate() {
        HibernateTemplate hibernateTemplate = new HibernateTemplate(hibernateFactory);
        return hibernateTemplate;
    }

    /**
     * @param <T>
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public <T> List<?> getListViaJdbcTemplate(int page, int pageSize, String sql, Class<?> clazz) {
        int begin = pageSize * (page - 1);
        int end = pageSize;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql + " limit " + begin + "," + end);
        List<T> results = new ArrayList<T>(pageSize);
        for (Map<String, Object> r : result) {
            results.add((T) this.cast(r, clazz));
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    private <T> T cast(Map<String, Object> obj, Class<?> clazz) {
        try {
            Object result = clazz.getConstructor().newInstance();
            Iterator<String> keys = obj.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                for (Method method : clazz.getMethods()) {
                    if (method.getName().equals("set" + key.substring(0, 1).toUpperCase() + key.substring(1))) {
                        switch (method.getParameterTypes().getClass().getName()) {
                            case "java.lang.String":
                                method.invoke(result, obj.get(key).toString());
                                break;
                            default:
                                break;
                        }

                    }
                }
            }
            return (T) result;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(final Object o) {
        this.hibernateTemplate.execute(session -> {
            session.delete(o);
            return true;
        });
    }

    public boolean executeHQL(final String hql) {
        return this.hibernateTemplate.execute(session -> {
            int num = session.createQuery(hql.toString()).setCacheable(true).executeUpdate();
            if (num > 0) {
                flag = true;
            } else {
                flag = false;
            }
            return flag;
        });
    }

    public Object executeHQLGetObject(final String hql) {
        return this.hibernateTemplate.execute(session -> session.createQuery(hql.toString()).setCacheable(true).uniqueResult());
    }

    public boolean executeSql(final String sql) {
        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<?> get(final String sql, final Object object) {
        return this.hibernateTemplate.execute((HibernateCallback<List<?>>) session -> {
            List<?> list = new ArrayList<>(20);
            Query query = session.createSQLQuery(sql).addEntity(object.getClass());
            query.setCacheable(true);
            if (getHibernateTemplate().getQueryCacheRegion() != null) {
                // 设置查询缓存区域
                query.setCacheRegion(getHibernateTemplate().getQueryCacheRegion());
            }
            list = query.list();
            return list;
        });
    }

    public List<?> get(final String sql) {
        return this.hibernateTemplate.execute((HibernateCallback<List<?>>) session -> {
            List<?> list = new ArrayList<>(20);
            Query query = session.createSQLQuery(sql);
            list = query.list();
            return list;
        });
    }

    public List<?> getAllByHQL(final String hql) {
        return this.hibernateTemplate.execute((HibernateCallback<List<?>>) session -> {
            Query query = session.createQuery(hql);
            List<?> list = query.list();
            return list;
        });
    }

    /**
     * 通过HQL分页获取
     *
     * @param startNum 开始位置
     * @param size     个数
     * @param hql      hql
     * @return 列表
     */
    public List<?> getAllByPageViaHQL(final int startNum, final int size, final String hql) {
        return (List<?>) this.hibernateTemplate.execute((HibernateCallback<Object>) session -> {
            List<?> list = new ArrayList<>(20);
            try {
                Query queryObject = session.createQuery(hql);
                queryObject.setFirstResult(startNum);
                queryObject.setMaxResults(size);
                if (getHibernateTemplate().getQueryCacheRegion() != null) {
                    queryObject.setCacheRegion(getHibernateTemplate().getQueryCacheRegion());
                }
                list = queryObject.list();
            } catch (Exception e) {
                try {
                    throw new Exception(e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            return list;
        });
    }

    public List<?> getAllByPageViaSQL(final int startNum, final int resultSize, final String sql, final Object object) {
        return (List<?>) this.hibernateTemplate.execute((HibernateCallback<Object>) session -> {
            List<?> list = new ArrayList<>(20);
            try {
                Query queryObject = session.createSQLQuery(sql).addEntity(object.getClass());
                queryObject.setFirstResult(startNum);
                queryObject.setMaxResults(resultSize);
                if (getHibernateTemplate().getQueryCacheRegion() != null) {
                    queryObject.setCacheRegion(getHibernateTemplate().getQueryCacheRegion());
                }
                list = queryObject.list();
            } catch (Exception e) {
                try {
                    throw new Exception(e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            return list;
        });
    }

    public Object getByCondition(final Class<?> clazz, final String condition) {
        List<?> list = this.getAllByHQL("from " + clazz.getName() + " obj where " + condition);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public Serializable save(Object obj) {
        return this.hibernateTemplate.save(obj);
    }

    public Object loadByID(final Class<?> clazz, final Serializable serializable) {
        return hibernateTemplate.getSessionFactory().getCurrentSession().load(clazz, serializable);
    }

    public Object getByID(final Class<?> clazz, final Serializable serializable) {
        return hibernateTemplate.get(clazz, serializable);
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate ht) {
        this.hibernateTemplate = ht;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getListViaSQL(final String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    public int saveIDIsNum(final Object obj) {
        Serializable serializable = hibernateTemplate.save(obj);
        return Integer.parseInt(serializable.toString());
    }

    public void saveOrUpdate(final Object obj) {
        this.hibernateTemplate.saveOrUpdate(obj);
    }

    public void update(final Object obj) {
        hibernateTemplate.update(obj);
    }

}
